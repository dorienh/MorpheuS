/* -----------------------------------------------------------------
   EChew: 19 Feb 2002 (11 Apr 2002)
   This class defines minor keys, extending the superclass key.
   An triads i,V,iv,v and IV are defined, and also the position of 
   the key as represented in the Spiral Array.
--------------------------------------------------------------------*/

package spiralArray;


import java.io.*;

public class MinorKey extends Key implements MinorKeyInterface {

    // ---- inherits String name, double[] position, int index ;
    private MinorTriad i;
    private MajorTriad V;
    private MinorTriad iv;
    private MinorTriad v;
    private MajorTriad IV;

    // ----------- Defining constructors

    public MinorKey() {

	// *** Make default minor key "A" with default parameters

	super("A");
	int index = super.getIndex();
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }

    public MinorKey(Parameters par) {

	// *** Make default major key "A" with given parameters

	super("A");
	int index = super.getIndex();
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }

    public MinorKey(String p) {

	// *** Make key based on pitchName p

	super(p);
	int index = super.getIndex();
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }

    public MinorKey(String p, Parameters par) {

	// *** Make key based on pitchName p

	super(p);
	int index = super.getIndex();
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }

    public MinorKey(int index) {

	// *** Make key based on index

	super(index);
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	Parameters par = new Parameters();
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }

    public MinorKey(int index, Parameters par) {

	// *** Make key based on index

	super(index);
	i     = new MinorTriad(index);
	V     = new MajorTriad(index+1);
	iv    = new MinorTriad(index-1);
	v     = new MinorTriad(index+1);
	IV    = new MajorTriad(index-1);

	double[] iPos  = i.getPosition();
	double[] VPos  = V.getPosition();
	double[] ivPos = iv.getPosition();
	double[] vPos  = v.getPosition();
	double[] IVPos = IV.getPosition();
	
	double[] w = par.getMinorKeyWeight();
	double[] pos = {0.0,0.0,0.0};

	pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	super.setPosition(pos);

    }


    // ----------- Defining set methods

    public void setMinorKey (String p, Parameters par) {

	if (super.getName()!=p) { 
	    if (Constants.isPitchName(p)) { 

		super.setKey(p); 
		int index = super.getIndex();
		i.setTriad(index,par);
		V.setTriad(index+1,par);
		iv.setTriad(index-1,par);
		v.setTriad(index+1,par);
		IV.setTriad(index-1,par);

		double[] iPos  = i.getPosition();
		double[] VPos  = V.getPosition();
		double[] ivPos = iv.getPosition();
		double[] vPos  = v.getPosition();
		double[] IVPos = IV.getPosition();
	
		double[] w = par.getMinorKeyWeight();
		double[] pos = {0.0,0.0,0.0};

		pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
		pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
		pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
		super.setPosition(pos);
	    }
	    else { System.out.println("ERROR: Invalid name ..."); }
	}
    }

    public void setMinorKey (int index, Parameters par) { 
	if ((index<20) && (index>=-15)) {

	    super.setKey(index);
	    i.setTriad(index,par);
	    V.setTriad(index+1,par);
	    iv.setTriad(index-1,par);
	    v.setTriad(index+1,par);
	    IV.setTriad(index-1,par);

	    double[] iPos  = i.getPosition();
	    double[] VPos  = V.getPosition();
	    double[] ivPos = iv.getPosition();
	    double[] vPos  = v.getPosition();
	    double[] IVPos = IV.getPosition();
	
	    double[] w = par.getMinorKeyWeight();
	    double[] pos = {0.0,0.0,0.0};

	    pos[0] = w[0]*iPos[0]+(w[3]*w[1])*VPos[0]+((1-w[3])*w[1])*vPos[0]+(w[4]*w[2])*ivPos[0]+((1-w[4])*w[2])*IVPos[0];
	    pos[1] = w[0]*iPos[1]+(w[3]*w[1])*VPos[1]+((1-w[3])*w[1])*vPos[1]+(w[4]*w[2])*ivPos[1]+((1-w[4])*w[2])*IVPos[1];
	    pos[2] = w[0]*iPos[2]+(w[3]*w[1])*VPos[2]+((1-w[3])*w[1])*vPos[2]+(w[4]*w[2])*ivPos[2]+((1-w[4])*w[2])*IVPos[2];
	    super.setPosition(pos);
	}
	else { System.out.println("ERROR: Index out of range ..."); }
    }

    // ----------- Defining get methods

    public String getName       () { return super.getName(); }
    public int getIndex         () { return super.getIndex(); }
    public MinorTriad geti      () { return i; }
    public MajorTriad getV      () { return V; }
    public MinorTriad getiv     () { return iv; }
    public MinorTriad getv      () { return v; }
    public MajorTriad getIV     () { return IV; }

    public double[] getPosition () { return super.getPosition(); }

    // ----------- Defining other methods

    public void displayKey () { 

	super.displayKey();
	System.out.println("The i chord is...") ; i.displayTriad();
	System.out.println("The V chord is...") ; V.displayTriad();
	System.out.println("The iv chord is..."); iv.displayTriad();
	System.out.println("The v chord is...") ; v.displayTriad();
	System.out.println("The IV chord is..."); IV.displayTriad();

    }

    // -- This method creates an array of minor keys for all keynames in Constants.PitchNames
    // -- Method added November 30, 2004 (on flight between LAX and BOS)

    public static MinorKey[] createMinorKeys () { 

	int N = Constants.PitchNames.length;// - Constants.KeyOffset();

	MinorKey[] minorKeys = new MinorKey[N];


		//DH changed n=0 from n=1 to prevent out of bound error
	for (int n = 0; n != N; n++) {
	    minorKeys[n] = new MinorKey(Constants.PitchNames[n]);
	}

	return minorKeys;

    }


}



