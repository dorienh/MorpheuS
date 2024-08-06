/* -----------------------------------------------------------------
   EChew: 9 Jan 2002 (7 Mar 2002)
   This class defines pitches,
   and their 3D position on the Spiral Array.
--------------------------------------------------------------------*/
package spiralArray;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.*;

/**
 * Pitch class
 */
public class Pitch extends SpiralArrayObject implements PitchInterface {

    // ----------- Inherits: String name; double[] position;
    private int index;

    // ----------- Defining constructors

    public Pitch() {
        // *** Make default pitch "C" with default parameters
        super("C");
        //System.out.println("Creating default pitch C with default parameters");
        double[] pos = {0.0, (new Parameters()).getRadius(), 0.0};
        super.setPosition(pos);
        index = 0;
    }

    public Pitch(Parameters par) {
        // *** Make default pitch "C"
        super("C");
        //System.out.println("Creating default pitch C");
        double[] pos = {0.0, par.getRadius(), 0.0};
        super.setPosition(pos);
        index = 0;
    }

    /**
     * Create the pitch (p) with default parameters
     *
     * @param p   see constant pitch e.g. A##, resulting object contains e.g. 3 coordinates in the spiral array, name (=index of note)
     */
    public Pitch(String p) {


        super();
        //MARK By YUN_CHING CHEN //
        //System.out.println("Creating pitch of name "+p+" with default parameters");

        // *** Find the index of the pitch in Constants.PitchNames[]
        int i = -1;
        index = -1;
        while ((i++ < 35) && (index == -1)) {
            //System.out.println(p);
            if (p.equals(Constants.PitchNames[i])) {
                index = i;
            }
        }

        // *** Set the pitch name, index and position
        if (index != -1) {
            super.setName(p);
            // *** Set the pitch index
            index = index - 15;
            // *** Set the pitch position
            Parameters par = new Parameters();
            double c = ((double) index - (4.0 * Math.floor((double) index / 4.0)));
            double[] pos = {0.0, 0.0, 0.0};
            switch ((int) c) {
                //for each possible position on a quarter of the spiral:
                case 0:
                    pos[1] = par.getRadius();
                    break;
                case 1:
                    pos[0] = par.getRadius();
                    break;
                case 2:
                    pos[1] = (-1.0) * par.getRadius();
                    break;
                case 3:
                    pos[0] = (-1.0) * par.getRadius();
                    break;
            }
            //height in the spiral:
            pos[2] = index * par.getVerticalStep();
            super.setPosition(pos);
            //System.out.println(pos[0] + " " + pos[1] + " " +pos[2]);
        } else {
            System.out.println("ERROR: Invalid name ...");
        }
        ;
    }


    /**
     *
     * Create the pitch (p) with inputted parameters
     *
     * @param p    pitch: see constant pitch e.g. A, output: 3 coordinates in the spiral array
     * @param par  parameter object
     */
    public Pitch(String p, Parameters par) {

        // *** Create pitch (p)
        //System.out.println("Creating pitch of name "+p);

        // *** Find the index of the pitch in Constants.PitchNames[]
        int i = -1;
        index = -1;
        while ((i++ < 35) && (index == -1)) {
            if (p.equals(Constants.PitchNames[i])) {
                index = i;
            }
        }

        // *** Set the pitch name, index and position
        if (index != -1) {
            super.setName(p);
            // *** Set the pitch index
            index = index - 15;
            // *** Set the pitch position
            double c = ((double) index - (4.0 * Math.floor((double) index / 4.0)));
            double[] pos = {0.0, 0.0, 0.0};
            switch ((int) c) {
                case 0:
                    pos[1] = par.getRadius();
                    break;
                case 1:
                    pos[0] = par.getRadius();
                    break;
                case 2:
                    pos[1] = (-1.0) * par.getRadius();
                    break;
                case 3:
                    pos[0] = (-1.0) * par.getRadius();
                    break;
            }
            pos[2] = index * par.getVerticalStep();
            super.setPosition(pos);
        } else {
            System.out.println("ERROR: Invalid name ...");
        }
        ;
    }


    /**
     *
     * Create pitch (index i) with default parameters and calculate
     *
     * @param i  index in the pitch constant
     */
    public Pitch(int i) {


        super();
        //System.out.println("Creating pitch of index "+i);

        // *** Set the pitch index and name
        index = i;


        //DH: added this check to allow pitches that go outside the defined keyrange
//        System.out.print("i: " + i);
        if (i>=20){
            super.setName(Constants.PitchNamesNext[i-20]);
        }
        else if (i < -15){
            super.setName(Constants.PitchNamesPrevious[i+7+15]);
        }else{
            super.setName(Constants.PitchNames[i+15]);
        }

        // *** Set the pitch position
        Parameters par = new Parameters();
        double c = ((double) i - (4.0 * Math.floor((double) i / 4.0)));
        double[] pos = {0.0, 0.0, 0.0};
        switch ((int) c) {
            case 0:
                pos[1] = par.getRadius();
                break;
            case 1:
                pos[0] = par.getRadius();
                break;
            case 2:
                pos[1] = (-1.0) * par.getRadius();
                break;
            case 3:
                pos[0] = (-1.0) * par.getRadius();
                break;
        }
        pos[2] = index * par.getVerticalStep();
        super.setPosition(pos);
    }

    public Pitch(int i, Parameters par) {

        // *** Create pitch (index i)
        super();
        //System.out.println("Creating pitch of index "+i);

        // *** Set the pitch index and name
        index = i;
        super.setName(Constants.PitchNames[i + 15]);

        // *** Set the pitch position
        double c = ((double) i - (4.0 * Math.floor((double) i / 4.0)));
        double[] pos = {0.0, 0.0, 0.0};
        switch ((int) c) {
            case 0:
                pos[1] = par.getRadius();
                break;
            case 1:
                pos[0] = par.getRadius();
                break;
            case 2:
                pos[1] = (-1.0) * par.getRadius();
                break;
            case 3:
                pos[0] = (-1.0) * par.getRadius();
                break;
        }
        pos[2] = index * par.getVerticalStep();
        super.setPosition(pos);
    }


    // ----------- Defining set methods

    public void setName(String p, Parameters par) {
        if (super.getName() != p) {
            if (Constants.isPitchName(p)) {
                super.setName(p);
                System.out.println("Changing pitch to " + p);
                setIndex(p, par);
                setPosition(index, par);
            } else {
                System.out.println("ERROR: Invalid name ...");
            }
        }
    }

    public void setIndex(int i, Parameters p) {
        if ((i < 20) && (i >= -15)) {
            index = i;
            super.setName(Constants.PitchNames[i + 15]);
            System.out.println("Changing pitch to " + super.getName());
            setPosition(index, p);
        } else System.out.println("ERROR: Index out of range ...");
    }

    public void setIndex(String p, Parameters par) {
        int i = -1;
        index = -1;
        while ((++i < 35) && (index == -1)) {
            if (p.equals(Constants.PitchNames[i])) {
                index = i - 15;
                setPosition(index, par);
            }
        }
    }

    private void setPosition(int i, Parameters par) {
        double c = ((double) i - (4.0 * Math.floor((double) i / 4.0)));
        double[] pos = {0.0, 0.0, 0.0};
        switch ((int) c) {
            case 0:
                pos[1] = par.getRadius();
                break;
            case 1:
                pos[0] = par.getRadius();
                break;
            case 2:
                pos[1] = (-1.0) * par.getRadius();
                break;
            case 3:
                pos[0] = (-1.0) * par.getRadius();
                break;
        }
        pos[2] = index * par.getVerticalStep();
        super.setPosition(pos);
    }


    // ----------- Defining get methods

    public String getName() {
        return super.getName();
    }

    public int getIndex() {
        return index;
    }

    public double[] getPosition() {
        return super.getPosition();
    }

    // ----------- Defining other methods

    public void displayPitch() {
        super.displaySpiralArrayObject();
        System.out.println("Index:    " + index);
    }

    //***********************************************************
    //add this part so that positions are able to put on the 3D model
    public float PitchPosition(int thisPos) {
        double[] pos = super.getPosition();
        return (float) (pos[thisPos]);
    }


}



