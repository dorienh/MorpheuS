/* ---------------------------------------------------------------------------
   Elaine Chew: 30 November 2004
   KeyFinder.java
   Contains methods called by KeyFinderTestor.java
   --------------------------------------------------------------------------- */
package spiralArray;

import java.io.*;

public class KeyFinder {

    private int iWindow;
    private int iStep;
    private String[] sPitchSequence;
    private double[][] dPosSequence;

    //----------- default (2-parameter) constructor
    public KeyFinder(String[] pseq, double[][] qseq) { 
	iStep = 1;
    }

    //--------------- 3-parameter constructor
    public KeyFinder(int w, String[] pseq, double[][] qseq) { 
	iWindow = w;
	iStep = 1;
	sPitchSequence = pseq;
	dPosSequence = qseq;
    }

    //--------------- 4-parameter constructor
    public KeyFinder(int w, int s, String[] pseq, double[][] qseq) { 
	iWindow = w;
	iStep = s;
	sPitchSequence = pseq;
	dPosSequence = qseq;
    }

    //------------- set methods
    public void setWindow (int w) { iWindow = w; }
    public void setStep (int s) { iStep = s; }
    public void setPitchSequence (String[] pseq) { sPitchSequence = pseq; }
    public void setPitchSequence (double[][] qseq) { dPosSequence = qseq; }

    //------------- get methods
    public int getWindow () { return iWindow; }
    public int getStep () { return iStep;}
    public String[] getPitchSequence () { return sPitchSequence; }
    public double[][] getPosSequence () { return dPosSequence; }


    //------------- finds time series of distance-minimizing keys (cumulative window)
//    public String CEMinDistKeySeq() {
//
//		int N = Constants.PitchNames.length;
//		MajorKey[] majorKeys = MajorKey.createMajorKeys();
//		MinorKey[] minorKeys = MinorKey.createMinorKeys();
//			//DH changed next, added s's and added first argument:
//		double[][] distances = CEDistance2Keys(CE, majorKeys, minorKeys);
//
//    }


    //------------- finds distance-minimizing key
    public static String CEDistanceMinimizingKey(double[][] distances) {

	//DH:
	int N = Constants.PitchNames.length;
	int[] iMin = {0,0};
		//DH should it not start from n=0 instead of 1? Changed this
	for (int n=0; n<N; n++) {
	    if (distances[0][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 0; iMin[1] = n; }
	    if (distances[1][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 1; iMin[1] = n; }
	}

	String sMinDistKey = Constants.PitchNames[iMin[1]];
	if (iMin[0] == 0) { sMinDistKey = sMinDistKey + " Major"; }
	if (iMin[0] == 1) { sMinDistKey = sMinDistKey + " Minor"; }

	return sMinDistKey;

    }




	//------------- finds distance-minimizing key and return its position //added by DH
	public static double[] CEDistanceMinimizingKeyPosition(double[][] distances) {

		//DH:
		int N = Constants.PitchNames.length;
		int[] iMin = {0,0};
		//DH should it not start from n=0 instead of 1? Changed this
		for (int n=0; n<N; n++) {
			if (distances[0][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 0; iMin[1] = n; }
			if (distances[1][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 1; iMin[1] = n; }
		}

		String sMinDistKey = Constants.PitchNames[iMin[1]];

		double keyPosition[] = new double[3];
		if (iMin[0] == 0) {

			//sMinDistKey = sMinDistKey + "Major";
			MajorKey Mkey = new MajorKey(sMinDistKey);
			keyPosition = Mkey.getPosition();


		}
		if (iMin[0] == 1) {

			//sMinDistKey = sMinDistKey + "Minor";
			MinorKey key = new MinorKey(sMinDistKey);
			keyPosition = key.getPosition();

		}

		//override key

		//System.out.println("Closest key is: " + sMinDistKey);

		//override for specific example
		//MajorKey Mkey = new MajorKey("C");
		//MinorKey Mkey = new MinorKey("A");
		//keyPosition = Mkey.getPosition();
//dorien

		//System.out.println("Key: "+ keyPosition[0]+ " "+ keyPosition[1]+ " "+ keyPosition[2]+ " ");
		return keyPosition;

	}




	//------------- finds distance-minimizing key distance
	public static double CEDistanceMinimizingKeyDistance(double[][] distances) {

		//DH:
		int N = Constants.PitchNames.length;
		int[] iMin = {0,0};
		//DH changed n=1 to  n=0
		for (int n=0; n<N; n++) {
			if (distances[0][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 0; iMin[1] = n; }
			if (distances[1][n] < distances[iMin[0]][iMin[1]]) { iMin[0] = 1; iMin[1] = n; }
		}

//		String sMinDistKey = Constants.PitchNames[iMin[1]];
//
		return distances[iMin[0]][iMin[1]];

	}


    //------------- calculates distances between a CE and all keys
    public static double[][] CEDistance2Keys(double[] CE, MajorKey[] majorKeys, MinorKey[] minorKeys) {

	int N = Constants.PitchNames.length;
	double[][] distances = new double[2][N];



		double[] d = new double[3];

	for (int n=0; n<N; n++) {


		//DH changed n from b

//		System.out.print(majorKeys.length);
	    double[] majorKeyPosition = majorKeys[n].getPosition();
	    d[0] = CE[0] - majorKeyPosition[0];
	    d[1] = CE[1] - majorKeyPosition[1];
	    d[2] = CE[2] - majorKeyPosition[2];
	    distances[0][n] = Math.sqrt(d[0]*d[0] + d[1]*d[1] + d[2]*d[2]);


//	    System.out.println(majorKeys[n].getName()  + "Major: ");
//	    System.out.println(majorKeyPosition[0]+ ", "+ majorKeyPosition[1]+ ", "+ majorKeyPosition[2]);

	    double[] minorKeyPosition = minorKeys[n].getPosition();
	    d[0] = CE[0] - minorKeyPosition[0];
	    d[1] = CE[1] - minorKeyPosition[1];
	    d[2] = CE[2] - minorKeyPosition[2];
	    distances[1][n] = Math.sqrt(d[0]*d[0] + d[1]*d[1] + d[2]*d[2]);

//		System.out.println(minorKeys[n].getName()  + "Minor: ");
//		System.out.println(minorKeyPosition[0]+ ", "+ minorKeyPosition[1]+ ", "+ minorKeyPosition[2]);

	}

	return distances;
    }


    //------------- generates sequence of cumulative CEs
    public double[][] cumCESequence() {

	int B = sPitchSequence.length;
	double[][] CEseq = new double[B][3];

	for (int b=0; b<B; b++) { CEseq[b] = Auxiliaries.SumCEs(this.sPitchSequence,0,b-1); }

	return CEseq;

    }

}


