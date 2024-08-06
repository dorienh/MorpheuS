package featureX;

import music.Note;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static featureX.featureX.*;

/**
 * Created by dorienhs on 25/01/16.
 */
public class structure {

    public static void structure(String filename, ArrayList<Note> allNotes, Integer midiDivs, Integer xmlDivs) throws
            IOException {



        Double tempoFactor= (double) midiDivs / xmlDivs;


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

        for(String thisline: lines) {

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


               // Pvector.add(getNote(time, midi));

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


                        //todo is it really the time or a shift?
                        time = (int) (time / tempoFactor);

                        //setStructure(Pvector, time, change);

                        //todo check if it really changes
                        //movement or start date in tantums?

                    }


                    counter++;


                }




        }


        //for each vector

        //if it's the first: set as fixed: so in fact, do nothing







    }







}


