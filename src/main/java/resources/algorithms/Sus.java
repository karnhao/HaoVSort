package resources.algorithms;

import java.util.Arrays;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Sus extends Algorithms<Sus> {

    private Integer[] old_array;
    private Float[] fake_pitch;

    @Override
    public void sort(Integer[] a) {
        for (int i = 0; i < a.length; i++) {
            setPitchs(this.fake_pitch[i]);
            setIndexes(i);
            show();
        }
        Arrays.sort(old_array);
    }

    @Override
    public void init() {
        this.setDelay(100l);
        this.old_array = this.getArray();
        this.setArray(new Integer[] {
                -1, -1, -1, 15,
                -1, 18, -1, 22,
                -1, 24, -1, 22,
                -1, 18, -1, 15,
                -1, -1, -1, -1,
                -1, 12, 17, 15
        });
        // https://minecraft.fandom.com/wiki/Note_Block#:~:text=the%20particle%27s%20texture.-,Pitch,-(Octave%201)
        this.fake_pitch = Arrays.asList(new Integer[] {
                -1, -1, -1, 18,
                -1, 21, -1, 23,
                -1, 24, -1, 23,
                -1, 21, -1, 18,
                -1, -1, -1, -1,
                -1, 16, 20, 18
        }).stream().map(Sus::noteCal).toArray(Float[]::new);
    }

    private static float noteCal(int count) {
        return count != -1 ? (float) Math.pow(2, ((float) count - 12) / 12) : 0;
    }
}
