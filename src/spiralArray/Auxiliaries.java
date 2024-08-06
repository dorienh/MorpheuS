/* ---------------------------------------------------------------------------
   Elaine Chew: 25 October 2004 (28 February 2004)
   Auxiliaries.java
   Contains methods for reading data files.
   --------------------------------------------------------------------------- */
package spiralArray;

import java.io.*;

public class Auxiliaries {


    //------------- read data from text file
    //------------- creates and an array of strings of pitch names

    public static String[] ReadMusicDataToArray(String file) throws IOException {

	FileInputStream stream = new FileInputStream(file);
	InputStreamReader reader = new InputStreamReader(stream);
	BufferedReader buffer = new BufferedReader(reader);

	int i = 0; String line;

	String[] pseqTemp = new String[2500]; //------ assume 2500 is max no. of time segments

	while ( (line=buffer.readLine()) != null ) {
	    if (line.indexOf("-") == -1) { pseqTemp[i++] = line.trim(); }
	    if (i>2500) { System.out.println("Max array size exceeded!"); break; }
	}

	// return truncateArray(pseq,i);
	String[] pseq = new String[i];
	for (int j=0; j<i; j++) { pseq[j] = pseqTemp[j]; }
	return pseq;

    }


    //------------- takes a string of pitch names
    //------------- and maps them it to its CE position

    public static double[] MapToCE(String pstring) {
	if ((pstring.substring(0,1)).equals("Z")) { return null; }
	else {
	    double[] CE = {0.0,0.0,0.0};
	    int i = 0; // note counter
	    String name;
	    while (pstring != null) {
		int space = pstring.indexOf(" ");
		if (space != -1) { 
		    name = (pstring.substring(0,space)).trim();
		    pstring = (pstring.substring(space)).trim(); 
		}
		else { 
		    name = pstring.trim(); 
		    pstring = null;
		}
		Pitch p = new Pitch(name);
		double[] position = p.getPosition();
		CE[0] = CE[0] + position[0];
		CE[1] = CE[1] + position[1];
		CE[2] = CE[2] + position[2];
		i++;
	    }
	    CE[0] = CE[0]/i;
	    CE[1] = CE[1]/i;
	    CE[2] = CE[2]/i;
	    return CE;
	}
    }


    //------------- takes an array of strings of pitch names
    //------------- and maps them to their respective CE positions

    public static double[][] MapToCESequence(String[] pseq) {
	int length = pseq.length;
	double[][] qseq = new double[length][3];
	for (int i=0; i<length; i++) { 
	    double[] qseqTemp = MapToCE(pseq[i]); 
	    if ( qseqTemp != null ) {
		qseq[i][0] = qseqTemp[0];
		qseq[i][1] = qseqTemp[1];
		qseq[i][2] = qseqTemp[2];
	    }
	}
	return qseq;
    }


    //------------- takes an array of strings of pitch names
    //------------- and calculates the CE for all pitches in
    //------------- a window [i,j]

    public static double[] SumCEs(String[] sPitchSequence, int i, int j) {
	int count = 0;
	String name;
	double[] CE = {0.0, 0.0, 0.0};
	for (int k=i; k<j+1; k++) {
	    String pstring = sPitchSequence[k];
	    if ((pstring.substring(0,1)).equals("Z")) {}
	    else {
		while (pstring != null) {
		    int space = pstring.indexOf(" ");
		    if (space != -1) { 
			name = (pstring.substring(0,space)).trim();
			pstring = (pstring.substring(space)).trim(); 
		    }
		    else { 
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
	CE[0] = CE[0]/count;
	CE[1] = CE[1]/count;
	CE[2] = CE[2]/count;
	if (count!=0) { return CE; }
	else { return null; }
    }



}


