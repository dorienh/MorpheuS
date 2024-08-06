package spiralArray;
import featureX.musicXMLparserDH;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Dorien Herremans on 03/02/15.
 */
public class KeySearchInArray {


        public static void main(String[] args) throws IOException {


            System.out.println("Reading in file... ");
            String[] songSequence = null;
            String[] songSequenceParsed = null;
            String filename = "";

            if (args.length > 0){
                filename = args[0];

            }
            else {
                System.out.print("You did not specify any filename as on option.");
                System.exit(0);
            }
//       filename = "./data/Prelude_C_Major_-_Bach.xml";

            musicXMLparserDH parser = new musicXMLparserDH(filename);
            String[] flatSong = parser.parseMusicXML();


//        System.out.print(flatSong.length);


            //print out the songI j
//        for (int i = 0; i < flatSong.length; i++){
//            System.out.println(flatSong[i]);
//        }





            //read the data: reads one line of data in each string element of songSequence and remove measure marks -----
            //one string is one point in time
//        try {
//            songSequence = KeySearch.ReadMusicDataToArray(args[0]);
//            System.out.println("Length of the song: " + songSequence.length);
//
//        } catch (IOException e) {
//            System.out.print("problem reading files");
//            e.printStackTrace();
//        }

            //print the song
//        System.out.println("Read the following song: ");
//        for (String note : songSequence){
//            System.out.println(note + " ");
//        }
//        System.out.println("End of song.");


            //map array of strings of letter names to CE positions of each time slice
        /*double[][] qseq = KeySearch.MapToCESequence(songSequence);

        //print CE positions for each time slice
        for (double[] note : qseq) {
            System.out.println(note[0] + " " + note[1] + " " + note[2] +" ") ;
        }   */


            //calculate key on whole song

//        String[] songSequence2 = new String[2];
//        songSequence2[0] = "C E G";
//        songSequence2[1] = "C E A";
//

            //setup a new keysearch, this initialises all the minor and major key positions
            KeySearch keySearch = new KeySearch();

            keySearch.getKeyFromWindow(flatSong, 0, flatSong.length - 1);

            System.out.print("with a minimal distance of " + keySearch.getClosestDistanceFromWindow(flatSong, 0, flatSong.length-1));
//        //find the closest key
//        keySearch.getKeyFromWindow(songSequence, 0, songSequence.length-1);
//
//        keySearch.getKeyFromWindow(songSequence2, 0, songSequence2.length - 1);



            //find the lowest distance to a key
            //System.out.print(keySearch.getClosestDistanceFromWindow(songSequence, 0, songSequence.length-1));






            //try segmenting the piece

            ArrayList<Integer> segments = new ArrayList<Integer>();

            //when an int is present in segment, it means there is a segment that starts at this position.


            Segmenter segmenter = new Segmenter(songSequence, keySearch);

            //set random segments, 2 in this case

            //DHsegmenter.generateRandomSegments(2);

            //DHsegmenter.printSegments();

            //get closest key + its distance for each segment
            //System.out.println("total  min distance summed for each segments: " + segmenter.minDistanceOfSegments());



            System.out.println("\nDone. ");





        }





}
