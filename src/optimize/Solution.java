package optimize;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


import com.sun.org.apache.xpath.internal.operations.Bool;
import music.Fragment;
import music.Note;
import music.Speller;
import featureX.*;
import spiralArray.MajorKey;
import spiralArray.MinorKey;

/**
 * Created by dorien on 01/12/14.
 */
public class Solution {

    private HashMap<Integer, Integer> pitches = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> spelling = new HashMap<>();  //spelling 0 normal or hash if needed, 1 more hashes,
    // -1 more flats (??)
    private Fragment fragment;
    //private RNN model; SWITCH WHEN RNN
    private featureX model;
    private HashMap<Integer, Integer> bestPitches = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> bestSpelling = new HashMap<Integer, Integer>();
    private Boolean firstdescent;
    private double bestScore;
    private ArrayList<ArrayList<Note>> reducedSlices;
    private ArrayList<ArrayList<Note>> windowsNotes, reducedWindowsNotes;

    private ArrayList<Double> TensionDiam;
    private ArrayList<Double> TensionKey;
    private ArrayList<Double> TensionMomentum;
    private ArrayList<Double> TensionAngle;
    private Speller speller;
    private ArrayList<Double> TensionDiamProfile;
    private ArrayList<Double> TensionMomentumProfile;
    private ArrayList<Double> TensionKeyProfile;
    private ArrayList<Double> TensionAngleProfile;
    HashMap<Integer, ArrayList<Integer>> noteWindowReference;
    private int backtrack = 12;

    private Double maxRandDiam, maxRandKey, maxRandMomentum, maxRandAngle;
    private PrintWriter advanceFile;

    private ArrayList<Double> improvementOfScore, improvementOfBestScore;



    private Integer perturbationPercent;
private double[] keyPosition;
    private ArrayList<Integer> sortedNoteIdsByStartInPitches = new ArrayList<Integer>();
    private ArrayList<Integer> sortedNoteIdsByStartInPitchesBase = new ArrayList<Integer>();

    Long startTimeMs;
    private ArrayList<Long> timesteps;


    public Solution(Fragment fragment, featureX model, Integer windowSizeInDiv, String keyname, Boolean keyModeMajor) throws
            FileNotFoundException, UnsupportedEncodingException {

        improvementOfScore = new ArrayList<>();
        improvementOfBestScore = new ArrayList<>();
        maxRandAngle = 1.;
        maxRandKey = 1.;
        maxRandMomentum = 1.;
        maxRandDiam = 1.;
        //this.pitches = pitches;
        this.fragment = fragment;
        perturbationPercent = 12;

        startTimeMs = System.currentTimeMillis( );
        this.model = model;
        fragment.correctStructure(model.getSong());

        //todo, correct
        model.calculateWindowsWithNotesPerDivs(windowSizeInDiv); //necessary to calculate tension
        windowsNotes = model.getWindowsNotes();
        timesteps = new ArrayList<>();

        reducedWindowsNotes = reduce(windowsNotes);

        speller = new Speller();

        //todo set key automatically
        //MinorKey Mkey = new MinorKey("A");
        if (keyModeMajor){
            MajorKey Mkey = new MajorKey(keyname);
            keyPosition = Mkey.getPosition();
        }
        else{
            MinorKey MinKey = new MinorKey(keyname);
            keyPosition = MinKey.getPosition();
        }
        //dorien
        //keyPosition = getPosition("Cmajor");

        setInitialAsTemplate();
        fragment.buildRelatedNotesList(pitches); //needed for feasibility check

        setRandomInitial();

        //dorien setInitialAsTemplate();


        setNoteWindowReference();
        firstdescent = true;
        reducedSlices = model.getReducedSlices();
        // this.numberofVisualisationSlotsPerMeasure = number;

        //add all the notes
        ArrayList<Note> sortedNotesByStart = new ArrayList<>();
        for (Map.Entry<Integer, Integer> anote : pitches.entrySet()) {
            //if (anote.getKey().getReferenceNote() == null) {
//
            sortedNotesByStart.add(fragment.getEventsInFragment().get(anote.getKey()));

//            }
        }
        //now sort them
        Collections.sort(sortedNotesByStart, new Comparator<Note>() {
            @Override
            public int compare(Note p1, Note p2) {
                return p1.getStartTime() - p2.getStartTime(); // Ascending
            }

        })

        ;


        advanceFile = new PrintWriter("advanceFile.txt", "UTF-8");


        sortedNoteIdsByStartInPitchesBase = new ArrayList<>();


        for (int i = 0; i < sortedNotesByStart.size(); i++){

            sortedNoteIdsByStartInPitches.add(sortedNotesByStart.get(i).getId());
            if (getNoteFromID(sortedNoteIdsByStartInPitches.get(i)).getReferenceNoteID() == 0) {

                sortedNoteIdsByStartInPitchesBase.add(sortedNoteIdsByStartInPitches.get(i));
            }
        }



        setCurrentAsBest();



        System.out.println("Number of unique pitches: " + pitches.size());





        //testing
//        for (int id : fragment.getEventsInFragment().keySet()){
//
//                System.out.print("\n id: " + id + " " + getPitchFull(id) + " staff: " + getNoteFromID(id).getStaff());
//
//        }
//
//
//        int test = 0;

    }

    private ArrayList<ArrayList<Note>> reduce(ArrayList<ArrayList<Note>> windowsNotes) {


        ArrayList<ArrayList<Note>> buffer = new ArrayList<>();

        //remove all the double notes from each window

        for (ArrayList<Note> window: windowsNotes){

            ArrayList<Note> bufferwin = new ArrayList<>();
            Set<Note> hs = new LinkedHashSet<>();
            hs.addAll(window);
            bufferwin.addAll(hs);

            buffer.add(bufferwin);

        }


        //now also remove double windows
        ArrayList<ArrayList<Note>> reduced = new ArrayList<>();

        Set<ArrayList<Note>> hs = new LinkedHashSet<>();
        hs.addAll(buffer);
        reduced.addAll(hs);


        return reduced;
    }

    private void setMaxTensionOfCurrent() throws FileNotFoundException, UnsupportedEncodingException {


        ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, this.pitches, this.spelling, speller,
                fragment.getEventsInFragment());

        maxRandDiam = Collections.max(getTensionDiam(windowsNotes, this.pitches, this.spelling));
        maxRandKey = Collections.max(getTensionKey(centroids));
        maxRandMomentum = Collections.max(getTensionMomentum(centroids));
        maxRandAngle = Collections.max(getTensionAngle(centroids));

    }









    public void setRandomInitial() throws FileNotFoundException, UnsupportedEncodingException {
        Random random = new Random();
//        for (Map.Entry<Integer, MusicEvent> event : this.fragment.getEventsInFragment().entrySet()){
        for (Note event : this.fragment.getSongList()) {

            //only do this if it's a unique not that is not constrained by structure
            if (event.getReferenceNoteID() == 0) {
//            System.out.print("a");
                Integer randomPitch = this.fragment.getPitchValues().getRandom(event.getStaff());

                while (!fragment.getPitchValues().checkInAllPatterns(event.getId(), randomPitch)){
                    randomPitch = this.fragment.getPitchValues().getRandom(event.getStaff());
                }

                pitches.put(event.getId(), randomPitch);
            }


        }


        //create a list of sorted notes by time, only those that occur in pitches




        // put the spelling on 0 for now
        for (Note event : this.fragment.getSongList()) {
            Integer spell = 0;
            //todo implement potential change of spell
            //if (event.getReferenceNote() == null) {
                spelling.put(event.getId(), spell);
            //}
//
        }

        System.out.println("first solution:");
        print();
        System.out.println("end.");

        this.bestPitches = (HashMap) this.pitches.clone();
        this.bestSpelling = (HashMap) this.spelling.clone();

        setMaxTensionOfCurrent();



        getScore();

        this.bestScore = getBestScore();
        System.out.println("Random initial solution set");

        //first time
        improvementOfScore.add(bestScore);
        improvementOfBestScore.add(bestScore);
        timesteps.add(startTimeMs-startTimeMs);

    }


    public void setInitialAsTemplate() throws FileNotFoundException, UnsupportedEncodingException {

        pitches.clear();

        for (Note event : this.model.getSong()) {
              if (event.getReferenceNoteID()==0){
                //System.out.print(event.getMidiPitch());
                pitches.put(event.getId(), event.getMidiPitch());

            }
            else{
                  //System.out.println("Note references another"); //todo why is this never run??
              }
        }

        for (Note event : this.model.getSong()) {
            Integer spell = 0;
            //todo implement potential change of spell
            //if (event.getReferenceNote() == null) {
            spelling.put(event.getId(), spell);
            //}
//
        }

        this.bestPitches = (HashMap) this.pitches.clone();
        this.bestSpelling = (HashMap) this.spelling.clone();

        setTensionProfile();


        bestScore = getBestScore();
    }


    public void writeMidiEqualDur() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("outputMidi", "UTF-8");
        //write headers
        writer.println("MFile 0 1 480\n" +
                "MTrk\n" +
                "0 Tempo 300000");

        //for each note

        //int[] setSolution = new int[]{61,65,68,72,67,68,65,63,61,60,58,56,61,64,67,64,61,57,45,47,49,51,52,49,42,54,56,58,59,58,56,58,54,49,54,53,54,49,46,52,55,59,60,57,53,48,53,45,43,46,39,61,58,60,54,68,66,64,63,61,63,68,63,68,59,64,59,61,52,45,75,73,71,70,46,65,58,58,58,53,51,49,48,49,53,51,49,48,46,44,46,44,43,41,39,69,68,66,64,63};

        for (Note entry : this.fragment.getSongList()) {

            int i = entry.getId();

            System.out.print(getBestPitch(entry.getId()) + " ");//+ entry.getId() + "-   ");

            //for (int i = 0; i < bestPitches.size(); i++) {


            writer.println(i * 480 + " On ch=1 n=" + bestPitches.get(entry) + " v=64");
            writer.println((i * 480) + 480 + " Off ch=1 n=" + bestPitches.get(entry) + " v=64");

//            writer.println(i*480 + " On ch=1 n=" + setSolution[i] + " v=64");
//            writer.println((i*480)+480 + " Off ch=1 n=" + setSolution[i] + " v=64");

        }

        writer.println(bestPitches.size() * 480 + "Meta TrkEnd\nTrkEnd");


        writer.close();
    }


    public void setCurrentAsBest() throws FileNotFoundException, UnsupportedEncodingException {
        this.bestPitches = (HashMap) this.pitches.clone();
        this.bestSpelling = (HashMap) this.spelling.clone();
        bestScore = getBestScore();

    }

    public HashMap<Integer, Integer> getPitches() {

        return pitches;
    }

    public HashMap<Integer, Integer> getSpelling() {
        return spelling;
    }

    public double getScore() throws FileNotFoundException, UnsupportedEncodingException {



        return getScore(this.pitches, this.spelling);
    }


    public double getBestScore() throws FileNotFoundException, UnsupportedEncodingException {
        return getScore(this.bestPitches, this.bestSpelling);
    }


    public double getScore(HashMap<Integer, Integer> thesePitches, HashMap<Integer, Integer> theseSpelling) throws
            FileNotFoundException, UnsupportedEncodingException {

        double score = 0;


        //put back for RNN: *RNN*

//            Integer[] melody = new Integer[thesePitches.size()];
//
//            //for (Map.Entry<MusicEvent, Integer> event : thesePitches.entrySet()) {
//
//            //put the solution inthe format the RNN takes
//            //for each possible root
//                for (int i = 0; i < thesePitches.size(); i ++){
//                    melody[i] = thesePitches.get(fragment.getEventsInFragment().get(i));
//                }

        //access RNN

        //FOR MARKOV:
        //score = model.evaluate_piece(thesePitches);

        //}

        score = getTensionScore(thesePitches, theseSpelling, 0, -500);



        //test: just add pitches
//        for (Integer value : thesePitches.values()) {
//            score = score + value;
//        }

        return score;
    }





    public void setPitch(Note event, Integer pitch) {
        this.pitches.put(event.getId(), pitch);
    }


    public Integer getPitch(Integer event) {
        return
              //  tools.getPitchWithStructure(event,pitches, fragment.getEventsInFragment() );
        this.pitches.get(event);
    }

    public Integer getPitchFull(Integer event){
return  tools.getPitchWithStructure(event,pitches, fragment.getEventsInFragment() );
    }

    public Integer getBestPitch(Integer event) {
        if (this.bestPitches.get(event)!= null){

            return this.bestPitches.get(event);
        }
        else{
            return null;
        }
    }

    public void print() {

        System.out.print("\n Solution: (only unique)");
        for (Integer entry : pitches.keySet()) {
            System.out.print(pitches.get(entry) + "" + "(" + entry+") ");// + entry.getId() + "-    ");
        }

    }



    public void printBest() {

        System.out.print("\nBest Solution: (only unique)");
        for (Integer entry : sortedNoteIdsByStartInPitches) {


            System.out.print(getBestPitch(entry) + ", " + entry + "- " + getNoteFromID(entry)
                            .getReferenceChange() + " " +
                            "------- "
            );//+
            // entry.getId() + "-
            // ");
        }
        System.out.println("end.");

    }


    public Note getNoteFromID(Integer Id){
        return fragment.getEventsInFragment().get(Id);
    }


//    public void putAllToConstant() {
    // should only be the original ones
//        for (Integer event : sortedNoteIdsByStartInPitches) {
//            pitches.put(event, 60);
//        }
//    }






    public Boolean compareBestCurrent() throws FileNotFoundException, UnsupportedEncodingException {


        Boolean improved = false;


        Double current = getScore(this.pitches, this.spelling);
        Double best = getScore(this.bestPitches, this.bestSpelling);
        if (Double.compare(current, best) < 0){
            //System.out.println("setting current as new best");
            setCurrentAsBest();
        }

        return improved;

    }




    public Boolean ChangeMoveNote() throws FileNotFoundException, UnsupportedEncodingException {


        //create neighbour variables
        HashMap<Integer, Integer> neighbour;
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();

        //make a working copy of fragment
        //Fragment neighbour = new Fragment(fragment);
        //Fragment bestNeighbour = new Fragment(fragment);
        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Double thisbestScore = getScore();
        Double originalScore = thisbestScore;
//        Integer bestPitch = null;

        //System.out.print("\n\nstart: " + bestScore);

//        Boolean thisNoteImprovement = false;

        //int testcount = 0;
        //int testcount2 = 0;


        Boolean stoploop = false;
//
        String message = "";
        //System.out.print("\n Number of notes: " + sortedNoteIdsByStartInPitches.size());

        //for each of the notes in the solution
        //for (Map.Entry<Note, Integer> event : getPitches().entrySet()){
        //but now sorted per starttime




        for (Integer event : this.sortedNoteIdsByStartInPitchesBase){
        //for (Integer event : this.pitches.keySet()) {



            //System.out.print("\nnote with ID: " + event.getId());
            //testcount++;


            //  for (int i = 0; i < getPitches().size(); i++) {

            //System.out.print(event.getValue());
            //make a new neighbour
            neighbour = (HashMap) this.pitches.clone();


            //store the original Pitch
            Integer originalPitch = getPitch(event);


            // System.out.print(" was changed: " + fragment.getPitchValues().getPossibleValues(event.getStaff())
            //         .size() + "times\n");
            //for each possible pitch
            for (Integer newPitch : fragment.getPitchValues().getPossibleValues(getNoteFromID(event).getStaff())) {

                //testcount2++;
                //System.out.print(testcount2 + "  ");


                //check feasibility
                if (fragment.getPitchValues().checkInAllPatterns(event, newPitch)) {

                    neighbour = (HashMap) this.pitches.clone();
                    neighbour.put(event, newPitch);

                    double neighbourScore = getScore(neighbour, spelling);
                    //System.out.print(" " + neighbourScore);
                    //if score better, store this as best of neighbourhood
                    if (Double.compare(neighbourScore, thisbestScore) < 0) {

                        //first descent
//                    System.out.print("c");
                        //and if there has been a change versus the original

//                    Integer id = ((Note) event.getValue()).getId();
                        if (originalPitch != newPitch) {

//                        System.out.print("T: "+ event.getKey().getId());
                            improvement = true;

                            thisbestScore = neighbourScore;
                            bestNeighbour = (HashMap) neighbour.clone();


//                            message = ("changed note with id: " + event + " at: " + getNoteFromID(event)
//                                    .getStartTime() + " to: " +
//                                    newPitch);
                            message = ("\nC1 all time best: " + bestScore + " current score: " + bestNeighbour);


                            //stop after a first improvement is found in the case of first descent
                            if (firstdescent) {

                                stoploop = true;
                                //break; only stop after all combinations for this note have been tried

                            }

                        }
                    }
                }


            }
            if (stoploop) {
                break;
            }

        }

        //set new fragment
        if (improvement) {
//            this.pitches.put(bestChanged, bestPitch);
            this.pitches = (HashMap<Integer, Integer>) bestNeighbour.clone();

            //setCurrentAsBest();

            System.out.print("\n" + message);
        }
        else{
            System.out.print("\n");
        }

        compareBestCurrent();

        advanceFile.print("\n -- New score C1: " + thisbestScore + " All time best: " + bestScore);
        advanceFile.flush();
        improvementOfScore.add(thisbestScore);
        improvementOfBestScore.add(bestScore);
        timesteps.add(System.currentTimeMillis()-startTimeMs);



        //print();

        return improvement;


    }










    public Boolean ChangeMoveNoteFaster() throws FileNotFoundException, UnsupportedEncodingException {

        int pointer = 0;
        Boolean globalImprovement = false;

        //create neighbour variables
        HashMap<Integer, Integer> neighbour;
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();
        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Double thisbestScore = getScore();
        Double originalScore = thisbestScore;
        Boolean stoploop = false;
        String message = "";

        while (pointer < sortedNoteIdsByStartInPitchesBase.size()){

            Integer event = sortedNoteIdsByStartInPitchesBase.get(pointer);
            
            //make a new neighbour
            neighbour = (HashMap) this.pitches.clone();

            //store the original Pitch
            Integer originalPitch = getPitch(event);

            //for each possible pitch
            for (Integer newPitch : fragment.getPitchValues().getPossibleValues(getNoteFromID(event).getStaff())) {

                //check feasibility
                if (fragment.getPitchValues().checkInAllPatterns(event, newPitch)) {

                    neighbour = (HashMap) this.pitches.clone();
                    neighbour.put(event, newPitch);

                    double neighbourScore = getScore(neighbour, spelling);
                    //if score better, store this as best of neighbourhood
                    if (Double.compare(neighbourScore, thisbestScore) < 0) {

                        if (originalPitch != newPitch) {
                            improvement = true;
                            globalImprovement = true;

                            thisbestScore = neighbourScore;
                            bestNeighbour = (HashMap) neighbour.clone();

                            message = ("\nC1fast all time best: " + bestScore + " current score: " + bestNeighbour);

                            //stop after a first improvement is found in the case of first descent
                            if (firstdescent) {
                                stoploop = true;
                            }
                        }
                    }
                }


            }
//            if (stoploop) {
//                break;
//            }

            if(improvement) {

                //set new fragment
                if (improvement) {
                    this.pitches = (HashMap<Integer, Integer>) bestNeighbour.clone();
                    improvement = false;
                }
                else{
                    System.out.print("\n");
                }

                System.out.println("\nC1fast: "+ "all time best: "+ bestScore + " current score: " + thisbestScore );
                compareBestCurrent();

                advanceFile.print("\n -- New score C1fast: " + thisbestScore + " All time best: " + bestScore + " at " +
                        "pointer: " + pointer);
                advanceFile.flush();
                improvementOfScore.add(thisbestScore);
                improvementOfBestScore.add(bestScore);
                timesteps.add(System.currentTimeMillis()-startTimeMs);

                       // " pointer: " + pointer);
                if (pointer > backtrack -1) {
                    pointer = pointer - backtrack;
                } else {
                    pointer = 0;
                }
            }
            else{
                pointer++;
            }

        }





        //print();

        return globalImprovement;


    }







    public Boolean ChangeMoveTwoNotes() throws FileNotFoundException, UnsupportedEncodingException {

        //create neighbour variables
        HashMap<Integer, Integer> neighbour = (HashMap) this.pitches.clone();
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();

        //make a working copy of fragment
        //Fragment neighbour = new Fragment(fragment);
        //Fragment bestNeighbour = new Fragment(fragment);
        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Double bestScore = getScore();

//        MusicEvent bestChanged = null;
//        Integer bestPitch = null;

        //Boolean thisNoteImprovement = false;

        Boolean stoploop = false;

        String message = "";

        //for each of the notes in the solution
//        for (Map.Entry<MusicEvent, Integer> event : getPitches().entrySet()){
//        for (Note event : this.fragment.getSongList()) {
        //but now sorted per starttime


        for (int counter = 0; counter < sortedNoteIdsByStartInPitchesBase.size() - 1; counter++) {
            Integer event = sortedNoteIdsByStartInPitchesBase.get(counter);


            //if (event.getId() < getPitches().size() - 1) {
            //Note event2 = this.fragment.getSongList().get(event.getId() + 1);

            //but now sorted per starttime
            Integer event2 = sortedNoteIdsByStartInPitchesBase.get(counter + 1);

            //make a new neighbour
            neighbour = (HashMap) this.pitches.clone();

            //store the original Pitches
            Integer originalPitch = getPitch(event);
            Integer originalPitch2 = getPitch(event2);
            //store the next pitch

            //for each possible pitch
            for (Integer newPitch : fragment.getPitchValues().getPossibleValues(getNoteFromID(event).getStaff())) {

                neighbour.put(event, newPitch);

                //for each possible pitch2
                for (Integer newPitch2 : fragment.getPitchValues().getPossibleValues(getNoteFromID(event2).getStaff()
                )) {

                    neighbour.put(event2, newPitch2);

                    //if score better, store this as best of neighbourhood
                    if (Double.compare(getScore(neighbour, spelling), bestScore) < 0) {
//                    System.out.print("c");
                        //and if there has been a change versus the original

//                    Integer id = ((Note) event.getValue()).getId();
                        if (!((originalPitch == newPitch) && (originalPitch2 == newPitch2))) {

//                        System.out.print("newPitch: " + newPitch);
                            //update best

//                        System.out.print("T: "+ event.getKey().getId());
                            improvement = true;
//                        bestPitch = newPitch;
//                        bestChanged = event.getKey();
                            bestScore = getScore(neighbour, spelling);
                            bestNeighbour = (HashMap) neighbour.clone();
//                        bestNeighbour = neighbour;
//                        bestNeighbourScore = getScore(bestNeighbour);
//                        thisNoteImprovement = true;


                            //stop after a first improvement is found in the case of first descent
                            if (firstdescent) {

                                stoploop = true;
                                //break;
                                message = (" changed note with id: " + event + " at: " + getNoteFromID(event)
                                        .getStartTime() + " to: " + newPitch + " And " + event2+ " at: " +
                                        "" + getNoteFromID(event2).getStartTime() + " to: " + newPitch2 + "  --  ");
//
                            }


                        }
                    }
                }

                if (stoploop) {
                    break;

                }
            }


        }

        //reset neighbour
        //neighbour = fragment;

        System.out.print("New score C2: " + bestNeighbourScore);

        //set new fragment
        if (improvement) {
//            this.pitches.put(bestChanged, bestPitch);
            this.pitches = bestNeighbour;
            System.out.print(message + "\n");
        }

//        System.out.print("start: ");
        //bestNeighbour.print();

        //print();

        //if improvement found, return true

        return improvement;


    }


    public Boolean SwapTwoNotes() throws FileNotFoundException, UnsupportedEncodingException {

        //create neighbour variables
        HashMap<Integer, Integer> neighbour;
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();

        //make a working copy of fragment
        //Fragment neighbour = new Fragment(fragment);
        //Fragment bestNeighbour = new Fragment(fragment);
        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Double bestScore = getScore();

        System.out.println("Startscore" + bestScore);

//        MusicEvent bestChanged = null;
//        Integer bestPitch = null;

        //Boolean thisNoteImprovement = false;


        //for each of the notes in the solution

        Boolean stoploop = false;


        for(int id : this.pitches.keySet()){



            //for each note after that.

            for (int id2: this.pitches.keySet()) {

                if (getNoteFromID(id).getStaff() == getNoteFromID(id2).getStaff()){


                if (id != id2) {


                    //make a new neighbour
                    neighbour = (HashMap) this.pitches.clone();

                    //store the original Pitches
                    Integer originalPitch = getPitch(id);
                    Integer originalPitch2 = getPitch(id2);
                    //store the next pitch


                    if (fragment.getPitchValues().checkInAllPatterns(id, originalPitch2)&&(fragment.getPitchValues
                            ().checkInAllPatterns(id2, originalPitch))) {

                    //swap pitches
                    neighbour.put(id, originalPitch2);
                    neighbour.put(id2, originalPitch);


                    //if score better, store this as best of neighbourhood
                    if (Double.compare(getScore(neighbour, spelling), bestScore) < 0) {
//                    System.out.print("c");
                        //and if there has been a change versus the original

//                        System.out.print("newPitch: " + newPitch);
                        //update best

//                        System.out.print("T: "+ event.getKey().getId());
                        improvement = true;
//                        bestPitch = newPitch;
//                        bestChanged = event.getKey();
                        bestScore = getScore(neighbour, spelling);
                        bestNeighbour = (HashMap) neighbour.clone();
//                        bestNeighbour = neighbour;
//                        bestNeighbourScore = getScore(bestNeighbour);
//                        thisNoteImprovement = true;


                        //stop after a first improvement is found in the case of first descent
                        if (firstdescent) {

                            stoploop = true;
                            break;
                        }

                    }

                    }
                }


                }
            }
            if (stoploop) {
                break;
            }

        }

        //reset neighbour
        //neighbour = fragment;

//        advanceFile.print("New score SW: " + bestNeighbourScore);

        //set new fragment
        if (improvement) {
//            this.pitches.put(bestChanged, bestPitch);
            this.pitches = bestNeighbour;
        }

//        System.out.print("start: ");
        //bestNeighbour.print();

        //print();

        //if improvement found, return true

        advanceFile.print("\nSW: all time best: "+ bestScore + " current score: " + this.bestScore);
        System.out.println("\nSW " + " All time best: " + bestScore + " current score: " + this.bestScore);
        compareBestCurrent();

        advanceFile.flush();
        improvementOfScore.add(bestNeighbourScore);
        improvementOfBestScore.add(this.bestScore);
        timesteps.add(System.currentTimeMillis()-startTimeMs);

        return improvement;


    }


    public void PerturbNotes() throws FileNotFoundException, UnsupportedEncodingException {
//        //change percentage randomly


        Random r = new Random();

        Integer perturbationSize = Math.round(perturbationPercent * this.pitches.size() / 100);
        ArrayList<Integer> ids = new ArrayList();
        Random random = new Random();
        List<Integer> possibleIDs = new ArrayList<Integer>(this.pitches.keySet());


        //select a unique ids
        for (int i = 0; i < perturbationSize; i++) {
            Integer newID = possibleIDs.get(random.nextInt(possibleIDs.size()));

            while (ids.contains(newID) == true) {
                newID = possibleIDs.get(random.nextInt(possibleIDs.size()));
            }


            ids.add(newID);
            //System.out.println("-");

        }

//        //for each of the IDs, set a random note  .changeRandom()
        for (Integer id : ids) {

            Note changednote = getNoteFromID(id);


            if (changednote.getStaff() == 1 || changednote.getStaff() == 2) {
                ArrayList<Integer> possibleValues = fragment.getPitchValues().getPossibleValues(changednote.getStaff());

                //Integer lowestValue = fragment.getPitchValues().getPossibleValues(changednote.getStaff()).get(0);
                int newPitch = possibleValues.get(random.nextInt(possibleValues.size()));


                //feasibility check
                while (!fragment.getPitchValues().checkInAllPatterns(changednote.getId(), newPitch)) {
                    newPitch = possibleValues.get(random.nextInt(possibleValues.size()));
                }

                pitches.put((Integer) changednote.getId(), newPitch);
            }
            else{
                System.out.println("Staff problem in perturbation.");
            }



        }

        double thisscore = getScore();
        advanceFile.print("\n -- New score Perturbation: " + thisscore + " All time best: " + bestScore);
        System.out.println("\nPerturbation " + " All time best: " + bestScore + " current score: " + thisscore);
        advanceFile.flush();

        improvementOfScore.add(thisscore);
        improvementOfBestScore.add(bestScore);
        timesteps.add(System.currentTimeMillis()-startTimeMs);

    }





    public void recursiveChange (ArrayList<Note> window, HashMap<Integer, Integer>
            neighbour, int index){


        if (index >= window.size()) {
            //neighbourList.add(neighbour); redefine public variable to use this function
            //return;
        }
        else{

            int id = window.get(index).getId();
            ArrayList<Integer> newvalues = fragment.getPitchValues().getPossibleValues(window.get(index).getStaff());

            for(int i = 0; i < newvalues.size(); i++){

                HashMap<Integer, Integer> neighbour_new = (HashMap<Integer, Integer>) neighbour.clone();

                neighbour_new.put(id, newvalues.get(i));


                //todo see if no double entries

                recursiveChange(window, neighbour_new, index + 1);
            }

        }

    }




    public boolean ChangeMoveSlice() throws FileNotFoundException, UnsupportedEncodingException {


        //create neighbour variables
        HashMap<Integer, Integer> neighbour;
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();

        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Double thisbestScore = getScore();
        Double originalScore = thisbestScore;
//        Integer bestPitch = null;

        //System.out.print("\n\nstart: " + bestScore);

//        Boolean thisNoteImprovement = false;

        //int testcount = 0;
        //int testcount2 = 0;

        Boolean stoploop = false;
//
        String message = "";
        //System.out.print("\n Number of notes: " + sortedNoteIdsByStartInPitches.size());


        ArrayList<HashMap<Integer, Integer>> neighbourList;

        //for each window
        for (ArrayList<Note> window : reducedWindowsNotes) {


            //make a new neighbour
            //neighbour = (HashMap) this.pitches.clone();


            neighbourList = new ArrayList<>(); //global variable needed for the recursion

            if (window.size() != 0) {

                //pick two randomly
                ArrayList<Note> randwindow = new ArrayList<>();
                Random random = new Random();

                int indexfirst = random.nextInt(window.size());
                int indexsecond = random.nextInt(window.size());

                int idfirst = getOriginalID(window.get(indexfirst).getId());
                int idsecond = getOriginalID(window.get(indexsecond).getId());
                ;

                int count = 0;
                if (window.size() > 1) {
                    while (idfirst == idsecond) {
                        int size = window.size();
                        indexsecond = random.nextInt(size);
                        idsecond = getOriginalID(window.get(indexsecond).getId());
                        count++;
                        if (count > 100) {
                            break;
                        }
                    }


                    randwindow.add(window.get(indexfirst));
                    randwindow.add(window.get(indexsecond));


                    //recursiveChange(randwindow, neighbour, 0);

                    ArrayList<Integer> newvalues1 = fragment.getPitchValues().getPossibleValues(window.get(indexfirst).getStaff
                            ());
                    ArrayList<Integer> newvalues2 = fragment.getPitchValues().getPossibleValues(window.get(indexsecond).getStaff
                            ());


                    int originalpitch1 = this.pitches.get(idfirst);
                    int originalpitch2 = this.pitches.get(idsecond);


                    //for each potential pitch of the first note
                    for (Integer newPitch1 : newvalues1) {

                        if (originalpitch1 != newPitch1) {

                            for (Integer newPitch2 : newvalues2) {

                                if (originalpitch2 != newPitch2) {

                                    if (fragment.getPitchValues().checkInAllPatterns(idfirst, newPitch1) && (fragment
                                            .getPitchValues
                                                    ().checkInAllPatterns(idsecond, newPitch2))) {

                                        neighbour = (HashMap) this.pitches.clone();

                                        neighbour.put(idfirst, newPitch1);
                                        neighbour.put(idsecond, newPitch2);

                                        neighbourList.add(neighbour);

                                    }
                                }

                            }
                        }
                    }

                }


                for (HashMap<Integer, Integer> thisneighbour : neighbourList) {

                    double neighbourScore = getScore(thisneighbour, spelling);
                    //System.out.print(" " + neighbourScore);
                    //if score better, store this as best of neighbourhood
                    if (Double.compare(neighbourScore, thisbestScore) < 0) {

                        //first descent
//                    System.out.print("c");
                        //and if there has been a change versus the original

//                    Integer id = ((Note) event.getValue()).getId();
                        //if (originalPitch != newPitch) {

//                        System.out.print("T: "+ event.getKey().getId());
                        improvement = true;

                        thisbestScore = neighbourScore;
                        bestNeighbour = (HashMap) thisneighbour.clone();


//                            message = ("changed note with id: " + event + " at: " + getNoteFromID(event)
//                                    .getStartTime() + " to: " +
//                                    newPitch);


                        //stop after a first improvement is found in the case of first descent
                        if (firstdescent) {

                            stoploop = true;
                            //break; only stop after all combinations for this note have been tried

                        }

                    }
                }


                if (stoploop) {
                    break;
                }

            }
        }





        //set new fragment
        if (improvement) {
//            this.pitches.put(bestChanged, bestPitch);
            this.pitches = (HashMap<Integer, Integer>) bestNeighbour.clone();

            //setCurrentAsBest();

            System.out.print("\n" + message);
        }
        else{
            System.out.print("\n");
        }

        compareBestCurrent();

        advanceFile.print("\n -- New score CS: " + thisbestScore + " All time best: " + bestScore);
        advanceFile.flush();
        improvementOfScore.add(thisbestScore);
        improvementOfBestScore.add(bestScore);
        timesteps.add(System.currentTimeMillis()-startTimeMs);

        print();

        return improvement;


    }


























    public boolean ChangeMoveSliceFaster() throws FileNotFoundException, UnsupportedEncodingException {


        //create neighbour variables
        HashMap<Integer, Integer> neighbour;
        HashMap<Integer, Integer> bestNeighbour = (HashMap) this.pitches.clone();

        Double bestNeighbourScore = getScore(bestNeighbour, spelling);
        Boolean improvement = false;
        Boolean globalImprovement = false;
        Double thisbestScore = getScore();
        Double originalScore = thisbestScore;
//        Integer bestPitch = null;
        int pointer = 0;

        //System.out.print("\n\nstart: " + bestScore);

//        Boolean thisNoteImprovement = false;

        //int testcount = 0;
        //int testcount2 = 0;

        Boolean stoploop = false;
//
        String message = "";
        //System.out.print("\n Number of notes: " + sortedNoteIdsByStartInPitches.size());


        ArrayList<HashMap<Integer, Integer>> neighbourList;

        //for each window
        while (pointer < reducedWindowsNotes.size()) {
            //for (ArrayList<Note> window : windowsNotes){


            ArrayList<Note> window = reducedWindowsNotes.get(pointer);

            if (window.size()!=0) {

                //make a new neighbour
                //neighbour = (HashMap) this.pitches.clone();


                neighbourList = new ArrayList<>(); //global variable needed for the recursion


                //pick two randomly
                ArrayList<Note> randwindow = new ArrayList<>();
                Random random = new Random();

                int indexfirst = random.nextInt(window.size());
                int indexsecond = random.nextInt(window.size());

                int idfirst = getOriginalID(window.get(indexfirst).getId());

                int idsecond = getOriginalID(window.get(indexsecond).getId());
                ;

                int count = 0;
                if (window.size() > 1) {
                    while (idfirst == idsecond) {
                        int size = window.size();
                        indexsecond = random.nextInt(size);
                        idsecond = getOriginalID(window.get(indexsecond).getId());
                        count++;
                        if (count > 100) {
                            break;
                        }
                    }
                    randwindow.add(window.get(indexfirst));
                    randwindow.add(window.get(indexsecond));


                    //recursiveChange(randwindow, neighbour, 0);

                    ArrayList<Integer> newvalues1 = fragment.getPitchValues().getPossibleValues(window.get(indexfirst).getStaff
                            ());
                    ArrayList<Integer> newvalues2 = fragment.getPitchValues().getPossibleValues(window.get(indexsecond).getStaff
                            ());


                    int originalpitch1 = this.pitches.get(idfirst);
                    int originalpitch2 = this.pitches.get(idsecond);


                    //for each potential pitch of the first note
                    for (Integer newPitch1 : newvalues1) {

                        if (originalpitch1 != newPitch1) {

                            for (Integer newPitch2 : newvalues2) {

                                if (originalpitch2 != newPitch2) {

                                    if (fragment.getPitchValues().checkInAllPatterns(idfirst, newPitch1) && (fragment
                                            .getPitchValues
                                                    ().checkInAllPatterns(idsecond, newPitch2))) {

                                        neighbour = (HashMap) this.pitches.clone();

                                        neighbour.put(idfirst, newPitch1);
                                        neighbour.put(idsecond, newPitch2);

                                        neighbourList.add(neighbour);

                                    }
                                }

                            }
                        }
                    }

                    //dorien System.out.println("pointer " + pointer + " neighsize: " + neighbourList.size());

                }


                for (HashMap<Integer, Integer> thisneighbour : neighbourList) {

                    double neighbourScore = getScore(thisneighbour, spelling);
                    //System.out.print(" " + neighbourScore);
                    //if score better, store this as best of neighbourhood
                    if (Double.compare(neighbourScore, thisbestScore) < 0) {

                        //first descent
//                    System.out.print("c");
                        //and if there has been a change versus the original

//                    Integer id = ((Note) event.getValue()).getId();
                        //if (originalPitch != newPitch) {

//                        System.out.print("T: "+ event.getKey().getId());
                        improvement = true;
                        globalImprovement = true;

                        thisbestScore = neighbourScore;
                        bestNeighbour = (HashMap) thisneighbour.clone();


                        message = ("changed note with id: " + idfirst + " and: " + idsecond + " pointer: " +
                                pointer);
//                            "getNoteFromID(event))
//                                    .getStartTime() + " to: " +
//                                    newPitch);


                        //stop after a first improvement is found in the case of first descent
                        if (firstdescent) {

                            stoploop = true;
                            //break; only stop after all combinations for this note have been tried

                        }

                    }
                }
            }


//            if (stoploop) {
//                break;
//            }

            //set new fragment
            if (improvement) {
//            this.pitches.put(bestChanged, bestPitch);
                this.pitches = (HashMap<Integer, Integer>) bestNeighbour.clone();

                //setCurrentAsBest();

                //System.out.print("\n" + message);

                improvement = false;

                compareBestCurrent();
                System.out.print("\n -- New score CSfast: " + thisbestScore + " All time best: " + bestScore);

                advanceFile.print("\n -- New score CSfast: " + thisbestScore + " All time best: " + bestScore);
                advanceFile.flush();
                improvementOfScore.add(thisbestScore);
                improvementOfBestScore.add(bestScore);
                timesteps.add(System.currentTimeMillis()-startTimeMs);

                if (pointer > (backtrack-1)) {
                    pointer = pointer - backtrack;
                } else {
                    pointer = 0;
                }


            } else {
                //System.out.print("\n no improvement, pointer: "+ pointer);
                pointer++;
            }




            }





//        //set new fragment
//        if (improvement) {
////            this.pitches.put(bestChanged, bestPitch);
//            this.pitches = (HashMap<Integer, Integer>) bestNeighbour.clone();
//
//            //setCurrentAsBest();
//
//            System.out.print("\n" + message);
//        }
//        else{
//            System.out.print("\n");
//        }

          print();

        return globalImprovement;


    }























    private int getOriginalID(Integer id) {

        int origID = id;

        while (fragment.getEventsInFragment().get(origID).getReferenceNoteID() != 0){

            origID = fragment.getEventsInFragment().get(origID).getReferenceNoteID();

        }


return origID;
    }

    private int findOriginalNote(int id){


        while (fragment.getEventsInFragment().get(id).getReferenceNoteID() != 0) {

                id = fragment.getEventsInFragment().get(id).getReferenceNoteID();
        }

        return id;
    }


    public Boolean compareCurrentWithBest() throws FileNotFoundException, UnsupportedEncodingException {

        Boolean better = false;

        if (Double.compare(getScore(), getBestScore())<0){
            setCurrentAsBest();
            better = true;
        }

        return better;
    }


    public void setTensionProfile() throws FileNotFoundException, UnsupportedEncodingException {


        //set the initial tension as profile

        //assume that current solution is initial solutions

        setCurrentAsProfile();






    }



    private void setCurrentAsProfile() throws FileNotFoundException, UnsupportedEncodingException {





        TensionDiamProfile = getTensionDiam(windowsNotes, pitches, spelling);
        //TensionMomentumProfile = getTensionMomentum(windowsNotes, pitches, spelling);
        ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, pitches, spelling, speller,
                fragment.getEventsInFragment());

        TensionMomentumProfile = getTensionMomentum(centroids);
        TensionAngleProfile = getTensionAngle(centroids);
        TensionKeyProfile = getTensionKey(centroids);


    }


    public void setExternalTensionProfile(ArrayList<ArrayList<Double>> newTensionProfile) throws
            FileNotFoundException, UnsupportedEncodingException {


        int size0 = TensionDiamProfile.size();
        int size1 = TensionMomentumProfile.size();
        int size2 = TensionKeyProfile.size();
        int size3 = TensionAngleProfile.size();


        if (newTensionProfile.size() >= 4){



            TensionDiamProfile = matchArray(newTensionProfile.get(0), size0);
            //TensionMomentumProfile = getTensionMomentum(windowsNotes, pitches, spelling);
            //ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, pitches, spelling, speller,
            //        fragment.getEventsInFragment());

            TensionMomentumProfile = matchArray(newTensionProfile.get(1), size1);
            TensionKeyProfile = matchArray(newTensionProfile.get(2), size2);
            TensionAngleProfile = matchArray(newTensionProfile.get(3), size3);

        }else{
            System.out.println("Error, not enough tension profiles assed from the template file. ");

        }




    }

    private ArrayList<Double> matchArray(ArrayList<Double> doubles, int size) {

        ArrayList<Double> match = new ArrayList<>();


        int counter = 0;
        for (int i = 0; i < size; i++){

            if (counter >= doubles.size()){
                //start again from the beginning
                counter = 0;
            }

            match.add(doubles.get(counter));

            counter++;
        }


        return match;

    }


    private ArrayList<ArrayList<Double>> getCurrentTension() throws FileNotFoundException,
            UnsupportedEncodingException {


        ArrayList<ArrayList<Double>> sumCurrentTensionLines = new ArrayList<>();

        sumCurrentTensionLines.add(getTensionDiam(windowsNotes, pitches, spelling));

        //TensionMomentumProfile = getTensionMomentum(windowsNotes, pitches, spelling);
        ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, pitches, spelling, speller,
                fragment.getEventsInFragment());

        sumCurrentTensionLines.add(getTensionMomentum(centroids));
        sumCurrentTensionLines.add(TensionKeyProfile = getTensionKey(centroids));
        sumCurrentTensionLines.add(TensionAngleProfile = getTensionAngle(centroids));


        return sumCurrentTensionLines;




    }



    private Double getTensionScore(HashMap<Integer, Integer> thesePitches, HashMap<Integer, Integer> theseSpelling,
                                   Integer
                                   windowStart,
                                   Integer windowEnd)
            throws
            FileNotFoundException,
            UnsupportedEncodingException {


        Double tensionScore = 0.;

        ArrayList<Double> TensionDiam = getTensionDiam(windowsNotes, thesePitches, theseSpelling);

        ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, thesePitches, spelling, speller,
                fragment.getEventsInFragment());

        ArrayList<Double> TensionMomentum = getTensionMomentum(centroids);
        ArrayList<Double> TensionAngle = getTensionAngle(centroids);
        ArrayList<Double> TensionKey = getTensionKey(centroids);


        Double tensionScoreDiam = compareTensionWithProfile(TensionDiam, TensionDiamProfile, 0, -500);
        Double tensionScoreMomentum = compareTensionWithProfile(TensionMomentum, TensionMomentumProfile, 0, -500);
        Double tensionScoreKey = compareTensionWithProfile(TensionKey, TensionKeyProfile, 0, -500);
        Double tensionScoreAngle = compareTensionWithProfile(TensionAngle, TensionAngleProfile, 0, -500);


        tensionScore = 2 * tensionScoreDiam/maxRandDiam + tensionScoreMomentum/maxRandMomentum // ; //+
            +    tensionScoreKey/maxRandKey;// + tensionScoreAngle/maxRandAngle;



        //dorien add fix certain notes:

        //tensionScore = tensionScore + noteConstraints(thesePitches);


        return tensionScore;



    }

    private Double noteConstraints(HashMap<Integer, Integer> thesePitches) {

        //dorien this is for certain notes of this given piece!
        Double score = 0.;
        if (thesePitches.containsKey(2)){
        if (thesePitches.get(2)!= 70){
            score = score + 100;
        }}if (thesePitches.containsKey(3)) {
            if (thesePitches.get(3) != 70) {
                score = score + 100;
            }
        }if (thesePitches.containsKey(4)){
        if (thesePitches.get(4)!= 75){
            score = score + 100;
        }}if (thesePitches.containsKey(5)) {
            if (thesePitches.get(5) != 75) {
                score = score + 100;
            }
        } if (thesePitches.containsKey(6)) {
            if (thesePitches.get(6) != 74) {
                score = score + 100;
            }
        }if (thesePitches.containsKey(7)) {
            if (thesePitches.get(7) != 51) {
                score = score + 100;
            }
        }if (thesePitches.containsKey(8)) {
            if (thesePitches.get(8) != 55) {
                score = score + 100;
            }
        }
        if (thesePitches.containsKey(9)){
        if (thesePitches.get(9)!= 67){
            score = score + 100;
        }}
        if (thesePitches.containsKey(11)) {
            if (thesePitches.get(11) != 72) {
                score = score + 100;
            }
        }
        if (thesePitches.containsKey(13)){

        if (thesePitches.get(13)!= 70) {
            score = score + 100;
        }}
        if (thesePitches.containsKey(15)){
        if (thesePitches.get(15)!= 68){
            score = score + 100;
        }}if (thesePitches.containsKey(16)){

        if (thesePitches.get(16)!= 68){
            score = score + 100;
        }}if (thesePitches.containsKey(18)){
        if (thesePitches.get(18)!= 67){
            score = score + 100;
        }}if (thesePitches.containsKey(20)) {
                                    if (thesePitches.get(20) != 65) {
                                        score = score + 100;
                                    }
                                }




        return score;
    }


    public void writeTensionGraphsProfile(String filename) throws FileNotFoundException, UnsupportedEncodingException {





        model.writeFullGraphs(TensionMomentumProfile, TensionKeyProfile, TensionDiamProfile,
                filename
        );

        model.writeListToFile(TensionMomentumProfile, "TensionMomuntumProfile.dat");
        //model.writeListToFile(TensionAngleProfile, "TensionAngleProfile.dat");
        model.writeListToFile(TensionKeyProfile, "TensionKeyProfile.dat");
        model.writeListToFile(TensionMomentumProfile, "TensionDiamProfile.dat");

    }


    public void writeTensionGraphsCurrent(String filename) throws FileNotFoundException, UnsupportedEncodingException {


        ArrayList<Double> TensionDiam = getTensionDiam(windowsNotes, this.pitches, this.spelling);

        ArrayList<double[]> centroids = tension.createCentroids(windowsNotes, this.pitches, spelling, speller,
                fragment.getEventsInFragment());

        ArrayList<Double> TensionMomentum = getTensionMomentum(centroids);
        ArrayList<Double> TensionAngle = getTensionAngle(centroids);
        ArrayList<Double> TensionKey = getTensionKey(centroids);


        model.writeFullGraphs(TensionMomentum, TensionKey, TensionDiam, filename);

        model.writeListToFile(TensionMomentum, filename+"TensionMomentum.dat");
        //model.writeListToFile(TensionAngle, filename+"TensionAngle.dat");
        model.writeListToFile(TensionKey, filename+"TensionKey.dat");
        model.writeListToFile(TensionDiam, filename+"TensionDiam.dat");


    }




    private Double compareTensionWithProfile(ArrayList<Double> tension, ArrayList<Double> tensionProfile, Integer
            windowStart, Integer windowEnd) {
        Double difference = 0.;

        if (windowEnd == -500){
            windowEnd = tension.size();
        }

        for (int i = windowStart; i < windowEnd; i++  ){


            //euclidean distance
            difference += Math.abs(Math.pow(tension.get(i) - tensionProfile.get(i),2));



        }

        difference = (Math.sqrt(difference)) / tension.size();


        return difference;
    }



    private ArrayList<Double> getTensionKey(ArrayList<double[]> centroids) {
        return tension.getTensionKey(centroids, keyPosition);

    }




    private ArrayList<Double> getTensionAngle(ArrayList<double[]> centroids) {

        return tension.getTensionAngle(centroids);



    }



    private ArrayList<Double> getTensionDiam(ArrayList<ArrayList<Note>> windowsNotes, HashMap<Integer, Integer> pitches,
                                             HashMap<Integer, Integer>
            spelling) throws FileNotFoundException, UnsupportedEncodingException {


        ArrayList<Double> diameters = tension.getCloudDiameter(windowsNotes, pitches, spelling, speller, fragment.getEventsInFragment());

//        PrintWriter writer = new PrintWriter("newTensionDiam.data", "UTF-8");
//
//        for (Double diam : diameters){
//            writer.print(diam + " ");
//
//        }
//
//        writer.close();




        return diameters;
    }



    private ArrayList<Double> getTensionMomentum(ArrayList<double[]> centroids) {


        ArrayList<Double> diameters = tension.getCloudMomentum(centroids);



        return diameters;
    }


    public void setNoteWindowReference() {

        noteWindowReference =new HashMap<>();

        //for each window

        int counter = 0;
        for (ArrayList<Note> window : windowsNotes){

            for (Note thisnote : window){

                ArrayList<Integer> uniqueNotesWindow = new ArrayList<>();
                if (!uniqueNotesWindow.contains(thisnote.getId())) {

                    if (noteWindowReference.containsKey(thisnote.getId())) {
                        ArrayList<Integer> mylist = noteWindowReference.get(thisnote.getId());

                        mylist.add(counter);
                        noteWindowReference.put(thisnote.getId(), mylist);

                    } else {
                        ArrayList<Integer> mylist = new ArrayList<>();

                        mylist.add(counter);
                        noteWindowReference.put(thisnote.getId(), mylist);
                    }

                    uniqueNotesWindow.add(thisnote.getId());
                }



            }

            counter++;



        }

        //todo finish!!! dorien
        System.out.println("test");
    }

    public void closeSolution() throws IOException {


        model.writeTwoFullGraphs(improvementOfScore, improvementOfBestScore, "optimization", "Score", "Best score");

        model.writePlots(improvementOfScore, improvementOfBestScore, timesteps);

        model.writePlotsTime(improvementOfScore, improvementOfBestScore, timesteps);

        model.writeCSVtime(improvementOfScore, improvementOfBestScore, timesteps);


        advanceFile.close();
    }


    //method to load pitches that where outputted to terminal
    public void setLoadedPitches(String loadedPitches) {

        String[] items = loadedPitches.split(" ");
        List<String> splitLoadedPitches = Arrays.asList(items);
        //ArrayList<String> splitLoadedPitches = Arrays.asList(items);

        for (String thisnote : splitLoadedPitches) {

            //todo strip ()
            String[] items2 = thisnote.split("\\(");
            List<String> seperatedNote = Arrays.asList(items2);
            if (seperatedNote.size()>1) {
                Integer firstthisnote = Integer.parseInt(seperatedNote.get(0));
                Integer secondthisnote = Integer.parseInt(seperatedNote.get(1).replaceAll("\\p{P}",""));
                //secondthisnote = secondthisnote.replaceAll("\\p{P}","");
                this.pitches.put(secondthisnote, firstthisnote);
            }
            else{
                System.out.println("line 2130 loading pitches, ( item not complete");
            }


            //System.out.print(pitches.get(entry) + "" + "(" + entry+") ");// + entry.getId() + "-    ");
        }
    }
}
