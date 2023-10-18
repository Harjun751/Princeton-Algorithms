import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;

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
        SeamCarverWorking working = new SeamCarverWorking(pic);


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
                working.removeVerticalSeam(seam);
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
                working.removeHorizontalSeam(seam);
            }

            // now, check if ALL the energies return the same value for cached vs non. cached
            for (int y = 0; y < carver.height(); y++){
                for (int x = 0; x < carver.width(); x++){
                    double calculatedEnergy = carver.energy(x, y);
                    double workingEnergy = working.energy(x, y);
                    assertEquals(calculatedEnergy, workingEnergy);
                    System.out.println(String.format("Row: %d \tCol: %d\tEnergy: %.4f\t\tWorking: %.4f",y ,x, calculatedEnergy, workingEnergy));
                }
            }
            System.out.println("\n==================================\n\tCOMPLETED THIS PICTURE\n\t WIDTH:"+carver.width()+" HEIGHT:" + carver.height());
            System.out.println("\n==================================\n\n\n\n");
        }
        carver.picture();
        working.picture();
    }

    @Test
    public void timeTrial(){
        String filePath = new File("").getAbsolutePath() + "/inputs/chameleon.png";
        long totalTimeNew = 0;
        long totalTimeOld = 0;
        long genPicTimeNew = 0;
        long genPicTimeOld = 0;
        for (int i = 0; i < 3; i++){
            // start timer
            long startTime = System.nanoTime();
            SeamCarverWorking working = new SeamCarverWorking(new Picture(filePath));
            for (int x=0; x<100; x++){
                working.removeVerticalSeam(working.findVerticalSeam());
                working.removeHorizontalSeam(working.findHorizontalSeam());
            }
            // end timer
            long endTime = System.nanoTime();
            totalTimeOld += (endTime - startTime);
            startTime = System.nanoTime();
            working.picture();
            endTime = System.nanoTime();
            genPicTimeOld += (endTime - startTime);


            // start timer
            startTime = System.nanoTime();
            SeamCarver carver = new SeamCarver(new Picture(filePath));
            for (int x=0; x<100; x++){
                carver.removeVerticalSeam(carver.findVerticalSeam());
                carver.removeHorizontalSeam(carver.findHorizontalSeam());
            }
            // endTimer
            endTime = System.nanoTime();
            totalTimeNew += (endTime - startTime);
            startTime = System.nanoTime();
            carver.picture();
            endTime = System.nanoTime();
            genPicTimeNew += (endTime - startTime);
        }
        System.out.println("Average time for new implementation find/remove: " + (totalTimeNew/1000000000.0)/3.0 +"s");
        System.out.println("Average time for old implementation find/remove: " + (totalTimeOld/1000000000.0)/3.0 + "s");
        System.out.println("Average time for new implementation generate pic: " + (genPicTimeNew/1000000000.0)/3.0 +"s");
        System.out.println("Average time for old implementation generate pic: " + (genPicTimeOld/1000000000.0)/3.0 + "s");
//        Average time for new implementation: 3.788576983s
//        Average time for old implementation: 3.893677311333333s
        assertTrue(totalTimeNew < totalTimeOld);
    }

    @Test
    public void testChammy(){
        String filePath = new File("").getAbsolutePath() + "/inputs/chameleon.png";
        SeamCarver carver = new SeamCarver(new Picture(filePath));
        for (int x=0; x<100; x++){
            carver.removeVerticalSeam(carver.findVerticalSeam());
            carver.removeHorizontalSeam(carver.findHorizontalSeam());
        }
        carver.picture();
    }
    @Test
    public void timeTrialShort(){
        String filePath = new File("").getAbsolutePath() + "/inputs/chameleon.png";
        long totalTimeNew = 0;
        long totalTimeOld = 0;
        for (int i = 0; i < 1; i++){
            // start timer
            long startTime = System.nanoTime();
            SeamCarverWorking working = new SeamCarverWorking(new Picture(filePath));
            for (int x=0; x<100; x++){
                working.removeVerticalSeam(working.findVerticalSeam());
                working.removeHorizontalSeam(working.findHorizontalSeam());
            }
            // end timer
            long endTime = System.nanoTime();
            totalTimeOld += (endTime - startTime);


            // start timer
            startTime = System.nanoTime();
            SeamCarver carver = new SeamCarver(new Picture(filePath));
            for (int x=0; x<100; x++){
                carver.removeVerticalSeam(carver.findVerticalSeam());
                carver.removeHorizontalSeam(carver.findHorizontalSeam());
            }
            // endTimer
            endTime = System.nanoTime();
            totalTimeNew += (endTime - startTime);
        }
        System.out.println("Average time for new implementation: " + (totalTimeNew/1000000000.0) +"s");
        System.out.println("Average time for old implementation: " + (totalTimeOld/1000000000.0) + "s");
//        Average time for new implementation: 3.8s
        assertTrue(totalTimeNew < totalTimeOld);
    }
}