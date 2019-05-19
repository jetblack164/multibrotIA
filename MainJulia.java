import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.*;


public class MainJulia {
    //Global Variable Declaration

    public static int maxIter = 1000;
    public static int sizex = 200;
    public static int sizey =200;
    public static double powerOfImaj = 0.5;
    public static double jreal=-0.8;
    public static double  jimaj=0.156;
    public static double gamma = 0.2;
    //Complex Number Functions

    public static double[] complexMultiply(double real, double imaj, double power) {
        double result[] = new double[2];

        if ((real == 0) & (imaj == 0)) {
            result[0] = 0;
            result[1] = 0;
            return result;
        } else {
            double angleRad = Math.atan(imaj / real);
            double magni = 0;
            if (real != 0) {
                magni = real / Math.cos(angleRad);
            } else {
                magni = imaj / Math.sin(angleRad);
            }


            angleRad = angleRad * power;
            magni = Math.pow(magni, power);

            imaj = Math.sin(angleRad) * magni;
            real = Math.cos(angleRad) * magni;

            result[0] = real;
            result[1] = imaj;

            return result;

        }
        //returns the result of the power operation
    }

    //Iteration Function. Does most of the hard work
    public static int iterCalculation(double x, double y) {
       // System.out.println("NEW CALC");
        double real = 0;
        double imaj = 0;
        double powRes[];
        double creal =x ; //jreal;
        double cimaj =y; //jimaj;
        int iter = 0;
        double escapeVal = 4;
        while (  (iter < MainJulia.maxIter)){ //(Math.pow(Math.pow(real, 2) + Math.pow(imaj, 2), (0.5)) < escapeVal) &       while ( (Math.pow(Math.pow(real, 2) + Math.pow(imaj, 2), (0.5)) < escapeVal) & (iter < MainJulia.maxIter))

            creal=(real*real+imaj*imaj)^(powerOfImaj*0.25/2)*Math.cos(powerOfImaj*0.25*Math.atan2(y,x)) + x;
            imaj=(x*x+y*y)^(powerOfImaj*0.25*/2)*Math.sin(powerOfImaj*0.25*Math.atan2(y,x)) + y;
            real=creal;

            /*
            //System.out.println("Magnitude"+(Math.pow(Math.pow(real, 2) + Math.pow(imaj, 2), (0.5))));
            powRes = complexMultiply(real, imaj, MainJulia.powerOfImaj);
            real = powRes[0] + creal;
            imaj = powRes[1] + cimaj;

            iter += 1;*/

           // if (Math.pow(Math.pow(real, 2) + Math.pow(imaj, 2), (0.5)) > escapeVal)
            double Magni=(real*real+imaj*imaj);
            if ( Magni>= escapeVal)
            {
                System.out.println((Magni));
                break;

            }
        }

        //System.out.println(iter);
        if (iter == MainJulia.maxIter) {
            iter = 0;
        }


        return iter;


    }

    //Image creator, creates the image after the calculations
    public static void imageCreation(int Iter[][],int ImageCount) {
        int sizex = MainJulia.sizex;
        int sizey = MainJulia.sizey;
        BufferedImage FinalImage = new BufferedImage(sizex, sizey, BufferedImage.TYPE_INT_ARGB);
        File f = null;


        for (int countY = 0; countY < sizey; ++countY) {
            for (int countX = 0; countX < sizex; ++countX) {
                int iterations = Iter[countX][countY];
                int rgb;
                if (iterations == 0) {
                    rgb = new Color(0, 0, 0).getRGB();
                } else {

                    //rgb = new Color(255, 255, 255).getRGB();
                    //Defines RBG colour
                    int rgbColour[]= ColourFinder(iterations,0,0);

                    //Sets RGB colour to Picture Array

                    //rgb= new Color(rgbColour[0],rgbColour[1],rgbColour[2]).getRGB();
                    rgb = new Color(
                            (int)(Math.pow(((double) iterations / (double) MainJulia.maxIter),MainJulia.gamma)   * 255),
                            0 ,
                            0
                    ).getRGB();
                }
                // System.out.println("iterations:" + (int)(((float)iterations/(float)Main.maxIter)*255));
                FinalImage.setRGB(countX, countY, rgb);
            }

        }

        try {
            f = new File("output"+ImageCount+".jpg");
            ImageIO.write(FinalImage, "png", f);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //Colour Calculations
    //Colour finder
    public static int[] ColourFinder (int counter,double x,double y){

        double r=0;
        double g=0;
        double b=0;
        //System.out.println("Current Iteration="+ counter);
        double absVal=  Math.pow( (Math.pow(x,2)+ Math.pow(y,2)),2);
        double h= (((counter))/ MainJulia.maxIter)*360;
       // System.out.println("Current Iteration="+ h);
        double s=1;
        double v=1;
        double c= v*s;
        double col = c *(1-Math.abs(  ((h/60)%2)-1    ));
        double m=v-c;

        if (h <= 60)
        {
         r=c;
         g=col;
         b=0;

        }
        else if ( h<=120 && h >60 )
        {
            r=col;
            g=c;
            b=0;
        }
        else if (h<=180 && h> 120){
            r=0;
            g=c;
            b=col;
        }
        else if (h<=240 && h>180){
            r=0;
            b=col;
            b=c;
        }
        else if (h<= 300 && h>240)
        {
            r=col;
            g=0;
            b=c;
        }
        else if (h<=360 && h>300){
            r=c;
            g=0;
            b=col;
        }

        int rFin= (int)( (r+m)*255);
        int gFin= (int)((g+m)*255);
        int bFin= (int) ((b+m)*255);
        int iterColour[]={rFin,gFin,bFin};
        return iterColour;
    }

    //The Actual Calculation part of the Mandel/Julia set
    public static void MandelCalc(int StartIter,int EndIter) {
        int MandelPower=0;
        for (int ImageCount= StartIter;ImageCount < (EndIter+1); ++ImageCount) {

            MainJulia.powerOfImaj=ImageCount*0.1;

            //how far are we zoomed in?
            double currentZoom = 1;

            //determine the dimensions of the Mandelbrot set

            //determines pixel count for both x and y axes
            int sizex = MainJulia.sizex;
            int sizey = MainJulia.sizey;

            double maxY = 2; //how far y will go on
            double maxX = 2; //how far x will go on either side


            double centerx = 0;
            double centery = 0;

            double currX = 0; //the current X position to calculate (can be seen as real part)
            double currY = 0; //the current Y position to calculate (can be seen as imaginary part)

            //determines how much space is between each pixel in terms distances on the imaginary number plane
            double xPerPixel = (2 * maxX * currentZoom) / sizex;
            double yPerPixel = (2 * maxY * currentZoom) / sizey;
            int results[][] = new int[sizex][sizey];


            for (int counterY = 0; counterY < sizey; ++counterY) {
                for (int counterX = 0; counterX < sizex; ++counterX) {
                    currX = (counterX * xPerPixel) - maxX;
                    currY = (counterY * yPerPixel) - maxY;


                    int iter = iterCalculation(currX, currY);
                    results[counterX][counterY] = iter;


                }
            }
            imageCreation(results, ImageCount);
        }
    }


    public static void textCreation()
    {
        int sizex=50;
        int sizey=50;
        //for (int sizey = 0; sizey < sizey; ++sizey) {
          //  for (int sizex = 0; sizex < sizey; ++sizex) {






            //}

            //}




    }

    public static void main(String[] args) {

MandelCalc(2,20 );
      /*  while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Give Real");
            float real = sc.nextFloat();
            System.out.println(real);
            System.out.println("Give Imaj");
            float imaj = sc.nextFloat();
            System.out.println(imaj);
            double result[] =complexMultiply(real, imaj, 0.5);
            System.out.println(result[0]+" "+result[1]);
        }*/


                /*
                try {

                    File fout=new File("out.txt");
                    FileOutputStream fos=new FileOutputStream(fout);
                    BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(fos));

                    for(int countY=0;countY<sizey;++countY)
                    {
                        for (int countX=0;countX<sizex;++countX)
                        {
                            int iterations=results[countX][countY];
                            if (iterations >9)
                            {
                                iterations=9;
                                bw.write(Integer.toString(iterations));
                            }
                            else {
                                bw.write(Integer.toString(iterations));
                            }
                        }
                    bw.newLine();
                    }
                bw.close();

                }
                catch(IOException e)
                {
                    System.out.println("Uh oh!! Error!");
                }
                */








    }













}
