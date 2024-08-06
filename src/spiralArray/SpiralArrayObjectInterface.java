/* -----------------------------------------------------------------
   EChew: 5 Mar 2002

   This is the Interface of the SpiralArrayObject class.

--------------------------------------------------------------------*/
package spiralArray;

public interface SpiralArrayObjectInterface {

    // ----------- Set methods

    public abstract void setName (String name);
    public abstract void setPosition (double[] position);

    // ----------- Get methods

    public abstract String getName ();
    public abstract double[] getPosition ();

    // ----------- Other methods

    public abstract void displaySpiralArrayObject ();

    /* ---------------------------------------------------------
       displayPitch shows the attributes of the Pitch Object
       in the format - 
            Name     : name
	    Position : [ x, y, z ] 
    --------------------------------------------------------- */

}







