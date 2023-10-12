import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
    private int width;
    private int height;

    // pixel representation of the image
    private int[][] pixels;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        // make a copy of the picture object
        Picture pic = new Picture(picture);
        this.width = pic.width();
        this.height = pic.height();

        // create a 2d int array of RGB int-encoded pixels
        // reminder: y is for row, while x is for col.
        this.pixels = new int[height][width];
        for (int y = 0; y < this.height; y++) {
            // x represents which column
            for (int x = 0; x < this.width; x++) {
                pixels[y][x] = pic.getRGB(x, y);
            }
        }
    }

    public Picture picture() {
        // Populate a new Picture object
        // pixel-by-pixel using the array.
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pic.setRGB(x, y, pixels[y][x]);
            }
        }
        return pic;
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
        return this.calcEnergy(x, y);
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

    private class DijkstraSP {
        Integer[] pathTo;
        Double[] distTo;
        boolean vertical;
        Queue<Integer> minPQ;
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
            minPQ = new Queue<>();

            // initialize the distTo arr with the max double value
            for (int v = 0; v < width * height + 2; v++)
                distTo[v] = Double.POSITIVE_INFINITY;
            // Set the distance for the top pseudo-element to be 0
            distTo[width * height] = 0.0;

            // Start iterating through the minPQ array
            // by firstly enqueuing the top pseudo-element
            minPQ.enqueue(width * height);
            while (!minPQ.isEmpty()) {
                // get the pixel with the lowest priority/energy
                int v = minPQ.dequeue();
                for (int w : getAdjIndex(v))
                    relax(w, v);
            }
        }

        private void relax(int w, int v) {
            double wEnergy = getEnergy(w);
            // the distance to w is the total combined energies
            if (distTo[w] > distTo[v] + wEnergy) {
                // found a better path to w; update distance, path.
                distTo[w] = distTo[v] + wEnergy;
                if (!marked[w]){
                    minPQ.enqueue(w);
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
            int rowY = v / width;
            int colX = v % width;
            return energy(colX, rowY);
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
                i = height;
            } else {
                SP = new int[width];
                i = width;
            }

            Integer path = width * height + 1;
            while (path != null) {
                // if path is > width*height, it is the
                // top/btm pseudo-elements. we don't want
                // the pseudo-elements in the path array.
                if (path < width * height) {
                    if (vertical) {
                        SP[i] = path % width;
                    } else {
                        SP[i] = path / width;
                    }
                }
                i--;
                path = pathTo[path];
            }
            return SP;
        }

        // hehe
        public double bottomEnergy() {
            // get energy of the path
            // by obtaining the energy of the bottom
            // pseudo-element
            return distTo[width * height + 1];
        }
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

            // foreach column of delete input
            // find the row in which it resides

            // DONT SHIFT ARRAY
            // SIMPLY COPY THE BOTTOM TO UP
            int i = seam[col];
            while (i < height - 1){
                pixels[i][col] = pixels[i+1][col];
                i++;
            }
        }
        pixels[height-1] = null;
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
        int j = 0;
        int prevSeam = seam[0];

        for (int w : seam) {
            // ensure that items in the seam don't differ
            // by more than +-1 -> if it did, the pixels would
            // be incongruous
            if (Math.abs(w - prevSeam) > 1 || w < 0) {
                throw new IllegalArgumentException();
            }
            if (w >= width) {
                throw new IllegalArgumentException();
            }
            prevSeam = w;

            // Copy the pixels to a new array of -1 size
            // Leave out the pixel at the seam when copying
            int[] newArr = new int[width - 1];
            System.arraycopy(pixels[j], 0, newArr, 0, w);
            System.arraycopy(pixels[j], w + 1, newArr, w, width - w - 1);
            pixels[j] = newArr;
            j++;
        }

        // decrease the width
        width -= 1;
    }

    private double calcEnergy(int x, int y) {
        // reminder: column x, row y
        // Below checks elements on the BORDER of the picture
        // energy for borders is max, 1000.
        if (x == 0 || x + 1 == width || y == 0 || y + 1 == height) {
            return 1000;
        }

        // decode the rgb pixels
        int[] upperPixel = decodeRGB(this.pixels[y][x + 1]);
        int[] lowerPixel = decodeRGB(this.pixels[y][x - 1]);
        int[] leftPixel = decodeRGB(this.pixels[y - 1][x]);
        int[] rightPixel = decodeRGB(this.pixels[y + 1][x]);


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

    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("/home/arjun/Documents/prinston-algos/week-7/seams/inputs/6x5.png"));
        sc.removeVerticalSeam(new int[] { 3, 4, 5, 6, 5});
        // sc.removeHorizontalSeam(sc.findHorizontalSeam());
        // 1d array allows not to calculate transposed matrix back and forth, because function which will convert 2d coordinates to 1d can calculate correct transposed offset:
        sc.picture();
    }
}