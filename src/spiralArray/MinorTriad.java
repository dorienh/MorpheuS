/* -----------------------------------------------------------------
   EChew: 9 Jan 2002 (7 Mar 2002)
   This class defines MinorTriad as a subclass of the Chord class.
   The new elements are the minor third (as measured from the root)
   and the position of the minor triad in the Spiral Array.
--------------------------------------------------------------------*/
package spiralArray;

public class MinorTriad extends Chord implements TriadInterface {

    // Inherits: Chord (String name; double[] position; int index; 
    // and, Pitch root (String name; double[] position int index) )
    private Pitch fifth;
    private Pitch third;

    // ----------- Defining constructor

    public MinorTriad() {

	super();
	int i = super.getIndex();
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	Parameters par = new Parameters();
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = (super.getRoot()).getPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public MinorTriad(Parameters par) {

	super(par);
	int i = super.getIndex();
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = (super.getRoot()).getPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public MinorTriad(String p) {

	super(p);
	int i = super.getIndex();
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	Parameters par = new Parameters();
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = (super.getRoot()).getPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public MinorTriad(String p, Parameters par) {

	super(p);
	int i = super.getIndex();
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = (super.getRoot()).getPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public MinorTriad(int i) {

	super(i);
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	Parameters par = new Parameters();
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = super.getRootPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public MinorTriad(int i, Parameters par) {

	super(i);
	fifth = new Pitch(i+1);
	third = new Pitch(i-3);

	double[] pos = {0.0,0.0,0.0};
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = super.getRootPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }


    // ----------- Defining set methods

    public void setTriad (String p, Parameters par) {
	if (super.getName()!=p) { 
	    if (Constants.isPitchName(p)) { 
		super.setName(p,par);
		int i = super.getIndex();
		fifth = new Pitch(i+1);
		third = new Pitch(i-3);
		updatePosition(par);
	    }
	    else { System.out.println("ERROR: Invalid name ..."); }
	}
    }

    public void setTriad (int i, Parameters par) { 
	if ((i<20) && (i>=-15)) {
	    super.setIndex(i,par);
	    fifth = new Pitch(i+1);
	    third = new Pitch(i-3);
	    updatePosition(par);
	}
	else { System.out.println("ERROR: Index out of range ..."); }
    }

    // ----------- Defining get methods

    public String getName () { return super.getName(); }
    public int getIndex () { return super.getIndex(); }
    public double[] getPosition () { return super.getPosition(); }
    public Pitch getRoot () { return super.getRoot(); }
    public Pitch getFifth () { return fifth; }
    public Pitch getThird () { return third; }

    // ----------- Defining other methods

    public void updatePosition(Parameters par) {
	double[] pos = {0.0,0.0,0.0};
	double[] w = par.getMinorTriadWeight();
	double[] rootPosition  = super.getRootPosition();
	double[] fifthPosition = fifth.getPosition();
	double[] thirdPosition = third.getPosition();
	pos[0] = w[0]*rootPosition[0]+w[1]*fifthPosition[0]+w[2]*thirdPosition[0];
	pos[1] = w[0]*rootPosition[1]+w[1]*fifthPosition[1]+w[2]*thirdPosition[1];
	pos[2] = w[0]*rootPosition[2]+w[1]*fifthPosition[2]+w[2]*thirdPosition[2];
	super.setPosition(pos);
    }

    public void displayTriad () { 
	super.displayChord();
	System.out.println("The fifth is..."); fifth.displayPitch();
	System.out.println("The third is..."); third.displayPitch();
    }

}





