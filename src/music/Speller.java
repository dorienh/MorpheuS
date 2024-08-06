package music;

import java.util.HashMap;

/**
 * Created by dorienhs on 22/02/16.
 */
public class Speller {


    private HashMap<Integer, HashMap<Integer, String>> CodeToSpelling;
    private HashMap<String, Integer> spellingToCode;


    public Speller() {



        spellingToCode = new HashMap<>();


        CodeToSpelling = new HashMap<>();

        HashMap<Integer, String> codeToSpell1 = new HashMap<>();

        HashMap<Integer, String> codeToSpell2 = new HashMap<>();

        HashMap<Integer, String> codeToSpell3 = new HashMap<>();

        HashMap<Integer, String> codeToSpell4 = new HashMap<>();

        HashMap<Integer, String> codeToSpell5 = new HashMap<>();

        HashMap<Integer, String> codeToSpell6 = new HashMap<>();

        HashMap<Integer, String> codeToSpell7 = new HashMap<>();

        HashMap<Integer, String> codeToSpell8 = new HashMap<>();

        HashMap<Integer, String> codeToSpell9 = new HashMap<>();

        HashMap<Integer, String> codeToSpell10 = new HashMap<>();

        HashMap<Integer, String> codeToSpell11 = new HashMap<>();


        HashMap<Integer, String> codeToSpell12 = new HashMap<>();


        Boolean Cmaj = true;
        if(Cmaj) {

            codeToSpell1.put(0, "C");
            codeToSpell1.put(1, "B#");
            codeToSpell1.put(2, "A###");
            codeToSpell1.put(-1, "Dbb");
            codeToSpell1.put(-2, "Ebbbb");
            CodeToSpelling.put(0, codeToSpell1);

            codeToSpell2.put(0, "C#");
            codeToSpell2.put(1, "B##");
            codeToSpell2.put(2, "A####");
            codeToSpell2.put(-1, "Db");
            codeToSpell2.put(-2, "Ebbb");
            CodeToSpelling.put(1, codeToSpell2);


            codeToSpell3.put(0, "D");
            codeToSpell3.put(1, "C##");
            codeToSpell3.put(2, "B###");
            codeToSpell3.put(-1, "Ebb");
            codeToSpell3.put(-2, "Fbbb");
            CodeToSpelling.put(2, codeToSpell3);


            codeToSpell4.put(0, "Eb");
            codeToSpell4.put(1, "D#");
            codeToSpell4.put(2, "C###");
            codeToSpell4.put(-1, "Fbb");
            codeToSpell4.put(-2, "Gbbbb");
            CodeToSpelling.put(3, codeToSpell4);


            codeToSpell5.put(0, "E");
            codeToSpell5.put(1, "D##");
            codeToSpell5.put(2, "C####");
            codeToSpell5.put(-1, "Fb");
            codeToSpell5.put(-2, "Gbbb");
            CodeToSpelling.put(4, codeToSpell5);


            codeToSpell6.put(0, "F");
            codeToSpell6.put(1, "E#");
            codeToSpell6.put(2, "D###");
            codeToSpell6.put(-1, "Gbb");
            codeToSpell6.put(-2, "Abbbb");
            CodeToSpelling.put(5, codeToSpell6);


            codeToSpell7.put(0, "F#");
            codeToSpell7.put(1, "E##");
            codeToSpell7.put(2, "D####");
            codeToSpell7.put(-1, "Gb");
            codeToSpell7.put(-2, "Abbb");
            CodeToSpelling.put(6, codeToSpell7);


            codeToSpell8.put(0, "G");
            codeToSpell8.put(1, "F##");
            codeToSpell8.put(2, "E###");
            codeToSpell8.put(-1, "Abb");
            codeToSpell8.put(-2, "Gbbbb");
            CodeToSpelling.put(7, codeToSpell8);


            codeToSpell9.put(0, "G#");
            codeToSpell9.put(1, "F###");
            codeToSpell9.put(2, "E####");
            codeToSpell9.put(-1, "Ab");
            codeToSpell9.put(-2, "Gbbb");
            CodeToSpelling.put(8, codeToSpell9);


            codeToSpell10.put(0, "A");
            codeToSpell10.put(1, "G##");
            codeToSpell10.put(2, "F####");
            codeToSpell10.put(-1, "Bbb");
            codeToSpell10.put(-2, "Cbbb");
            CodeToSpelling.put(9, codeToSpell10);

            codeToSpell11.put(0, "Bb");
            codeToSpell11.put(1, "A#");
            codeToSpell11.put(2, "G###");
            codeToSpell11.put(-1, "Cbb");
            codeToSpell11.put(-2, "Dbbbb");
            CodeToSpelling.put(10, codeToSpell11);

            codeToSpell12.put(0, "B");
            codeToSpell12.put(1, "A##");
            codeToSpell12.put(2, "G####");
            codeToSpell12.put(-1, "Cb");
            codeToSpell12.put(-2, "Dbbb");
            CodeToSpelling.put(11, codeToSpell12);


            spellingToCode.put("C", 0);
            spellingToCode.put("D", 0);
            spellingToCode.put("E", 0);
            spellingToCode.put("F", 0);
            spellingToCode.put("G", 0);
            spellingToCode.put("A", 0);
            spellingToCode.put("B", 0);
            spellingToCode.put("C#", 0);
            //spellingToCode.put("D#",0);
            spellingToCode.put("Eb", 0);
            spellingToCode.put("F#", 0);
            spellingToCode.put("G#", 0);
            //spellingToCode.put("Ab",0);
            spellingToCode.put("Bb", 0);


            spellingToCode.put("B#", 1);
            spellingToCode.put("C##", 1);
            spellingToCode.put("D##", 1);
            spellingToCode.put("E#", 1);
            spellingToCode.put("F##", 1);
            spellingToCode.put("G##", 1);
            spellingToCode.put("A##", 1);

            spellingToCode.put("B###", 1);
            //spellingToCode.put("C###",1);
            spellingToCode.put("D#", 1);
            spellingToCode.put("E##", 1);
            spellingToCode.put("F###", 1);
            //spellingToCode.put("G#",1);
            spellingToCode.put("A#", 1);


            spellingToCode.put("A###", 2);
            spellingToCode.put("B###", 2);
            spellingToCode.put("C####", 2);
            spellingToCode.put("D###", 2);
            spellingToCode.put("E###", 2);
            spellingToCode.put("F####", 2);
            spellingToCode.put("G####", 2);

            spellingToCode.put("A#####", 2);
            //spellingToCode.put("B####",2);
            spellingToCode.put("C###", 2);
            spellingToCode.put("D####", 2);
            spellingToCode.put("E####", 2);
            //spellingToCode.put("F###",2);
            spellingToCode.put("G###", 2);


            spellingToCode.put("Dbb", -1);
            spellingToCode.put("Ebb", -1);
            spellingToCode.put("Fb", -1);
            spellingToCode.put("Gbb", -1);
            spellingToCode.put("Abb", -1);
            spellingToCode.put("Bbb", -1);
            spellingToCode.put("Cb", -1);

            spellingToCode.put("Db", -1);
            //spellingToCode.put("Eb",-1);
            spellingToCode.put("Fbb", -1);
            spellingToCode.put("Gb", -1);
            spellingToCode.put("Ab", -1);
            //spellingToCode.put("Bbbb",-1);
            spellingToCode.put("Cbb", -1);


            spellingToCode.put("Ebbbb", -2);
            spellingToCode.put("Fbbb", -2);
            spellingToCode.put("Gbbb", -2);
            spellingToCode.put("Abbbb", -2);
            spellingToCode.put("Bbbbb", -2);
            spellingToCode.put("Cbbb", -2);
            spellingToCode.put("Dbbb", -2);

            spellingToCode.put("Ebbb", -2);
            //spellingToCode.put("Fbb",-2);
            spellingToCode.put("Gbbbb", -2);
            spellingToCode.put("Abbb", -2);
            spellingToCode.put("Bbbb", -2);
            //spellingToCode.put("Cbbbb",-2);
            spellingToCode.put("Dbbbb", -2);


        }


        //flat key: quick hack
        //todo fix properly if not Cmaj
        else{

            codeToSpell1.put(0, "C");

            CodeToSpelling.put(0, codeToSpell1);

            codeToSpell2.put(0, "Db");

            CodeToSpelling.put(1, codeToSpell2);


            codeToSpell3.put(0, "D");

            CodeToSpelling.put(2, codeToSpell3);


            codeToSpell4.put(0, "Eb");

            CodeToSpelling.put(3, codeToSpell4);


            codeToSpell5.put(0, "E");

            CodeToSpelling.put(4, codeToSpell5);


            codeToSpell6.put(0, "F");

            CodeToSpelling.put(5, codeToSpell6);


            codeToSpell7.put(0, "Gb");

            CodeToSpelling.put(6, codeToSpell7);


            codeToSpell8.put(0, "G");

            CodeToSpelling.put(7, codeToSpell8);


            codeToSpell9.put(0, "Ab");

            CodeToSpelling.put(8, codeToSpell9);


            codeToSpell10.put(0, "A");

            CodeToSpelling.put(9, codeToSpell10);

            codeToSpell11.put(0, "Bb");

            CodeToSpelling.put(10, codeToSpell11);

            codeToSpell12.put(0, "B");

            CodeToSpelling.put(11, codeToSpell12);


            spellingToCode.put("C", 0);
            spellingToCode.put("D", 0);
            spellingToCode.put("E", 0);
            spellingToCode.put("F", 0);
            spellingToCode.put("G", 0);
            spellingToCode.put("A", 0);
            spellingToCode.put("B", 0);
            spellingToCode.put("Db", 0);
            //spellingToCode.put("D#",0);
            spellingToCode.put("Eb", 0);
            spellingToCode.put("Gb", 0);
            spellingToCode.put("Ab", 0);
            //spellingToCode.put("Ab",0);
            spellingToCode.put("Bb", 0);


        }


    }

    public String getSpellingFromCode(int thismidi, int thiscode){

        String spelling = "";


        int groundMidi = thismidi % 12;



        return CodeToSpelling.get(groundMidi).get(thiscode);


    }


    public Integer getCodeFromSpelling(String thispitch, String thisaccidental){
        //dont' need to know accidentals
        //C# Eb F# G# Bb

        int speller = 0;
        //int groundMidi = midi % 12;


        // normal: Ab Bb C# D# Eb F# G#

        speller = spellingToCode.get(thispitch+thisaccidental);

        return speller;



    }
}
