/* -----------------------------------------------------------------
   EChew: 19 Feb 2002 (11 Apr 2002)
   This class defines keys.  It is a superclass of MajorKey and
   MinorKey.  The name and index are defined in this class.

--------------------------------------------------------------------*/
package spiralArray;

import java.io.*;

public class Key extends SpiralArrayObject implements KeyInterface {

    // Inherits: String name; double[] position; 
    private int index;

    // ----------- Defining constructors

    public Key() { 
	// *** Make default key "C" with default parameters, position not set
	super("C"); index = 0; 
	System.out.println("Creating default key C with default parameters");
    }

    public Key(String p) {

	// *** Create the key (p), position not set
	super();
	//System.out.println("Creating key of name "+p);

	// *** Find the index of the pitch in Constants.PitchNames[]
	int i = -1; index = -1;
	while ((i++<35) && (index==-1)) { 
	    if ( p.equals(Constants.PitchNames[i]) ) { index = i; }
	}

	// *** Set the pitch name, index and position
	if (index != -1) { 
           super.setName(p); 
	   // *** Set the pitch index
 	   index = index - 15;
	}
    }

    public Key(int i) {

	// *** Create key (index i) 
        super();
	//System.out.println("Creating key of index "+i);

	// *** Set the pitch index and name
	index = i;
	super.setName(Constants.PitchNames[i+15]);
    }


    // ----------- Defining set methods

    public void setKey (String p) {
	if (super.getName()!=p) { 
	    if (Constants.isPitchName(p)) { 
		super.setName(p); 
		System.out.println("Changing key to "+p);
		int i = -1; index = -1;
		while ((++i<35) && (index==-1)) { 
		    if ( p.equals(Constants.PitchNames[i]) ) { 
			index = i-15; 
		    }
		}
	    }
	    else { System.out.println("ERROR: Invalid name ..."); }
	}
    }

    public void setKey (int i) { 
	if ((i<20) && (i>=-15)) {
	    index = i; 
	    super.setName(Constants.PitchNames[i+15]);
	    System.out.println("Changing key to " + super.getName());
	}
	else { System.out.println("ERROR: Index out of range ..."); }
    }

    public void setPosition (double[] pos) { super.setPosition(pos); }

    // ----------- Defining get methods

    public String getName () { return super.getName(); }
    public double[] getPosition () { return super.getPosition(); }
    public int getIndex () { return index; }

    // ----------- Defining other methods

    public void displayKey () { 
	System.out.println("key description...");
	displaySpiralArrayObject();
    }

}



