package main;

import featureX.featureX;
import music.Note;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by dorienhs on 29/02/16.
 */
public class xml2mid {


    public static void main(String[] args) throws IOException {


        System.out.println("-----------------");
        System.out.println("MorpheuS xml2mid");
        System.out.println("-----------------");
        System.out.println("Converting the xml file to the midi pitches. ");


        String filenameMelody = "";

        if (args.length > 0) {
            filenameMelody = args[0];

        } else {
            System.out.println("You did not specify any input filename as on option.");
            //filenameMelody = "./data/Haydn45fixed.xml";
            filenameMelody = "./data/bach1.xml";
            //System.exit(0);
        }


        String filenameOut = "bach1output.txt";

        if (args.length > 1) {
            filenameOut = args[1];

        } else {
            System.out.println("You did not specify any filename as output file, so writing to output.txt.");
            //System.exit(0);
        }


        featureX pitchmodel = new featureX(filenameMelody);

        ArrayList<Note> song = pitchmodel.getSong();

        PrintWriter writer = new PrintWriter(filenameOut, "UTF-8");

        int length = 0;

        for (Note inote : song) {

            int enddur = inote.getDuration() + inote.getStartTime();
            if (length < enddur) {
                length = enddur;
            }

        }


        writer.println("total length (in divisions): " + length);


        //read the midi text file
        FileInputStream fstream = new FileInputStream("data/Haydn45fixednorep.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String data;
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();
        data = br.readLine();


        ArrayList<ArrayList> midiList = new ArrayList<>();


        while ((data = br.readLine()) != null) {
            ArrayList<String> myArrayList = new ArrayList<>();
            myArrayList.clear();


            String[] tmp = data.split(" ");    //Split space

            if (tmp.length >3){
                myArrayList.add(tmp[0]);
                myArrayList.add(tmp[3]);

            }

            midiList.add(myArrayList);
        }


        writer.println("divisions_per_measure: " + pitchmodel.getDivpermeasure());
        writer.println("start_time pitch midi_pitch duration barnumber staff");
        for (Note inote : song){
           writer.println(inote.getStartTime() * 60 + " " +inote.getStartTime() + " " + inote.getPitch() + inote
                   .getAccidental() + " " + inote
                           .getMidiPitch() +  " " + inote.getDuration() + " " + inote.getMeasure() + " " + inote
                   .getStaff() + " " + inote.getVoice());


           //check if it exists in the midi file

           ArrayList<String> test = new ArrayList<>();
           int start = inote.getStartTime() * 60;
           String starts = (String) Integer.toString(start);
           test.add(starts);
           Boolean found = false;
           test.add("n="+inote.getMidiPitch());
           for (ArrayList<String> ilist : midiList){
               if (ilist.equals(test)){
                   found = true;
               }
               //if(ilist.get(0)=="230520"){
                 //  System.out.println("ok");
               //}
           }

           if (!found){
               System.out.println("NOT FOUND: " + inote.getStartTime() * 60 + " " +inote.getStartTime() + " " +   inote.getPitch() +   inote.getAccidental() + " " + inote.getMidiPitch() +  " " + inote.getDuration() + " " + inote.getMeasure() + " " + inote.getStaff() + " " + inote.getVoice());
           }
           //System.out.println(inote.getStartTime() * 60 + " " +inote.getStartTime() + " " + inote.getPitch() +
                    //inote.getAccidental() + " " + inote.getMidiPitch() +  " " + inote.getDuration() + " " + inote
                    //.getMeasure() + " " + inote.getStaff() + " " + inote.getVoice());
           //}



       }

        writer.close();




        System.out.println("Done.");


    }
}
