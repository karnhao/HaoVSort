package resources.algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.Main;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.SongCollector;
import com.hao.haovsort.sorting.utils.Sound;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;

import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Music extends Algorithms<Music> {

    private Song song;
    private int layerWidth;
    private int layer_count;
    private int length;
    private Integer[] old_array;
    private long tick_delay;
    private List<com.hao.haovsort.sorting.utils.Sound> sound;
    private boolean smooth = false;

    @Override
    public void sort(Integer[] a) {
        getPlayers().forEach((t) -> t
                .sendMessage(ChatColor.GREEN
                        + String.format("Sorting playing music : %s, %s", getSongName(), song.getAuthor())));
        long old_delay = getDelay();
        setDelay(tick_delay);
        for (int tick = 0; tick <= song.getLength() + 1; tick++) {
            setNoteSoundsAtTick(tick);
            setArrayAtTick(tick);
            setSelectedIndexesAtTick(tick);
            show();
            playSortingSound();
        }
        setDelay(old_delay);
        setArray(old_array);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return SongCollector.getAllSongsName(args[0]);
        if (args.length == 2)
            return Arrays.asList("true", "false").stream().filter((t) -> t.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        return null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (args.length <= 0 || args.length >= 3)
            throw new InvalidArgsException("Syntax error : /... <song> <smooth>");
        if (args.length >= 2) {
            if (!(args[1].equals("true") || args[1].equals("false")))
                throw new InvalidArgsException("Syntax error : smooth must be true or false but not " + args[1]);
        }
    }

    @Override
    public void init() {
        if (getArgs().length >= 2 && getArgs()[1].equalsIgnoreCase("true"))
            this.smooth = true;
        if (!Main.getNoteBlockAPI())
            throw new InvalidArgsException("NoteBlock API is not available");
        song = SongCollector.getSong(getArgs()[0]);
        if (song == null)
            throw new InvalidArgsException("Song not found");
        layer_count = song.getLayerHashMap().values().size();
        this.old_array = this.getArray();
        this.length = this.old_array.length;
        if (layer_count > length)
            throw new InvalidArgsException("Length too small");
        layerWidth = (int) Math.floor(((double) array.length) / ((double) layer_count));
        tick_delay = (long) (1000 / song.getSpeed());
    }

    private void setNoteSoundsAtTick(int tick) {
        LinkedList<Sound> list = new LinkedList<>();
        song.getLayerHashMap().values().forEach((t) -> {
            Note note = t.getNote(tick);
            if (note == null)
                return;
            list.add(new Sound(
                    getSoundName(note), SoundCategory.MASTER, NoteUtils.getPitchTransposed(note),
                    ((float) t.getVolume()) / 100));
        });
        this.setPitchs(Arrays.asList(0f).stream().toArray(Float[]::new));
        this.sound = list;
    }

    private void setSelectedIndexesAtTick(int tick) {
        Integer[] r = new Integer[layer_count];
        int temp = tick - (layerWidth * (int) Math.floor((tick - 1) / layerWidth)) - 1;
        for (int i = 0; i < layer_count; i++) {
            r[i] = temp;
            temp += layerWidth;
        }
        setIndexes(r);
    }

    private void setArrayAtTick(int tick) {
        int section = tickToSection(tick);
        LinkedList<Integer> list = new LinkedList<>();
        int start = layerWidth * section;
        int end = start + layerWidth;
        song.getLayerHashMap().values().forEach((t) -> {
            for (int i = start; i < end; i++) {
                int j = i < tick && smooth ? i + layerWidth : i;
                Note note = t.getNote(j);
                if (note == null) {
                    list.add(-1);
                    continue;
                }
                list.add(pitchToValue(NoteUtils.getPitchTransposed(note)));
            }
        });

        this.array = list.stream().toArray(Integer[]::new);
    }

    private int pitchToValue(float pitch) {
        return (int) Math.floor(((2 * pitch * this.array.length) - 100) / 3);
    }

    private int tickToSection(int tick) {
        return (int) Math.ceil((double) (tick / layerWidth));
    }

    private void playSortingSound() {
        sound.stream().filter(Music::isValidSound).forEach((p) -> {
            getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), p.getName(),
                        p.getSoundCategory(), p.getVolume(), p.getPitch());
            });
        });
    }

    private String getSoundName(Note note) {
        if (InstrumentUtils.isCustomInstrument(note.getInstrument()))
            return song.getCustomInstruments()[note.getInstrument()
                    - InstrumentUtils.getCustomInstrumentFirstIndex()].getSoundFileName();
        return InstrumentUtils.getSoundNameByInstrument(note.getInstrument());
    }

    private String getSongName() {
        return getArgs()[0];
    }

    private static boolean isValidSoundName(String name) {
        String temp = name.substring(name.indexOf(":") + 1);
        return temp.matches("^[a-z0-9/._-]+$");
    }

    protected static boolean isValidSound(Sound sound) {
        return sound.getPitch() != 0 && isValidSoundName(sound.getName());
    }
}