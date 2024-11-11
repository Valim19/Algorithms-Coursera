import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private static final int DEQUE_SIZE = 10;
    private static final int MAX_VALUE = DEQUE_SIZE - 1;
    private Node first;
    private Node last;
    private int  counter;

    public Deque() {
        this.first   = null;
        this.last    = null;
        this.counter = 0;
    }

    public int size() {
        return counter;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void addFirst(Item item) {
        validateNotNull(item);

        Node oldFirst  = first;

        first          = new Node();
        first.item     = item;
        first.next     = oldFirst;
        first.previous = null;

        if (isEmpty()) { last    = first; }
        else { oldFirst.previous = last; }

        counter++;
    }

    public void addLast(Item item) {
        validateNotNull(item);

        Node oldLast  = last;

        last          = new Node();
        last.item     = item;
        last.next     = null;
        last.previous = oldLast;

        if (isEmpty()) { first = last; }
        else { oldLast.next    = last; }

        counter++;
    }

    public Item removeFirst() {
        validateNotEmpty();

        Node removedFirst = first;
        first             = removedFirst.next;
        counter--;

        if (isEmpty()) { last = null; }
        else { first.previous = null; }

        return removedFirst.item;
    }

    public Item removeLast() {
        validateNotEmpty();

        Node removedLast = last;
        last             = removedLast.previous;
        counter--;

        if (isEmpty()) { first = null; }
        else { last.next      = null; }

        return removedLast.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();

        testIsEmpty(deck);
        testAddFirst(deck);
        testRemoveLast(deck);
        testAddLast(deck);
        testRemoveFirst(deck);
    }

    private void validateNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be a null element");
    }

    private void validateNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException("You cannot remove from an empty deck");
    }

    private static void testIsEmpty(Deque<Integer> deck) {
        assert deck.isEmpty() : "Deck should be initially empty";
        System.out.println("testIsEmpty passed.");
    }

    private static void testAddFirst(Deque<Integer> deck) {
        for (int i = 0; i < DEQUE_SIZE; i++) {
            deck.addFirst(i);
            assert deck.size() == i + 1 : "Size should increase as elements are added";
            assert !deck.isEmpty() : "Deck should not be empty after adding elements";
        }

        int expected = MAX_VALUE;
        for (Integer i : deck) {
            assert i == expected : "Elements should appear from 9 to 0";
            expected--;
        }

        System.out.println("testAddFirst passed.");
    }

    private static void testRemoveLast(Deque<Integer> deck) {
        for (int i = 0; i < DEQUE_SIZE; i++) {
            int removed = deck.removeLast();
            assert removed == i : "Elements should be removed from 0 to 9";
            assert deck.size() == MAX_VALUE - i : "Size should decrease as elements are removed";
        }

        assert deck.isEmpty() : "Deck should be empty after removing all elements";
        System.out.println("testRemoveLast passed.");
    }

    private static void testAddLast(Deque<Integer> deck) {
        for (int i = 0; i < DEQUE_SIZE; i++) {
            deck.addLast(i);
            assert deck.size() == i + 1 : "Size should increase as elements are added";
            assert !deck.isEmpty() : "Deck should not be empty after adding elements";
        }

        int expected = 0;
        for (Integer i : deck) {
            assert i == expected : "Elements should appear from 0 to 9";
            expected++;
        }

        System.out.println("testAddLast passed.");
    }

    private static void testRemoveFirst(Deque<Integer> deck) {
        for (int i = 0; i < DEQUE_SIZE; i++) {
            int removed = deck.removeFirst();
            assert removed == i : "Elements should be removed from 0 to 9";
            assert deck.size() == MAX_VALUE - i : "Size should decrease as elements are removed";
        }

        assert deck.isEmpty() : "Deck should be empty after removing all elements";
        System.out.println("testRemoveFirst passed.");
    }

    private class Node {
        Item item;
        Node next;
        Node previous;

        Node() { }

        Node(Item item, Node next) {
            this.item = item;
            this.next = next;
        }

        Node(Item item, Node next, Node previous) {
            this.item     = item;
            this.next     = next;
            this.previous = previous;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current   = current.next;

            return item;
        }

        public boolean hasNext() { return current != null; }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported by this class");
        }
    }
}
