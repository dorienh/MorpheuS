package main;

import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import music.*;
import optimize.*;
import featureX.*;

import java.io.IOException;

class Loader {
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
        String file = "strings1";
        Boolean tensionTemplate = false;


        if (tensionTemplate){
            String fileTensionTemplate = "/Users/dorien/workspace/PMusicOR/singapore/strings1.xml"; //ormstoday/castle/Castle_on_a_Cloud_s.xml"; //./data/Raindrop_Prelude.xml";
            tensionTemplateModel = new featureX(fileTensionTemplate);
        }


        //String filenameMelody = inputarguments.inputfile;
        //filenameMelody = "./data/"+file+".xml";  //override
        //String filenameMelody = "./data/Haydns_Serenade_for_strings_for_Piano.xml";
        //String filenameMelody = "./data/practicing_haydn.xml"; //bach1.xml";
        String filenameMelody = "/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+".xml";
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
        String filenameTemplate = "/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+".xml";
        featureX model = new featureX(filenameTemplate);
        //detect the patterns

        //set reference TM of the corpus to compare with and range
        //model.setRefTMs(pitchmodel.getHorizontalTM(), pitchmodel.getVerticalTM());
        model.setRefRange(pitchmodel.getMinPitch(), pitchmodel.getMaxPitch(), pitchmodel.getMinPitchStaff1(),
                pitchmodel.getMaxPitchStaff1(), pitchmodel.getMinPitchStaff2(), pitchmodel.getMaxPitchStaff2());



        ///

        model.applyStructure("/Users/dorien/workspace/PMusicOR/data/morpheus/singapore/"+file+"-chrom.cos" ,
                //model.applyStructure("/home/dorien/workspace/PMusicOR/data/morpheus/Etude_in_A_Minor-mid-2016-09-06-12-03-16-071//"+file+".SIATECCompress",
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
        Boolean change1fast = false;
        Boolean change2 = false; //depreciated
        Boolean swap = false;
        Boolean perturb = false;
        Boolean changeSlice = false;
        Boolean changeSliceFast = false;
        int iters = 0;



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

        boolean totalImproved = true;
        int counter = 0;



        //Solution: (only unique)48(1024) 47(3) 38(4) 36(772) 92(5) 49(6) 55(1032) 40(777) 46(530) 88(285) 88(286) 37(291) 36(294) 46(295) 36(809) 36(810) 48(811) 36(812) 40(45) 40(813) 72(814) 37(47) 52(818) 43(52) 57(822) 57(56) 52(57) 48(826) 43(59) 88(315) 37(60) 39(316) 36(61) 47(318) 36(63) 38(319) 52(320) 45(322) 39(69) 39(70) 50(327) 36(73) 36(841) 47(842) 48(75) 42(843) 44(77) 42(845) 48(334) 45(846) 40(80) 69(592) 47(81) 36(593) 44(82) 41(594) 64(595) 45(596) 36(597) 43(598) 39(854) 44(87) 42(599) 45(855) 45(88) 38(600) 43(856) 38(345) 47(857) 46(603) 40(604) 42(605) 52(606) 45(607) 48(864) 45(609) 80(100) 57(101) 88(614) 80(103) 45(616) 48(872) 44(106) 51(107) 38(619) 42(109) 45(116) 48(375) 51(633) 51(634) 51(635) 46(124) 91(125) 36(639) 43(895) 46(128) 43(896) 43(897) 50(898) 42(131) 36(644) 58(135) 45(656) 47(657) 55(403) 46(659) 47(661) 57(406) 43(409) 47(923) 43(412) 38(414) 38(670) 42(926) 43(415) 36(671) 41(416) 39(417) 43(673) 58(674) 36(421) 41(422) 43(678) 39(423) 43(679) 41(680) 80(681) 42(426) 52(429) 45(689) 47(690) 38(704) 79(705) 36(709) 57(201) 37(459) 52(462) 45(465) 60(721) 36(468) 36(725) 36(471) 41(729) 81(986) 50(475) 40(987) 36(988) 40(733) 43(992) 42(739) 40(995) 38(996) 63(231) 86(999) 43(745) 48(490) 45(1003) 40(1004) 47(1008) 40(1014) 38(1017) 45(1018) improved CSfast

        //todo load string to solution
        String solutionstring = "48(1024) 47(3) 38(4) 36(772) 92(5) 49(6) 55(1032) 40(777) 46(530) 88(285) 88(286) 37(291) 36(294) 46(295) 36(809) 36(810) 48(811) 36(812) 40(45) 40(813) 72(814) 37(47) 52(818) 43(52) 57(822) 57(56) 52(57) 48(826) 43(59) 88(315) 37(60) 39(316) 36(61) 47(318) 36(63) 38(319) 52(320) 45(322) 39(69) 39(70) 50(327) 36(73) 36(841) 47(842) 48(75) 42(843) 44(77) 42(845) 48(334) 45(846) 40(80) 69(592) 47(81) 36(593) 44(82) 41(594) 64(595) 45(596) 36(597) 43(598) 39(854) 44(87) 42(599) 45(855) 45(88) 38(600) 43(856) 38(345) 47(857) 46(603) 40(604) 42(605) 52(606) 45(607) 48(864) 45(609) 80(100) 57(101) 88(614) 80(103) 45(616) 48(872) 44(106) 51(107) 38(619) 42(109) 45(116) 48(375) 51(633) 51(634) 51(635) 46(124) 91(125) 36(639) 43(895) 46(128) 43(896) 43(897) 50(898) 42(131) 36(644) 58(135) 45(656) 47(657) 55(403) 46(659) 47(661) 57(406) 43(409) 47(923) 43(412) 38(414) 38(670) 42(926) 43(415) 36(671) 41(416) 39(417) 43(673) 58(674) 36(421) 41(422) 43(678) 39(423) 43(679) 41(680) 80(681) 42(426) 52(429) 45(689) 47(690) 38(704) 79(705) 36(709) 57(201) 37(459) 52(462) 45(465) 60(721) 36(468) 36(725) 36(471) 41(729) 81(986) 50(475) 40(987) 36(988) 40(733) 43(992) 42(739) 40(995) 38(996) 63(231) 86(999) 43(745) 48(490) 45(1003) 40(1004) 47(1008) 40(1014) 38(1017) 45(1018)";
        //instance.setPitches(solutionstring);

        instance.getSolution().setLoadedPitches(solutionstring);



        //write output
        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                        .getFragment().getEventsInFragment(),
                filenameTemplate+"_selected_output"+ ".xml");







        //todo set stopping criteria
        for (int i = 0; i < iters; i++){

            System.out.println("\nNumber of big loop: " + i);
//        while (totalImproved == true) {
//
            totalImproved = false;

            Double startloopscore = instance.getSolution().getScore();

//            while (instance.getSolution().SwapTwoNotes()) {
//                //System.out.print("improved ");
//                totalImproved = true;
//                System.out.print("improved ");
//            }


            int count = 0;

            if (change1fast) {


                while (instance.getSolution().ChangeMoveNoteFaster()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved C1fast");
                    count++;

                }
                System.out.print (" change1fast completed. ");

            }



            if (changeSlice) {


                while (instance.getSolution().ChangeMoveSlice()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved CS");
                    count++;

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
                    System.out.print(" changeSliceFast completed. ");

                }
                count++;


                //write output every hundred moves
                if (count%1 == 0){
                    model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                    .getFragment().getEventsInFragment(),
                            filenameTemplate+"_output"+count+ ".xml");
                }
            }





            if (swap) {


                while (instance.getSolution().SwapTwoNotes()){//&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved SW");
                    count++;

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
                    if (count%10 == 0){
                        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                                        .getFragment().getEventsInFragment(),
                                filenameTemplate+"_output"+count+ ".xml");
                    }
                }

                System.out.print (" change1 completed. ");
                System.out.print(instance.getSolution().getScore());



            }

            count = 0;
            if (change2) {


                while (instance.getSolution().ChangeMoveTwoNotes()&& (count < 50)) {
//              System.out.print("improved ");
                    totalImproved = true;
                    System.out.print("improved C2");
                    count++;

                }
                System.out.print (" change2 completed. ");

            }










            //if there was no improvement
            if(Double.compare(startloopscore, instance.getSolution().getScore()) == 0){
                System.out.print("\nNo improvement anymore this cycle");
                if (perturb) {
                    instance.getSolution().PerturbNotes();

//                    if (totalImproved == false) {
//                        // no improvement found: perturb
//
//                        counter++;
//
//                        //continue for 2 loops
//                        if (counter < 3) {
//                            totalImproved = true;
//                        }
//
//
//                    }
                }
            }
        }
        //rnn.evaluate_melody();


        System.out.println(" Done.");

        instance.getSolution().printBest();

        System.out.println(" with a score of: " + instance.getSolution().getBestScore());

        instance.getSolution().writeMidiEqualDur();
//        }



        //get entropy profile from prelude





        //write tension graphs end:
        instance.getSolution().writeTensionGraphsCurrent("tension_output");


        //write resulting piece
        model.writePitches(instance.getSolution().getPitches(), instance.getSolution().getSpelling(), instance
                        .getFragment().getEventsInFragment(),
                filenameTemplate+"_output.xml");

//instance.getFragment().getEventsInFragment()

        instance.getSolution().closeSolution();

    }









}