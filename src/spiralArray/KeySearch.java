/* ---------------------------------------------------------------------------
   Elaine Chew: 28 February 2004, modified 7 March 2007
   SegmentSearch.java
   Contains methods called by SegmentSearchTestor.java
   --------------------------------------------------------------------------- */
package spiralArray;

import java.io.*;

public class KeySearch {

      MajorKey[] majorKeys = MajorKey.createMajorKeys();
      MinorKey[] minorKeys = MinorKey.createMinorKeys();

//    private int iForwardWindow;
//    private int iBackWindow;
//    private String[] sPitchSequence;
//    private double[][] dPosSequence;

    //----------- default constructor
    public KeySearch() {
//        iForwardWindow = 4;
//        iBackWindow = 4;
    }

    //--------------- 4-parameter constructor
    public KeySearch(int f, int b, String[] pseq, double[][] qseq) {
//        iForwardWindow = f;
//        iBackWindow = b;
//        sPitchSequence = pseq;
//        dPosSequence = qseq;
    }

//    //------------- set methods
//    public void setForwardWindow(int f) {
//        iForwardWindow = f;
//    }
//
//    public void setBackWindow(int b) {
//        iBackWindow = b;
//    }
//
//    public void setPitchSequence(String[] pseq) {
//        sPitchSequence = pseq;
//    }
//
//    public void setPitchSequence(double[][] qseq) {
//        dPosSequence = qseq;
//    }
//
//    //------------- get methods
//    public int getForwardWindow() {
//        return iForwardWindow;
//    }
//
//    public int getBackWindow() {
//        return iBackWindow;
//    }
//
//    public String[] getPitchSequence() {
//        return sPitchSequence;
//    }
//
//    public double[][] getPosSequence() {
//        return dPosSequence;
//    }

//
//    //------------- calculates distance between CE of forwardwindow and backwindow
//    public double[] CEDistances() {
//        int B = sPitchSequence.length;
//        double[] distance = new double[B + 1];
//        double[] diff = new double[3];
//        for (int b = iBackWindow; b < B - iForwardWindow + 1; b++) {
//            double[] ForwardWindowCE = this.SumCEs(b, b + iForwardWindow - 1);
//            double[] BackWindowCE = this.SumCEs(b - iBackWindow, b - 1);
//            if ((ForwardWindowCE == null) || (BackWindowCE == null)) {
//                // added March 7, 2007 to handle block of rests
//                distance[b] = Double.POSITIVE_INFINITY;
//            } else {
//                diff[0] = ForwardWindowCE[0] - BackWindowCE[0];
//                diff[1] = ForwardWindowCE[1] - BackWindowCE[1];
//                diff[2] = ForwardWindowCE[2] - BackWindowCE[2];
//                distance[b] = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1] + diff[2] * diff[2]);
//            }
//            System.out.println(b + ", " + distance[b]);
//        }
//        return distance;
//    }
//    //------------- calculates distance between CE of forwardwindow and backwindow
//    public double[] CEDistances() {
//        int B = sPitchSequence.length;
//        double[] distance = new double[B + 1];
//        double[] diff = new double[3];
//        for (int b = iBackWindow; b < B - iForwardWindow + 1; b++) {
//            double[] ForwardWindowCE = this.SumCEs(b, b + iForwardWindow - 1);
//            double[] BackWindowCE = this.SumCEs(b - iBackWindow, b - 1);
//            if ((ForwardWindowCE == null) || (BackWindowCE == null)) {
//                // added March 7, 2007 to handle block of rests
//                distance[b] = Double.POSITIVE_INFINITY;
//            } else {
//                diff[0] = ForwardWindowCE[0] - BackWindowCE[0];
//                diff[1] = ForwardWindowCE[1] - BackWindowCE[1];
//                diff[2] = ForwardWindowCE[2] - BackWindowCE[2];
//                distance[b] = Math.sqrt(diff[0] * diff[0] + diff[1] * diff[1] + diff[2] * diff[2]);
//            }
//            System.out.println(b + ", " + distance[b]);
//        }
//        return distance;
//    }


    /**
     * sum CEs of all pitches in a given window [i,j]
     *
     * @param i
     * @param j
     * @return
     */
//    public double[] SumCEs(int i, int j) {
//        int count = 0;
//        String name;
//        double[] CE = {0.0, 0.0, 0.0};
//        for (int k = i; k < j + 1; k++) {
//            String pstring = sPitchSequence[k];
//            if ((pstring.substring(0, 1)).equals("Z")) {
//            } else {
//                while (pstring != null) {
//                    int space = pstring.indexOf(" ");
//                    if (space != -1) {
//                        name = (pstring.substring(0, space)).trim();
//                        pstring = (pstring.substring(space)).trim();
//                    } else {
//                        name = pstring.trim();
//                        pstring = null;
//                    }
//                    Pitch p = new Pitch(name);
//                    double[] position = p.getPosition();
//                    CE[0] = CE[0] + position[0];
//                    CE[1] = CE[1] + position[1];
//                    CE[2] = CE[2] + position[2];
//                    count++;
//                }
//            }
//        }
//        CE[0] = CE[0] / count;
//        CE[1] = CE[1] / count;
//        CE[2] = CE[2] / count;
//        if (count != 0) {
//            return CE;
//        } else {
//            return null;
//        }
//    }


    /**
     * Get the CEs from a song sequence (slices including multiple notes at the some time)
     * @param song       string[]  -- containing A B A# etc per row
     * @param i
     * @param j
     * @return
     */
    public static double[] SumCEsFromSeq(String[] song, int i, int j) {
        int count = 0;
        String name;
        double[] CE = {0.0, 0.0, 0.0};
        for (int k = i; k < j + 1; k++) {
            String pstring = song[k];
            if ((pstring.substring(0, 1)).equals("Z")) {
            } else {
                while (pstring != null) {
                    int space = pstring.indexOf(" ");
                    if (space != -1) {
                        name = (pstring.substring(0, space)).trim();
                        pstring = (pstring.substring(space)).trim();
                    } else {
                        name = pstring.trim();
                        pstring = null;
                    }
                    Pitch p = new Pitch(name);
                    double[] position = p.getPosition();
                    CE[0] = CE[0] + position[0];
                    CE[1] = CE[1] + position[1];
                    CE[2] = CE[2] + position[2];
                    count++;
                }
            }
        }
        CE[0] = CE[0] / count;
        CE[1] = CE[1] / count;
        CE[2] = CE[2] / count;
        if (count != 0) {
            return CE;
        } else {
            return null;
        }
    }


    /**
     *
     * Get the key from a song sequence (slices including multiple notes at the some time)
     * @param song       string[]  -- containing A B A# etc per row
     * @param i     start slice
     * @param j     end slice
     */
    public String getKeyFromWindow(String[] song, int i, int j){
        //get sum al the CEs
        double[] CE = SumCEsFromSeq(song, i, j);

        //which key is closest?
        //output array with distance to each key
//       KeyFinder.CEDistance2Keys
//        KeyFinder testKeyFinder = new KeyFinder(w,s,pseq,qseq);

        //KeyFinder keyfinder = new KeyFinder();
//        MajorKey[] majorKeys = MajorKey.createMajorKeys();
//        MinorKey[] minorKeys = MinorKey.createMinorKeys();
        //DH made these properties of the class

        double[][] distances = KeyFinder.CEDistance2Keys(CE, majorKeys, minorKeys);

        System.out.println("Distance to all of the keys: ");
        //print distances
        for (int k = 0; k < 2; k++){
            if (k == 0) {
                System.out.print("Major keys: ");
            }
            else {
                System.out.print("Minor keys: ");
            }
            for (int z = 0; z < distances[k].length; z++){
                System.out.print(Constants.PitchNames[z] + ": " + distances[k][z]+ " ");

            }
            System.out.println();
        }

        System.out.println("\nThe closest key is: " + KeyFinder.CEDistanceMinimizingKey(distances));

        return KeyFinder.CEDistanceMinimizingKey(distances);

    }


    /**
     * Find the smallest distance to a key
     * @param song
     * @param i
     * @param j
     */

    public double getClosestDistanceFromWindow(String[] song, int i, int j){
        //get sum al the CEs
        double[] CE = SumCEsFromSeq(song, i, j);

//        System.out.println(" CEsize: " + CE.length);

        //which key is closest?
        //output array with distance to each key
//       KeyFinder.CEDistance2Keys
//        KeyFinder testKeyFinder = new KeyFinder(w,s,pseq,qseq);

        //KeyFinder keyfinder = new KeyFinder();
//        MajorKey[] majorKeys = MajorKey.createMajorKeys();
//        MinorKey[] minorKeys = MinorKey.createMinorKeys();
        //DH made these properties of the class

        double[][] distances = KeyFinder.CEDistance2Keys(CE, majorKeys, minorKeys);

        //print distances
//        for (int k = 0; k < 2; k++){
//            for (int z = 0; z < distances[k].length; z++){
//                System.out.print(distances[k][z]);
//
//            }
//            System.out.println();
//        }

        return KeyFinder.CEDistanceMinimizingKeyDistance(distances);


    }




    public double[] getClosestKeyPosition(String[] song, int i, int j){
        //get sum al the CEs
        double[] CE = SumCEsFromSeq(song, i, j);

//        System.out.println(" CEsize: " + CE.length);

        //which key is closest?
        //output array with distance to each key
//       KeyFinder.CEDistance2Keys
//        KeyFinder testKeyFinder = new KeyFinder(w,s,pseq,qseq);

        //KeyFinder keyfinder = new KeyFinder();
//        MajorKey[] majorKeys = MajorKey.createMajorKeys();
//        MinorKey[] minorKeys = MinorKey.createMinorKeys();
        //DH made these properties of the class

        double[][] distances = KeyFinder.CEDistance2Keys(CE, majorKeys, minorKeys);

        //print distances
//
//        double minDist = 1000;
//        for (int k = 0; k < 2; k++){
//            for (int z = 0; z < distances[k].length; z++){
//                System.out.print(distances[k][z]);
////
//                if distances[k][z]
//            }
//            System.out.println();
//        }



        return KeyFinder.CEDistanceMinimizingKeyPosition(distances);


    }



    /**
     *
     * Reads the input data from a file
     *
     * @param file
     * @return  string[] that contains a string value for each time slot
     * @throws IOException
     */
    public static String[] ReadMusicDataToArray(String file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        int i = 0;
        String line;
        String[] pseqTemp = new String[2500]; //------ assume 2500 is max no. of time segments
        while ((line = buffer.readLine()) != null) {
            if (line.indexOf("-") == -1) {
                pseqTemp[i++] = line.trim();
            }
            if (i > 2500) {
                System.out.println("Max array size exceeded!");
                break;
            }
        }
        // return truncateArray(pseq,i);
        String[] pseq = new String[i];
        for (int j = 0; j < i; j++) {
            pseq[j] = pseqTemp[j];
        }
        return pseq;
    }



    /**
     *
     * map string of letter names for one unit of time to its CE position
     *
     * @param pstring String which contains a space separated list of pitches
     * @return double[]
     */

    public static double[] MapToCE(String pstring) {
        if ((pstring.substring(0, 1)).equals("Z")) {
            return null;
        } else {
            double[] CE = {0.0, 0.0, 0.0};
            int i = 0; // note counter
            String name;
            //for each note in the pstring:
            while (pstring != null) {
                int space = pstring.indexOf(" ");
                if (space != -1) {
                    name = (pstring.substring(0, space)).trim();
                    pstring = (pstring.substring(space)).trim();
                } else {
                    name = pstring.trim();
                    pstring = null;
                }
                //DH
                //System.out.print("DH:"+pstring);
                //create a new pitch
                Pitch p = new Pitch(name);
                //get the position of the new pitch
                double[] position = p.getPosition();
                CE[0] = CE[0] + position[0];
                CE[1] = CE[1] + position[1];
                CE[2] = CE[2] + position[2];
                i++;
            }

            //DH: is this correct? Divide by the number of notes that sound at the same time?
            CE[0] = CE[0] / i;
            CE[1] = CE[1] / i;
            CE[2] = CE[2] / i;
            return CE;
        }
    }



    /**
     *
     * Returns the CE of a string of pitches
     * (map array of strings of letter names to CE positions)
     *
     * @param pseq string of pitches at the same times
     * @return CE
     */

    public static double[][] MapToCESequence(String[] pseq) {
        int length = pseq.length;
        double[][] qseq = new double[length][3];
        //for each group of notes playes together
        for (int i = 0; i < length; i++) {
            double[] qseqTemp = MapToCE(pseq[i]);
            if (qseqTemp != null) {
                //sets the position for each of the 3 coordinates
                qseq[i][0] = qseqTemp[0];
                qseq[i][1] = qseqTemp[1];
                qseq[i][2] = qseqTemp[2];
            }
        }
        return qseq;
    }




    public void getKey(Double[][] qseq){


    }






}


