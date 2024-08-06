package main;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by dorienhs on 17/03/16.
 */
public class timeseriessampler {



    public static void main(String[] args) throws IOException {


        //read the midi text file
        FileInputStream fstream = new FileInputStream("data/Caprice_24_violin_timbralFeatures_inserted.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String data;



        ArrayList<ArrayList> midiList = new ArrayList<>();


        while ((data = br.readLine()) != null) {


            String[] tmp = data.split(" ");    //Split space
            ArrayList<String> myArrayList = new ArrayList<>();


            if (tmp.length > 1) {
                myArrayList.add(tmp[1]);
                myArrayList.add(tmp[1]);
                myArrayList.add(tmp[2]);
                myArrayList.add(tmp[3]);
                myArrayList.add(tmp[4]);
                myArrayList.add(tmp[5]);
                myArrayList.add(tmp[6]);
                myArrayList.add(tmp[7]);
            }

            midiList.add((ArrayList) myArrayList.clone());
        }


        System.out.println(midiList.get(0).get(0) + " " + midiList.get(0).get
                (1) + " " + midiList.get(0).get(2) + " " + midiList.get(0).get(3) + " " + midiList.get(0).get
                (4) + " " + midiList.get(0).get(5) + " " + midiList.get(0).get(6) + " " + midiList.get(0).get(7) );


        int counter = 32;
        //int thisend = 14050;
        for (int i = 1; i < midiList.size(); i++){

            //print value x times
            //for(int j = 0; j < 32; j++){

            if (counter == 32) {
                System.out.println(midiList.get(i).get(0) + " " + midiList.get(i).get
                        (1) + " " + midiList.get(i).get(2) + " " + midiList.get(i).get(3) + " " + midiList.get(i).get
                        (4) + " " + midiList.get(i).get(5) + " " + midiList.get(i).get(6) + " " + midiList.get(i).get
                        (7) );
                //}

                counter = 0;
            }
            else{
                counter++;
            }



        }




    }
}
