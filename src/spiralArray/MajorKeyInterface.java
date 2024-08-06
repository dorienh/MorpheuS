/* -----------------------------------------------------------------
   EChew: 19 Feb 2002

   This is the Interface of the MajorKey class.

--------------------------------------------------------------------*/
package spiralArray;

public interface MajorKeyInterface {

    /* ---------------------------------------------------------
       add text
    --------------------------------------------------------- */

    // ----------- Set methods

    public abstract void setMajorKey (String p, Parameters par);
    public abstract void setMajorKey (int i, Parameters par);

    // ----------- Get methods

    public abstract String getName ();
    public abstract int getIndex ();
    public abstract MajorTriad getI ();
    public abstract MajorTriad getV ();
    public abstract MajorTriad getIV ();

    public Pitch getTonic ();
    public Pitch getSupertonic ();
    public Pitch getMediant ();
    public Pitch getDominant ();
    public Pitch getSubdominant ();
    public Pitch getSubmediant ();
    public Pitch getLeadingNote ();

    public abstract double[] getPosition ();

   // ----------- Other methods

    public abstract void displayKey ();
}







