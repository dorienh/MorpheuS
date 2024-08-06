package main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import music.*;
import rnn.RNN;
import optimize.*;
import featureX.*;

import java.awt.print.Printable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Visualiser {


    public static void main(String[] args) throws IOException {
        System.out.println("-----------------");
        System.out.println("MorpheuS tension visualiser v2.0");
        System.out.println("-----------------");
        System.out.println("Converting the xml file to the midi pitches. ");

        Cli inputarguments = new Cli(args);
        inputarguments.parse();

        //initialise rnn
        //RNN model = new RNN(); SWITCH FOR RNN



        //String filenameMelody = "./data/icmpc/tristan/beethovenchord.xml";
        //String filenameMelody = "./data/examples/ckey.xml";
        //String filenameMelody = "./data/stimuli/A14.xml";
        //String filenameMelody = "./data/examples/ckey2.xml";

        String filenameMelody ="";

//        if (args.length > 0){
//            filenameMelody = args[0];
//
//        }
//        else {
//            System.out.print("You did not specify any filename as on option.");
//            //filenameMelody = "/home/dorienhs/workspace/PMusicOR/out/artifacts/TensionVisualiser/A14.xml";//
//            filenameMelody = "/home/dorien/workspace/PMusicOR/data/Haydn_half.xml"; //Haydn45_original_partcleaned.xml";   //readable_morpheusBach.xml"; //out/artifacts/TensionVisualiser/joplin.xml";//
//            // ./data/Haydns_Serenade_for_strings_for_Piano
//            // .xml"; // "
//                 //   ./data/icmpc/schubertintro.xml";
//            //System.exit(0);
//        }
//       filename = "./data/Prelude_C_Major_-_Bach.xml";


        filenameMelody = inputarguments.inputfile;
//        filenameMelody = "/home/dorien/workspace/PMusicOR/out/artifacts/Tension2.0/clowns_output.xml";





        featureX pitchmodel = new featureX(filenameMelody);
        pitchmodel.calctTM();
        pitchmodel.writeTM();




        //overridden manually later! so change twice!!
        int numberOfVisualisationSlotsPerMeasure = 1;  //8 for schubert and beethv  1 for stim 2 voor laatste


//        if (args.length > 2){
//            filenameMelody = args[2];
//            numberOfVisualisationSlotsPerMeasure = Integer.valueOf(args[1]);  //8 for schubert and beethv  1
//            // for
//            // stim 2
//            // voor
//            // laatste
//
//        }
//        else {
//
//            numberOfVisualisationSlotsPerMeasure = 4;  //8 for schubert and beethv  1 for stim 2 voor laatste
//        }


        numberOfVisualisationSlotsPerMeasure = inputarguments.windowsPerBar;

//
//        if (args.length> 1){
//
//
//            //number of quarter beats per measure
//            pitchmodel.setMeterUnits(Integer.valueOf(args[1])); //16 for schubert and beethoven 8 for bach
//
//        }
//        else {
//
//
//
//            pitchmodel.setMeterUnits(4); //16 for schubert and beethoven 8 for bach
//        }
            // (tristan and beethovenchord)

        pitchmodel.setMeterUnits(inputarguments.meterUnits);

        //manual override
        //numberOfVisualisationSlotsPerMeasure = 4;  //8 for schubert and beethv  1 for stim 2 voor laatste
        //pitchmodel.setMeterUnits(4); //16 for schubert and beethoven 8 for bach


        //todo read this
        int divPerQuarter = 4;

        int windowSizeInDiv = 0;

        //8 per quarter
        // 2 _> 16
        // 4 -- 8
        // 16 -- 2

        //// TODO: 29/06/16
        //check divisions per quarter note and set the
        if (inputarguments.windowLength != 0){
            // calculate the windowLength

            //todo calculate this
            divPerQuarter = pitchmodel.getDivperQuarterNote();
            windowSizeInDiv =  (divPerQuarter * 4)  / inputarguments.windowLength;



            //filenameOut = "tester.html";

            System.out.println("Visualising...");

            //todo werkt niet bij 8: misschien te veel lege windows?


            pitchmodel.calculateWindowsForSize(windowSizeInDiv); //16 is windowsize; prelude: 8 per maat


        }
        else {

            System.out.println("Visualising...");

            pitchmodel.calculateWindows(numberOfVisualisationSlotsPerMeasure); //16 is windowsize; prelude: 8 per maat


        }





        System.out.println("windows calculated");


        //calculate tension profile
        pitchmodel.calculateTension();

        //pitchmodel.visualiseNotes();


        //System.out.println("Uploading to server...");
        //ftpUploader upload = new ftpUploader(filenameOut);

        System.out.println("Done.");


        //todo set normalisation not for 0 off or on for longer or short fragments



        //get entropy profile from prelude




    }





}