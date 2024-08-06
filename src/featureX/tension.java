package featureX;

import music.Note;
import music.Speller;
import optimize.tools;
import spiralArray.KeySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static spiralArray.Utils.*;

/**
 * Created by dorienhs on 19/02/16.
 */
public class tension {

    public static ArrayList<Double> getCloudDiameter(ArrayList<ArrayList<Note>> windows, HashMap<Integer, Integer>
            pitches, HashMap<Integer, Integer> spelling, Speller speller, Map<Integer, Note> eventsInFragment) {

        ArrayList<Double> diameters = new ArrayList<>();

        //for each window calculate diameter
        for (int i = 0; i < windows.size(); i++) {

            ArrayList<String> uniqueWindow = new ArrayList<>(); // Store unique items in result
            HashSet<Integer> set = new HashSet<>(); // Record encountered Notes

            // Loop over argument list.
            for (Note item : windows.get(i)) {

                // If String is not in set, add it to the list and the set.
                if (!set.contains(item.getId())) {

                    int midi = tools.getPitchWithStructure(item.getId(), pitches, eventsInFragment);
                    int spell = tools.getSpellingWithStructure(item.getId(), spelling, eventsInFragment);
                    String spelled = speller.getSpellingFromCode(midi, spell);
                    uniqueWindow.add(spelled);
                    set.add(item.getId()); //just to keep track and avoid doubles
                }
            }
            diameters.add(getDiameter(uniqueWindow));
        }
        return diameters;
    }

    public static Double getDiameter(ArrayList<String> points) {

        double diameter = 0;
        if (points.size() > 1) { //get distance between all points
            //point a
            for (int i = 0; i < points.size() - 1; i++) {
                //compared to point b
                for (int j = 1; j < points.size(); j++) {

                    double distance = getDistanceFromPositions(getCoordinatesFromNote(points.get(i)), getCoordinatesFromNote(points.get(j)));
                    if (distance > diameter) {
                        diameter = distance;
                    }
                }
            }
        }
        // System.out.println(diameter);
        return diameter;
    }




    public static ArrayList<double[]> createCentroids(ArrayList<ArrayList<Note>> windows, HashMap<Integer, Integer>
            pitches, HashMap<Integer, Integer> spelling, Speller speller, Map<Integer, Note> eventsInFragment) {

        ArrayList<double[]> centroids = new ArrayList<>();
        //for each window
        centroids.clear();
        ArrayList<String> windowSpelled = new ArrayList();

        for (ArrayList<Note> window : windows) {

            windowSpelled.clear();
            for (Note thisnote : window){

                int midi = tools.getPitchWithStructure(thisnote.getId(), pitches, eventsInFragment);
                int spell = tools.getSpellingWithStructure(thisnote.getId(), spelling, eventsInFragment);

                String spelled = speller.getSpellingFromCode(midi, spell);
                windowSpelled.add(spelled);
            }

            if (window.size() != 0) {
                centroids.add(getCentroidFromNotes(windowSpelled));
            } else {
                double[] empty = {-500, -500, -500};
                //repeat the last element if there was a rest
                centroids.add(empty);
            }
        }
        return centroids;
    }


    public static ArrayList<Double> getCloudMomentum(ArrayList<double[]> centroids) {


        //ArrayList<double[]> centroids = createCentroids(windowsNotes, pitches, spelling, speller, eventsInFragment);


        //todoget momentum

        ArrayList<Double> distances = new ArrayList<>();


        //first element (will be normalised to zero later on -- kindo f
        //todo, could be added as zero later to be exacter
        // distances.add(0.5);


        for (int i = 0; i < centroids.size() - 1; i++) {


            if (centroids.get(i)[0] == -500) {

                //empty window, no movement
                distances.add(0.);


                //System.out.println("yes " + i);
            } else {
                if (centroids.get(i + 1)[0] != -500) {
                    distances.add(getDistance(centroids.get(i), centroids.get(i + 1)));
                } else {
                    distances.add(0.);
                }
            }


        }




        return distances;



    }

    public static ArrayList<Double> getTensionAngle(ArrayList<double[]> elements) {

        //elements = centroids


        ArrayList<Double> angles = new ArrayList<Double>();

        angles.add(0.);
        angles.add(0.);


        //remove rests
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i)[0] == -500) {
                //if it's a rest: put the previous value for ce:

                if (i > 0) {

                    elements.get(i)[0] = elements.get(i - 1)[0];
                    elements.get(i)[1] = elements.get(i - 1)[1];
                    elements.get(i)[2] = elements.get(i - 1)[2];
                } else {

                    elements.remove(0);
                    angles.add(0.);
                }
            }
        }


        for (int i = 0; i < elements.size() - 2; i++) {

            double[] a = elements.get(i);
            double[] b = elements.get(i + 1);
            double[] c = elements.get(i + 2);

            double[] v1 = new double[3];
            double[] v2 = new double[3];


            //create the vectors
            for (int j = 0; j < 3; j++) {

                v1[j] = b[j] - a[j];
                v2[j] = c[j] - b[j];

            }

            double vdot = (v1[0] * v2[0]) + (v1[1] * v2[1]) + (v1[2] * v2[2]);

            double magv1 = Math.sqrt(Math.pow(v1[0], 2) +
                    Math.pow(v1[1], 2) + Math.pow(v1[2], 2));

            double magv2 = Math.sqrt(Math.pow(v2[0], 2) +
                    Math.pow(v2[1], 2) + Math.pow(v2[2], 2));


            double cos;

            if (magv1 * magv2 == 0.) {
                cos = -1; //if one of the vectors is zero, there should be no angular movement
            } else {

                cos = (vdot / (magv1 * magv2));
            }

            double normcos = (cos/2) + 0.5;

            //todo, potentially just get the value around 90 or pi/2?

            //angles.add(Math.acos(cos));

            angles.add(normcos);  //normalise it between 0 and 1
            //System.out.println("ok");


        }


        return angles;




    }

    public static ArrayList<Double> getTensionKey(ArrayList<double[]> elements, double[] keyPosition) {



        ArrayList<Double> distances = new ArrayList<>();
//
//        //get keyposition
//
//        KeySearch keySearch = new KeySearch();
//        keySearch.getKeyFromWindow(flatSong, 0, flatSong.length - 1);
//
//        double[] keyPosition = keySearch.getClosestKeyPosition(flatSong, 0, flatSong.length - 1);


        for (int i = 0; i < elements.size(); i++) {


            if (elements.get(i)[0] == -500) {

                //empty window

                distances.add(0.);
                //System.out.println("yes");
            } else {
                distances.add(getDistance(elements.get(i), keyPosition));

            }


        }

        return distances;

    }
}
