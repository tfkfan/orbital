package io.github.tfkfan.orbital.core.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;

public class UniqueQueue<T> {

    private final LinkedHashSet<T> queue;

    public UniqueQueue() {
        queue = new LinkedHashSet<>();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean contains(T o) {
        return queue.contains(o);
    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }

    public boolean add(T item) {
        if (contains(item))
            throw new IllegalArgumentException("Item already exists");

        queue.addFirst(item);
        return true;
    }

    public void clear() {
        queue.clear();
    }

    public T poll() {
        return queue.removeFirst();
    }

    public boolean removeIf(Predicate<? super T> filter) {
        return queue.removeIf(filter);
    }

    public List<T> chunk(int size) {
        final List<T> chunk = new ArrayList<>();
        while (chunk.size() != size)
            chunk.add(poll());
        return chunk;
    }
}
