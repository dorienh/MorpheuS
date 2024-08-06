package main;

import featureX.featureX;

import java.io.IOException;




    import music.*;
    import rnn.RNN;
    import optimize.*;
    import featureX.*;

    import java.awt.print.Printable;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.io.PrintWriter;

    public class AudioVisualiser {
        public static void main(String[] args) throws IOException {


            System.out.println("-----------------");
            System.out.println("MorpheuS Audio Tension Visualiser v2.0");
            System.out.println("-----------------");
            System.out.println("Calculating tension from input file. ");

            Cli inputarguments = new Cli(args);
            inputarguments.parse();

            //initialise rnn
            //RNN model = new RNN(); SWITCH FOR RNN



            //String filenameMelody = "./data/icmpc/tristan/beethovenchord.xml";
            //String filenameMelody = "./data/examples/ckey.xml";
            //String filenameMelody = "./data/stimuli/A14.xml";
            //String filenameMelody = "./data/examples/ckey2.xml";

            String filenameMelody ="";
//
//            if (args.length > 0){
//                filenameMelody = args[0];
//
//            }
//            else {
//                System.out.print("You did not specify any filename as on option.");
//                filenameMelody = "/home/dorien/workspace/PMusicOR/data/resultsChords.txt"; // "./data/icmpc/schubertintro.xml";
//
//                //System.exit(0);
//            }
////       filename = "./data/Prelude_C_Major_-_Bach.xml";


            filenameMelody = inputarguments.inputfile;








            featureX pitchmodel = new featureX(filenameMelody, true);
            //pitchmodel.calctTM();
            //pitchmodel.writeTM();




//            int numberOfVisualisationSlotsPerMeasure = 4;  //8 for schubert and beethv  1 for stim 2 voor laatste
//
//
//            if (args.length > 2){
//                filenameMelody = args[2];
//                numberOfVisualisationSlotsPerMeasure = Integer.valueOf(args[1]);  //8 for schubert and beethv  1
//                // for
//                // stim 2
//                // voor
//                // laatste
//
//            }
//            else {
//
//                numberOfVisualisationSlotsPerMeasure = 1;  //8 for schubert and beethv  1 for stim 2 voor laatste
//            }
//


//            if (args.length> 1){
//
//
//                //number of quarter beats per measure
//                pitchmodel.setMeterUnits(Integer.valueOf(args[1])); //16 for schubert and beethoven 8 for bach
//
//            }
//            else {
//
//
//
//                pitchmodel.setMeterUnits(4); //16 for schubert and beethoven 8 for bach
//            }
//            // (tristan and beethovenchord)
//


            pitchmodel.setMeterUnits(inputarguments.meterUnits);


            //filenameOut = "tester.html";

            System.out.println("Visualising...");


            //pitchmodel.calculateWindows(numberOfVisualisationSlotsPerMeasure); //16 is windowsize; prelude: 8 per maat



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
