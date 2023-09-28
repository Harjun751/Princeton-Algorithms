import edu.princeton.cs.algs4.*;
public class MinPQ<T extends Comparable<T>> {
    T[] heap;
    int capacity;
    int back;
    int front;
    public MinPQ(){
        this.capacity = 10;
        this.heap = (T[]) new Comparable[capacity];
        heap[0] = null;
        this.front = 1;
        this.back = 1;
    }
    public MinPQ(T[] data){
        this.capacity = data.length*2;
        this.heap = (T[]) new Comparable[capacity];
        heap[0] = null;
        int i =0;
        this.front = 1;
        this.back = 1;
        while (i<data.length) {
            insert(data[i]);
            i++;
        }
    }

    public int size() {
        return back - front - 1;
    }

    private void growArray() {
        // worst case = O(N)
        capacity = capacity * 2;
        if (capacity == 2) {
            // increase capacity again as adding to front or back
            // if capacity was increased from 1 will lead to growing again.
            capacity = 4;
        }
        int startPoint = 1;
        T[] copy = (T[]) new Comparable[capacity];
        for (int i = 1; i < size(); i++) {
            copy[i] = heap[i];
        }
        // Update instance variables
        heap = copy;
        back = size();
        front = 1;
    }

    private void shrinkArray() {
        // worst case O(N)
        capacity = (int) ((double) capacity / 2);
        T[] copy = (T[]) new Comparable[capacity];
        for (int i = 1; i < size(); i++) {
            copy[i] = heap[i];
        }

        heap = copy;
        back = size();
        front = 1;
    }

    private void swim(int k){
        // swim an item up.
        // an item's parent is k/2
        T parent = heap[k/2];
        T swimmer = heap[k];
        // if the swimmer is smaller than the parent
        while (k>1 && swimmer.compareTo(parent)<0){
            // exchange positions
            heap[k/2] = swimmer;
            heap[k] = parent;
            // update k
            k = k/2;
            parent = heap[k/2];
            swimmer = heap[k];
        }
    }

    public void insert(T obj){
        if (back+1>capacity){
            growArray();
        }
        this.heap[back] = obj;
        swim(back);
        back++;
    }

    private void sink(int k){
        T child_1 = heap[2*k];
        T child_2 = heap[2*k+1];
        T parent = heap[k];
        // while parent is more than either child (tho it should lesser than both?)
        while (parent.compareTo(child_1)>0 || parent.compareTo(child_2)>0){
            // parent becomes smaller
            // needs to sink down
            if (child_1.compareTo(child_2)<=0){
                // exchange child 1 and parent
                heap[2*k] = parent;
                heap[k] = child_1;
                k = 2*k;
            } else {
                // exchange child 2 and parent
                heap[2*k+1] = parent;
                heap[k] = child_2;
                k = 2*k+1;
            }
            if (2*k>=back){
                break;
            } else {
                parent = heap[k];
                child_1 = heap[2*k];
                child_2 = heap[2*k+1];
                if (child_2==null){
                    if (parent.compareTo(child_1)>0){
                        heap[2*k] = parent;
                        heap[k] = child_1;
                        break;
                    }
                    break;
                }
            }
        }
    }

    public T delMin(){
        T max = heap[1];
        if (back-front==1){
            heap[1]=null;
            back-=1;
            return max;
        }
        T bottom = heap[back-1];
        heap[1] = bottom;
        sink(1);
        heap[back-1] = null;
        back-=1;
        return max;
    }

    private void visualizer(){
        int currLevel = 1;
        int i = 1;
        int x = 0;
        while (i<back && x<currLevel){
            System.out.print(this.heap[i].toString() + " ");
            x++;
            i++;
            if (x>=currLevel) {
                currLevel*=2;
                x=0;
                System.out.println("");
            }
        }
    }

    public static void main(String[] args){
        MinPQ<Integer> pqTest = new MinPQ<>();
        for (int i =0; i < 21; i++){
            pqTest.insert(StdRandom.uniformInt(100));
        }
        pqTest.visualizer();
        int max = pqTest.delMin();
        System.out.println("\nDeleted min item :"+max);
        pqTest.visualizer();
        System.out.println("Test ctor");
        MinPQ<Integer> testCtor = new MinPQ<>(new Integer[]{1,2,3,79,43,12});
        testCtor.visualizer();
    }
}