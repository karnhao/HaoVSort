
package Test;

import java.util.List;

public abstract class Testy implements Interfaceky{
    protected List<Integer> array;
    @Override
    public void sort() {
        this.array.clear();
    }
}
