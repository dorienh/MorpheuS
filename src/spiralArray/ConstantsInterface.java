/* -----------------------------------------------------------------
   EChew: 4 Feb 2002 

   This is the Interface of the Constants class.
   Global constants and methods are defined.

--------------------------------------------------------------------*/
package spiralArray;

public interface ConstantsInterface {

    /* ---------------------------------------------------------
       A global array of allowbale pitch names is defined:
           the name - the format for names is restricted to 
           Nbb, Nb, N, N# and N##, where N = {A,B,C,D,E,F,G}
    --------------------------------------------------------- */

    // ----------- methods

    /* ---------------------------------------------------------
       isPitchName checks to see if the String is one of the 
       pitch names listed in Constants.PitchNames, that is to
       say, if it is in the range Fbb through B##. 
    --------------------------------------------------------- */

    /* ---------------------------------------------------------
       findIndex returns the index of the pitch p as given
       by its distance (measured by perfect 5ths) from C.
    --------------------------------------------------------- */

}








