/* -----------------------------------------------------------------
   EChew: 19 Feb 2002

   This is the Interface of the Key class.

--------------------------------------------------------------------*/
package spiralArray;

public interface KeyInterface {

    /* ---------------------------------------------------------
       A global array of allowable chord names are:
           Nbb, Nb, N, N# and N##, where N = {A,B,C,D,E,F,G}
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setKey (String p);
    public abstract void setKey (int i);

    // ----------- Get methods

    public abstract String getName ();
    public double[] getPosition ();
    public int getIndex ();

    /* ---------------------------------------------------------
       findIndex returns the index of the key as given
       by the distance of the tonic (measured by perfect 5ths) 
       from C.
    --------------------------------------------------------- */

    public abstract void displayKey ();

}







