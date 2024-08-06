/* -----------------------------------------------------------------
   EChew: 31 Jan 2002

   This is the interface for the Parameter class.
   Set and get methods are defined.

--------------------------------------------------------------------*/
package spiralArray;

public interface ParameterInterface {

    /* ---------------------------------------------------------
       The parameters that are defined in this class are:

       Vertical Height, 
          h = height gain per quarter turn of the Spiral Array
       Radius, r = radius of the Spiral Array

       Major Triad Weights, an array containing:
          w1 = weight on root of chord
	  w2 = weight on fifth of chord
	  w3 = weight on third of chord ( = 1 - w1 - w2 )
       Minor Triad Weights, similarly defined.

       Major Key Weights, an array containing:
          v1 = weight on tonic triad
	  v2 = weight on V chord
	  v3 = weight on IV chord ( = 1 - v1 - v2 )

       Major Key Weights, an array containing:
          v1 = weight on tonic triad
	  v2 = weight on V/v chord
	  v3 = weight on IV/iv chord ( = 1 - v1 - v2 )
	  alpha = weight on V vs. v
	  beta = weight on iv vs. IV

       --------------------------------------------------------- */

    // ------------ Set methods

    public abstract void setVerticalStep (double h);
    public abstract void setRadius (double r);

    public abstract void setMajorTriadWeight (double w1, double w2);
    public abstract void setMinorTriadWeights (double w1, double w2);
    public abstract void setMajorKeyWeights (double v1, double v2);
    public abstract void setMinorKeyWeights (double v1, double v2);
    public abstract void setMinorKeyWeights (double v1, double v2, double a, double b);

    // ------------ Get methods

    public abstract double getVerticalStep ();
    public abstract double getRadius ();

    public abstract double[] getMajorTriadWeight ();
    public abstract double[] getMinorTriadWeight ();

    public abstract double[] getMajorKeyWeight ();
    public abstract double[] getMinorKeyWeight ();

    // ------------ Other methods

    public abstract void displayParameters ();

}

