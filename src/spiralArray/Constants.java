/* -----------------------------------------------------------------
   EChew: 20 Jan 2002 (4 Feb 2002)
   This class defines constants related to pitches,
   and methods for checking pitches.
--------------------------------------------------------------------*/
package spiralArray;

public class Constants implements ConstantsInterface {

    // ----------- Defining global constants

    public static String[] PitchNames = {
//            "Gbbb", "Dbbb", "Abbb", "Ebbb", "Bbbb",
            "Fbb", "Cbb", "Gbb", "Dbb", "Abb", "Ebb", "Bbb",
            "Fb", "Cb", "Gb", "Db", "Ab", "Eb", "Bb",
            "F", "C", "G", "D", "A", "E", "B",
            "F#", "C#", "G#", "D#", "A#", "E#", "B#",
            "F##", "C##", "G##", "D##", "A##", "E##", "B##",
//            "F###", "C###", "G###", "D###", "A###"
    };

    /** DH: These arrays allow us to name pitches outside of the keyrange defined by Pitchanmes
     *
     */
    public static String[]  PitchNamesNext = {
            "F###", "C###", "G###", "D###", "A###", "E###", "B###"
//            "F###", "C###", "G###", "D###", "A###"
    };

    public static String[]  PitchNamesPrevious = {
            "Fbbb", "Cbbb", "Gbbb", "Dbbb", "Abbb", "Ebbb", "Bbbb"
//            "F###", "C###", "G###", "D###", "A###"
    };


    public static int KeyOffset ()  {
        return 5;
    }

    // ----------- Defining PitchNames methods

    public static boolean isPitchName(String p) {
        boolean isPitch = false;
        int i = 0;
        while ((i++ < PitchNames.length) && (isPitch == false)) {
            if (p.equals(PitchNames[i])) {
                isPitch = true;
            }
        }
        return isPitch;
    }

    public static int findIndex(String p) {

        // *** Find the index of the pitch in PitchNames[]

        int i = -1;     // counter
        int index = -1; // index of pitch p

        while ((i++ < PitchNames.length) && (index == -1)) {
            if (p.equals(PitchNames[i])) {
                index = i;
            }
        }

        if (index == -1) {
            System.out.println("ERROR: Invalid name ...");
            return -999;
        } else {
            return index - 15;
        }
    }

}

