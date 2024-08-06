//import org.gradle.api.Namer;
package spiralArray;

import java.util.*;

/**
 * Created by Dorien Herremans on 30/01/15.
 */
public class Segmenter {

    private String[] songSequence;
    private KeySearch keySearch;
    private ArrayList<Integer> currentSegments;


    /**
     * Constructor, sets the song and defines the keySearch (including major and minor keys already set)
     * @param songSequence
     * @param keySearch
     */
    public Segmenter(String[] songSequence, KeySearch keySearch) {

        this.songSequence = songSequence;
        this.keySearch = keySearch;
        currentSegments = new ArrayList<Integer>();



    }


    public void generateRandomSegments(int numberOfSegments){




        //e.g. there are 2 segments:

        Random randGenerator = new Random();


        //put initial segments in a random position in the song
        for (int i = 0; i < numberOfSegments; i ++ ){

            //make sure the segments are unique
            int rand = randGenerator.nextInt(this.songSequence.length-2);
            while(this.currentSegments.contains(rand)){
                //generate a new one
                rand = randGenerator.nextInt(songSequence.length-2);
            }
                this.currentSegments.add(rand + 1);
            //a segment at the first element does not have meaning. Therefore +1

        }

        Collections.sort(currentSegments);




    }


    public void printSegments(){
        printSegments(currentSegments);

    }


    public void printSegments(ArrayList<Integer> thissegments){
        System.out.println("\nSegments: ");

        for (int i = 0; i < thissegments.size(); i++ ){
            System.out.print(thissegments.get(i)+ " ");
        }

    }


    /***
     * get the distance of the segments stored in the segmenter object, aka, the set segments
     * @return
     */
    public double minDistanceOfSegments() {
        return minDistanceOfSegments(currentSegments);


    }


        public double minDistanceOfSegments(ArrayList<Integer> passedSegments){

        //calculate min distance for each segment

        double totalDistance = 0;
        int previous = 0;
        ArrayList<Double> distances = new ArrayList<Double>();



        for (int i = 0; i < passedSegments.size(); i++){

            //calculate distance


            //this function includes end and beginning
            distances.add(keySearch.getClosestDistanceFromWindow(songSequence, previous, passedSegments.get(i)-1));

//            System.out.println(keySearch.getClosestDistanceFromWindow(songSequence, previous, segments.get(i)-1));


            totalDistance = totalDistance + (distances.get(distances.size()-1) * (passedSegments.get(i) - previous));

//            System.out.println("summed total: " + totalDistance);
//
//            System.out.println(segments.get(i) - previous);

            //totalDistance += keySearch.getClosestDistanceFromWindow(songSequence, previous, segments.get(i));
            previous = passedSegments.get(i);


        }
        //also the last segmenent!!!


        //this function includes end and beginning
        distances.add(keySearch.getClosestDistanceFromWindow(songSequence, previous, songSequence.length-1));

//        System.out.println(keySearch.getClosestDistanceFromWindow(songSequence, previous, songSequence.length-1));



        totalDistance = totalDistance + ((distances.get(distances.size()-1) * (songSequence.length - previous)));
//        System.out.println("summed total: " + totalDistance);
//
//        System.out.println(songSequence.length - previous);


        //DH Assumption: we take the average of all fragments, weighted towards size

        totalDistance = totalDistance / (songSequence.length);
//        System.out.println("print: " + totalDistance);
//
//        System.out.print("sequence length: " + songSequence.length);


        return totalDistance;

    }






    public  HashMap<Integer, String> minKeysOfSegments(){

        //calculate min distance for each segment

        int previous = 0;
        ArrayList<String> keys = new ArrayList<String>();

        HashMap<Integer, String> SegmentKeys = new HashMap<Integer, String>();


        for (int i = 0; i < currentSegments.size(); i++){

            //calculate distance


            //this function includes end and beginning
            SegmentKeys.put(previous, keySearch.getKeyFromWindow(songSequence, previous, currentSegments.get(i) - 1));


            previous = currentSegments.get(i);


        }
        //also the last segmenent!!!


        //this function includes end and beginning
        SegmentKeys.put(previous, keySearch.getKeyFromWindow(songSequence, previous, songSequence.length - 1));

//        System.out.println(keySearch.getClosestDistanceFromWindow(songSequence, previous, songSequence.length-1));

//
//
//        totalDistance = totalDistance + ((distances.get(distances.size()-1) * (songSequence.length - previous)));
//        System.out.println("summed total: " + totalDistance);
//
//        System.out.println(songSequence.length - previous);





//
//        System.out.print("sequence length: " + songSequence.length);

           return SegmentKeys;

    }


    public Double move1(){
        return move1(currentSegments);
    }

    /***
     * Move 1 changes 1 of the segments to any of the other possibilities
     * sets currentSegments to the best found (if improvement is found)
     * @param passedSegments
     */
    public Double move1(ArrayList<Integer> passedSegments) {

        ArrayList<Integer> workingSegments = new ArrayList<Integer>();
        workingSegments = (ArrayList<Integer>) passedSegments.clone();
        Double bestScore = minDistanceOfSegments(passedSegments);
//        ArrayList<Integer> bestSegments = null;

//        System.out.println("best; "+bestScore);
//        HashMap<ArrayList<Integer>, Double> nbh = new HashMap<ArrayList<Integer>, Double>();

        for (int k = 0; k < workingSegments.size(); k++) {
            //for each element in the passedSegments
//        for (Integer segment : workingSegments){

            Integer segment = workingSegments.get(k);

            //change it to another value < numberofslices
            for (int i = 1; i < songSequence.length - 1; i++) {

                workingSegments = (ArrayList<Integer>) passedSegments.clone();

                if (!workingSegments.contains(i)) {





                    //is this replaced in working segments? check todo
                    workingSegments.set(k, i);
                    Collections.sort(workingSegments);

//                    printSegments(workingSegments);


                    //check if score is less todo less the nor equal then
                    Double newScore = minDistanceOfSegments(workingSegments);
                    if (newScore.compareTo(bestScore) < 0) {
//                        System.out.println("better: "+ newScore) ;

                        bestScore = newScore;

                        currentSegments = (ArrayList<Integer>) workingSegments.clone();
                    }
                }
            }

        }









        //output: new segments
        return bestScore;

    }

    public Double move2(){
        return move2(currentSegments);
    }

    /***
     * Move 1 changes 1 of the segments to any of the other possibilities
     * sets currentSegments to the best found (if improvement is found)
     * @param passedSegments
     */
    public Double move2(ArrayList<Integer> passedSegments) {

        ArrayList<Integer> workingSegments = new ArrayList<Integer>();
        workingSegments = (ArrayList<Integer>) passedSegments.clone();
        Double bestScore = minDistanceOfSegments(passedSegments);
//        ArrayList<Integer> bestSegments = null;

//        System.out.println("best; "+bestScore);
//        HashMap<ArrayList<Integer>, Double> nbh = new HashMap<ArrayList<Integer>, Double>();

        for (int k = 0; k < workingSegments.size(); k++) {
            //for each element in the passedSegments
//        for (Integer segment : workingSegments){

            Integer segment = workingSegments.get(k);

            //change it to another value < numberofslices
            for (int i = 1; i < songSequence.length - 1; i++) {

                workingSegments = (ArrayList<Integer>) passedSegments.clone();

                if (!workingSegments.contains(i)) {





                    //is this replaced in working segments? check todo
                    workingSegments.set(k, i);
                    Collections.sort(workingSegments);

//                    printSegments(workingSegments);


                    //check if score is less todo less the nor equal then
                    Double newScore = minDistanceOfSegments(workingSegments);
                    if (newScore.compareTo(bestScore) < 0) {
//                        System.out.println("better: "+ newScore) ;

                        bestScore = newScore;

                        currentSegments = (ArrayList<Integer>) workingSegments.clone();
                    }
                }
            }

        }









        //output: new segments
        return bestScore;

    }


}
