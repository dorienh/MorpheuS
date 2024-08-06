/**
 * Created by dorien on 10/11/14.
 */
package optimize;

import music.Fragment;
import music.Note;
import music.Type;
import rnn.RNN;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import featureX.*;


public class Instance {
    private Integer length;
    private Type type;
    private Fragment fragment;
    private Fragment bestFragment;
    private double bestScore;

    private Solution solution;
    private featureX model;

    private Integer numberofVisualisationSlotsPerMeasure;

    private Integer windowSizeInDiv;




    public Instance(Integer length, featureX model, ArrayList<Note> allNotes, Integer windowSizeInDiv, String keyname, Boolean keyModeMajor) throws FileNotFoundException,
            UnsupportedEncodingException {
        this.length = length;
        this.type = type;
        this.windowSizeInDiv = windowSizeInDiv;
        //this.numberofVisualisationSlotsPerMeasure = numberofVisualisationSlotsPerMeasure;
        this.model = model;


        //create new fragment
        this.fragment = new Fragment(length, model);


        //set random notes (defined by type)
        //this.fragment.setRandom(type); dont: this stores in on the note level!
        this.fragment.setTemplate(allNotes);


        //calculate bestScore
        //setCurrentAsBest();
//        bestScore = bestFragment.getScore();



        solution = new Solution(this.fragment, this.model, this.windowSizeInDiv, keyname, keyModeMajor);

        //settings chooce starting options
        //solution.setRandomInitial(); done automatically
        //solution.setInitialAsTemplate();
        //solution.putAllToConstant();

        //set pitch spelling to zero for now.




        //solution.print();






    }


//    public void setCurrentAsBest() {
//        this.bestFragment = fragment;
//
//    }

    public Fragment getFragment() {
        return fragment;
    }



    public void LocalSearchChange(){


        //this in separate class?


        //TODO while improvements

        //perform 1 move

//        ChangeMoveChords();


        //maybe return best
    }

    public Solution getSolution() {
        return solution;
    }


    //
//
//    public Boolean ChangeMoveChords(){
//
//
//
//        //make a working copy of fragment
//        Fragment neighbour = new Fragment(fragment);
//        Fragment bestNeighbour = new Fragment(fragment);
//        Double bestNeighbourScore = bestNeighbour.getScore();
//        Boolean improvement = false;
//
//
//        //for each of the chords
//        for (Map.Entry<Integer,MusicEvent> entry : neighbour.getEventsInFragment().entrySet()) {
//
//            //for each possible root
//
//            for(RootNote root : RootNote.values()){
//
//                ((Chord)entry.getValue()).setRootNote(root);
//
//                for (ChordMode chordMode : ChordMode.values()) {
//
//                    ((Chord)entry.getValue()).setChordMode(chordMode);
//
//                    //if score better, store this as best of neighbourhood
//                    if (neighbour.getScore() < bestNeighbourScore){
//
//                        //and if there has been a change versus the original
//                        //TODO test if IDs are reallly the same in copied objects
//                        Integer id = ((Chord) entry).getId();
//                        if (((Chord)fragment.getEventsInFragment().get(id)).getChordMode() != chordMode){
//                            if (((Chord)fragment.getEventsInFragment().get(id)).getRootNote() != root){
//
//                                //update best
//
//                                improvement = true;
//                                bestNeighbour = neighbour;
//                                bestNeighbourScore = bestNeighbour.getScore();
//
//
//                            }
//                        }
//
//
//                    }
//
//
//
//
//                }
//
//            }
//
//
//
//        }
//
//        //reset neighbour
//        neighbour = fragment;
//
//        //if improvement found, return true
//
//        return improvement;
//
//    }
//
//
//
//    public void PerturbChords(){
//        //change percentage randomly
//
//        Integer perturbationSize = Math.round(perturbationPercent * fragment.getEventsInFragment().size() / 100);
//        ArrayList<Integer> ids = new ArrayList();
//        Random random = new Random();
//
//        //select a unique ids
//        for (int i = 0; i < perturbationSize; i++){
//            Integer newID = random.nextInt(perturbationSize);
//            while (ids.contains(newID) == true)
//                newID = random.nextInt(perturbationSize);
//            ids.add(newID);
//        }
//
//        //for each of the IDs, set a random chord  .changeRandom()
//        for (Integer id : ids) {
//            ((Chord)fragment.getEventsInFragment().get(id)).changeRandom();
//        }
//
//
//
//
//    }
//
//
//
//
//
//    public void PerturbNotes(){
//        //change percentage randomly
//
//        Integer perturbationSize = Math.round(perturbationPercent * fragment.getEventsInFragment().size() / 100);
//        ArrayList<Integer> ids = new ArrayList();
//        Random random = new Random();
//
//        //select a unique ids
//        for (int i = 0; i < perturbationSize; i++){
//            Integer newID = random.nextInt(perturbationSize);
//            while (ids.contains(newID) == true)
//                newID = random.nextInt(perturbationSize);
//            ids.add(newID);
//
//        }
//
//        //for each of the IDs, set a random note  .changeRandom()
//        for (Integer id : ids) {
//            ((Note)fragment.getEventsInFragment().get(id)).changeRandom();
//
//        }
//
//
//
//
//    }
//

}
