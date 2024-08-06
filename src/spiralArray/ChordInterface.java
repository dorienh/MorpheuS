/* -----------------------------------------------------------------
   EChew: 4 Feb 2002 (19 Feb 2002) 

   This is the Interface of the Chord class.

--------------------------------------------------------------------*/
package spiralArray;

public interface ChordInterface {

    /* ---------------------------------------------------------
       A global array of allowable chord names are:
           Nbb, Nb, N, N# and N##, where N = {A,B,C,D,E,F,G}
    --------------------------------------------------------- */

    // ----------- methods

    public abstract void setName (String p, Parameters par);
    public abstract void setIndex (int i, Parameters par);
    public abstract void setPosition (double[] pos);

    public abstract String getName ();
    public abstract int getIndex ();
    public abstract Pitch getRoot ();
    public abstract double[] getRootPosition ();
    public abstract double[] getChordPosition ();

    /* ---------------------------------------------------------
       findIndex returns the index of the chord as given
       by the distance of its root ((measured by perfect 5ths) 
       from C.
    --------------------------------------------------------- */

    public abstract void displayChord ();

}







