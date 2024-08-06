package main;

import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import music.*;
import optimize.*;
import featureX.*;

import java.io.IOException;

class PMusicOR {
    public static void main(String[] args) throws IOException {


        System.out.println("-----------------");
        System.out.println("MorpheuS music generation 2.0");
        System.out.println("-----------------");
        System.out.println("Converting the xml file to the midi pitches. ");


        Cli inputarguments = new Cli(args);
        inputarguments.parse();


        featureX tensionTemplateModel = null;
        //initialise rnn
        //RNN model = new RNN(); SWITCH FOR RNN

        //todo other arguments as command line

        //String file = "Bach_Cello_Suite_3_-_Bourree_I"; //Castle_on_a_Cloud_s";
        //String file = "bach1";
        //String file ="Etude_in_A_Minor";
//        String file = "rach2_short";
//        String file = "Kabalevsky_Toccatina";
        //String file = "Bela_Bartok-Stick_Game";
        //String file = "Minuet_BWV_Anh";
        //String file = "The_Clowns";
        //String file = "The_Entertainer_short";
//        String file = "Chopin_Ballade_No._2_Piano_solo";


        String file = inputarguments.inputfile;

        //SET TO OVERRIDE filen
//        String file = "/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/strings1";
        Boolean tensionTemplate = false;


        if (tensionTemplate){
//            String fileTensionTemplate = "/Users/dorien/workspace/PMusicOR/singapore/strings1.xml"; //ormstoday/castle/Castle_on_a_Cloud_s.xml"; //./data/Raindrop_Prelude.xml";
            String fileTensionTemplate = file + ".xml";
            tensionTemplateModel = new featureX(fileTensionTemplate);
        }


        //filenameMelody = "./data/"+file+".xml";  //override
        //String filenameMelody = "./data/Haydns_Serenade_for_strings_for_Piano.xml";
        //String filenameMelody = "./data/practicing_haydn.xml"; //bach1.xml";
//        String filenameMelody = "/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+".xml";
        String filenameMelody = file + ".xml";
        featureX pitchmodel = new featureX(filenameMelody);



        String keyname = "C";
        Boolean keyModeMajor = true;


        //pitchmodel.calctTM();
        //pitchmodel.writeTM();

        //todo rename model2 to model later

        //TODO CAREFUL model is only for markov - for length etc, use template


        //create new instance with a certain length (or structure) and generate random notes in it


        //read in template for structure
        //String filenameTemplate = "./data/Haydns_Serenade_for_strings_for_Piano.xml"; //"./data/bach20red.xml";
//        String filenameTemplate = "/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+".xml";
        String filenameTemplate = filenameMelody;

        featureX model = new featureX(filenameTemplate);
        //detect the patterns

        //set reference TM of the corpus to compare with and range
        //model.setRefTMs(pitchmodel.getHorizontalTM(), pitchmodel.getVerticalTM());
        model.setRefRange(pitchmodel.getMinPitch(), pitchmodel.getMaxPitch(), pitchmodel.getMinPitchStaff1(),
                pitchmodel.getMaxPitchStaff1(), pitchmodel.getMinPitchStaff2(), pitchmodel.getMaxPitchStaff2());



        ///
        model.applyStructure(file + "-chrom.cos",
//        model.applyStructure("/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+"-chrom.cos" ,
                //model.a pplyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Etude_in_A_Minor-mid-2016-09-06-12-03-16-071//"+file+".SIATECCompress",
        //USED model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/rach2D/"+file+".SIATECCompress",
                    //USEDFIRST    model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Etude_in_A_Minor-mid-2016-09-06-01-25-25-751/"+file+".SIATECCompress",
        //model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Kabalevsky_Toccatina-mid-2016-09-06-01-19-08-677/"+file+".SIATECCompress",
        //model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Bela_Bartok-Stick_Game-mid-2016-09-06-00-25-42-078/"+file+".SIATECCompress",
             //used   model.applyStructure("/Users/dorien/workspace/PMusicOR/data/morpheus/The_Clowns-mid-2016-08-29-19-20-06-387/"+file+".SIATECCompress",  //+"-chrom.cos",
        //USEDmodel.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Minuet_BWV_Anh-mid-2016-08-29-18-22-56-278/"+file+".SIATECCompress",  //+"-chrom.cos",
             //   "Minuet_BWV_Anh-mid-2016-08-29-17-55-38-390/"+file+".SIATECCompress",  //+"-chrom.cos",
            //    "Minuet_BWV_Anh-mid-2016-08-27-13-02-44-100
        //        model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/The_Clowns-mid-2016-08-27-13-01-45-317/"+file+".SIATECCompress",
//        // Haydns_Serenade_for_strings_for_Piano-chrom.cos", / );
//        // bach20red-chrom
//        // .cos",
                480); //480 is the midi divisions
//
//
//        model.applyStructure("/home/dorienhs/workspace/PMusicOR/sia/Haydn45fixednorep-mid-2016-03-27-11-11-18-622" +
//                "/Haydn45fixednorep.SIATECCompress", 480);
//
//
        //second one:
//        model.applyStructure("/home/dorienhs/workspace/PMusicOR/sia/Haydn45fixednorep-mid-2016-03-27-23-06-36-320/" +
//                "/Haydn45fixednorep.SIATECCompress", 480);


        int windowSizeInDiv = 0;

        if (inputarguments.windowLength != 0) {
            // calculate the windowLength

            //todo calculate this
            int divPerQuarter = pitchmodel.getDivperQuarterNote();
            windowSizeInDiv = (divPerQuarter * 4) / inputarguments.windowLength;

        }else{
            System.out.println("Error. Please specify windowLength > 0");
            System.exit(0);
        }

            Integer numberOfVisualisationSlotsPerMeasure = inputarguments.windowsPerBar;


        //Integer instanceLength = 100;
        Integer instanceLength = model.getSizeofSong();

        Type type = Type.Note;
        //Instance instance = new Instance(instanceLength, model, model.getSong(), numberOfVisualisationSlotsPerMeasure);

        Instance instance = new Instance(instanceLength, model, model.getSong(), windowSizeInDiv, keyname, keyModeMajor);


        //calculate tension profile in tension model
        //set Tensionprofile in normal model

        if(tensionTemplate) {
            tensionTemplateModel.calculateWindows(4);
            instance.getSolution().setExternalTensionProfile(tensionTemplateModel.getAndCalculateTension());
        }

        //print out random instance
        //instance.getFragment().print();

//        instance.getSolution().writeMidiEqualDur();




        //make a move

//        if (type == Type.Chord) {

//            while (instance.ChangeMoveChords()) {
//                System.out.print("improved ");
//            }
//            instance.PerturbChords();


//        }
//
//        if (type == Type.Note){


        //change a note to test if it changes in the correct place in the score
        //instance.getSolution().setPitch((instance.getFragment().getEventsInFragment().get(1)), 0);
        //instance.getSolution().setPitch((instance.getFragment().getEventsInFragment().get(0)), 60);



        //instance.getSolution().putAllToConstant();
        //write initial piece as xml file
        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                .getFragment().getEventsInFragment(), filenameTemplate+"_start.xml");




        System.out.println("Optimizing: ");


        Boolean change1 = false;
        Boolean change1fast = true;
        Boolean change2 = false; //depreciated
        Boolean swap = true;
        Boolean perturb = true;
        Boolean changeSlice = false;
        Boolean changeSliceFast = true;
        int iters = inputarguments.iters;



        //idee: number of slices, constrain number of octaves in one staff per half bar or slice?




        //moved these things to the constructor
        //instance.getSolution().setInitialAsTemplate(); //is actually already done automatically
        //calculate tension profile
        //based on what's currently in model
        //instance.getSolution().setTensionProfile();








        //write tension graphs start:

        instance.getSolution().writeTensionGraphsCurrent("tension_start");
        instance.getSolution().writeTensionGraphsProfile("tension_profile");

        //repeat whole loop

        int count = 0;
        boolean totalImproved = true;
        int counter = 0;


        //todo set stopping criteria
        for (int i = 0; i < iters; i++){

            System.out.println("\nNumber of big loop: " + i);
            totalImproved = false;
            Double startloopscore = instance.getSolution().getScore();



            if (change1fast) {



                while (instance.getSolution().ChangeMoveNoteFaster()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved C1fast");
                    count++;

                    //write output every hundred moves
                    if (count%2 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                    System.out.print(" changeSliceFast completed. ");

                }
                System.out.print (" change1fast completed. ");

            }



            if (changeSlice) {


                while (instance.getSolution().ChangeMoveSlice()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved CS");
                    count++;

                    //write output every hundred moves
                    if (count%2 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                    System.out.print(" changeSliceFast completed. ");

                }
                System.out.print (" changeSlice completed. ");

            }


            if (i%5 == 0) {
                if (changeSliceFast) {

                    System.out.println("\nstart change slice fast");

                    while (instance.getSolution().ChangeMoveSliceFaster()) {//&& (count < 50)) {
//              System.out.print("improved ");
                        totalImproved = true;
                        System.out.print("improved CSfast");
                        count++;
                    }


                    //write output every hundred moves
                    if (count%2 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                    System.out.print(" changeSliceFast completed. ");

                }
                count++;


            }





            if (swap) {


                while (instance.getSolution().SwapTwoNotes()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved SW");
                    count++;

                    //write output every hundred moves
                    if (count%2 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                    System.out.print(" changeSliceFast completed. ");

                }
                System.out.print (" swap completed. ");

            }


            if (change1) {
                while (instance.getSolution().ChangeMoveNote()){ //&& (count < 100)) {
                    totalImproved = true;
                    System.out.print(" improved C1");
                    //System.out.print(instance.getSolution().getScore());
                    count++;


                    //write output every hundred moves
                    if (count%2 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                }
                System.out.print (" change1 completed. ");
                System.out.print(instance.getSolution().getScore());



            }

            if (change2) {
                while (instance.getSolution().ChangeMoveTwoNotes() && (count < 50)) {
                    count++;

                    //write output every ten moves
                    if (count%10 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                .getFragment().getEventsInFragment(), filenameTemplate+"_output"+count+ ".xml");
                    }
                }
                System.out.print (" change2 completed. ");
            }

            //if there was no improvement
            if(Double.compare(startloopscore, instance.getSolution().getScore()) == 0){
                System.out.print("\nNo improvement anymore this cycle");
                if (perturb) {
                    instance.getSolution().PerturbNotes();
                    }
                }
            }


        System.out.println(" Done.");
        instance.getSolution().printBest();
        System.out.println(" with a score of: " + instance.getSolution().getBestScore());
        instance.getSolution().writeMidiEqualDur();

        //write tension graphs end:
        instance.getSolution().writeTensionGraphsCurrent("tension_output");

        //write resulting piece
        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                        .getFragment().getEventsInFragment(), filenameTemplate+"_output.xml");
        instance.getSolution().closeSolution();
    }
}