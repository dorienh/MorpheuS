package optimize;

import music.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dorienhs on 22/02/16.
 */
public class tools {




    public static Integer getPitchWithStructure(Integer note, HashMap<Integer, Integer> pitches, Map<Integer, Note>
            eventsInFragment){

        Integer pitch = -600;


        //Note thisnote = note;
        Integer change = 0;

        //int startnote = note;
        //Note thisnote = eventsInFragment.get(note);

        if (eventsInFragment.get(note).getReferenceNoteID() == 0){


            pitch = pitches.get(note);

        }



        while (eventsInFragment.get(note).getReferenceNoteID() != 0){

            //if(pitches.containsKey(note)){



            note = eventsInFragment.get(eventsInFragment.get(note).getReferenceNoteID()).getId();

            change = change + eventsInFragment.get(note).getReferenceChange();


        }


        pitch = pitches.get(note) + change;



        if (pitch == -600){

            //todo check this
            System.out.println("Warning getPitchWithStructure didn't find the note");
        }


        return pitch;
    }






    public static Integer getSpellingWithStructure(Integer note, HashMap<Integer, Integer> spelling, Map<Integer,
            Note>
            eventsInFragment){

        Integer spell = -500;

        if (eventsInFragment.get(note).getReferenceNoteID() == 0){


            spell = spelling.get(note);

        }



        while (eventsInFragment.get(note).getReferenceNoteID() != 0){

            //if(pitches.containsKey(note)){



            note = eventsInFragment.get(eventsInFragment.get(note).getReferenceNoteID()).getId();


        }


        spell = spelling.get(note);



        if (spell == -600){

            //todo check this
            System.out.println("Warning getPitchWithStructure didn't find the note");
        }


        return spell;
    }



}
