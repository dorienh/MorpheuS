package featureX;
//import org.apache.commons.*;
import music.Speller;
import spiralArray.KeySearch;
import music.Note;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spiralArray.Utils.*;

import spiralArray.KeySearch;
/**
 * Created by Dorien Herremans on 05/02/15.
 */
public class featureX {


    public String filename;
    public musicXMLparserDH parser;
    Boolean print;
    ArrayList<Note> allNotes;
    ArrayList<ArrayList<Note>> organiseSlices;
    ArrayList<ArrayList<Note>> reducedSlices;
    ArrayList<Note> songSequenceOfNoteObjects;
    Double[][] horizontTM;
    Double[][] verticalTM;
    Double[][] refhorizontTM; //tm of the original corpus (different corpus, to compare with)
    Double[][] refverticalTM;
    String[] songSequence;
    String[] songSequenceParsed;
    public String[] flatSong;
    String[] flatSongWithRests;
    ArrayList<ArrayList<String>> windows;
    String filenameOut;
    int slicesPerMeasure;
    int numberOfVisualisationSlotsPerMeasure;
    ArrayList<ArrayList<Note>> windowsNotes;
    Double tempoFactor;
    int divperQuarterNote;
    HashMap<Integer, Integer> divperQuarterNotePerMeasure;
    int windowSize;
    public int runID;

    boolean setkey;
    String keylabel;
    boolean majorkey = true;

    String deepruntype;

    String inputwindow, outputwindow;

    int measures;
    int endduration;
    int meterunits;
    int divpermeasure;
    int numberOfOutputs;
    int maxPitch;
    int minPitch, minPitchStaff1, minPitchStaff2, maxPitchStaff1, maxPitchStaff2;
    int refMaxPitch;
    int refMinPitch, refMinPitchStaff1, refMinPitchStaff2, refMaxPitchStaff1, refMaxPitchStaff2;


    public ArrayList<ArrayList<Note>> getReducedSlices() {
        return reducedSlices;
    }

    public int getDivpermeasure() {
        return divpermeasure;
    }

    public featureX(String filename) throws IOException {

        this.filename = filename;

        System.out.println("Reading in file... " + filename);

        print = false;

        songSequence = null;
        songSequenceParsed = null;
        //filename = "./data/Prelude_C_Major_-_Bach.xml";

        this.parser = new musicXMLparserDH(this.filename);


        //steps to prepare for TM calculation
        //attention, this only can be put here instead of the TM function when the pitches are stored seperately from the Note object (in solution)

        //prints out the note sounding at the same slice (each division of the musicxml file
        flatSong = parser.parseMusicXML();
        flatSongWithRests = parser.getFlatSongWithRests();


        divperQuarterNotePerMeasure = parser.getDivisions();

        divperQuarterNote = divperQuarterNotePerMeasure.get(1); //todo, we are only using the value for the first measure here. Not taking changes into account. //dorien changed this from 1 to 0

        this.allNotes = parser.getNotesOfSongNoRests();

        //System.out.println("Output size: " + flatSong.length);

        meterunits = 4;
        divpermeasure =  divperQuarterNote; //parser.divisions;



        organiseSlices = parser.organiseSlices(allNotes);

        reducedSlices = parser.reduceList(organiseSlices);


        filenameOut = "";

        if (filename.endsWith(".xml")) {
            filenameOut = filename.substring(0, filename.length() - 4);
        } else {
            filenameOut = filename;
        }


        measures = findMaxMeasure();


        //get max and min

        minPitch = 127;
        maxPitch = 0;
        minPitchStaff1 = 127;
        minPitchStaff2 = 127;
        maxPitchStaff1 = 0;
        maxPitchStaff2 = 0;
        for (Note note : allNotes) {
            if (note.getMidiPitch() < minPitch) {
                minPitch = note.getMidiPitch();
            }
            if (note.getMidiPitch() > maxPitch) {
                maxPitch = note.getMidiPitch();
            }
            //get max and min per staff


            if (note.getStaff() == 1) {

                if (note.getMidiPitch() < minPitchStaff1) {
                    minPitchStaff1 = note.getMidiPitch();
                }
                if (note.getMidiPitch() > maxPitchStaff1) {
                    maxPitchStaff1 = note.getMidiPitch();
                }


            }
            if (note.getStaff() == 2) {

                //System.out.println(note.getMidiPitch() + " ");

                if (note.getMidiPitch() < minPitchStaff2) {
                    minPitchStaff2 = note.getMidiPitch();
                }
                if (note.getMidiPitch() > maxPitchStaff2) {
                    maxPitchStaff2 = note.getMidiPitch();
                }

            }


        }


        //store the range of the song

        for (int i = minPitchStaff1; i <= maxPitchStaff1; i++) {

        }


        for (int i = minPitchStaff2; i <= maxPitchStaff2; i++) {

        }




        //print out the songI j
//        if(print){

        PrintWriter writer = new PrintWriter("flatsongWithRests.txt", "UTF-8");


        for (int i = 0; i < flatSongWithRests.length; i++) {
            writer.println(flatSongWithRests[i]);
        }

//        }

        writer.close();


        //returns an ArrayList containing all the note objects
        //songSequenceOfNoteObjects = parser.getNotesOfSong();


    }

























    //constructor for when there is no xml file to re
    public featureX(String filename, Boolean audio) throws IOException {

        this.filename = filename;

        System.out.println("Reading in file... " + filename);

        print = false;

        songSequence = null;
        songSequenceParsed = null;
        //filename = "./data/Prelude_C_Major_-_Bach.xml";

        //this.parser = new musicXMLparserDH(this.filename);


        //steps to prepare for TM calculation
        //attention, this only can be put here instead of the TM function when the pitches are stored seperately from the Note object (in solution)

        //prints out the note sounding at the same slice (each division of the musicxml file



        windows = new ArrayList();//parser.parseMusicXML();
        //flatSongWithRests = flatSong; //todo check if flatsong has empty lines


        try{
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {


                String[] line = strLine.split(" ");
                //todo how to store this
                ArrayList<String> myline = new ArrayList<>();
                for (String iline : line) {
                    if (iline.equals("")){
                        iline = "Z";
                    }
                    myline.add(iline);

                }

                //if (strLine.equals("")) {
                //    myline.add("Z");
                //}
                windows.add(myline);


            }
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        //this.allNotes = parser.getNotesOfSongNoRests();

        //System.out.println("Output size: " + flatSong.length);

        meterunits = 2;
        //divpermeasure = parser.divisions;


        //organiseSlices = parser.organiseSlices(allNotes);

        //reducedSlices = parser.reduceList(organiseSlices);


        filenameOut = "";

        if (filename.endsWith(".txt")) {
            filenameOut = filename.substring(0, filename.length() - 4);
        } else {
            filenameOut = filename;
        }


        int i = 0;

        flatSong = new String[windows.size()];
        for (ArrayList<String> mywin : windows){


            int counter = 0;
            String e = "";
            for (String el : mywin) {

                if (counter != 0) {
                    e = e + " ";
                }

                e = e + el;
                counter++;

            }

            flatSong[i] = e;
            i++;
        }

//        for (int j = 0; j < flatSong.length; j++){
//            if (flatSong[j].equals("")){
//                flatSong[j] = "Z";
//            }
//        }

        //measures = findMaxMeasure();





        //print out the songI j
//        if(print){

//        PrintWriter writer = new PrintWriter("flatsongWithRests.txt", "UTF-8");
//
//
//        for (int i = 0; i < flatSongWithRests.length; i++) {
//            writer.println(flatSongWithRests[i]);
//        }
//
////        }
//
//        writer.close();


        //returns an ArrayList containing all the note objects
        //songSequenceOfNoteObjects = parser.getNotesOfSong();


    }













    //constructor for when there is no xml file to read AND input / output slice given
    public featureX(Boolean audio, String inputwindow, String outputwindow, boolean setkey, boolean majorkey, String keylabel, String runtype, int runID) throws IOException {


        this.runID = runID;
        //todo: set this for other programs too, e.g. audio and morpheus
        this.setkey = setkey;
        this.majorkey = majorkey;
        this.keylabel = keylabel;

        this.deepruntype = runtype;

        this.inputwindow = inputwindow;
        this.outputwindow = outputwindow;
        this.filename = filename;

        // System.out.println("Processing in slices... " + filename);

        print = false;

        songSequence = null;
        songSequenceParsed = null;
        //filename = "./data/Prelude_C_Major_-_Bach.xml";

        //this.parser = new musicXMLparserDH(this.filename);


        //steps to prepare for TM calculation
        //attention, this only can be put here instead of the TM function when the pitches are stored seperately from the Note object (in solution)

        //prints out the note sounding at the same slice (each division of the musicxml file



        windows = new ArrayList();//parser.parseMusicXML();
        //flatSongWithRests = flatSong; //todo check if flatsong has empty lines



        Speller thisspeller = new Speller();



        String[] line = inputwindow.split(" ");
        //todo how to store this
        ArrayList<String> myline = new ArrayList<>();
        for (String iline : line) {
            if (iline.equals("")){
                iline = "Z";
            }
            iline = thisspeller.getSpellingFromCode(Integer.parseInt(iline), 0);
            myline.add(iline);
        }

        windows.add(myline);


        String[] line2 = outputwindow.split(" ");
        //todo how to store this
        ArrayList<String> myline2 = new ArrayList<>();
        for (String iline : line2) {
            if (iline.equals("")){
                iline = "Z";
            }else {
                iline = thisspeller.getSpellingFromCode(Integer.parseInt(iline), 0);
            }

            myline2.add(iline);

        }

        windows.add(myline2);



        //this.allNotes = parser.getNotesOfSongNoRests();

        //System.out.println("Output size: " + flatSong.length);

        meterunits = 2;
        //divpermeasure = parser.divisions;


        //organiseSlices = parser.organiseSlices(allNotes);

        //reducedSlices = parser.reduceList(organiseSlices);


        filename = "not.txt";
        filenameOut = "";

//        if (filename.endsWith(".txt")) {
//            filenameOut = filename.substring(0, filename.length() - 4);
//        } else {
//            filenameOut = filename;
//        }


        int i = 0;


        //put it from arraylist in array
        flatSong = new String[windows.size()];
        for (ArrayList<String> mywin : windows){


            int counter = 0;
            String e = "";
            for (String el : mywin) {

                if (counter != 0) {
                    e = e + " ";
                }

                e = e + el;
                counter++;

            }

            flatSong[i] = e;
            i++;
        }

//        for (int j = 0; j < flatSong.length; j++){
//            if (flatSong[j].equals("")){
//                flatSong[j] = "Z";
//            }
//        }

        //measures = findMaxMeasure();





        //print out the songI j
//        if(print){

//        PrintWriter writer = new PrintWriter("flatsongWithRests.txt", "UTF-8");
//
//
//        for (int i = 0; i < flatSongWithRests.length; i++) {
//            writer.println(flatSongWithRests[i]);
//        }
//
////        }
//
//        writer.close();


        //returns an ArrayList containing all the note objects
        //songSequenceOfNoteObjects = parser.getNotesOfSong();


    }









    public void setMeterUnits(int meterunits) {
        this.meterunits = meterunits;
    }

    public Double[][] getHorizontalTM() {
        return horizontTM;

    }


    public Double[][] getVerticalTM() {
        return verticalTM;

    }

    public int getMeasures() {
        return measures;
    }

    public void writePitches(HashMap<Integer, Integer> pitches, HashMap<Integer, Integer> spelling, Map<Integer, Note>
            eventsInFragment, String filename) throws IOException {


        //make a list of all notes sorted by start time:
        songSequenceOfNoteObjects = new ArrayList<Note>();
        for (Map.Entry<Integer, Note> entry : eventsInFragment.entrySet()) {

            songSequenceOfNoteObjects.add(entry.getValue());
        }


        Collections.sort(songSequenceOfNoteObjects, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return Double.compare(o1.getStartTime(), o2.getStartTime());
            }
        });


        //create full pitches
        HashMap<Integer, Integer> fullpitches = new HashMap<>();

        for (int i = 0; i < songSequenceOfNoteObjects.size(); i++) {

            int previd = songSequenceOfNoteObjects.get(i).getId();
            int id = songSequenceOfNoteObjects.get(i).getReferenceNoteID();

            int pitch = 0;


            //for the note, see if there is a reference pitch

            if (id == 0) {
                //it's a unique note, so look at pitches

                pitch = pitches.get(songSequenceOfNoteObjects.get(i).getId());
            } else {
                //it's not a unique note, it takes the pitch of a reference note, which can be transposed

                //todo test this, especially the while loop


                //while the note that is refered to actually refers to another note as well
                while (eventsInFragment.get(id).getReferenceNoteID() != 0) {

                    //pitch = pitch + pitches.get(eventsInFragment.get(id));
                    int change = eventsInFragment.get(previd).getReferenceChange();
                    pitch = pitch + change;
                    previd = id;

                    id = eventsInFragment.get(id).getReferenceNoteID();

                }


                //assign the pitch of the actual note to which all the referencing occurs
                int change = eventsInFragment.get(previd).getReferenceChange();
                //do it again for the last note
                pitch = pitch + pitches.get(eventsInFragment.get(id).getId()) + change;

//                if (eventsInFragment.get(songSequenceOfNoteObjects.get(i).getReferenceNoteID()).getReferenceNoteID() !=
//                        0) {
//                    System.out.print("0 reference note error");
//                }

            }


            //save it
            fullpitches.put(songSequenceOfNoteObjects.get(i).getId(), pitch);


        }


        parser.writeXMLNewPitches(fullpitches, spelling, eventsInFragment, filename);
    }

    public int getSizeofSong() {

        //todo does this include rests? Z?

        return allNotes.size();
    }

    public void calctTM() {


        //extract all horizontal intervals (between all slices)
        horizontTM = parser.getHorizontalInt(reducedSlices);
        verticalTM = parser.getVerticalInt(reducedSlices);


    }

    public void calctTM(HashMap<Note, Integer> inputPitches) {


        //todo make more efficient


//        for (ArrayList<Note> list : reducedSlices){
//            for (Note thisnote : list){
//
//                System.out.print(thisnote.getId() + " at: " + thisnote.getStartTime() + " -- ");
//            }
//            System.out.print("\n");
//
//        }

        //extract all horizontal intervals (between all slices)
        horizontTM = parser.getHorizontalInt(reducedSlices, inputPitches);
        verticalTM = parser.getVerticalInt(reducedSlices, inputPitches);


    }


    public void writeTM() throws FileNotFoundException, UnsupportedEncodingException {

        //print matrices
        parser.writeMatrix(horizontTM, "horizontaltm.txt");
        parser.writeMatrix(verticalTM, "verticaltm.txt");


    }

    public int getMaxPitch() {
        return maxPitch;
    }

    public int getMinPitch() {
        return minPitch;
    }

    public int getMinPitchStaff1() {
        return minPitchStaff1;
    }

    public int getMinPitchStaff2() {
        return minPitchStaff2;
    }

    public int getMaxPitchStaff1() {
        return maxPitchStaff1;
    }

    public int getMaxPitchStaff2() {
        return maxPitchStaff2;
    }


    public void setRefTMs(Double[][] refHorizontInt, Double[][] refVerticalInt) {

        this.refhorizontTM = refHorizontInt;
        this.refverticalTM = refVerticalInt;


    }


    public void setRefRange(int min, int max, int min1, int max1, int min2, int max2) {

        this.refMaxPitch = max;
        this.refMinPitch = min;
        this.refMinPitchStaff1 = min1;
        this.refMaxPitchStaff1 = max1;
        this.refMinPitchStaff2 = min2;
        this.refMaxPitchStaff2 = max2;

    }


    public int getRefMaxPitch() {
        return refMaxPitch;
    }

    public int getRefMinPitch() {
        return refMinPitch;
    }

    public int getRefMinPitchStaff1() {
        return refMinPitchStaff1;
    }

    public int getRefMinPitchStaff2() {
        return refMinPitchStaff2;
    }

    public int getRefMaxPitchStaff1() {
        return refMaxPitchStaff1;
    }

    public int getRefMaxPitchStaff2() {
        return refMaxPitchStaff2;
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Reading in file... ");
        String[] songSequence = null;
        String[] songSequenceParsed = null;
        String filename = "";

        if (args.length > 0) {
            filename = args[0];

        } else {
            System.out.print("You did not specify any filename as on option.");
            filename = "./data/Prelude_C_Major_-_Bach.xml";
            //System.exit(0);
        }

        musicXMLparserDH parser = new musicXMLparserDH(filename);


        //prints out the note sounding at the same slice (each division of the musicxml file
        String[] flatSong = parser.parseMusicXML();

        ArrayList<Note> allNotes = parser.getNotesOfSong();

        System.out.println("Output size: " + flatSong.length + args[1]);


        ArrayList<ArrayList<Note>> organiseSlices = parser.organiseSlices(allNotes);

        ArrayList<ArrayList<Note>> reducedSlices = parser.reduceList(organiseSlices);


        //print out the songI j
        if (args.length > 1) {
            if (args[1].equals("-print")) {
                for (int i = 0; i < flatSong.length; i++) {
                    System.out.println(flatSong[i]);
                }
            }
        }


        //returns an ArrayList containing all the note objects
        ArrayList<Note> songSequenceOfNoteObjects = parser.getNotesOfSong();


        //extract all horizontal intervals (between all slices)
        Double[][] horizontInt = parser.getHorizontalInt(reducedSlices);
        Double[][] veriticalInt = parser.getVerticalInt(reducedSlices);


        //print matrices
        parser.writeMatrix(horizontInt, "horizontaltm.txt");
        parser.writeMatrix(veriticalInt, "verticaltm.txt");


        //reduce list to no repeated slices

        //horizontal intervals again

        //vertical intervals on both


    }


    public ArrayList<Note> getSong() {
        return this.allNotes;
    }


    public double evaluate_piece(HashMap<Note, Integer> inputPitches) {

        double score = 0;


        //make sure that the slices already prepared  (in constructor, so ok...)

        //calculate TMs


        //DORIENcalctTM(inputPitches);


        //calculate distance

        //KL divergence?
//        score = Math.abs(klDivergenceMultiDim(horizontTM, refhorizontTM) + klDivergenceMultiDim(verticalTM,
//                refverticalTM));
//        //todo check right order + weights?

        //DORIENscore = Math.abs(euclideanDistance(horizontTM, refhorizontTM) + euclideanDistance(verticalTM,refverticalTM));


        return score;
    }


    public static double euclideanDistance(Double[][] p1, Double[][] p2) {

        if (p1.length != p2.length) {
            System.out.println("Matrices different size");
        }
        //assert p1[0].length = p2[0].length;
        //todos
        double distance = 0.;

        double coord = 0;

        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p1[0].length; j++) {


                coord = (double) Math.pow((p1[i][j] - p2[i][j]), 2);
                distance += coord;

            }

        }

        distance = Math.sqrt(distance);


        return distance;
    }


    public static double klDivergenceMultiDim(Double[][] p1, Double[][] p2) {

        //todo check order

        double klDiv = 0.0;

        for (int i = 0; i < p1.length; ++i) {

            for (int j = 0; j < p1[i].length; j++) {

                if (p1[i][j] == 0) {
                    continue;
                }
                if (p2[i][j] == 0.0) {
                    continue;
                } // Limin

                klDiv += p1[i][j] * Math.log(p1[i][j] / p2[i][j]);

            }
        }

        return klDiv / log2; // moved this division out of the loop -DM
    }


    public static final double log2 = Math.log(2);









    public void calculateTensionReduced() throws FileNotFoundException, UnsupportedEncodingException {


        //get maximum distance or circumference


        //calculate all distances in a list
        //sort
        //take last element


        ArrayList<double[]> centroids = new ArrayList<>();
        //for each window


        for (ArrayList<String> window : windows) {


            //getCentroid()
            if (window.size() != 0) {
                centroids.add(getCentroidFromNotes(window));
            } else {
                double[] empty = {-500, -500, -500};
                //repeat the last element if there was a rest
                //centroids.add(centroids.get(centroids.size()-1));
                centroids.add(empty);


            }

        }


        //todo display centroids


        //display distance between centroids

        ArrayList<Double> centroidDistances = getDistancesBetweenSeqElements(centroids);


        //String centr_filename = filename + "_centroids";

        //ArrayList<Double> normal_centers = normalise(centroidDistances);
        //normal_centers.add(0, 0.5);

//        numberOfOutputs = centroidDistances.size() + 1;  //needed for writing the inscore file

        //writeInscoreFromList(normal_centers, centr_filename, filenameOut, "Movement of Centroids", 0);

        //prepend 0.5 so that the signal doesn't come from 0. on the first:

        //String fname;
//        fname = filenameOut + "_centroid";
//        writeListToFile(centroidDistances, fname);


        //get centroid angular momentum
        /*ArrayList<Double> centroidAngles = getAngleBetweenSeqElements(centroids);
        ArrayList<Double> normal_angle = normalise(centroidAngles);
        String ang_filename = filename + "_angles";
        writeInscoreFromList(normal_angle, ang_filename, filenameOut, "Angular cloud momentum", 1);
        fname = filenameOut + "_ang";
        writeListToFile(centroidAngles, fname);*/


        //get distance from key centre
        ArrayList<Double> centroidDistanceFromKey = getDistancesBetweenSeqElementsFromFixedKey(centroids);
        //ArrayList<Double> normal_key = normalise(centroidDistanceFromKey);
//        String key_filename = filename + "_keydistance";
//        writeInscoreFromList(normal_key, key_filename, filenameOut, "Distance from key", 2);
//        fname = filenameOut + "_key";
//        writeListToFile(centroidDistanceFromKey, fname);


        //get tension
        //get biggest distance

        ArrayList<Double> maxDistances = getCloudDiameter(windows);


//        ArrayList<Double> normal_diameter = normalise(maxDistances);
//
//        fname = filenameOut + "_diameter";
//        String diam_filename = filename + "_diameter";
//        writeInscoreFromList(normal_diameter, diam_filename, filenameOut, "Tension", 3);
//        //System.out.println("a");

//        writeListToFile(maxDistances, fname);


//        writeFullGraphs(centroidDistances, centroidDistanceFromKey, maxDistances,
//                filename+"_tensionGraphs"); //dorien: removed second argument: centroidAngles,



        writeSlicesOnlyToFileForDeeplearningTest(centroidDistances, centroidDistanceFromKey, maxDistances);
        //System.out.println("b");
//
//
//        System.out.println("tension in measure 7: ");
//
//        for (int i = 192; i < 223; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }
//
//
//        System.out.println("tension in measure 8: ");
//
//        for (int i = 224; i < 256; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }
//
//        System.out.println("tension in measure 9: ");
//
//        for (int i = 256; i < 288; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }

        //getDistance()


    }





    public void calculateTension() throws FileNotFoundException, UnsupportedEncodingException {


        //get maximum distance or circumference


        //calculate all distances in a list
        //sort
        //take last element


        ArrayList<double[]> centroids = new ArrayList<>();
        //for each window


        for (ArrayList<String> window : windows) {


            //getCentroid()
            if (window.size() != 0) {
                centroids.add(getCentroidFromNotes(window));
            } else {
                double[] empty = {-500, -500, -500};
                //repeat the last element if there was a rest
                //centroids.add(centroids.get(centroids.size()-1));
                centroids.add(empty);


            }

        }


        //todo display centroids


        //display distance between centroids

        ArrayList<Double> centroidDistances = getDistancesBetweenSeqElements(centroids);


        String centr_filename = filename + "_centroids";

        ArrayList<Double> normal_centers = centroidDistances; //DOriennormalise(centroidDistances);
        //normal_centers.add(0, 0.5);

        numberOfOutputs = centroidDistances.size() + 1;  //needed for writing the inscore file

        writeInscoreFromList(normal_centers, centr_filename, filenameOut, "Movement of Centroids", 0);

        //prepend 0.5 so that the signal doesn't come from 0. on the first:

        String fname;
        fname = filenameOut + "_centroid";
        writeListToFile(centroidDistances, fname);


        //get centroid angular momentum
        /*ArrayList<Double> centroidAngles = getAngleBetweenSeqElements(centroids);
        ArrayList<Double> normal_angle = normalise(centroidAngles);
        String ang_filename = filename + "_angles";
        writeInscoreFromList(normal_angle, ang_filename, filenameOut, "Angular cloud momentum", 1);
        fname = filenameOut + "_ang";
        writeListToFile(centroidAngles, fname);*/


        //get distance from key centre
        ArrayList<Double> centroidDistanceFromKey = getDistancesBetweenSeqElementsFromKey(centroids);
        ArrayList<Double> normal_key = centroidDistanceFromKey;  //Dorien normalise()
        String key_filename = filename + "_keydistance";
        writeInscoreFromList(normal_key, key_filename, filenameOut, "Distance from key", 2);
        fname = filenameOut + "_key";
        writeListToFile(centroidDistanceFromKey, fname);


        //get tension
        //get biggest distance

        ArrayList<Double> maxDistances = getCloudDiameter(windows);


        ArrayList<Double> normal_diameter = maxDistances; //Dorien normalise()

        fname = filenameOut + "_diameter";
        String diam_filename = filename + "_diameter";
        writeInscoreFromList(normal_diameter, diam_filename, filenameOut, "Tension", 3);
        //System.out.println("a");

        writeListToFile(maxDistances, fname);


        writeFullGraphs(centroidDistances, centroidDistanceFromKey, maxDistances,
                filename+"_tensionGraphs"); //dorien: removed second argument: centroidAngles,



        writeSlicesOnlyToFileForDeeplearningTest(centroidDistances, centroidDistanceFromKey, maxDistances);
        //System.out.println("b");
//
//
//        System.out.println("tension in measure 7: ");
//
//        for (int i = 192; i < 223; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }
//
//
//        System.out.println("tension in measure 8: ");
//
//        for (int i = 224; i < 256; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }
//
//        System.out.println("tension in measure 9: ");
//
//        for (int i = 256; i < 288; i++) {
//            System.out.print(maxDistances.get(i) + " ");
//        }

        //getDistance()


    }

    private void writeSlicesOnlyToFileForDeeplearningTest(ArrayList<Double> centroidDistances, ArrayList<Double> centroidDistanceFromKey, ArrayList<Double> maxDistances) {


        String thisline = "";

        thisline = this.deepruntype + ", " +  this.keylabel + ", " + this.inputwindow + ", " +  this.outputwindow + ", " +  maxDistances.get(0) + ", " + maxDistances.get(1) + ", " + centroidDistanceFromKey.get(0) + ", " + centroidDistanceFromKey.get(1) + ", " + centroidDistances.get(0) + ", " + this.runID;


        System.out.println(thisline);

        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("./outputDeepModel.csv", true)));
            out.println(thisline);
        }catch (IOException e) {
            System.err.println(e);
        }finally{
            if(out != null){
                out.close();
            }
        }



    }







    public ArrayList<ArrayList<Double>> getAndCalculateTension() throws FileNotFoundException,
            UnsupportedEncodingException {

//caution: call externally calculate windows first


        ArrayList<ArrayList<Double>> sumCurrentTensionLines = new ArrayList<>();


        ArrayList<double[]> centroids = new ArrayList<>();
        //for each window

        for (ArrayList<String> window : windows) {

            //getCentroid()
            if (window.size() != 0) {
                centroids.add(getCentroidFromNotes(window));
            } else {
                double[] empty = {-500, -500, -500};
                //repeat the last element if there was a rest
                //centroids.add(centroids.get(centroids.size()-1));
                centroids.add(empty);

            }
        }


        ArrayList<Double> centroidDistances = getDistancesBetweenSeqElements(centroids);



        //ArrayList<Double> normal_centers = normalise(centroidDistances);
        //normal_centers.add(0, 0.5);

        numberOfOutputs = centroidDistances.size() + 1;  //needed for writing the inscore file

        //prepend 0.5 so that the signal doesn't come from 0. on the first:


        //get centroid angular momentum
        ArrayList<Double> centroidAngles = getAngleBetweenSeqElements(centroids);
    //    ArrayList<Double> normal_angle = normalise(centroidAngles);

        //get distance from key centre
        ArrayList<Double> centroidDistanceFromKey = getDistancesBetweenSeqElementsFromKey(centroids);
//        ArrayList<Double> normal_key = normalise(centroidDistanceFromKey);

        //get tension
        //get biggest distance

        ArrayList<Double> maxDistances = getCloudDiameter(windows);


        sumCurrentTensionLines.add(maxDistances);
        sumCurrentTensionLines.add(centroidDistances);
        sumCurrentTensionLines.add(centroidDistanceFromKey);
        sumCurrentTensionLines.add(centroidAngles);

        return sumCurrentTensionLines;

    }



    public void writeListToFile(ArrayList<Double> list, String filenameOut) throws FileNotFoundException,
            UnsupportedEncodingException {


        filenameOut = filenameOut + ".data";
        double previous = -100000;

        PrintWriter writer = new PrintWriter(filenameOut, "UTF-8");

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            //int j = i + 1;
            //if (!list.get(i).equals(previous)) {
                writer.println(i + "\t " + list.get(i));
            //}
            //previous = list.get(i);
        }

        writer.close();
    }




    public void writeFullGraphs(ArrayList<Double> list,  ArrayList<Double> list3,
    ArrayList<Double> list4, String filenameOut) throws FileNotFoundException, UnsupportedEncodingException {

        filenameOut = filenameOut + ".html";
        //double previous = -100000;

        ArrayList<Double> list2 = new ArrayList<>();

        PrintWriter writer = new PrintWriter(filenameOut, "UTF-8");

        //1
        //write header
        writer.println("   <html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
               " google.charts.load('current', {'packages':['corechart']});" +
                        "google.charts.setOnLoadCallback(drawChart);" +
                        " function drawChart() {" +
                " var data = google.visualization.arrayToDataTable([ " +
                "\n['time', 'tension'],");



        //for all 4 lists

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            //int j = j + 1;
          //  if (!list.get(i).equals(previous)) {

            double dat = 0;
//            double dat2 = 0;
//            double dat3 = 0;
//            double dat4 = 0;

//            if (list2.size() >  i){
//                dat2 = list2.get(i);
//            }
//
//            if (list3.size() >  i){
//                dat3 = list3.get(i);
//            }
//
//            if (list4.size() >  i){
//                dat4 = list4.get(i);
//            }
            writer.print("\n[ " + i + "\t , " + list.get(i)+ "]"); //," + dat2 + "," + dat3 + "," + dat4 + "]");
            //}
            //previous = list.get(i);
            if (i != list.size()-1){
                writer.print(",");
            }
        }

        //write middle
        writer.print(" ]);\n" +
                        "\n" +
                        "        var options = {\n" +
                        "          title: 'Cloud movement',\n" +
                        "          curveType: 'function',\n" +
                        "          legend: { position: 'bottom' }\n" +
                        "        };\n" +
                        "\n" +
                        "        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));\n" +
                        "\n" +
                        "        chart.draw(data, options);\n");




        //2

        writer.println(" var data2 = google.visualization.arrayToDataTable([ " +
                "\n['time', 'tension'],");



        //for all 4 lists

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list2.size(); i++) {
            //int j = j + 1;
            //  if (!list.get(i).equals(previous)) {

            double dat = 0;

            writer.print("\n[ " + i + "\t , " + list2.get(i)+ "]"); //," + dat2 + "," + dat3 + "," + dat4 + "]");
            //}
            //previous = list.get(i);
            if (i != list2.size()-1){
                writer.print(",");
            }
        }

        //write middle
        writer.print(" ]);\n" +
                "\n" +
                "        var options2 = {\n" +
                "          title: 'Angular movement',\n" +
                "          curveType: 'function',\n" +
                "          legend: { position: 'bottom' }\n" +
                "        };\n" +
                "\n" +
                "        var chart2 = new google.visualization.LineChart(document.getElementById('curve_chart2'));\n" +
                "\n" +
                "        chart2.draw(data2, options2);\n");




        //3

        writer.println(" var data3 = google.visualization.arrayToDataTable([ " +
                "\n['time', 'tension'],");



        //for all 4 lists

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list3.size(); i++) {
            //int j = j + 1;
            //  if (!list.get(i).equals(previous)) {

            double dat = 0;

            writer.print("\n[ " + i + "\t , " + list3.get(i)+ "]"); //," + dat2 + "," + dat3 + "," + dat4 + "]");
            //}
            //previous = list.get(i);
            if (i != list3.size()-1){
                writer.print(",");
            }
        }

        //write middle
        writer.print(" ]);\n" +
                "\n" +
                "        var options3 = {\n" +
                "          title: 'Distance to the key',\n" +
                "          curveType: 'function',\n" +
                "          legend: { position: 'bottom' }\n" +
                "        };\n" +
                "\n" +
                "        var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart3'));\n" +
                "\n" +
                "        chart3.draw(data3, options3);\n");





        //4

        writer.println("var data4 = google.visualization.arrayToDataTable([ " +
                "\n['time', 'tension'],");



        //for all 4 lists

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list4.size(); i++) {
            //int j = j + 1;
            //  if (!list.get(i).equals(previous)) {

            double dat = 0;

            writer.print("\n[ " + i + "\t , " + list4.get(i)+ "]"); //," + dat2 + "," + dat3 + "," + dat4 + "]");
            //}
            //previous = list.get(i);
            if (i != list4.size()-1){
                writer.print(",");
            }
        }

        //write middle
        writer.print(" ]);\n" +
                "\n" +
                "        var options4 = {\n" +
                "          title: 'Cloud diameter',\n" +
                "          curveType: 'function',\n" +
                "          legend: { position: 'bottom' }\n" +
                "        };\n" +
                "\n" +
                "        var chart4 = new google.visualization.LineChart(document.getElementById('curve_chart4'));\n" +
                "\n" +
                "        chart4.draw(data4, options4);\n" +
                "      } ");



        writer.println("    </script>\n" +
        "  </head>\n" +
        "  <body><h3>Tonal tension of "+ filename + "</h3>\n" +
        "    <div id=\"curve_chart\" style=\"width: 1400px; height: 500px\"></div>\n" +
                "    <div id=\"curve_chart2\" style=\"width: 1400px; height: 500px\"></div>\n" +
                "    <div id=\"curve_chart3\" style=\"width: 1400px; height: 500px\"></div>\n" +
                "    <div id=\"curve_chart4\" style=\"width: 1400px; height: 500px\"></div>\n" +
        "  </body>\n" +
        "</html>\n");



        //write footer
        //writer.println();




        writer.close();

    }































    public void writeTwoFullGraphs(ArrayList<Double> list,
                                ArrayList<Double> list4, String filenameOut, String title1, String title2) throws
            FileNotFoundException,
            UnsupportedEncodingException {

        filenameOut = filenameOut + ".html";
        //double previous = -100000;

        PrintWriter writer = new PrintWriter(filenameOut, "UTF-8");

        //1
        //write header
        writer.println("   <html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                " google.charts.load('current', {'packages':['corechart']});" +
                "google.charts.setOnLoadCallback(drawChart);" +
                " function drawChart() {" +
                " var data = google.visualization.arrayToDataTable([ " +
                "\n['time', 'Current score','All time best score'],");



        //for all 4 lists

        //System.out.println("listsize: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            //int j = j + 1;
            //  if (!list.get(i).equals(previous)) {

            double dat = 0;
//            double dat2 = 0;
//            double dat3 = 0;
//            double dat4 = 0;

//            if (list2.size() >  i){
//                dat2 = list2.get(i);
//            }
//
//            if (list3.size() >  i){
//                dat3 = list3.get(i);
//            }
//
//            if (list4.size() >  i){
//                dat4 = list4.get(i);
//            }
            writer.print("\n[ " + i + "\t , " + list.get(i)+  "\t , " + list4.get(i)+"]"); //," + dat2 + "," + dat3 +
            // "," + dat4 + "]");
            //}
            //previous = list.get(i);
            if (i != list.size()-1){
                writer.print(",");
            }
        }

        //write middle
        writer.print(" ]);\n" +
                "\n" +
                "        var options = {\n" +
                "          title: 'Optimization process',\n" +
                "          curveType: 'function',\n" +
                "          legend: { position: 'bottom' }\n" +
                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));\n" +
                "\n" +
                "        chart.draw(data, options);\n");



//
//                //4
//
//        writer.println("var data4 = google.visualization.arrayToDataTable([ " +
//                "\n['time', 'tension'],");
//
//
//
//        //for all 4 lists
//
//        //System.out.println("listsize: " + list.size());
//        for (int i = 0; i < list4.size(); i++) {
//            //int j = j + 1;
//            //  if (!list.get(i).equals(previous)) {
//
//            double dat = 0;
//
//            writer.print("\n[ " + i + "\t , " + list4.get(i)+ "]"); //," + dat2 + "," + dat3 + "," + dat4 + "]");
//            //}
//            //previous = list.get(i);
//            if (i != list4.size()-1){
//                writer.print(",");
//            }
//        }
//
//        //write middle
//        writer.print(" ]);\n" +
//                "\n" +
//                "        var options4 = {\n" +
//                "          title: '\"+ title2 + \"C',\n" +
//                "          curveType: 'function',\n" +
//                "          legend: { position: 'bottom' }\n" +
//                "        };\n" +
//                "\n" +
//                "        var chart4 = new google.visualization.LineChart(document.getElementById('curve_chart4'));\n" +
//                "\n" +
//                "        chart4.draw(data4, options4);\n" +
          writer.print("     } ");



        writer.println("    </script>\n" +
                "  </head>\n" +
                "  <body><h3>Tonal tension of "+ filename + "</h3>\n" +
                "    <div id=\"curve_chart\" style=\"width: 1400px; height: 500px\"></div>\n" +
                "  </body>\n" +
                "</html>\n");



        //write footer
        //writer.println();




        writer.close();

    }








    private ArrayList<Double> getCloudDiameter(ArrayList<ArrayList<String>> windows) {
        //todo

        ArrayList<Double> diameters = new ArrayList<>();


        //for each window calculate diameter

        for (int i = 0; i < windows.size(); i++) {


            //remove doubles from list

            // Store unique items in result.
            ArrayList<String> uniqueWindow = new ArrayList<>();

            // Record encountered Strings in HashSet.
            HashSet<String> set = new HashSet<>();

            // Loop over argument list.
            for (String item : windows.get(i)) {

                // If String is not in set, add it to the list and the set.
                if (!set.contains(item)) {
                    uniqueWindow.add(item);
                    set.add(item);
                }
            }

            diameters.add(getDiameter(uniqueWindow));


            System.out.println(uniqueWindow);
            System.out.println(getDiameter(uniqueWindow));


        }


        return diameters;
    }

    private Double getDiameter(ArrayList<String> points) {

        double diameter = 0;

        if (points.size() > 1) {


            //get distance between all points


            //point a
            for (int i = 0; i < points.size() - 1; i++) {

                //compared to point b

                for (int j = 1; j < points.size(); j++) {

                    double distance = getDistanceFromPositions(getCoordinatesFromNote(points.get(i)),
                            getCoordinatesFromNote(points.get(j)));

                    if (distance > diameter) {
                        diameter = distance;

                    }


                }


            }


        }

        return diameter;


    }


    private ArrayList<Double> getDistancesBetweenSeqElements(ArrayList<double[]> elements) {


        ArrayList<Double> distances = new ArrayList<>();


        //first element (will be normalised to zero later on -- kindo f
        //todo, could be added as zero later to be exacter
        // distances.add(0.5);


        for (int i = 0; i < elements.size() - 1; i++) {


            if (elements.get(i)[0] == -500) {

                //empty window, no movement
                distances.add(0.);


                //System.out.println("yes " + i);
            } else {
                if (elements.get(i + 1)[0] != -500) {
                    distances.add(getDistance(elements.get(i), elements.get(i + 1)));
                } else {
                    distances.add(0.);
                }
            }


        }

        return distances;
    }


    private ArrayList<Double> getAngleBetweenSeqElements(ArrayList<double[]> elements) {


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





    private ArrayList<Double> getDistancesBetweenSeqElementsFromFixedKey(ArrayList<double[]> elements) {

        ArrayList<Double> distances = new ArrayList<>();


        //get keyposition
        //KeySearch keySearch = new KeySearch();
        //keySearch.getKeyFromWindow(flatSong, 0, flatSong.length - 1);

        double[] keyPosition = new double[3]; // = keySearch.getClosestKeyPosition(flatSong, 0, flatSong.length - 1);


        if (this.setkey == true){

            if (this.majorkey == true) {
                spiralArray.MajorKey Mkey = new spiralArray.MajorKey(this.keylabel);
                keyPosition = Mkey.getPosition();
            }else{
                spiralArray.MinorKey Mkey = new spiralArray.MinorKey(this.keylabel);
                keyPosition = Mkey.getPosition();
            }



        }

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





    private ArrayList<Double> getDistancesBetweenSeqElementsFromKey(ArrayList<double[]> elements) {

        ArrayList<Double> distances = new ArrayList<>();


        //get keyposition
        KeySearch keySearch = new KeySearch();
        keySearch.getKeyFromWindow(flatSong, 0, flatSong.length - 1);

        double[] keyPosition = keySearch.getClosestKeyPosition(flatSong, 0, flatSong.length - 1);


        if (this.setkey == true){

            if (this.majorkey == true) {
                spiralArray.MajorKey Mkey = new spiralArray.MajorKey(this.keylabel);
                keyPosition = Mkey.getPosition();
            }else{
                spiralArray.MinorKey Mkey = new spiralArray.MinorKey(this.keylabel);
                keyPosition = Mkey.getPosition();
            }



        }

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


    public ArrayList<ArrayList<String>> getWindows() {
        return windows;
    }




    public void calculateWindows(Integer numberOfVisualisationSlotsPerMeasure) throws FileNotFoundException, UnsupportedEncodingException {

        slicesPerMeasure = (int) flatSongWithRests.length / measures;


        this.numberOfVisualisationSlotsPerMeasure = numberOfVisualisationSlotsPerMeasure;
        windowSize = (int) slicesPerMeasure / numberOfVisualisationSlotsPerMeasure;


        windows = new ArrayList<>();


        //set this as you wish: sliding window
        Boolean sliding = false;
        windows.clear();

        ArrayList<String> part = new ArrayList<>();
        if (flatSongWithRests.length >= windowSize) {


            if (sliding == true) {


                //work from string Elaine: flatsong


                //visualise notes in windows
                //for each window

                int window = 0;
                windows.clear();

                //ArrayList<String> part = new ArrayList<>();
                part.clear();

                //add first notes growingly

                //for each intro window
                for (int k = 1; k < windowSize; k++) {

                    part.clear();

                    //all preceding segments
                    for (int i = 0; i < k; i++) {

                        String[] splitted;
                        if (flatSongWithRests[i] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);

                        }

                    }


                    //sort part
                    Collections.sort(part);


                    windows.add((ArrayList<String>) part.clone());
                }


                //for each slice in flatsong (the entire song)
                for (int i = 0; i <= flatSongWithRests.length - windowSize; i++) {

                    //System.out.println("slice: " + i);
                    part.clear();


                    //for each segment in the window add to window arraylist
                    for (int j = 0; j < windowSize; j++) {

                        String[] splitted;
                        if (flatSongWithRests[i + j] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i + j].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);


                        }

                    }


                    //sort part
                    Collections.sort(part);

                    //System.out.println(i + " " + flatSongWithRests.length);

                    windows.add((ArrayList<String>) part.clone());
                    window++;

                }


            } else {


                //non sliding windows

                //for each slice in flatsong (the entire song)
                for (int i = 0; i <= flatSongWithRests.length - windowSize; i += windowSize) {

                    //System.out.println("slice: " + i);
                    part.clear();


                    //for each segment in the window add to window arraylist
                    for (int j = 0; j < windowSize; j++) {

                        String[] splitted;
                        if (flatSongWithRests[i + j] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i + j].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);


                        }

                    }


                    //sort part
                    Collections.sort(part);

                    //System.out.println(i + " " + flatSongWithRests.length);

                    windows.add((ArrayList<String>) part.clone());
                    //window++;

                }
            }


//
//
//            //add last notes schrinking
//
//            //for each outro window
//            for (int k = 1; k < windowSize; k++) {
//
//                part.clear();
//
//                //all preceding segments
//                for (int i = flatSongWithRests.length-1; i > flatSongWithRests.length - 1 - k; i--) {
//
//                    String[] splitted;
//                    if (flatSongWithRests[i] == null) { //if all rests
//                        splitted = new String[0];
//                    } else {
//                        splitted = flatSongWithRests[i].split(" ");
//                    }
//
//                    for (String thissplit : splitted) {
//
//                        part.add(thissplit);
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part);
//
//
//                windows.add((ArrayList<String>) part.clone());
//            }

            System.out.println("\nSliding windows created");


//COMMENT THIS IS WITHOUT SLIDING WINDOW
//            //for each slice in flatsong (the entire song)
//            for (int i = 0; i < flatSongWithRests.length - windowSize; i += windowSize) {
//
//                part.clear();
//
//
//                //for each window add to window arraylist
//                for (int j = 0; j < windowSize; j++) {
//
//                    String[] splitted;
//                    if (flatSongWithRests[i+j] == null){ //if all rests
//                        splitted = new String[0];
//                    }
//                    else {
//                        splitted = flatSongWithRests[i + j].split(" ");
//                    }
//
//                    for (String thissplit : splitted) {
//
//                        part.add(thissplit);
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part);
//
//
//                windows.add((ArrayList<String>) part.clone());
//                window++;
//
//            }


        } else {
            System.out.println("Song not long enough to analyse.");
        }


        //print windows

        PrintWriter file = new PrintWriter("windows.txt", "UTF-8");

        for (int i = 0; i < windows.size(); i++) {

            for (int j = 0; j < windows.get(i).size(); j++) {
                file.print(windows.get(i).get(j) + " ");
            }
            file.print("\n");


        }

        file.close();


    }




    public void calculateWindowsForSize(Integer windowSize) throws FileNotFoundException, UnsupportedEncodingException {

//        slicesPerMeasure = (int) flatSongWithRests.length / measures;
//
//
//        this.numberOfVisualisationSlotsPerMeasure = numberOfVisualisationSlotsPerMeasure;
//        int windowSize = (int) slicesPerMeasure / numberOfVisualisationSlotsPerMeasure;


        this.windowSize = windowSize;

        windows = new ArrayList<>();


        //set this as you wish: sliding window
        Boolean sliding = false;
        windows.clear();

        ArrayList<String> part = new ArrayList<>();
        if (flatSongWithRests.length >= windowSize) {


            if (sliding == true) {


                //work from string Elaine: flatsong


                //visualise notes in windows
                //for each window

                int window = 0;
                windows.clear();

                //ArrayList<String> part = new ArrayList<>();
                part.clear();

                //add first notes growingly

                //for each intro window
                for (int k = 1; k < windowSize; k++) {

                    part.clear();

                    //all preceding segments
                    for (int i = 0; i < k; i++) {

                        String[] splitted;
                        if (flatSongWithRests[i] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);

                        }

                    }


                    //sort part
                    Collections.sort(part);


                    windows.add((ArrayList<String>) part.clone());
                }


                //for each slice in flatsong (the entire song)
                for (int i = 0; i <= flatSongWithRests.length - windowSize; i++) {

                    //System.out.println("slice: " + i);
                    part.clear();


                    //for each segment in the window add to window arraylist
                    for (int j = 0; j < windowSize; j++) {

                        String[] splitted;
                        if (flatSongWithRests[i + j] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i + j].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);


                        }

                    }


                    //sort part
                    Collections.sort(part);

                    //System.out.println(i + " " + flatSongWithRests.length);

                    windows.add((ArrayList<String>) part.clone());
                    window++;

                }


            } else {


                //non sliding windows

                //for each slice in flatsong (the entire song)
                for (int i = 0; i <= flatSongWithRests.length - windowSize; i += windowSize) {

                    //System.out.println("slice: " + i);
                    part.clear();


                    //for each segment in the window add to window arraylist
                    for (int j = 0; j < windowSize; j++) {

                        String[] splitted;
                        if (flatSongWithRests[i + j] == null) { //if all rests
                            splitted = new String[0];
                        } else {
                            splitted = flatSongWithRests[i + j].split(" ");
                        }

                        for (String thissplit : splitted) {

                            part.add(thissplit);


                        }

                    }


                    //sort part
                    Collections.sort(part);

                    //System.out.println(i + " " + flatSongWithRests.length);

                    windows.add((ArrayList<String>) part.clone());
                    //window++;

                }
            }


//
//
//            //add last notes schrinking
//
//            //for each outro window
//            for (int k = 1; k < windowSize; k++) {
//
//                part.clear();
//
//                //all preceding segments
//                for (int i = flatSongWithRests.length-1; i > flatSongWithRests.length - 1 - k; i--) {
//
//                    String[] splitted;
//                    if (flatSongWithRests[i] == null) { //if all rests
//                        splitted = new String[0];
//                    } else {
//                        splitted = flatSongWithRests[i].split(" ");
//                    }
//
//                    for (String thissplit : splitted) {
//
//                        part.add(thissplit);
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part);
//
//
//                windows.add((ArrayList<String>) part.clone());
//            }

            System.out.println("\nSliding windows created");


//COMMENT THIS IS WITHOUT SLIDING WINDOW
//            //for each slice in flatsong (the entire song)
//            for (int i = 0; i < flatSongWithRests.length - windowSize; i += windowSize) {
//
//                part.clear();
//
//
//                //for each window add to window arraylist
//                for (int j = 0; j < windowSize; j++) {
//
//                    String[] splitted;
//                    if (flatSongWithRests[i+j] == null){ //if all rests
//                        splitted = new String[0];
//                    }
//                    else {
//                        splitted = flatSongWithRests[i + j].split(" ");
//                    }
//
//                    for (String thissplit : splitted) {
//
//                        part.add(thissplit);
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part);
//
//
//                windows.add((ArrayList<String>) part.clone());
//                window++;
//
//            }


        } else {
            System.out.println("Song not long enough to analyse.");
        }


        //print windows

        PrintWriter file = new PrintWriter("windows.txt", "UTF-8");

        for (int i = 0; i < windows.size(); i++) {

            for (int j = 0; j < windows.get(i).size(); j++) {
                file.print(windows.get(i).get(j) + " ");
            }
            file.print("\n");


        }

        file.close();


    }




    public void visualiseNotes() throws FileNotFoundException, UnsupportedEncodingException {

        //todo check what happens where there is an empty window (all rests)


        filenameOut = filenameOut + ".html";


        PrintWriter writer = new PrintWriter(filenameOut, "UTF-8");


        //write header of motion chart
        writer.println("<html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.load(\"visualization\", \"1\", {packages:[\"motionchart\"]});\n" +
                "      google.setOnLoadCallback(drawChart);\n" +
                "      function drawChart() {\n" +
                "        var data = new google.visualization.DataTable();\n");


        //add all the notes

        writer.println("data.addColumn('string', 'Note');\n" +
                "        data.addColumn('number', 'Measure');\n" +
                "        data.addColumn('number', 'X');\n" +
                "        data.addColumn('number', 'Y');\n" +
                "        data.addColumn('number', 'Z');\n" +
                "        data.addColumn('number', 'Intensity');");


        String output = "data.addRows([";


        //todo find a way to output in a 3d motion chart with sound
        //or 2 charts for top and front at the same time

        //embed in a website with autoplay on different charts and music

        int intensity = 0;


        //for each window
        for (int i = 0; i < windows.size(); i++) {

            intensity = 1;

            //for each element in the window:

            for (int j = 0; j < windows.get(i).size(); j++) {

                if (windows.get(i) != null) {
                    String note = windows.get(i).get(j);


                    //count intensity
                    if (j > 0) {
                        //if it's the same as the previous
                        if (windows.get(i).get(j).equals(windows.get(i).get(j - 1))) {
                            intensity++;
                        } else {
                            intensity = 1;
                        }
                    }


                    //file writing

                    if (j + 1 < windows.get(i).size()) {

                        //check if it's continued on the next note
                        if (windows.get(i).get(j).equals(windows.get(i).get(j + 1))) {
                            //if so, don't write just yet

                        } else {
                            //if not continued, write to file

                            output += "\n['" + note + "', " + i * 100 + ", " + getCoordinatesFromNote(note)[0] + ", " + getCoordinatesFromNote(note)[1] + ", " + getCoordinatesFromNote(note)[2] + ", " + intensity + "],";
                            //['A',  100, 1, 1000, 300, 'East'],
                            //System.out.println(i);
                        }


                    } else {
                        //if last note, also write to file,

                        if (i == windows.size() - 1) {

                            output += "\n['" + note + "', " + i * 100 + ", " + getCoordinatesFromNote(note)[0] + ", " + getCoordinatesFromNote(note)[1] + ", " + getCoordinatesFromNote(note)[2] + ", " + intensity + "],]);\n";
                        } else {
                            output += "\n['" + note + "', " + i * 100 + ", " + getCoordinatesFromNote(note)[0] + ", " + getCoordinatesFromNote(note)[1] + ", " + getCoordinatesFromNote(note)[2] + ", " + intensity + "],";
                        }
                        //['A',  100, 1, 1000, 300, 'East'],

                    }
                } else {
                    System.out.println("empty window found");
                }


            } //end for each slice

        } //end for each window


//        output += "['A',  100, 1, 1000, 300, 'East'],\n" +
//                "          ['B', 100, 2, 1150, 200, 'West'],\n" +
//                "          ['C', 100, 3, 300,  250, 'West'],\n" +
//                "          ['Apples',  1200, 4, 1200, 400, 'East'],\n" +
//                "        ['Apples',  4000, 1, 1000, 300, 'East'],\n" +
//                "          ['Oranges', 40000, 2, 1150, 200, 'West'],\n" +
//                "          ['Bananas', 40000, 3, 300,  250, 'West'],\n" +
//                "        ]);\n";
        writer.println(output);


        //write footer
        writer.println("\n" +
                "        var chart = new google.visualization.MotionChart(document.getElementById('chart_div'));\n" +
                "\n" +
                "        chart.draw(data, {width: 600, height:300});\n" +
                "      }\n</script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"chart_div\" style=\"width: 600px; height: 300px;\"></div>\n" +
                "  </body>\n" +
                "</html>");


        writer.close();


    }


    public void writeInscoreFromList(ArrayList<Double> list, String filename, String filenameBase, String title,
                                     int type)
            throws
            FileNotFoundException, UnsupportedEncodingException {

        //type 0 centr, 1 ang, 2 key, 3 diam
        System.out.println("Writing inscore file \"" + title + "\"...");

        filename = filename + ".inscore";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        String[] fileparts = filenameBase.split("/");
        String filenameOnly = fileparts[fileparts.length - 1];
        //int writeoutputs = numberOfOutputs;

        int writeoutputs;
        if (divperQuarterNote !=0)

    {
        writeoutputs = (windowSize * windows.size()) / divperQuarterNote;
    }else{
        writeoutputs = windowSize;
        }

        //write header
        writer.print(" !clear variables\n" +
                "                /ITL/scene/* del;\n" +
                "/ITL/scene/signal/* del;\n" +
                "\n" +
                "\n" +
                "\n" +
                "!load song - figure out how to load musicxml\n" +
                "!/ITL/scene/score set gmn \"[ c e g ]\";\n" +
                "/ITL/scene/score set gmnf \"" + filenameOnly + ".gmn\" ;\n" +
                "/ITL/scene/score scale 0.23;\n" +
                "\n" +
                "! signals accept floating point values that should be in the range [-1, 1]\n" +
                "\n" +
                "/ITL/scene/signal/s1 ");
        //0. 1. 0. 1. 0. 1. 0. 1. 0. 1. 0. 1. 0. 1. 0. 1.;


        //write list
        for (int i = 0; i < list.size(); i++) {
//
//            if(a == 0.){
//                writer.print("1. ");
//            }
//            if(a == 1.){
//                writer.print("0. ");
//            }
//            if ((a > 0.) && (a < 1.)){
//                double b = Math.log10(a)+1;
//                BigDecimal bd = new BigDecimal(Double.toString(-Math.log10(a)));
//                bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
//                 writer.print(bd.doubleValue()+ " ");
//                if (bd.doubleValue() > 1){
//                }
//                //System.out.println(-Math.log10(a) + " " + bd.doubleValue());
//            }

            double a = list.get(i);
            double b = a;//(a*2)-1;

            DecimalFormat df = new DecimalFormat("0.0000 ");
//            for (int k = 1; k < 10; k++){
            writer.print(df.format(b));
//                writer.print(" ");
//            }
//            BigDecimal bd = new BigDecimal(Double.toString(b));
//            bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
            //double f = b - (long) b;

            //System.out.printf("%.10f%n", f)
            //writer.printf("%.5f%n " ,f);


//          interpolate (linear)
//            if ( i!= list.size()-1){
//                double j = list.get(i+1);
//                double c = ((j*2)-1 + b)/2;
//                BigDecimal bd2 = new BigDecimal(Double.toString(c));
//                bd2 = bd2.setScale(6, BigDecimal.ROUND_HALF_UP);
//                writer.print(bd2.doubleValue() + " " );
//            }


        }

        writer.print("; \n/ITL/scene/signal/s2 ");

        for (double a : list) {

            //double b =  -a ; //Math.log(a);
            writer.print("0. ");

        }

        //int writeoutput = numberOfVisualisationSlotsPerMeasure * measures;
        int quarter = 4;
//        double buffer = (double) meterunits / quarter;
//        double buffer2 = buffer * (double) measures;
//        double buffer3 = (double) writeoutputs / buffer2;
//        double writemeter2 = Math.round(buffer3);//todo generalise noto just for
//        int writemeter = Integer.valueOf((int) writemeter2);
        int writemeter = 4;
        // meters
        // based on quarternotes


        //write footer
        //
        String color = "0";

        //type 0 centr, 1 ang, 2 key, 3 diam
        if (type == 0) {
            color = "0.2";
            writeoutputs--;
        } else if (type == 1) {
            color = "0.25";
            writeoutputs = writeoutputs - 2;
        } else if (type == 2) {
            color = "0.0";
        } else if (type == 3) {
            color = "0.1";
        }

        writer.println(";\n" +
                "! below a graphic signal is set with the signal s1 followed by constant signals that controls\n" +
                "! '0.03' the line thickness\n" +
                "! next the color is expressed using 4 signals that control a HSBA model\n" +
                "! (one for each color component in the given order)\n" +
                "! '0.' the color hue\n" +
                "! '1.' the color saturation\n" +
                "! '1.' the brigthness\n" +
                "! '1.' the alpha chanel (transparency)\n" +
                "/ITL/scene/signal/gs set s2 s1 " + color + " 1. 1. 0.01;\n" +
                "!/ITL/scene/signal/gs set s2 0.05 0. 1. 1. 0.5;\n" +
                "!/ITL/scene/signal/gs set s1 s2;\n" +
                "/ITL/scene/siggraph set graph gs;\n" +
                "/ITL/scene/siggraph height 1.;\n" +
                "\n" +
                "! below the signal duration is adjusted to the score duration\n" +
                "/ITL/scene/siggraph duration " + writeoutputs + " " + writemeter + ";\n" +
                "\n" +
                "/ITL/scene/sync siggraph score h sync;\n" +
                "/ITL/scene/siggraph dy 0.1;\n" +
                "\n" +
                "/ITL/scene/title set txt \"" + title + "\";\n" +
                "/ITL/scene/title y -0.8;\n" +
                "\n" +
                "/ITL/scene/forward set txt \"Page\";\n" +
                "/ITL/scene/forward y 0.72;\n" +
                "\n" +
                "\n" +
                "\n" +
                "/ITL/scene/p1 set txt \"1\";\n" +
                "/ITL/scene/p2 set txt \"2\";\n" +
                "/ITL/scene/p3 set txt \"3\";\n" +
                "/ITL/scene/p4 set txt \"4\";\n" +
                "/ITL/scene/p5 set txt \"5\";\n" +
                "/ITL/scene/p6 set txt \"6\";\n" +
                "/ITL/scene/p7 set txt \"7\";\n" +
                "/ITL/scene/p8 set txt \"8\";\n" +
                "/ITL/scene/p9 set txt \"9\";\n" +
                "/ITL/scene/p10 set txt \"10\";\n" +
                "/ITL/scene/p11 set txt \"11\";\n" +
                "/ITL/scene/p12 set txt \"12\";\n" +

                "\n" +
                "/ITL/scene/p2 y 0.72;\n" +
                "/ITL/scene/p3 y 0.72;\n" +
                "/ITL/scene/p4 y 0.72;\n" +
                "/ITL/scene/p1 y 0.72;\n" +
                "/ITL/scene/p5 y 0.72;\n" +
                "/ITL/scene/p6 y 0.72;\n" +
                "/ITL/scene/p7 y 0.72;\n" +
                "/ITL/scene/p8 y 0.72;\n" +
                "/ITL/scene/p9 y 0.72;\n" +
                "/ITL/scene/p10 y 0.72;\n" +
                "/ITL/scene/p11 y 0.72;\n" +
                "/ITL/scene/p12 y 0.72;\n" +
                "\n" +
                "/ITL/scene/p1 x 0.08;\n" +
                "/ITL/scene/p2 x 0.12;\n" +
                "/ITL/scene/p3 x 0.16;\n" +
                "/ITL/scene/p4 x 0.20;\n" +
                "/ITL/scene/p5 x 0.24;\n" +
                "/ITL/scene/p6 x 0.28;\n" +
                "/ITL/scene/p7 x 0.32;\n" +
                "/ITL/scene/p8 x 0.36;\n" +
                "/ITL/scene/p9 x 0.40;\n" +
                "/ITL/scene/p10 x 0.46;\n" +
                "/ITL/scene/p11 x 0.52;\n" +
                "/ITL/scene/p12 x 0.58" +
                ";\n" +
                "\n" +
                "\n" +
                "/ITL/scene/p2 watch mouseDown (\n" +
                "\t/ITL/scene/score page 2);\n" +
                "\t\n" +
                "\t\n" +
                "/ITL/scene/p3 watch mouseDown (\n" +
                "\t/ITL/scene/score page 3);\n" +
                "\t\n" +
                "\t\n" +
                "/ITL/scene/p4 watch mouseDown (\n" +
                "\t/ITL/scene/score page 4);\n" +
                "\t\n" +
                "\t/ITL/scene/p5 watch mouseDown (\n" +
                "\t/ITL/scene/score page 5);\n" +
                "\t\n" +
                "\t/ITL/scene/p6 watch mouseDown (\n" +
                "\t/ITL/scene/score page 6);\n" +
                "\t\n" +
                "\t/ITL/scene/p7 watch mouseDown (\n" +
                "\t/ITL/scene/score page 7);\n" +
                "\t\n" +
                "\t/ITL/scene/p8 watch mouseDown (\n" +
                "\t/ITL/scene/score page 8);\n" +
                "\t\n" +
                "\t/ITL/scene/p9 watch mouseDown (\n" +
                "\t/ITL/scene/score page 9);\n" +
                "\t\n" +
                "\t/ITL/scene/p10 watch mouseDown (\n" +
                "\t/ITL/scene/score page 10);\n" +
                "\t\n" +
                "\t/ITL/scene/p11 watch mouseDown (\n" +
                "\t/ITL/scene/score page 11);\n" +
                "\t\n" +
                "\t/ITL/scene/p12 watch mouseDown (\n" +
                "\t/ITL/scene/score page 12);\n" +
                "\t\n" +
                "\t\n" +
                "/ITL/scene/p1 watch mouseDown (\n" +
                "\t/ITL/scene/score page 1);\n" +
                "\t\n" +
                "\t");


        writer.close();

    }


    public static ArrayList<Double> normalise(ArrayList<Double> mylist) {

        //find maximum & min
        double max = 0;
        double min = 9;
        ArrayList<Double> norm = new ArrayList<>();

        for (double item : mylist) {
            if (item > max) {
                max = item;
            }
            //todo dorien turn on for long erpieces
            if (item < min) {
//                if (item > 0.000001) {
                min = item;
//                }
            }
        }

        // min = 0;

        //normalise each item

  /*      //override max and min
        INTRO
        CE: Max: 2.5716402029314542 and min: 0.0885061203156782
        The closest key is: Ab Major Max: 3.007491509354155 and min: 0.1800562513973485
        diameter: Max: 2.966479394838265 and min: 1.4605934866804429

        OUTRO
        Max: 3.4689095308660534 and min: 0.19854655780070146
        The closest key is: F Minor Max: 2.4753669895318566 and min: 0.34696262939592354
        Max: 3.5777087639996634 and min: 1.4605934866804429
        new:

        The closest key is: F Minor
        Max: 3.2586708710000165 and min: 0.20816836389380186
        Writing inscore file "Distance from key"...
        Max: 3.5777087639996634 and min: 1.4605934866804429
        Writing inscore file "Tension"...
        Done.*/

//        schubert key
//        max = 3.2586708710000165;
//        min = 0.1800562513973485;

//        schubert diam
//        max = 3.5777087639996634 ;
//        min = 1.4605934866804429;


//        schubert ce
//        max = 3.4689095308660534;
//        min = 0.19854655780070146;


//        for stimuli:
//
////DIAM:
//max=  4.163331998932265;
//min= 1.7888543819998317;
//
//
//CE:
//max= 2.033060090930254;
//        min= 0.1641037511827913;
//
//KEY:
//max= 1.471417404694308;
//min=  0.2642027097541744;
//
//
//

        // min = 0;

        for (int i = 0; i < mylist.size(); i++) {

            if ((max - min) != 0) {
                double item = mylist.get(i);
                if (item > 0.000001) {
                    norm.add((item - min) / (max - min));
                    //norm.add((mylist.get(i))/10);
                    //=(value-min)/(max-min)
                } else {
                    norm.add(item);
                }
            } else {
                norm.add((double) 0);
            }
        }

        System.out.println("Max: " + max + " and min: " + min);


        return norm;


    }


    public int findMaxMeasure() {

        int max = 0;

        for (Note note : allNotes) {
            //todo
            if (note.getMeasure() > max) {
                max = note.getMeasure();
            }
        }

        System.out.println("measures: " + max);
        return max;
    }


    public void applyStructure(String filename, Integer midiDivs) throws IOException {

        //structure.structure(filename, allNotes, midiDivs, divpermeasure);
        tempoFactor = (double) midiDivs / divpermeasure;



        //test DORIEN print reduced slices

        PrintWriter print = new PrintWriter("./reducedSlices.txt");
        for (ArrayList<Note> thisSlice: reducedSlices){

            print.print("\n");
            for (Note thisnote: thisSlice){
                int time = (int) (thisnote.getStartTime() *
                        tempoFactor);
                print.print("\nstart: " + thisnote.getStartTime() + " midiStart: " + time + " pitch: " + thisnote
                        .getMidiPitch() + " measure: " + thisnote.getMeasure() + " duration: " + thisnote.getDuration());
            }
        }


        //read in cos file
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;

        reader = new

                BufferedReader(new FileReader(filename)

        );

        String line = null;

        while ((line = reader.readLine()) != null)

        {
            lines.add(line);


        }

        reader.close();


        //match structure from the file to the notes
        //todo

        //note: a note can be constrained by multiple patterns... only store the first, that should be ok


        //for each pattern (=line)

        ArrayList<ArrayList<Note>> allPvectors = new ArrayList<>();
        //ArrayList<ArrayList<Note>> allVvectors = new ArrayList<>();

        ArrayList<Note> Pvector = new ArrayList<>();

        for (String thisline : lines) {

            //for each of the lines

            //find all the Pvectors


            Pvector.clear();
            Pattern pattern = Pattern.compile("p\\((\\d+),(\\d+)\\)");
            Matcher matcher = pattern.matcher(thisline);
            while (
                    matcher.find()) {


                int time = Integer.valueOf(matcher.group(1));
                int midi = Integer.valueOf(matcher.group(2));

                time = Integer.valueOf((int) (time / tempoFactor));


                Note newnote = getNote(time, midi);
                if (newnote ==null){
                    System.out.println("note note found in Pvector");
                }
                else{
                    Pvector.add(newnote);
                }



            }


            //find all the Vvectors

            pattern = Pattern.compile("v\\((\\d+),(-?\\d+)\\)");
            matcher = pattern.matcher(thisline);

            int counter = 0;
            while (
                //for each pattern
                    matcher.find()) {


                //it it's the first pattern: ignore (that can be the base of the template)
                if (counter != 0) {


                    int time = Integer.valueOf(matcher.group(1));
                    String test = matcher.group(2);
                    int change = Integer.valueOf(matcher.group(2));


                    //for the next ones --> refer to the fist + or - the initial vector reference
                    //add note property -> reference note and pitch difference
                    //find note that starts at that time and has the same midipitch



                    int notetime = (int) (time / tempoFactor);

                    setStructure(Pvector, notetime, change);

                     //movement or start date in tantums?

                }


                counter++;


            }


        }

        int test = 0;


        //for each vector

        //if it's the first: set as fixed: so in fact, do nothing


    }


    //midi pitch and xml duration
    public Note getNote(int time, int pitch) {

        Note match = null;

        for (Note thisnote : allNotes) {

            if (thisnote.getMidiPitch() == pitch) {

                if (thisnote.getStartTime() == time) {

                    match = thisnote;
                    return thisnote;

                }
            }


        }

        return match;
    }



    private void setStructure(ArrayList<Note> allPvectors, int time, int change) {

        //for all of the vectors
        int count = 0;
        for (Note thisNote : allPvectors) {

            if (thisNote != null) {
                int startTime = time + thisNote.getStartTime();
                int midi = thisNote.getMidiPitch() + change;
                //find related note+++
                Note changedNote = getNote(startTime, midi);

                if (changedNote != null) {

                    for (Note thisn : allNotes) {
                        if (thisn.getId() == changedNote.getId()) {
                            thisn.setReferenceChange(change);
                            thisn.setReferenceNoteID(thisNote.getId());
                        }
                    }

                } else {
                    System.out.print("\nNote from pattern not found at midi start time " + startTime * tempoFactor);
                }

            }
            else{
                System.out.println("Empty Pvector");
            }
            count++;

        }


    }




















    public void calculateWindowsWithNotesPerDivs(Integer windowSizeInDiv)
            throws
            FileNotFoundException,
            UnsupportedEncodingException {

        calculateWindowsForSize(windowSizeInDiv);

        Integer flatSongWRLength =  flatSongWithRests.length;

        windowsNotes = new ArrayList<ArrayList<Note>>();

        //int slicesPerMeasure = (int) flatSongWRLength / measures;

        windowSize = windowSizeInDiv;



        //for each notes in sorted note list: organisedslices
        //that starts in current window. Add to it.



        ArrayList<Note> part = new ArrayList<>();

        int windowslength = (measures*numberOfVisualisationSlotsPerMeasure)+1;
        //dorien overwrite for var length
        //windowslength = 200;




        endduration = 0;
        for (Note thisnote: allNotes) {

            int thisend = thisnote.getStartTime()+thisnote.getDuration();
            if (endduration < thisend) {
                endduration = thisend;
            }
        }


        windowslength = endduration;


        for (int i = 1; i < windowslength; i++){
            ArrayList<Note> window = new ArrayList<>();
            windowsNotes.add(window);
        }

        int max = 0;



        for (Note thisnote: allNotes){

            for (int i = 0; i < thisnote.getDuration(); i++){

                int time = i + thisnote.getStartTime();

                int slot = (int) Math.floor((time) / windowSize);

                if (slot > max){
                    max = slot;
                }
                System.out.println(windowsNotes.size() + " "  + slot);

                if (slot < windowsNotes.size()) {
                    windowsNotes.get(slot).add(thisnote);
                } else{
                    System.out.print("problem detected here, total duration of song doesn't match the windows. ");
                }


            }

        }

        System.out.print("\nWindows with notes for tension calculation created");
//
//
//        if (flatSongWRLength >= windowSize) {
//
//
//            //non sliding windows
//
//            //for each slice in flatsong (the entire song)
//            for (int i = 0; i <= flatSongWRLength - windowSize; i += windowSize) {
//
//                //System.out.println("slice: " + i);
//                part.clear();
//
//
//                //for each segment in the window add to window arraylist
//                for (int j = 0; j < windowSize; j++) {
//
//                    ArrayList<Note> splitted;
//                    if (organiseSlices.get(i + j) == null) { //if all rests
//                        splitted = new ArrayList<>();
//                        //do nothing
//
//                    } else {
//                        splitted = organiseSlices.get(i + j);
//
//                    }
//
//                    for (Note thissplit : splitted) {
//
//                        part.add(thissplit);
//
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part, new Comparator<Note>() {
//                    @Override
//                    public int compare(Note o1, Note o2) {
//                        return Double.compare(o1.getStartTime(), o2.getStartTime());
//                    }
//                });
//
//                //System.out.println(i + " " + flatSongWithRests.length);
//
//                windowsNotes.add((ArrayList<Note>) part.clone());
//                //window++;
//
//            }
//
//
//            System.out.println("Sliding windows created");
//
//
//        } else {
//            System.out.println("Song not long enough to analyse.");
//        }


        //print windows

        PrintWriter file = new PrintWriter("windowsNotes.txt", "UTF-8");

        for (int i = 0; i < windowsNotes.size();i++){

            for (int j = 0; j < windowsNotes.get(i).size(); j++){
                file.print(windowsNotes.get(i).get(j).getPitch() + " ");
            }
            file.print("\n");


        }

        file.close();


    }





    public void calculateWindowsWithNotes(Integer numberOfVisualisationSlotsPerMeasure)
            throws
            FileNotFoundException,
            UnsupportedEncodingException {

        calculateWindows(numberOfVisualisationSlotsPerMeasure);

        Integer flatSongWRLength =  flatSongWithRests.length;

        windowsNotes = new ArrayList<ArrayList<Note>>();

        int slicesPerMeasure = (int) flatSongWRLength / measures;

        windowSize = (int) slicesPerMeasure / numberOfVisualisationSlotsPerMeasure;



        //for each notes in sorted note list: organisedslices
        //that starts in current window. Add to it.



        ArrayList<Note> part = new ArrayList<>();

        int windowslength = (measures*numberOfVisualisationSlotsPerMeasure)+1;
        //dorien overwrite for var length
        //windowslength = 200;




        endduration = 0;
        for (Note thisnote: allNotes) {

            int thisend = thisnote.getStartTime()+thisnote.getDuration();
            if (endduration < thisend) {
                endduration = thisend;
            }
        }


        windowslength = endduration;


        for (int i = 1; i < windowslength; i++){
            ArrayList<Note> window = new ArrayList<>();
            windowsNotes.add(window);
        }

        int max = 0;



        for (Note thisnote: allNotes){

            for (int i = 0; i < thisnote.getDuration(); i++){

                int time = i + thisnote.getStartTime();

                int slot = (int) Math.floor((time) / windowSize);

                if (slot > max){
                    max = slot;
                }
                windowsNotes.get(slot).add(thisnote);

            }

        }

        System.out.print("\nWindows with notes for tension calculation created");
//
//
//        if (flatSongWRLength >= windowSize) {
//
//
//            //non sliding windows
//
//            //for each slice in flatsong (the entire song)
//            for (int i = 0; i <= flatSongWRLength - windowSize; i += windowSize) {
//
//                //System.out.println("slice: " + i);
//                part.clear();
//
//
//                //for each segment in the window add to window arraylist
//                for (int j = 0; j < windowSize; j++) {
//
//                    ArrayList<Note> splitted;
//                    if (organiseSlices.get(i + j) == null) { //if all rests
//                        splitted = new ArrayList<>();
//                        //do nothing
//
//                    } else {
//                        splitted = organiseSlices.get(i + j);
//
//                    }
//
//                    for (Note thissplit : splitted) {
//
//                        part.add(thissplit);
//
//
//                    }
//
//                }
//
//
//                //sort part
//                Collections.sort(part, new Comparator<Note>() {
//                    @Override
//                    public int compare(Note o1, Note o2) {
//                        return Double.compare(o1.getStartTime(), o2.getStartTime());
//                    }
//                });
//
//                //System.out.println(i + " " + flatSongWithRests.length);
//
//                windowsNotes.add((ArrayList<Note>) part.clone());
//                //window++;
//
//            }
//
//
//            System.out.println("Sliding windows created");
//
//
//        } else {
//            System.out.println("Song not long enough to analyse.");
//        }


        //print windows

        PrintWriter file = new PrintWriter("windowsNotes.txt", "UTF-8");

        for (int i = 0; i < windowsNotes.size();i++){

            for (int j = 0; j < windowsNotes.get(i).size(); j++){
                file.print(windowsNotes.get(i).get(j).getPitch() + " ");
            }
            file.print("\n");


        }

        file.close();


    }


    public ArrayList<ArrayList<Note>> getWindowsNotes() {
        return windowsNotes;
    }

    public void writePlotsTime(ArrayList<Double> improvementOfScore, ArrayList<Double> improvementOfBestScore, ArrayList<Long> timesteps) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter file = new PrintWriter("plotsTime.tex", "UTF-8");

        file.print("\\documentclass[varwidth]{standalone}[2011/12/21]\n" +
                "\\usepackage{pgfplots}\n" +
                "\n" +
                "\n" +
                " \\pgfplotsset{compat=1.3} \n" +
                "\\usepackage{etex} \n" +
                " \\usepackage{amsmath}\n" +
                " \\usepackage{graphicx}\n" +
                " \\usepackage{fancybox}\n" +
                "\\usepackage{pgfplots}\n" +
                "\n" +
                "\\usepackage{float}\n" +
                "\n" +
                "\\usepackage{setspace}\n" +
                "\n" +
                "\n" +
                "\\begin{document}\n" +
                "\n" +
                "\n" +
                "\\begin{tikzpicture}[scale=1]\n" +
                "\\begin{axis}[\n" +
                "axis x line=bottom,\n" +
                "axis y line=left,\n" +
                "xlabel=Computing time (ms),\n" +
                "ymode=log,log basis y=10,\n" +
                "% xmax = 500,\n" +
                "% ymin = 1.5,\n" +
                "%legend columns=1,\n" +
                "legend entries={best solution, Current solution},\n" +
                "%legend to name=named,\n" +
                "ylabel=D(x)]\n" +
                "legend pos= north east]\n" +
                "% \\legend{$f^a(s)$, $f(s)$, $f(s_\\text{best})$}\n" +
                "\\addplot[dashed] coordinates {");


        for (int i= 0; i < timesteps.size(); i++){
            file.print("(" + i + ", " + improvementOfScore.get(i) + ")\n");
        }


        file.print("\n" +
                "};\n" +
                "\\addplot[dotted] coordinates {");

        for (int i= 0; i < timesteps.size(); i++){
            file.print("(" + i + ", " + improvementOfBestScore.get(i) + ")\n");
        }


        file.print("\n" +
                "};\n" +
                "\\end{axis}\n" +
                "\\end{tikzpicture}\n" +
                "\n" +
                "\n" +
                "\n" +
                "\\end{document}");



        file.close();


    }


    public void writeCSVtime(ArrayList<Double> improvementOfScore, ArrayList<Double> improvementOfBestScore, ArrayList<Long> timesteps) throws IOException {

        //appending
        BufferedWriter file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("evolutionSolution.csv", true),"UTF-8"));
//        PrintWriter file = new PrintWriter("evolutionSolution.csv", true, "UTF-8");

//        file.print("timestep, Current, Best");
//
//        for (int i= 0; i < timesteps.size(); i++){
//            file.print(i + ", " + improvementOfScore.get(i) + ", " + improvementOfBestScore.get(i) + "\n");
//        }


        file.write("Step, ");
        for (int i= 0; i < timesteps.size(); i++) {
            file.write(i + ", ");
        }


        file.write("\nTime, ");
        for (int i= 0; i < timesteps.size(); i++) {
            file.write(timesteps.get(i) + ", ");
        }


        file.write("\nCurrent, ");
        for (int i= 0; i < timesteps.size(); i++) {
            file.write(improvementOfScore.get(i) + ", ");
        }

        file.write("\nBest, ");
        for (int i= 0; i < timesteps.size(); i++) {
            file.write(improvementOfBestScore.get(i) + ", ");
        }
        file.write("\n");


        file.write("");


        file.close();


    }











    public void writePlots(ArrayList<Double> improvementOfScore, ArrayList<Double> improvementOfBestScore, ArrayList<Long> timesteps) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter file = new PrintWriter("plots.tex", "UTF-8");

        file.print("\\documentclass[varwidth]{standalone}[2011/12/21]\n" +
                "\\usepackage{pgfplots}\n" +
                "\n" +
                "\n" +
                " \\pgfplotsset{compat=1.3} \n" +
                "\\usepackage{etex} \n" +
                " \\usepackage{amsmath}\n" +
                " \\usepackage{graphicx}\n" +
                " \\usepackage{fancybox}\n" +
                "\\usepackage{pgfplots}\n" +
                "\n" +
                "\\usepackage{float}\n" +
                "\n" +
                "\\usepackage{setspace}\n" +
                "\n" +
                "\n" +
                "\\begin{document}\n" +
                "\n" +
                "\n" +
                "\\begin{tikzpicture}[scale=1]\n" +
                "\\begin{axis}[\n" +
                "axis x line=bottom,\n" +
                "axis y line=left,\n" +
                "xlabel=Computing time (ms),\n" +
                "ymode=log,log basis y=10,\n" +
                "% xmax = 500,\n" +
                "% ymin = 1.5,\n" +
                "%legend columns=1,\n" +
                "legend entries={best solution, Current solution},\n" +
                "%legend to name=named,\n" +
                "ylabel=D(x)]\n" +
                "legend pos= north east]\n" +
                "% \\legend{$f^a(s)$, $f(s)$, $f(s_\\text{best})$}\n" +
                "\\addplot[dashed] coordinates {");


        for (int i= 0; i < timesteps.size(); i++){
            file.print("(" + timesteps.get(i) + ", " + improvementOfScore.get(i) + ")\n");
        }


        file.print("\n" +
                "};\n" +
                "\\addplot[dotted] coordinates {");

        for (int i= 0; i < timesteps.size(); i++){
            file.print("(" + timesteps.get(i) + ", " + improvementOfBestScore.get(i) + ")\n");
        }


        file.print("\n" +
                "};\n" +
                "\\end{axis}\n" +
                "\\end{tikzpicture}\n" +
                "\n" +
                "\n" +
                "\n" +
                "\\end{document}");



        file.close();


    }


    //todo
    public int getDivperQuarterNote() {
        return divperQuarterNote;
    }




    public void setAutoEncoderWindows(){

        //how many divisions in 32 notes?

        //todo: make one on one matrix 
        //based on calculate windows? based on sorted all notes

        int EncoderDivsPerQuarter = 8;




        int[][] encoder;
        //FIX DIMENTIONS

        //TODO FIX
        encoder = new int[10000][10000];



        //1 window = 32note: quarter note = 8 divs
        if (divperQuarterNote == 8){
            //1 to 1
            //


        }
        else if(divperQuarterNote < 8){

            int repeat = (int) 8 / divperQuarterNote;

            //todo repeat x times

        }else if(divperQuarterNote > 8){
            //todo
            System.out.println("Warning, the divisions of the xml file are bigger then the neural network. ");
        }





        if(endduration == 0){


            for (Note thisnote: allNotes) {

                int thisend = thisnote.getStartTime()+thisnote.getDuration();
                if (endduration < thisend) {
                    endduration = thisend;
                }
            }

        }




        //for all notes: put them in a big array.

        ArrayList<ArrayList<Integer>> expandedNotes = new ArrayList(new ArrayList());


        for (Note note: allNotes){

            //per division in xml

            for (int i=0; i < note.getDuration(); i++){
                //todo check if duration is in divs? Or in beats

                //add the note to
                if(!(expandedNotes.get(i).contains(note.getId()))){
                    expandedNotes.get(i).add(note.getId());
                }

            }
        }

        //todo now see if expandedNotes are in fact in the right division


        //todo cut them into segments and int[][] for passing to the encoder


        //todo change this size
        int encoderWindow = 4;


        ArrayList<int[][]> encoderWindows = new ArrayList();

        //todo change to the new array in correct divisions
        for (int i = 0; i < expandedNotes.size(); i++){

            int encoderNotesInWindow = 5; //todo set

            //create a full window

            //what is the size of the encoder: 3 for starters (pitch, start, end) but then how many notes?

            int[][] thiswindow = new int[encoderNotesInWindow][3];


            //for each note in the window
            for(int j = 0; j < expandedNotes.get(i).size(); j++){

                //todo add the window
            }




        }



    }


    public void setPitches(String pitches) {
        //todo
    }
}