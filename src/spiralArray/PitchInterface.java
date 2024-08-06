/* -----------------------------------------------------------------
   EChew: 9 Jan 2002 (25 Jan 2002)

   This is the Interface of the Pitch class.
   Set and get methods are defined.

   In addition, the following methods are required:
   isPitchName, displayPitch

--------------------------------------------------------------------*/
package spiralArray;

public interface PitchInterface {

    /* ---------------------------------------------------------
       Description of Pitch object:
       Each Pitch has 3 attributes:
       [1] the name - the format for names is restricted to 
           Nbb, Nb, N, N# and N##, where N = {A,B,C,D,E,F,G}
       [2] the index - this refers to how far the pitch is from C
           along the Spiral Array.  For example, G has index 1
	   and is positioned 1 step (1 perfect 5th) above C.
       [3] the position - each position is a 3-D array [x,y,z] 
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setName(String name, Parameters par);

    /* ---------------------------------------------------------
       When setName is called, the index and position are also 
       automatically updated.  setName calls setIndex(p, par) 
       and setPosition(i) 
    --------------------------------------------------------- */

    public abstract void setIndex(int index, Parameters par);

    /* ---------------------------------------------------------
       When setIndex(i,par) is called, the name is re-assigned 
       and setPosition(i,par) is called 
    --------------------------------------------------------- */

    public abstract void setIndex(String name, Parameters par);

    /* ---------------------------------------------------------
       When setIndex(p, par) is called, it finds the index of 
       that pitch and assigns the index value, and calls 
       setPosition(i,par). 
    --------------------------------------------------------- */

    // ----------- Get methods

    public abstract String getName();

    public abstract int getIndex();

    public abstract double[] getPosition();

    // ----------- Other methods

    /* ---------------------------------------------------------
       isPitchName checks to see if the String is one of the 
       pitch names listed in Constants.PitchNames, that is to
       say, if it is in the range Fbb through B##. 
    --------------------------------------------------------- */

    public abstract void displayPitch();

    /* ---------------------------------------------------------
       displayPitch shows the attributes of the Pitch Object
       in the format - 
            Name     : name
	    Index    : index
	    Position : [ x, y, z ] 
    --------------------------------------------------------- */

}







