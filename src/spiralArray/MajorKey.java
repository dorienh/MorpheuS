package spiralArray;
/* -----------------------------------------------------------------
   EChew: 19 Feb 2002 (11 Apr 2002)
   This class defines major keys, extending the superclass key.
   An triads I,V and IV are defined, and also the position of key
   as represented in the Spiral Array.
--------------------------------------------------------------------*/

import java.io.*;

public class MajorKey extends Key implements MajorKeyInterface {

    // ---- inherits String name, double[] position, int index ;
    private MajorTriad I;
    private MajorTriad V;
    private MajorTriad IV;

    // ----------- Defining constructors

    public MajorKey() {

	// *** Make default major key "C" with default parameters

	super();
	int i = super.getIndex();
	I     = new MajorTriad(i);
	V     = new MajorTriad(i+1);
	IV    = new MajorTriad(i-1);

	double[] IPos  = I.getPosition();
	double[] VPos  = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }

    public MajorKey(Parameters par) {

	// *** Make default major key "C" with given parameters

	super();
	int i = super.getIndex();
	I     = new MajorTriad(i);
	V     = new MajorTriad(i+1);
	IV    = new MajorTriad(i-1);

	double[] IPos = I.getPosition();
	double[] VPos = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }

    public MajorKey(String p) {

	// *** Make key based on pitchName p

	super(p);
	int i = super.getIndex();
	I     = new MajorTriad(i);
	V     = new MajorTriad(i+1);
	IV    = new MajorTriad(i-1);

	double[] IPos  = I.getPosition();
	double[] VPos  = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }

    public MajorKey(String p, Parameters par) {

	// *** Make key based on pitchName p

	super(p);
	int i = super.getIndex();
	I     = new MajorTriad(i);
	V     = new MajorTriad(i+1);
	IV    = new MajorTriad(i-1);

	double[] IPos  = I.getPosition();
	double[] VPos  = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }

    public MajorKey(int index) {

	// *** Make key based on index i

	super(index);
	I  = new MajorTriad(index);
	V  = new MajorTriad(index+1);
	IV = new MajorTriad(index-1);

	double[] IPos  = I.getPosition();
	double[] VPos  = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }

    public MajorKey(int index, Parameters par) {

	// *** Make key based on index i

	super(index);
	I  = new MajorTriad(index);
	V  = new MajorTriad(index+1);
	IV = new MajorTriad(index-1);

	double[] IPos  = I.getPosition();
	double[] VPos  = V.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMajorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	super.setPosition(pos);

    }


    // ----------- Defining set methods

    public void setMajorKey (String p, Parameters par) {

	if (super.getName()!=p) { 
	    if (Constants.isPitchName(p)) { 

		super.setKey(p); 
		int index = super.getIndex();
		I.setTriad(index,par);
		V.setTriad(index+1,par);
		IV.setTriad(index-1,par);

		double[] IPos  = I.getPosition();
		double[] VPos  = V.getPosition();
		double[] IVPos = IV.getPosition();
	
		double[] w = par.getMajorKeyWeight();
		double[] pos = {0.0,0.0,0.0};

		pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
		pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
		pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
		super.setPosition(pos);

	    }
	    else { System.out.println("ERROR: Invalid name ..."); }
	}
    }

    public void setMajorKey (int index, Parameters par) { 
	if ((index<20) && (index>=-15)) {

	    super.setKey(index);
	    I.setTriad(index,par);
	    V.setTriad(index+1,par);
	    IV.setTriad(index-1,par);

	    double[] IPos  = I.getPosition();
	    double[] VPos  = V.getPosition();
	    double[] IVPos = IV.getPosition();
	
	    double[] w = par.getMajorKeyWeight();
	    double[] pos = {0.0,0.0,0.0};

	    pos[0] = w[0]*IPos[0]+w[1]*VPos[0]+w[2]*IVPos[0];
	    pos[1] = w[0]*IPos[1]+w[1]*VPos[1]+w[2]*IVPos[1];
	    pos[2] = w[0]*IPos[2]+w[1]*VPos[2]+w[2]*IVPos[2];
	    super.setPosition(pos);

	}
	else { System.out.println("ERROR: Index out of range ..."); }
    }

    // ----------- Defining get methods

    public String getName () { return super.getName(); }
    public int getIndex () { return super.getIndex(); }
    public MajorTriad getI () { return I; }
    public MajorTriad getV () { return V; }
    public MajorTriad getIV () { return IV; }

    public Pitch getTonic () { return I.getRoot(); }
    public Pitch getSupertonic () { return V.getFifth(); }
    public Pitch getMediant () { return I.getThird(); }
    public Pitch getDominant () { return V.getRoot(); }
    public Pitch getSubdominant () { return IV.getRoot(); }
    public Pitch getSubmediant () { return IV.getThird(); }
    public Pitch getLeadingNote () { return V.getThird(); }

    public double[] getPosition () { return super.getPosition(); }

    // ----------- Defining other methods

    public void displayKey () { 

	super.displayKey();
	
	System.out.println("The I chord is..."); I.displayTriad();
	System.out.println("The V chord is..."); V.displayTriad();
	System.out.println("The IV chord is..."); IV.displayTriad();

    }


    // -- This method creates an array of major keys for all keynames in Constants.PitchNames
    // -- Method added November 30, 2004 (on flight between LAX and BOS)

    public static MajorKey[] createMajorKeys () { 




	int N = Constants.PitchNames.length;// - Constants.KeyOffset();

	MajorKey[] majorKeys = new MajorKey[N];


	//DH changed n=0 from n=1 to prevent out of bound error
	for (int n = 0; n != N; n++) {
//		System.out.println("For key: " + n);
	    majorKeys[n] = new MajorKey(Constants.PitchNames[n]);
	}

	return majorKeys;

    }

}



