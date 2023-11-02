import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SeamCarverTest {
    @Test
    public void energyABTest(){
        String[] hexes = new String[] { "#040003" ,"#070707","#030507","#030109","#030203","#040901","#090103","#010206","#080108","#040709","#070106","#080005","#010001","#070808","#080004","#040509","#030101","#010406","#030808","#040605","#040004","#000506","#030307","#020507","#070107","#040807","#060903","#070401","#030708","#020803","#060406","#040109","#080404","#080507","#060204","#030803" };
        Picture pic = new Picture(6, 6);
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 6; j++){
                int index = i*6 + j;
                Color color = Color.decode(hexes[index]);
                pic.set(j, i, color);
            }
        }
        SeamCarver carver = new SeamCarver(pic);


        while(carver.width() > 1 && carver.height() > 1){
            boolean isVertical;
            // Set if the code will generate a vertical seam or not
            if (carver.width() > 1 && carver.height() > 1){
                // if both width & height > 1, we can randomly choose a
                // vertical/horizontal seam
                isVertical = StdRandom.bernoulli(0.5);
            } else if (carver.width()==1){
                // if the width is 1, we can only make new horizontal seams
                isVertical = false;
            } else {
                // else (height=1), we can only make vertical seams
                isVertical = true;
            }

            int height = carver.height();
            int width = carver.width();
            int i = 0;

            if (isVertical){
                int[] seam = new int[height];
                // create a vertical seam
                int h = StdRandom.uniformInt(width);
                while (i<height){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < width){
                                h+=1;
                            }
                        } else {
                            if (h-1 > 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeVerticalSeam(seam);
            } else {
                int[] seam = new int[width];
                // create a horizontal seam
                int h = StdRandom.uniformInt(height);
                while (i<width){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < height){
                                h+=1;
                            }
                        } else {
                            if (h-1 > 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeHorizontalSeam(seam);
            }

            // now, check if ALL the energies return the same value for cached vs non. cached
            for (int y = 0; y < carver.height(); y++){
                for (int x = 0; x < carver.width(); x++){
                    double calculatedEnergy = carver.energy(x, y);
                    double cachedEnergy = carver.cachedEnergy(x, y);
                    assertEquals(calculatedEnergy, cachedEnergy);
                    System.out.println(String.format("Row: %d \tCol: %d\tEnergy: %.4f\t\tCached: %.4f",y ,x, calculatedEnergy, cachedEnergy));
                }
            }
            System.out.println("\n==================================\n\tCOMPLETED THIS PICTURE\n\t WIDTH:"+carver.width()+" HEIGHT:" + carver.height());
            System.out.println("\n==================================\n\n\n\n");

        }
    }



    @Test
    public void energyABTestHJOcean(){
        Picture pic = new Picture("C:\\Users\\Arjun\\Documents\\Personal Documents\\Princeton-Algorithms\\week-7\\seams\\inputs\\HJocean.png");
        SeamCarver carver = new SeamCarver(pic);


        while(carver.width() > 1 || carver.height() > 1){
            boolean isVertical;
            if (carver.width() > 1 && carver.height() > 1){
                isVertical = StdRandom.bernoulli(0.5);
            } else if (carver.width()==1){
                isVertical = false;
            } else {
                isVertical = true;
            }
            // randomly choose vertical/horizontal

            int height = carver.height();
            int width = carver.width();
            int i = 0;

            if (isVertical){
                int[] seam = new int[height];
                // create a vertical seam
                int h = StdRandom.uniformInt(width);
                while (i<height){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < width){
                                h+=1;
                            }
                        } else {
                            if (h-1 >= 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeVerticalSeam(seam);
            } else {
                int[] seam = new int[width];
                // create a horizontal seam
                int h = StdRandom.uniformInt(height);
                while (i<width){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < height){
                                h+=1;
                            }
                        } else {
                            if (h-1 >= 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeHorizontalSeam(seam);
            }

            // now, check if ALL the energies return the same value for cached vs non. cached
            for (int y = 0; y < carver.height(); y++){
                for (int x = 0; x < carver.width(); x++){
                    double calculatedEnergy = carver.energy(x, y);
                    double cachedEnergy = carver.cachedEnergy(x, y);
                    assertEquals(calculatedEnergy, cachedEnergy);
//                    System.out.println(String.format("Row: %d \tCol: %d\tEnergy: %.4f\t\tCached: %.4f",y ,x, calculatedEnergy, cachedEnergy));
                }
            }
            System.out.println("\n==================================\n\tCOMPLETED THIS PICTURE\n\t WIDTH:"+carver.width()+" HEIGHT:" + carver.height());
//            System.out.println("\n==================================\n\n\n\n");

        }
    }

    @Test
    public void energyABTestHugeFile(){
        Picture pic = new Picture("C:\\Users\\Arjun\\Documents\\Personal Documents\\Princeton-Algorithms\\week-7\\seams\\inputs\\huge.jpg");
        SeamCarver carver = new SeamCarver(pic);


        while(carver.width() > 1 || carver.height() > 1){
            boolean isVertical;
            if (carver.width() > 1 && carver.height() > 1){
                isVertical = StdRandom.bernoulli(0.5);
            } else if (carver.width()==1){
                isVertical = false;
            } else {
                isVertical = true;
            }
            // randomly choose vertical/horizontal

            int height = carver.height();
            int width = carver.width();
            int i = 0;

            if (isVertical){
                int[] seam = new int[height];
                // create a vertical seam
                int h = StdRandom.uniformInt(width);
                while (i<height){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < width){
                                h+=1;
                            }
                        } else {
                            if (h-1 > 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeVerticalSeam(seam);
            } else {
                int[] seam = new int[width];
                // create a horizontal seam
                int h = StdRandom.uniformInt(height);
                while (i<width){
                    // 3 options: Remain the same value as above,
                    // or add or minus 1
                    boolean changeValue = StdRandom.bernoulli(0.5);
                    if (changeValue){
                        boolean add = StdRandom.bernoulli(0.5);
                        if (add){
                            // ensure that the values don't leave the range
                            if (h+1 < height){
                                h+=1;
                            }
                        } else {
                            if (h-1 > 0){
                                h-=1;
                            }
                        }
                    }
                    seam[i] = h;
                    i++;
                }
                // remove the seam
                carver.removeHorizontalSeam(seam);
            }

            // now, check if ALL the energies return the same value for cached vs non. cached
            for (int y = 0; y < carver.height(); y++){
                for (int x = 0; x < carver.width(); x++){
                    double calculatedEnergy = carver.energy(x, y);
                    double cachedEnergy = carver.cachedEnergy(x, y);
                    assertEquals(calculatedEnergy, cachedEnergy);
//                    System.out.println(String.format("Row: %d \tCol: %d\tEnergy: %.4f\t\tCached: %.4f",y ,x, calculatedEnergy, cachedEnergy));
                }
            }
            System.out.println("\n==================================\n\tCOMPLETED THIS PICTURE\n\t WIDTH:"+carver.width()+" HEIGHT:" + carver.height());
//            System.out.println("\n==================================\n\n\n\n");

        }
    }

}