import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    private int width;
    private int height;
    private int trueW;
    private int trueH;

    // pixel representation of the image
    private int[] pixels;



    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        // make a copy of the picture object
        Picture pic = new Picture(picture);
        this.width = pic.width();
        this.height = pic.height();
        this.trueH = height;
        this.trueW = width;

        // create a 1d int array of RGB int-encoded pixels
        // reminder: y is for row, while x is for col.
        this.pixels = new int[height * width];
        for (int y = 0; y < this.height; y++) {
            // x represents which column
            for (int x = 0; x < this.width; x++) {
                pixels[getIndex(x, y)] = pic.getRGB(x, y);
            }
        }
    }

    public Picture picture() {
        // Populate a new Picture object
        // pixel-by-pixel using the array.
        fixArray();
        Picture pic = new Picture(width, height);
        int v = 0;
        int[] XY;
        while (v != width*height){
            XY = getXY(v);
            pic.setRGB(XY[0], XY[1], pixels[v]);
            v++;
        }
        return pic;
    }

    private void fixArray(){
        // basically, we have a 1d picture array with some -1s in there
        // 123 456 -1
        // 789 -1 124       => 123 456 -1 789 -1 124
        // this is because a hor/ver seam has been removed.
        // we need to fix the array to become:
        // 123 456
        // 789 124 => 123 456 789 124

        // SO: we loop thru the pixel array
        // once we find a -1:
        // look for the next non -1 integer
        // set the current vertex as that number
        int newIndex = 0;
        int[] newArr = new int[width*height];
        int searchIndex = 0;
        while (searchIndex < pixels.length){
            if (pixels[searchIndex] != Integer.MAX_VALUE){
                // if curr pixel is a valid number,
                // add it to the new array.
                newArr[newIndex] = pixels[searchIndex];
                newIndex++;
                searchIndex++;
            } else {
                // go to the next line of the image
                // 12 13 14 0
                // 15 16 17 0
                searchIndex+=(trueW-width);
            }
        }
        this.pixels = newArr;
        trueW = width;
        trueH = height;
    }

    // width of current picture
    public int width() {
        return this.width;
    }

    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1) {
            throw new IllegalArgumentException();
        }
        return calcEnergy(x, y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        DijkstraSP sp = new DijkstraSP(false);
        return sp.bottomSP();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        DijkstraSP sp = new DijkstraSP(true);
        return sp.bottomSP();
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (height == 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length!=width){
            throw new IllegalArgumentException();
        }
        int prevSeam = seam[0];
        for (int col = 0; col < seam.length; col++){
            if (Math.abs(seam[col] - prevSeam) > 1 || seam[col] < 0) {
                throw new IllegalArgumentException();
            }
            if (seam[col] >= height) {
                throw new IllegalArgumentException();
            }
            prevSeam = seam[col];

            int row = seam[col];
            int v = getIndex(col, row);

            // Shift the pixels below the seam upwards
            // to cover the gap (the "gap" is made by overwriting the pixel)
            int lowerIndex = getLowerIndex(v);
            while (lowerIndex < trueH*trueW){
                pixels[v] = pixels[lowerIndex];
                v = lowerIndex;
                lowerIndex = getLowerIndex(v);
            }

            // set last row to fill-in value
            pixels[v] = Integer.MAX_VALUE;
        }
        height-=1;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // do basic checks on seam object
        if (seam==null){
            throw new IllegalArgumentException();
        }
        if (seam.length!=height){
            throw new IllegalArgumentException();
        }
        if (width == 1) {
            throw new IllegalArgumentException();
        }
        int row = 0;
        int prevSeam = seam[0];

        for (int col : seam) {
            // ensure that items in the seam don't differ
            // by more than +-1 -> if it did, the pixels would
            // be incongruous
            if (Math.abs(col - prevSeam) > 1 || col < 0) {
                throw new IllegalArgumentException();
            }
            if (col >= width) {
                throw new IllegalArgumentException();
            }
            prevSeam = col;

            int v = getIndex(col, row);
            // get the "max" index of the current row
            // i.e. the rightmost pixel in the row
            int max = (row+1) * trueW - 1;

            // While there are still pixels on the right,
            // shift the pixels leftward to cover the gap
            while (v+1 <= max){
                pixels[v] = pixels[v+1];
                v++;
            }

            // set the rightmost pixel to fill-in value
            pixels[v] = Integer.MAX_VALUE;
            row++;
        }

        // decrease the width
        width -= 1;
    }

    private double calcEnergy(int x, int y) {
        // reminder: column x, row y
        // Below checks elements on the BORDER of the picture
        // energy for borders is max, 1000.
        if (x == 0 || x == width-1 || y == 0 || y == height-1) {
            return 1000;
        }

        int v = getIndex(x, y);

        // decode the rgb pixels
        int[] upperPixel = decodeRGB(this.pixels[getUpperIndex(v)]);
        int[] lowerPixel = decodeRGB(this.pixels[getLowerIndex(v)]);
        int[] leftPixel = decodeRGB(this.pixels[v-1]);
        int[] rightPixel = decodeRGB(this.pixels[v+1]);


        // calculate energy using formula
        double rX = rightPixel[0] - leftPixel[0];
        double gX = rightPixel[1] - leftPixel[1];
        double bX = rightPixel[2] - leftPixel[2];

        double rY = lowerPixel[0] - upperPixel[0];
        double gY = lowerPixel[1] - upperPixel[1];
        double bY = lowerPixel[2] - upperPixel[2];

        double xEnergy = rX * rX + gX * gX + bX * bX;
        double yEnergy = rY * rY + gY * gY + bY * bY;

        return Math.sqrt(xEnergy + yEnergy);
    }

    private int[] decodeRGB(int rgb) {
        // decode the encoded RGB integers
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return new int[]{r, g, b};
    }

    private int[] getXY(int v){
        int rowY = v / width;
        int colX = v % width;
        return new int[] { colX, rowY };
    }

    private int getIndex(int x, int y){
        return y*trueW + x;
    }

    private int getUpperIndex(int v){
        return v - trueW;
    }

    private int getLowerIndex(int v){
        return v + trueW;
    }


    private class DijkstraSP {
        Integer[] pathTo;
        Double[] distTo;
        boolean vertical;
        Queue<Integer> queue;
        boolean[] marked;

        private DijkstraSP(boolean vertical) {
            this.vertical = vertical;
            // init arrays with width*height+2
            // the +2 are the PSEUDO top & bottom elements
            // the pseudo-elements allow us to find the best path
            // from top to bottom in one go.
            pathTo = new Integer[width * height + 2];
            distTo = new Double[width * height + 2];
            marked = new boolean[width * height + 2];
            queue = new Queue<>();

            // Set the distance for the top pseudo-element to be 0
            distTo[width * height] = 0.0;

            // Start iterating through the queue array
            // by firstly enqueuing the top pseudo-element
            queue.enqueue(width * height);
            while (!queue.isEmpty()) {
                int v = queue.dequeue();
                for (int w : getAdjIndex(v))
                    relax(w, v);
            }
        }

        private void relax(int w, int v) {
            double wEnergy = getEnergy(w);
            // the distance to w is the total combined energies
            if (distTo[w]==null || distTo[w] > distTo[v] + wEnergy) {
                // found a better path to w; update distance, path.
                distTo[w] = distTo[v] + wEnergy;
                if (!marked[w]){
                    queue.enqueue(w);
                    marked[w] = true;
                }
                // Update the path to W to go thru V.
                pathTo[w] = v;
            }
        }

        private double getEnergy(int v) {
            // convert 1D index
            // to 2D (x, y) coordinates
            // then obtain the energy of that pixel.
            if (v == width * height + 1) {
                return 0;
            }
            int[] XY = getXY(v);
            return energy(XY[0], XY[1]);
        }

        private Iterable<Integer> getAdjIndex(int index) {
            // Obtains the adjacent indexes of the image.
            // Behaviour for vertical and horizontal paths are different,
            // so more specific methods are called.
            if (vertical) {
                return getVerticalAdjIndex(index);
            } else {
                return getHorizontalAdjIndex(index);
            }
        }

        private Iterable<Integer> getHorizontalAdjIndex(int index) {
            Stack<Integer> adj = new Stack<>();

            // if index is > width*height,
            // the index item is the
            // top pseudo-element
            if (index >= width * height) {
                for (int i = 0; i < height; i++) {
                    // add every top (horizontal) pixel to adjacent
                    adj.push(width * i);
                }
                return adj;
            }

            // if the remainder of index/width is equal to width-1
            // the index item is right BEFORE(ABOVE) the bottom pseudo-element
            // pf. diy
            if (index % width == width - 1) {
                // add the bottom pseudo-element
                adj.push(width * height + 1);
                return adj;
            }

            // Obtain adjacent bottom elements.
            int bottomIndex = index + 1;
            int bottomLeftIndex = index - width + 1;
            int bottomRightIndex = index + width + 1;

            adj.push(bottomIndex);

            // check if bottom left and right are valid vertexes -> e.g. not corner
            if (bottomLeftIndex % width == index % width + 1 && bottomLeftIndex < width * height) {
                adj.push(bottomLeftIndex);
            }
            if (bottomRightIndex % width == index % width + 1 && bottomRightIndex < width * height) {
                adj.push(bottomRightIndex);
            }
            return adj;
        }

        private Iterable<Integer> getVerticalAdjIndex(int index) {
            Stack<Integer> adj = new Stack<>();

            // if index is equal to width*height, it is the top
            // pseudo-element
            if (index == width * height) {
                // in this case, add every top pixel to the
                // adjacent list
                for (int i = 0; i < width; i++) {
                    adj.push(i);
                }
                return adj;
            }

            int bottomIndex = index + width;
            int bottomLeftIndex = index + width - 1;
            int bottomRightIndex = index + width + 1;

            if (bottomIndex >= width * height) {
                // return the bottom pseudo-element
                adj.push(width * height + 1);
                return adj;
            } else {
                adj.push(bottomIndex);
            }

            // check if bottom left and right are valid vertexes
            int originalRow = index / width;
            if (bottomLeftIndex / width == originalRow + 1) {
                adj.push(bottomLeftIndex);
            }
            if (bottomRightIndex / width == originalRow + 1) {
                adj.push(bottomRightIndex);
            }
            return adj;
        }

        public int[] bottomSP() {
            // traverse the best path and return it.
            int[] SP;
            int i;
            if (vertical) {
                SP = new int[height];
                i = height-1;
            } else {
                SP = new int[width];
                i = width-1;
            }

            Integer path = width * height + 1;
            path = pathTo[path];
            while (path!=width*height) {
                // if path is > width*height, it is the
                // top/btm pseudo-elements. we don't want
                // the pseudo-elements in the path array.
                if (vertical) {
                    SP[i] = path % width;
                } else {
                    SP[i] = path / width;
                }
                path = pathTo[path];

                i--;
            }
            return SP;
        }
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("/home/arjun/Documents/prinston-algos/week-7/seams/inputs/7x3.png"));
        sc.removeVerticalSeam(new int[] {2, 2, 2});
        System.out.println(sc.energy(2,0));
        // 1d array allows not to calculate transposed matrix back and forth, because function which will convert 2d coordinates to 1d can calculate correct transposed offset:
        sc.picture();
    }
}