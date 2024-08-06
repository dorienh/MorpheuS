/* -----------------------------------------------------------------
   EChew: 25 Jan 2002 (5 Mar 2002)
   The Parameter class defines parameters for the Spiral Array.
--------------------------------------------------------------------*/
package spiralArray;

import java.io.*;

public class Parameters implements ParameterInterface {

    private double verticalStep = 0.01;
    private double radius = 1.0;

    private double[] majorTriadWeight = {1.0,0.0,0.0};
    private double[] minorTriadWeight = {1.0,0.0,0.0};
    private double[] majorKeyWeight   = {1.0,0.0,0.0};
    private double[] minorKeyWeight   = {1.0,0.0,0.0,1.0,1.0};

    // ----------- Defining constructor

    public Parameters() {

	verticalStep = Math.sqrt(2.0/15.0);
	radius = 1.0;

	//verticalStep = 0.3*Math.sqrt(2.0/15.0);
	//radius = 0.4;

	majorTriadWeight[0] = 0.536;
	majorTriadWeight[1] = 0.274;
	majorTriadWeight[2] = 0.19;

	minorTriadWeight[0] = 0.536;
	minorTriadWeight[1] = 0.274;
	minorTriadWeight[2] = 0.19;

	majorKeyWeight[0] = 0.536;
	majorKeyWeight[1] = 0.274;
	majorKeyWeight[2] = 0.19;

	minorKeyWeight[0] = 0.536;
	minorKeyWeight[1] = 0.274;
	minorKeyWeight[2] = 0.19;
	minorKeyWeight[3] = 1.0;
	minorKeyWeight[4] = 1.0;

    }

    // ----------- Defining set methods

    public void setVerticalStep (double h) { verticalStep = h; }
    public void setRadius (double r) { radius = r; }

    public void setMajorTriadWeight (double w1, double w2) {
	majorTriadWeight[0] = w1;
	majorTriadWeight[1] = w2;
	majorTriadWeight[2] = 1-w1-w2;
    }

    public void setMinorTriadWeights (double w1, double w2) {
	minorTriadWeight[0] = w1;
	minorTriadWeight[1] = w2;
	minorTriadWeight[2] = 1-w1-w2;
    }

    public void setMajorKeyWeights (double v1, double v2) {
	majorKeyWeight[0] = v1;
	majorKeyWeight[1] = v2;
	majorKeyWeight[2] = 1-v1-v2;
    }

    public void setMinorKeyWeights (double v1, double v2) {
	minorKeyWeight[0] = v1;
	minorKeyWeight[1] = v2;
	minorKeyWeight[2] = 1-v1-v2;
    }

    public void setMinorKeyWeights (double v1, double v2, double a, double b) {
	minorKeyWeight[0] = v1;
	minorKeyWeight[1] = v2;
	minorKeyWeight[2] = 1-v1-v2;
	minorKeyWeight[3] = a;
	minorKeyWeight[4] = b;
    }

    // ----------- Defining get methods

    public double getVerticalStep () { return verticalStep; }
    public double getRadius () { return radius; }

    public double[] getMajorTriadWeight () { return majorTriadWeight; }
    public double[] getMinorTriadWeight () { return minorTriadWeight; }

    public double[] getMajorKeyWeight () { return majorKeyWeight; }
    public double[] getMinorKeyWeight () { return minorKeyWeight; }

    // ----------- Defining get methods

    private String arrayToString (double[] a) { 
	String arrayString = "[ ";
	for (int i=0; i<a.length; i++) { arrayString = arrayString + a[i] + ", "; };
	arrayString = arrayString + "]";
	return arrayString;
    }

    public void displayParameters () { 
	System.out.println("Vertical Step,  h  = " + verticalStep);
	System.out.println("Radius,         r  = " + radius + "\n");
	System.out.println("Major Triad Weight = " + arrayToString(majorTriadWeight));
	System.out.println("Major Triad Weight = " + arrayToString(minorTriadWeight));
	System.out.println("Major Key Weight   = " + arrayToString(majorKeyWeight));
	System.out.println("Minor Key Weight   = " + arrayToString(minorKeyWeight));
    }

}

