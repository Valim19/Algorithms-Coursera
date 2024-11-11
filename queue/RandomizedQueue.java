import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int arraySize;
    private Item[] elements;
    private int elementsCounter;

    public RandomizedQueue() {
        this.arraySize       = 1;
        this.elements        = (Item[]) new Object[arraySize];
        this.elementsCounter = 0;
    }

    public int size() {
        return elementsCounter;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void enqueue(Item item) {
        validateNotNull(item);

        if (isFull()) duplicateSize();

        elements[size()] = item;
        elementsCounter++;
    }

    public Item dequeue() {
        validateNotEmpty();

        Item itemToReturn = removeRandomItemAndReorder();

        if (isAlmostEmpty()) halfSize();

        return itemToReturn;
    }

    public Item sample() {
        validateNotEmpty();

        return elements[randomIndex()];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        testIsEmpty(queue);
        testEnqueue(queue);
        testDequeue(queue);
        testSample(queue);
        testIterator(queue);
    }

    private void validateNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be a null element");
    }

    private void validateNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException("It cannot bem an empty deck");
    }

    private void resize(int capacity) {
        if (isEmpty()) { return; }

        arraySize = capacity;
        Item[] copy = (Item[]) new Object[arraySize];

        int maxSize = size();
        if (maxSize >= 0) System.arraycopy(elements, 0, copy, 0, maxSize);

        elements = copy;
    }

    private void halfSize() {
        resize(arraySize / 2);
    }

    private void duplicateSize() {
        resize(2 * arraySize);
    }

    private static void testIsEmpty(RandomizedQueue<Integer> queue) {
        assert queue.isEmpty() : "Queue should be empty initially";
        assert queue.size() == 0 : "Size should be 0 when the queue is empty";
        System.out.println("testIsEmpty passed.");
    }

    private static void testEnqueue(RandomizedQueue<Integer> queue) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
            assert queue.size() == i + 1 : "Size should increase as elements are enqueued";
            assert !queue.isEmpty() : "Queue should not be empty after enqueuing elements";
        }
        System.out.println("testEnqueue passed.");
    }

    private static void testDequeue(RandomizedQueue<Integer> queue) {
        int initialSize = queue.size();
        int elementsRemoved = 0;

        for (int i = 0; i < initialSize; i++) {
            Integer removed = queue.dequeue();
            assert removed != null : "Dequeued element should not be null";
            elementsRemoved++;
            assert queue.size() == initialSize - elementsRemoved : "Size should decrease after dequeue";
        }

        assert queue.isEmpty() : "Queue should be empty after removing all elements";
        System.out.println("testDequeue passed.");
    }

    private static void testSample(RandomizedQueue<Integer> queue) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }

        Integer sampled = queue.sample();
        assert sampled != null : "Sampled element should not be null";
        assert queue.size() == 5 : "Sampling should not remove elements from the queue";
        System.out.println("testSample passed.");
    }

    private static void testIterator(RandomizedQueue<Integer> queue) {
        int initialSize = queue.size();
        int count = 0;

        for (Integer i : queue) {
            assert i != null : "Iterator should return non-null elements";
            count++;
        }

        assert count == initialSize : "Iterator should iterate over all elements";
        assert queue.size() == initialSize : "Iterator should not modify the size of the queue";
        System.out.println("testIterator passed.");
    }

    private boolean isFull() {
        return size() == arraySize;
    }

    private boolean isAlmostEmpty() {
        return size() <= arraySize / 4;
    }

    private int randomIndex() {
        return StdRandom.uniformInt(size());
    }

    private Item removeRandomItemAndReorder() {
        int index = randomIndex();
        int lastIndex = size() - 1;

        Item item = elements[index];
        elements[index] = elements[lastIndex];
        elements[lastIndex] = null;

        elementsCounter--;

        return item;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> copy;

        RandomizedQueueIterator() {
            copy = new RandomizedQueue<Item>();

            for (int i = 0; i < size(); i++) {
                copy.enqueue(elements[i]);
            }
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("There are no more elements");

            return copy.dequeue();
        }

        public boolean hasNext() {
            return copy.size() > 0;
        }
    }
}
