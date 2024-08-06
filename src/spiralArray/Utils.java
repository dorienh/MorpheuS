package spiralArray;

import java.util.ArrayList;


/**
 * Created by dorien on 26/10/15.
 *
 */
 public class Utils{
 /**
 * Returns distance between 3D set of coords
 *
 * @param x1
 *            first x coord
 * @param y1
 *            first y coord
 * @param z1
 *            first z coord
 * @param x2
 *            second x coord
 * @param y2
 *            second y coord
 * @param z2
 *            second z coord
 * @return distance between coords
 */





 public static double getDistanceFromPositions(double[] pos1, double[] pos2){

     return getDistance(pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]);
 }


public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2)
        {
            double dx = x1 - x2;
            double dy = y1 - y2;
            double dz = z1 - z2;

        // We should avoid Math.pow or Math.hypot due to performance reasons
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }



    public static double[] getCentroidFromNotes(ArrayList<String> notes){

        ArrayList<double[]> coordinates = new ArrayList<>();

        double[] prevcoordinates = new double[3];
        prevcoordinates[0] = -500;
        prevcoordinates[1] = -500;
        prevcoordinates[2] = -500;


        for (String note : notes){
            if (note != "Z") {
                prevcoordinates = getCoordinatesFromNote(note);
                coordinates.add(prevcoordinates);

            }
            else{
                coordinates.add(prevcoordinates);

            }
        }



        return getCentroid(coordinates);


    }

    public static double[] getCentroid(ArrayList<double[]> coordinates){



        double[] centroid =  new double[3];


        if (coordinates.size() == 0){
            centroid[0] = 0;
            centroid[1] = 0;
            centroid[2] = 0;
        }

        else {

            double cx, cy, cz;
            cx = 0;
            cy = 0;
            cz = 0;
            centroid[0] = 0;
            centroid[1] = 0;
            centroid[2] = 0;

            //for 3d coordinates

            for (double[] coo : coordinates) {


                if (coordinates.size() < 1) {
                    System.out.println("oh");
                }

                //average the numbers
                //System.out.println(coo[0] + " " + coo[1]);

                cx += coo[0];
                cy += coo[1];
                cz += coo[2];


            }

            centroid[0] = (double) cx / coordinates.size();
            centroid[1] = (double) cy / coordinates.size();
            centroid[2] = (double) cz / coordinates.size();
            //System.out.println(coordinates.size() +" "+ centroid[0]);
        }

        //dorien display ce's
        //System.out.println("\nce: " + centroid[0] + " " + centroid[1] + " " + centroid[2] + " ");


        return centroid;
    }





    public static double[] getCoordinatesFromNote(String note){

        //getting the coordinates
        Pitch p = new Pitch(note);
        double[] position = p.getPosition();
                 //dorien
        //System.out.println(note + " " + position[0] + " " + position[1] + " " + position[2]);

        return position;

    }

    public static double getDistance(double[] a, double[] b){
        //gets the euclidean distance between two points
        double distance = 0;

        double x, y, z =0;

        x = Math.abs(a[0] - b[0]);
        y = Math.abs(a[1] - b[1]);
        z = Math.abs(a[2] - b[2]);

        distance = Math.sqrt((x*x +y*y+ z*z));

        //System.out.println(x + " " + y +" " + z +" " + distance);

        return distance;



    }


}
