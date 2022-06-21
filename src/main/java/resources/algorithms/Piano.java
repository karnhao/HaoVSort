package resources.algorithms;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;

import com.hao.haovsort.Main;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.SongCollector;
import com.hao.haovsort.sorting.utils.Sound;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;

import net.md_5.bungee.api.ChatColor;

public class Piano extends Algorithms<Piano> {

    private Song song;
    private Integer[] old_array;
    private long tick_delay;
    private List<com.hao.haovsort.sorting.utils.Sound> sound;

    @Override
    public void sort(Integer[] a) {
        getPlayers().forEach((t) -> t
                .sendMessage(ChatColor.GREEN
                        + String.format("Sorting playing music : %s, %s", getSongName(), song.getAuthor())));
        long old_delay = getDelay();
        setDelay(tick_delay);
        for (int tick = 0; tick <= song.getLength() + 1; tick++) {
            setNoteSoundsAtTick(tick);
            setSelectedIndexesAtTick(tick);
            show();
            playSortingSound();
        }
        setDelay(old_delay);
        setArray(old_array);
    }

    private String getSongName() {
        return getArgs()[0];
    }

    private String getSoundName(Note note) {
        if (InstrumentUtils.isCustomInstrument(note.getInstrument()))
            return song.getCustomInstruments()[note.getInstrument()
                    - InstrumentUtils.getCustomInstrumentFirstIndex()].getSoundFileName();
        return InstrumentUtils.getSoundNameByInstrument(note.getInstrument());
    }

    @Override
    public void init() {
        if (!Main.getNoteBlockAPI())
            throw new InvalidArgsException("NoteBlock API is not available");
        song = SongCollector.getSong(getArgs()[0]);
        if (song == null)
            throw new InvalidArgsException("Song not found");
        this.old_array = this.getArray();
        Arrays.sort(this.getArray());
        tick_delay = (long) (1000 / song.getSpeed());
    }

    private void setSelectedIndexesAtTick(int tick) {
        LinkedList<Integer> list = new LinkedList<>();
        song.getLayerHashMap().values().forEach((t) -> {
            Note note = t.getNote(tick);
            if (note != null) {
                list.add(pitchToValue(NoteUtils.getPitchTransposed(note)));
            }
        });
        setIndexes(list);
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

    // private static float keyToPitch(float key) {
    // return (float) Math.pow(2, (float) ((key - 45) / 12));
    // }

    private int pitchToValue(float pitch) {
        return (int) Math.floor(((2 * pitch * this.array.length) - 100) / 3);
    }

    private void playSortingSound() {
        sound.stream().filter(Music::isValidSound).forEach((p) -> {
            getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), p.getName(),
                        p.getSoundCategory(), p.getVolume(), p.getPitch());
            });
        });
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return SongCollector.getAllSongsName(args[0]);
        return null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (args.length <= 0 || args.length >= 3)
            throw new InvalidArgsException("Syntax error : /... <song>");
    }

}
