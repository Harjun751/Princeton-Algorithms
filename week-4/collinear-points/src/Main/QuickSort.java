public class QuickSort {
    private int partition(Comparable<Object>[] arr, int lo, int hi){
        int pivot = lo;
        int i = lo+1;
        int j = hi;

        while (true){
            while(i<hi){
//                if (arr[i]>arr[pivot]){
                if (arr[i].compareTo(arr[pivot]) == 1){
                    break;
                }
                i++;
            }

            while(j>lo) {
                // find a j so that arr[j] is less than arr[pivot]
//                if (arr[j] < arr[pivot]) {
                if (arr[j].compareTo(arr[pivot]) == -1) {
                    break;
                }
                // decrement j, keep searching
                j--;
            }


            // Check if i has gone out of bounds
            if (i>=j){
                // we have found the convergence point
                // e.g.
                // [7, 1, 2, 3, 8, 10]
                //           j  i
                // swap a[j] with a[pivot]
                Comparable<Object> old = arr[pivot];
                arr[pivot] = arr[j];
                arr[j] = old;
                return j;
            } else {
                // swap i & j
                Comparable<Object> old = arr[i];
                arr[i] = arr[j];
                arr[j] = old;
            }

            // check if i has exceeded hi?
        }
    }

    // TODO: set private
    private void quickSort(Comparable<Object>[] arr, int lo, int hi){
        if (hi<=lo){
            return;
        }
        int piv = partition(arr,lo,hi);

        // now sort
        quickSort(arr, lo, piv-1);
        quickSort(arr, piv+1, hi);
    }
}
