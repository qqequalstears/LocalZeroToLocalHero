package Server.Model;

import java.util.List;

public class ConcreteAggregate implements Aggregate {
    private List<Object> collection;

    public Iterator createIterator() {
        return new ConcreteIterator(collection);
    }

    public void add(Object object) {
        collection.add(object);
    }

}
