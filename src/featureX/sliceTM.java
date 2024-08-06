package featureX;

import music.Note;

import java.util.*;

/**
 * Created by dorienhs on 14/12/15.
 */
public class sliceTM {


    //map to store

    TreeMap<ArrayList<Note>,TreeMap<ArrayList<Note>,Double>> markov;
    TreeMap<ArrayList<Note>, Integer> itemsPerRowInMarkov;

//    mapOfMaps.put(newObject, new LinkedHashMap<Object,Object>());
//    Map<Object,Object> objectMap = mapOfMaps.get(newObject);



    //as input a sliced collection of notes
    public void sliceTM(ArrayList<ArrayList<Note>> input){

        markov = new TreeMap<ArrayList<Note>, TreeMap<ArrayList<Note>,Double>>();


        //for each slice in input

        //count in the markov model or put one if doesn't exist yet



        //increment the row counter or put one if doesn't exist yet




    }

}
