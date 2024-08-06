/* -----------------------------------------------------------------
   EChew: 5 Mar 2002 (7 Mar 2002)

   This class defines points in the Spiral Array.  
   Each point has a name and a position.

--------------------------------------------------------------------*/
package spiralArray;

import java.io.*;

public class SpiralArrayObject implements SpiralArrayObjectInterface {

    private String name;
    private double[] position = { 0.0, 0.0, 0.0 };

    // ----------- Defining constructors

    public SpiralArrayObject() {
	name = "";
	position[0] = 0.0;
	position[1] = 0.0;
	position[2] = 0.0;
    }

    /**
     * Create a SpiralArrayObject with a name
     * @param name
     */
    public SpiralArrayObject(String name) {
	this.name = name;
	position[0] = 0.0;
	position[1] = 0.0;
	position[2] = 0.0;
    }

    public SpiralArrayObject(double[] position) {
	name = "";
	this.position = position;
    }

    public SpiralArrayObject(String name, double[] position) {
	this.name = name;
	this.position = position;
    }

    // ----------- Defining get methods

    public String getName () { return name; }
    public double[] getPosition () { return position; }

    // ----------- Defining set methods

    public void setName (String name) { this.name = name; }
    public void setPosition (double[] position) { this.position = position; }

    // ----------- Defining other methods

    public void displaySpiralArrayObject () { 
	System.out.println("Name :    " + name);
	// display numbers to 1-100th accuracy
	double pos0 = Math.round(position[0]*100.0)/100.0;
	double pos1 = Math.round(position[1]*100.0)/100.0;
	double pos2 = Math.round(position[2]*100.0)/100.0;
	System.out.println("Position: [ " + pos0 + " , " + pos1 + " , " + pos2 + " ]");
    }

}



