

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class SeamCarver {
    private int width;
    private int height;

    // pixel representation of the image
    private int[][] pixels;

    private Double[][] energies;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        // make a copy of the picture object
        Picture pic = new Picture(picture);
        this.width = pic.width();   // 3
        this.height = pic.height(); // 7 ; 3 x 7 pic
        this.energies = new Double[height][width];

        this.pixels = new int[height][width];
        // create a 2d int array of RGB int-encoded pixels
        // y represents which row
        for (int y = 0; y < this.height; y++) {
            // x represents which column
            for (int x = 0; x < this.width; x++) {
                pixels[y][x] = pic.getRGB(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return setPic();
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
        if (energies[y][x] == null) {
            energies[y][x] = this.calcEnergy(x, y);
        }
        return energies[y][x];
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
        IndexMinPQ<Double> minPQ;

        private DijkstraSP(boolean vertical) {
            this.vertical = vertical;
            pathTo = new Integer[width * height + 2];
            distTo = new Double[width * height + 2];
            minPQ = new IndexMinPQ<Double>(width * height + 2);

            for (int v = 0; v < width * height + 2; v++)
                distTo[v] = Double.POSITIVE_INFINITY;
            distTo[width * height] = 0.0;

            minPQ.insert(width * height, 0.0);
            while (!minPQ.isEmpty()) {
                int v = minPQ.delMin();
                for (int w : getAdjIndex(v))
                    relax(w, v);
            }
        }

        private void relax(int w, int v) {
            double wEnergy = getEnergy(w);
            if (distTo[w] > distTo[v] + wEnergy) {
                // the distance to w is the total combined energies
                distTo[w] = distTo[v] + wEnergy;
                if (minPQ.contains(w)) {
                    minPQ.decreaseKey(w, distTo[w]);
                } else {
                    minPQ.insert(w, distTo[w]);
                }
                pathTo[w] = v;
            }
        }

        private Iterable<Integer> getAdjIndex(int index) {
            if (vertical) {
                return getVerticalAdjIndex(index);
            } else {
                return getHorizontalAdjIndex(index);
            }
        }

        private Iterable<Integer> getHorizontalAdjIndex(int index) {
            Stack<Integer> adj = new Stack<>();

            if (index >= width * height) {
                // top pseudo element
                for (int i = 0; i < height; i++) {
                    adj.push(width * i);
                }
                return adj;
            }

            int bottomIndex = index + 1;
            int bottomLeftIndex = index - width + 1;
            int bottomRightIndex = index + width + 1;

//            if (bottomIndex >= width * height) {
            if (index % width == width - 1) {
                // reached bottom row
                adj.push(width * height + 1);
                return adj;
            } else {
                adj.push(bottomIndex);
            }

            // check if bottom left and right are valid vertexes
            if (bottomLeftIndex % width == index % width + 1 && bottomLeftIndex < width * height) {
                adj.push(bottomLeftIndex);
            }
            if (bottomRightIndex % width == index % width + 1 && bottomRightIndex < width * height) {
                adj.push(bottomRightIndex);
            }
            return adj;
        }

        private Iterable<Integer> getVerticalAdjIndex(int index) {
            Stack<Integer> adj = new Stack<Integer>();
            if (index == width * height) {
                // top pseudo element
                for (int i = 0; i < width; i++) {
                    adj.push(i);
                }
                return adj;
            }
            int bottomIndex = index + width;
            int bottomLeftIndex = index + width - 1;
            int bottomRightIndex = index + width + 1;

            if (bottomIndex >= width * height) {
                // bottom pseudo element
                adj.push(width * height + 1);
                return adj;
            } else {
                adj.push(bottomIndex);
            }
            int originalRow = index / width;

            // check if bottom left and right are valid vertexes
            if (bottomLeftIndex / width == originalRow + 1) {
                adj.push(bottomLeftIndex);
            }
            if (bottomRightIndex / width == originalRow + 1) {
                adj.push(bottomRightIndex);
            }
            return adj;
        }

        private double getEnergy(int v) {
            if (v == width * height + 1) {
                return 0;
            }
            int rowY = v / width;
            int colX = v % width;
            return energy(colX, rowY);
        }

        public int[] bottomSP() {
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
                if (path >= width * height) {
                    i--;
                    path = pathTo[path];
                    continue;
                }
                if (vertical) {
                    SP[i] = path % width;
                } else {
                    SP[i] = path / width;
                }
                i--;
                path = pathTo[path];
            }
            return SP;
        }

        public double bottomEnergy() {
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
        pixels = transpose(pixels);
        energies = transpose(energies);
        int old = width;
        width = height;
        height = old;

        removeVerticalSeam(seam);

        pixels = transpose(pixels);
        energies = transpose(energies);
        int nold = width;
        width = height;
        height = nold;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
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
        Stack<Integer[]> toRemove = new Stack<Integer[]>();

        for (int w : seam) {
            if (Math.abs(w - prevSeam) > 1 || w < 0) {
                throw new IllegalArgumentException();
            }
            if (w >= pixels[j].length) {
                throw new IllegalArgumentException();
            }
            prevSeam = w;
            int[] newArr = new int[width - 1];
            System.arraycopy(pixels[j], 0, newArr, 0, w);
            System.arraycopy(pixels[j], w + 1, newArr, w, width - w - 1);
            pixels[j] = newArr;

            // handling the energy array
            Double[] newPixArr = new Double[width - 1];
            System.arraycopy(energies[j], 0, newPixArr, 0, w);
            System.arraycopy(energies[j], w + 1, newPixArr, w, width - w - 1);
            energies[j] = newPixArr;
            toRemove.push(new Integer[]{j, w});
            toRemove.push(new Integer[]{j, w - 1});
            j++;
        }

        width -= 1;
        for (Integer[] coords : toRemove) {
            int h = coords[0];
            int k = coords[1];
            if (h < 0 || h > height - 1 || k < 0 || k > width - 1) {
                continue;
            }
            energies[h][k] = null;
        }
    }

    private int[][] transpose(int[][] array) {
        // empty or unset array, nothing do to here
        if (array == null || array.length == 0)
            return array;

        int width = array.length;
        int height = array[0].length;

        int[][] array_new = new int[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                array_new[y][x] = array[x][y];
            }
        }

        return array_new;
    }
    private Double[][] transpose(Double[][] array) {
        // empty or unset array, nothing do to here
        if (array == null || array.length == 0)
            return array;

        int width = array.length;
        int height = array[0].length;

        Double[][] array_new = new Double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                array_new[y][x] = array[x][y];
            }
        }

        return array_new;
    }

    private Picture setPic() {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pic.setRGB(x, y, pixels[y][x]);
            }
        }
        return pic;
    }

    private double calcEnergy(int x, int y) {
        // column x, row y
        // border elements
        if (x == 0 || x + 1 == width || y == 0 || y + 1 == height) {
            return 1000;
        }

        int[] upperPixel = decodeRGB(this.pixels[y][x + 1]);
        int[] lowerPixel = decodeRGB(this.pixels[y][x - 1]);
        int[] leftPixel = decodeRGB(this.pixels[y - 1][x]);
        int[] rightPixel = decodeRGB(this.pixels[y + 1][x]);


        // calculate proper energy
        double rX = rightPixel[0] - leftPixel[0];
        double rY = lowerPixel[0] - upperPixel[0];
        double gX = rightPixel[1] - leftPixel[1];
        double gY = lowerPixel[1] - upperPixel[1];
        double bX = rightPixel[2] - leftPixel[2];
        double bY = lowerPixel[2] - upperPixel[2];

        double xEnergy = rX * rX + gX * gX + bX * bX;
        double yEnergy = rY * rY + gY * gY + bY * bY;

        return Math.sqrt(xEnergy + yEnergy);
    }

    private int[] decodeRGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        return new int[]{r, g, b};
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("/home/arjun/Documents/prinston-algos/week-7/seams/inputs/6x5.png"));
        int[] seam = new int[] { 1, 2, 1, 2, 1, 0 };
        sc.removeHorizontalSeam(seam);
    }
}