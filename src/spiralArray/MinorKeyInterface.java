/* -----------------------------------------------------------------
   EChew: 19 Feb 2002

   This is the Interface of the MinorKey class.

--------------------------------------------------------------------*/
package spiralArray;

public interface MinorKeyInterface {

    /* ---------------------------------------------------------
       add text
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setMinorKey (String p, Parameters par);
    public abstract void setMinorKey (int i, Parameters par);

    // ----------- Get methods

    public abstract String getName ();
    public abstract int getIndex ();
    public abstract MinorTriad geti ();
    public abstract MajorTriad getV ();
    public abstract MinorTriad getv ();
    public abstract MinorTriad getiv ();
    public abstract MajorTriad getIV ();

    public abstract double[] getPosition ();

   // ----------- Other methods

    public abstract void displayKey ();
}







