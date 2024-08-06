/* -----------------------------------------------------------------
   EChew: 19 Feb 2002

   This is the Interface of the MinorTriad class.

--------------------------------------------------------------------*/
package spiralArray;

public interface MinorTriadInterface {

    /* ---------------------------------------------------------
       add text
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setMinorTriad (String p, Parameters par);
    public abstract void setMinorTriad (int i, Parameters par);

    // ----------- Get methods

    public abstract String getName ();
    public abstract int getIndex ();
    public abstract Pitch getRoot ();
    public abstract Pitch getFifth ();
    public abstract Pitch getThird ();
    public abstract double[] getPosition ();

}







