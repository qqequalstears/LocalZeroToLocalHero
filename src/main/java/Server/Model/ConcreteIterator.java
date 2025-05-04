package Server.Model;

import java.util.List;

public class ConcreteIterator implements Iterator {
    private List<Object> collection;
    private int index;

    public ConcreteIterator(List<Object> collection) {
        this.collection = collection;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < collection.size();
    }

    public Object next() {
        if (hasNext()) {
            return collection.get(index++);
        }
        return null;
    }
}
