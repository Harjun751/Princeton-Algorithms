import java.util.Iterator;
import java.util.NoSuchElementException;

// Let's try using a resizable array.
public class Deque<Item> implements Iterable<Item> {
    private LinkedList q;

    private int back = 1;
    private int capacity = 2;
    private int front = 0;



    // construct an empty item array of back=1
    public Deque() {
        q = new LinkedList();
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (q.size() == 0);
    }

    // return the number of items on the deque
    public int size() {
        return q.size();
    }

    // add the item to the front
    public void addFirst(Item item) {
        q.addFront(item);
    }

    // add the item to the back
    public void addLast(Item item) {
        q.addBack(item);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        return q.removeFirst();
    }

    // remove and return the item from the back
    public Item removeLast() {
        return q.removeLast();
    }

    private class LinkedList{

        private Node front;
        private Node back;

        private int size;

        public LinkedList(){
            this.front = null;
            this.back = null;
            this.size = 0;
        }

        public int size(){
            return this.size;
        }

        public void addFront(Item item){
            if (item == null) {
                throw new IllegalArgumentException();
            }
            if (this.size==0){
                front = new Node(item);
                back = front;
            } else {
                // create a new node with the next pointing to
                // the current front
                Node ite = new Node(item, front);
                front.previous = ite;
                front = ite;
            }
            size+=1;
        }

        public void addBack(Item item){
            if (item == null) {
                throw new IllegalArgumentException();
            }
            if (this.size==0){
                front = new Node(item);
                back = front;
            } else {
                // create a new node with a null next
                // the previous back node will point to this node.
                Node ite = new Node(item);
                back.next = ite;
                ite.previous = back;
                back = ite;
            }
            size+=1;
        }

        public Item removeFirst(){
            if (size() == 0) {
                throw new NoSuchElementException();
            }
            Item firstItem = front.item;
            front = front.next;
            if (front!=null){
                front.previous = null;
            }
            size-=1;
            if (size==0){
                front=null;
                back=null;
            }
            return firstItem;
        }
        public Item removeLast(){
            if (size() == 0) {
                throw new NoSuchElementException();
            }
            Item lastItem = back.item;
            back = back.previous;
            if (back!=null){
                back.next = null;
            }
            size-=1;
            if (size==0){
                back=null;
                front=null;
            }
            return lastItem;
        }

        private Iterator<Item> getIterator(){
            return new LLIterator();
        }

        private class LLIterator implements Iterator<Item>{
            private Node current = front;
            @Override
            public boolean hasNext() {
                return current!=null;
            }

            @Override
            public Item next() {
                if (current==null){
                    throw new NoSuchElementException();
                }
                Item curr = current.item;
                current = current.next;
                return curr;
            }
        }

        private class Node{
            private Item item;
            private Node next;

            private Node previous;

            public Node(Item item){
                this.item = item;
                this.next = null;
                this.previous = null;
            }
            public Node(){
                this.item = null;
                this.next = null;
                this.previous = null;
            }
            public Node(Item item, Node next){
                this.item = item;
                this.next = next;
                this.previous = null;
            }
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return q.getIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> q = new Deque<>();
        q.addFirst(1);
        q.addFirst(2);
        q.addFirst(3);
        q.addFirst(4);

        // capacity = 4, size=4 -> size=1 required to trigger shrink
        q.removeFirst();
        q.removeFirst();
        q.removeFirst();
        // Should have shrinked by now. Check array size and items.
        int last = q.removeFirst();
        System.out.println(last);


    }

}