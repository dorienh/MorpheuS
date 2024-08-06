/**
 * Created by dorien on 01/12/14.
 */
package optimize;
import featureX.featureX;
import music.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by dorien on 13/11/14.
 */

public class PitchValue {


    private ArrayList<Integer> possibleValuesStaff1 = new ArrayList<Integer>();

    private ArrayList<Integer> possibleValuesStaff2 = new ArrayList<Integer>();

    private Map<Integer, Note> eventsInFragment = new HashMap<>();

    Map<Integer, ArrayList<Integer>> relatedNotes;


    public PitchValue(featureX refmodel) {
        //Cello 36-76

        for (int i = refmodel.getRefMinPitchStaff1(); i <= refmodel.getMaxPitchStaff1(); i++) {
            this.possibleValuesStaff1.add(i);
        }

        for (int i = refmodel.getRefMinPitchStaff2(); i <= refmodel.getMaxPitchStaff2(); i++) {
            this.possibleValuesStaff2.add(i);
        }
//        this.possibleValues.add(60);
//        this.possibleValues.add(62);
//        this.possibleValues.add(64);
//        this.possibleValues.add(65);
//        this.possibleValues.add(67);
//        this.possibleValues.add(69);
//        this.possibleValues.add(71);
//        this.possibleValues.add(72);
//        this.possibleValues.add(74);
//
    }


    public Integer getRandom(int staff) {
        Random random = new Random();

        Integer newPitch;
        if (staff == 1) {
            newPitch = this.possibleValuesStaff1.get(random.nextInt(this.possibleValuesStaff1.size()));
//        System.out.print("SET: "+ newPitch);
        } else if (staff == 2) {
            newPitch = this.possibleValuesStaff2.get(random.nextInt(this.possibleValuesStaff2.size()));
//
        } else {

            //other staffs, let's just set them as staff 1
            newPitch = this.possibleValuesStaff1.get(random.nextInt(this.possibleValuesStaff1.size()));
//
        }

        return newPitch;
    }

    public Integer getRandomVoice2() {
        Random random = new Random();
        Integer newPitch = this.possibleValuesStaff2.get(random.nextInt(this.possibleValuesStaff2.size()));
//        System.out.print("SET: "+ newPitch);
        return newPitch;
    }


    public ArrayList<Integer> getPossibleValuesStaff1() {
        return possibleValuesStaff1;
    }


    public ArrayList<Integer> getPossibleValues(int voice) {
        if (voice == 1) {
            return possibleValuesStaff1;
        } else if (voice == 2) {
            return possibleValuesStaff2;
        } else {
            return possibleValuesStaff1;
        }
    }

    public ArrayList<Integer> getPossibleValuesStaff2() {
        return possibleValuesStaff2;
    }


    public void buildPatternChecker(Map<Integer, Integer> pitches) {

        relatedNotes = new HashMap<>();


        //for each id from pitches, save list of other ids so that we can quickly check(staff and change)


        //for each note in pitches
        for (Integer uniqueid : pitches.keySet()) {


            ArrayList<Integer> refnotelist = new ArrayList<>();


            for (Note note : eventsInFragment.values()) {

                if (uniqueid == note.getReferenceNoteID()) {
                    //check if the found note (changed) is within the range

                    refnotelist.add(note.getId());

                }

            }
            relatedNotes.put(uniqueid, refnotelist);
        }
    }


    //todo test
    public boolean checkInAllPatterns(int id, int pitch) {
        boolean ok = true;


        //get all then otes that refer to the original id
        for (Integer thisid : relatedNotes.get(id)) {


            int thisstaff = eventsInFragment.get(thisid).getStaff();
            int thispitch = pitch + eventsInFragment.get(thisid).getReferenceChange();


            if (!(getPossibleValues(thisstaff).contains(thispitch))) {
                ok = false;
                break;
            }

        }


        return ok;


    }






    public void setEventsInFragment(Map<Integer, Note> eventsInFragment){

        this.eventsInFragment = eventsInFragment;


    }


}
