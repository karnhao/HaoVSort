package resources.algorithms;

import java.util.ArrayList;
import java.util.List;

import com.hao.haovsort.sorting.utils.Algorithms;
import com.xxmicloxx.NoteBlockAPI.model.Song;

public class Piano extends Algorithms<Piano> {

    private Song song;
    private int length;
    private Integer[] old_array;
    private long tick_delay;
    private List<com.hao.haovsort.sorting.utils.Sound> sound;
    
    @Override
    public void sort(Integer[] a) {
    }
}
