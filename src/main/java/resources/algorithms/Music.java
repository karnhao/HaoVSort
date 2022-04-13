package resources.algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.hao.haovsort.Main;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.SongCollector;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

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

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        getPlayers().forEach((t) -> t
                .sendMessage(ChatColor.GREEN
                        + String.format("Sorting playing music : %s, %s", getSongName(), song.getAuthor())));
        long old_delay = getDelay();
        for (int tick = 0; tick <= song.getLength(); tick++) {
            setNoteSoundsAtTick(tick);
            setArrayAtSection(tickToSection(tick));
            setSelectedIndexesAtTick(tick);
            setDelay(tick_delay);
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
        return null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (args.length != 1)
            throw new InvalidArgsException("Expected 1 argument");
    }

    @Override
    public void init() {
        this.old_array = this.getArray();
        this.length = this.old_array.length;
        if (!Main.getNoteBlockAPI())
            throw new InvalidArgsException("NoteBlock API is not available");
        song = SongCollector.getSong(getArgs()[0]);
        if (song == null)
            throw new InvalidArgsException("Song not found");
        layer_count = song.getLayerHashMap().values().size();
        if (layer_count > length)
            throw new InvalidArgsException("Length too small");
        layerWidth = (int) Math.floor(((double) array.length) / ((double) layer_count));
        tick_delay = (long) (1000 / song.getSpeed());
    }

    private void setNoteSoundsAtTick(int tick) {
        LinkedList<com.hao.haovsort.sorting.utils.Sound> list = new LinkedList<>();
        song.getLayerHashMap().values().forEach((t) -> {
            Note note = t.getNote(tick);
            if (note == null) {
                return;
            }
            float pitch = keyToPitch(fixKey(note.getKey()));
            list.add(new com.hao.haovsort.sorting.utils.Sound(
                    getSoundName(note.getInstrument()), SoundCategory.MASTER, pitch, ((float) t.getVolume()) / 100));
        });
        this.setPitchs(Arrays.asList(0f).stream().toArray(Float[]::new));
        this.sound = list;
    }

    private void setSelectedIndexesAtTick(int tick) {
        Integer[] r = new Integer[layer_count];
        int temp = tick - (layerWidth * (int) Math.floor(tick / layerWidth));
        for (int i = 0; i < layer_count; i++) {
            r[i] = temp;
            temp += layerWidth;
        }
        setIndexes(r);
    }

    private void setArrayAtSection(int section) {
        LinkedList<Integer> list = new LinkedList<>();
        int start = layerWidth * section;
        int end = start + layerWidth;
        song.getLayerHashMap().values().forEach((t) -> {
            for (int tick = start; tick < end; tick++) {
                Note note = t.getNote(tick);
                if (note == null)
                    list.add(-1);
                else {
                    list.add(pitchToValue(keyToPitch(note.getKey())));
                }
            }
        });

        this.array = list.stream().toArray(Integer[]::new);
    }

    private static float keyToPitch(byte key) {
        return (float) Math.pow(2, (float) (key - 45) / 12);
    }

    private int pitchToValue(float pitch) {
        return (int) Math.floor(((2 * pitch * length) - 100) / 3);
    }

    private int tickToSection(int tick) {
        return (int) Math.ceil((double) (tick / layerWidth));
    }

    private void playSortingSound() {
        sound.stream().filter((t) -> t.getPitch() != 0).forEach((p) -> {
            getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), p.getName(),
                        p.getSoundCategory(), p.getVolume(), p.getPitch());
            });
        });
    }

    private String getSoundName(byte data) {
        return InstrumentUtils.getSoundNameByInstrument(data);
    }

    private byte fixKey(byte key) {
        byte temp = key;
        while (temp > 57)
            temp -= 12;
        while (temp < 33)
            temp += 12;
        return temp;
    }

    private String getSongName() {
        return getArgs()[0];
    }
}
