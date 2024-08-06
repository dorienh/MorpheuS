/* -----------------------------------------------------------------
   EChew: 19 Feb 2002 (7 Mar 2002)

   This is the Interface of the MajorTriad class.

--------------------------------------------------------------------*/
package spiralArray;

public interface TriadInterface {

    /* ---------------------------------------------------------
       add text
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setTriad (String p, Parameters par);
    public abstract void setTriad (int i, Parameters par);

    // ----------- Get methods

    public abstract String getName ();
    public abstract int getIndex ();
    public abstract Pitch getRoot ();
    public abstract Pitch getFifth ();
    public abstract Pitch getThird ();
    public abstract double[] getPosition ();

   // ----------- Other methods

    public abstract void displayTriad ();

}







