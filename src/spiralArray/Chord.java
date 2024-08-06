/* -----------------------------------------------------------------
   EChew: 4 Feb 2002 (7 Mar 2002)

   This class defines chords, and since triads and seventh chords
   have the root and fifth in common, the pitches defined in this
   superclass are the Root and the Fifth.  The index is the index
   of the Root, and the name is the name of the root.

   --------------------------------------------------------------------*/
package spiralArray;

import java.io.*;

public class Chord extends SpiralArrayObject implements ChordInterface {

    // Inherits: String name; double[] position; 
    private int index;
    private Pitch root;

    // ----------- Defining constructors

    // *** Make default root pitch "C" with default parameters
    public Chord() { 
	super("C"); index = 0;
	root = new Pitch("C");
	System.out.println("Creating default chord C with default parameters");
    }

    // *** Make default root pitch "C"
    public Chord(Parameters par) { 
	super("C"); index = 0;
	root = new Pitch("C",par);
	System.out.println("Creating default chord C");
    }

    // *** Make chord based on pitchName p with default parameters
    public Chord(String p) { 
	super(p); 
	root = new Pitch(p);
	index = root.getIndex();
	//System.out.println("Creating chord "+p+" with default parameters");
    }

    // *** Make chord based on pitchName p
    public Chord(String p, Parameters par) { 
	super(p); 
	root = new Pitch(p,par);
	index = root.getIndex();
	//System.out.println("Creating chord "+p);
    }

    // *** Make chord based on index i with default parameters
    public Chord(int i) { 
	super(); index = i;
	root = new Pitch(i);
	super.setName(root.getName()); 
	//System.out.println("Creating chord based on index "+i+" with default parameters");
    }

    // *** Make chord based on index i
    public Chord(int i, Parameters par) { 
	super(); index = i;
	root = new Pitch(i,par);
	super.setName(root.getName());
	//System.out.println("Creating chord based on index "+i);
    }

    // ----------- Defining set methods

    //************************************************************************
    //add "Constants" before isPitchName
    public void setName (String p, Parameters par) { 
	if (Constants.isPitchName(p)) {
	        super.setName(p);
		    root.setName(p,par); 
		        index = root.getIndex();
			    System.out.println("Changing chord to "+p);
			    }
    }

    public void setIndex (int i, Parameters par) { 
	index = i;
	root.setIndex(i,par); 
	super.setName(root.getName());
	System.out.println("Changing chord root to pitch at index "+i);
    }

    public void setPosition (double[] pos) { super.setPosition(pos); }

    // ----------- Defining get methods

    public String getName () { return super.getName(); }
    public int getIndex () { return index; }
    public Pitch getRoot () { return root; }
    public double[] getRootPosition () { return root.getPosition(); }
    public double[] getChordPosition () { return super.getPosition(); }

    // ----------- Defining other methods

    public void displayChord () { 
	System.out.println("Chord description..."); super.displaySpiralArrayObject();
	System.out.println("Index:    " + index);
	System.out.println("The root is..."); root.displayPitch();
    }

}



