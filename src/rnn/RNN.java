package rnn;

import music.Note;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Dorien Herremans on 02/07/15.
 */
public class RNN {
    private HashMap<Note, Integer> pitches;
    double[][] Wxh;
    double[][] Whh;
    double[][] Why;
    double[] bh;
    double[] by;
    double[] ceTemplate;
    Integer max_interval;
    Integer startPitch;
    //Eval optimize = Eval.Profile;
    Eval optimize = Eval.Min;

    public RNN() throws IOException {
        // # Step 1: Define an RNN:

        // # input to hidden

//        Wxh = new double[][]{
//                {0.1, 0.7},
//                {0.8, 0.9}};

        Wxh = readFile("model/Wxh_trained.txt");

        //# hidden to hidden

//        Whh = new double[][]{
//                {0.9, 0.1},
//                {0.2, 0.8}};

        Whh = readFile("model/Whh_trained.txt");


        //# hidden to output
//        Why = new double[][]{
//                {0.1, 0.9},
//                {0.3, 0.2}};

        Why = readFile("model/Why_trained.txt");



        //# hidden bias

//        bh = new double[]
//                {0.5, 0.1};

        bh = readFileOneDim("model/bh_trained.txt");


        //# output bias
//        by = new double[]
//                {0.8, 0.9};

        by = readFileOneDim("model/by_trained.txt");

        //# 3. Compute the Mean Cross-Entropy of the random melody under the model

        //double[][]melody = new double[pitches.size()][2];
        //todo fill melody

//        double[][] melody = new double[][]{
//                {0, 0},
//                {0, 0},
//                {1, 1},
//                {1, 0},
//                {1, 1},
//                {1, 0},
//                {0, 1},
//                {1, 1},
//                {1, 1},
//                {0, 1}};


        ceTemplate = readFileOneDim("crossentropy_prelude_fixed");



        //read in template pitches

        List<Integer> lines = new ArrayList<Integer>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("model/pitches_preludeVoice1"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(Integer.valueOf(line));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        Integer[] template_pitches = new Integer[lines.size()];
        for (int i = 0; i < lines.size(); i++){
        template_pitches[i] = lines.get(i);

    }



        //convert them to 1 hot notation
        max_interval = 24;

        double[][] template_melody = pitchToMelody1Hot(template_pitches);

        //for now, we are just calculating entropy based on the template

        double[][] melody = template_melody.clone();


        //print the melody in one hot notation
//        System.out.println("melody");
//        for (int i = 0 ; i < melody.length; i ++){
//
//            for (int j = 0; j < melody[0].length; j++){
//                System.out.print(melody[i][j] + " ");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("end");


        startPitch = 72;


//        Double mce = mean(evaluate_melody(melody));
//
//        System.out.printf("evaluated melody: " + mce);
//







    }






    double[][] readFile(String filename){

        String line = null;
        BufferedReader stream = null;
        List<String[]> lines = new ArrayList<String[]>();

        try {
            //stream = new BufferedReader(new FileReader("./data/test.txt"));
            stream = new BufferedReader(new FileReader(filename));


            //for each line
            while ((line = stream.readLine()) != null) {
                String[] splitted = line.split("\t");


                lines.add(splitted);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numrows = lines.get(0).length;
        double[][] array = new double[lines.size()][numrows];

        for (int i = 0; i < lines.size(); i++ ){
            for (int j = 0; j < lines.get(0).length; j++){
                //double test2 = (double) Double.valueOf(lines.get(i)[j]);
                array[i][j] = (double) Double.valueOf(lines.get(i)[j]);
            }
        }

        return array;

    }




    double[] readFileOneDim(String filename){

        double[] array;
        String line = null;
        BufferedReader stream = null;
        List<String> lines = new ArrayList<String>();

        try {
            //stream = new BufferedReader(new FileReader("./data/test.txt"));
            stream = new BufferedReader(new FileReader(filename));


            //for each line
            while ((line = stream.readLine()) != null) {
                //String[] splitted = line.split(" ");
                //List<String> dataLine = new ArrayList<String>(splitted.length);
                lines.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        array = new double[lines.size()];

        for (int i = 0; i < lines.size(); i++ ){

                array[i] = (double) Double.valueOf(lines.get(i));

        }

        return array;

    }


    double[][] pitchToMelody1Hot (Integer[] pitches){

        double[][] hots;



        hots = new double[pitches.length-1][2*max_interval + 1];

//        which units to activate in each row

        assert pitches.length >= (hots.length+1);

        for (int i = 0; i < hots.length; i++){


            int one_hot_idx =  (int) (pitches[i+1] - pitches[i]);

            //System.out.println("one hot" + one_hot_idx);
            //System.out.flush();
            boolean changed = false;
            int old_hot = one_hot_idx;
            //todo max interval
            if (Math.abs(one_hot_idx) > max_interval){
                one_hot_idx = max_interval;
                changed = true;
            }

            one_hot_idx = one_hot_idx + max_interval;
            //set the activated units to one
            hots[i][one_hot_idx] = 1;
        }




        return hots;
    }


    double[] HotToPitch (double[][] hots){


        double[] pitch = new double[hots.length+1];

        pitch[0] = startPitch;


        for (int i = 0; i < hots.length; i++){

            int interval = 0;

            for (int j = 0; j < hots[i].length; j++){
                if (hots[i][j] == 1){
                    interval = j;
                }
            }

            //correct interval
            interval = interval - max_interval;



            pitch[i+1] = pitch[i] + interval;

        }


        return pitch;
    }


    ArrayList<Double>  evaluate_melody(double[][] melody) {
        //  """Compute the mean cross entropy of a given melody, given a model.

        /*If we assume the following:

        * A melody of N notes represented in M dimensions
                * An RNN with K hidden units

        Then the input arguments are arrays of the following sizes:

        :param melody: The melody as an N x M array
        :param Wxh: The input-to-hidden weights (M x K)
        :param Whh: The hidden-to-hidden weights (K x K)
        :param Why: The hidden-to-output weights (K x M)
        :param bh: The hidden biases (K-sized vector)
        :param by: The output biases (M-sized vector)

        :returns: Mean Cross Entropy of the melody under the model
        :rtype: float

        """

        # get the required sizes from the RNN parameters
        */

        int M = Wxh.length;
        int K = Wxh[0].length;


        //# initialize the hidden state (zeros, or better random?)
        double[] h_prev = new double[K];
        //java should initialise to zero
        h_prev[0] = 1;


        //# in order to evaluate CE of the first note of the melody, we need to
        //# compare it to a prediction from the model, and for that prediction, we
        //# need a (fictitious) previous note (zeros, or better random?)
        double[] note_prev = new double[M];
        note_prev[0] = 1; //just setting a random 1 for the first note


        //# we gather the cross-entropy for all notes in the list ce
        ArrayList<Double> ce = new ArrayList<Double>();

        double[] h_i = new double[bh.length];

        for (int i = 0; i < melody.length; i++) {

            //# compute the hidden state based on the input
            //# and the previous hidden state


            double[] a = product(note_prev, Wxh);
            double[] b = product(h_prev, Whh);
            double[] c = sum(a, b);

            h_i = sigmoid(sum(c, bh));


            //# compute the output based on the hidden state
            double[] y_i = sigmoid(sum(product(h_i, Why), by));


            ce.add(cross_entropy(y_i, melody[i]));

            //# save the hidden state for the next iteration
            h_prev = h_i;

            //# the current note will be the previous note in the next iteration
            note_prev = melody[i];


        }


//        System.out.println("Cross entropy: ");
//        for (int i = 0; i < ce.size(); i++){
//
//            System.out.print(ce.get(i) + " ");
//
//        }

        //# mean cross entropy over all notes
        //double mce = mean(ce);

        return ce;


    }






    public double evaluate_piece(Integer[] inputPitches){

        double score = 0;

        double[][] inputFeatures = pitchToMelody1Hot(inputPitches);


        if (optimize == Eval.Min){

            //this returns the cross entropy. We'll want something different
            score = mean(evaluate_melody(inputFeatures));
        }
        else if (optimize == Eval.Profile) {


            ArrayList<Double> profile = evaluate_melody(inputFeatures);
            //compare profile to template
            for (int i = 1; i < profile.size(); i++) {

                double term = profile.get(i) - ceTemplate[i];
                score += term * term;


            }
        }
                score =  Math.sqrt(score);




        return score;

    }



    double[][] generate_melody(int N) {


//            """Generate a melody using an RNN model.
//
//    If we assume the following:
//
//            * The length N of the desired melody, in notes
//    * An RNN with K hidden units, and an input/output size of M
//
//    Then the input arguments are as follows:
//
//            :param N: The length of the desired melody
//    :param Wxh: The input-to-hidden weights (M x K)
//    :param Whh: The hidden-to-hidden weights (K x K)
//    :param Why: The hidden-to-output weights (K x M)
//    :param bh: The hidden biases (K-sized vector)
//    :param by: The output biases (M-sized vector)
//
//    :returns: A melody of size N
//    :rtype: N x M array
//
//    """
//
//            # get the required sizes from the RNN parameters
        int M = Wxh.length;
        int K = Wxh[0].length;

//    # initialize the hidden state (zeros, or better random?)
        double[] h_prev = new double[K];
//    #
//    in order
//    to generate
//    the first
//    note of
//    the melody, we
//    need a
//    prediction
//    # from the model, and for that prediction, we need a (fictitious) previous
//    # note (zeros, or better random?)
        double[] note_prev = new double[M];

//            # we gather the generated notes in the list melody
//    melody = []

        double[] h_i = new double[bh.length];

        double[][] melody = new double[N][K];
        //todo m or k

        for (int i = 0; i  < N; i++) {

//    # compute the hidden state based on the input
//    # and the previous hidden state

            h_i = sigmoid(sum(sum(product(note_prev, Wxh), product(h_prev, Whh)), bh));

            h_i = sigmoid(sum(sum(product(note_prev, Wxh),
                    product(h_prev, Whh)), bh));


            //# compute the output based on the hidden state
            double[] y_i = sample(sigmoid(sum(product(h_i, Why), by)));


            melody[i] = y_i;


//            # save the hidden state for the next iteration
            h_prev = h_i;

//    # use the prediction y_i as the input for the next iteration
            note_prev = y_i;

        }

        return melody;
    }


    double[] sample(double[] a) {
//            "" "Generate a binary vector by drawing a sample from a vector of
//            probabilities, such that P (sample[i] = 1) = p[i], and
//            P(sample[i] = 0) = 1 - p[i],for all i.
//
//            :param p:A vector of values in the interval[0, 1]
//            :returns:
//            A vector of 1 and 0 values
//            :
//            rtype:
//            array(float)

        double [] q =  new double[a.length];
        double random ;


        for (int i = 0; i < a.length ; i++){

//            double test = 0;
            Double sample = new Random().nextDouble();
            Double chance = a[i];

            //0 if less, 1 if  more
            q[0]= 1 - sample.compareTo(chance);
        }

        return       q;

    }



    double mean(ArrayList<Double> a) {
        double mean = 0;

        for (int i = 0; i < a.size(); i++) {
            mean += a.get(i).doubleValue();
        }

        mean = mean / a.size();

        return mean;

    }


    //# the sigmoid function, a common non-linear activation function used in neural
    //# networks
    double[] sigmoid(double[] x) {

        double[] result = new double[x.length];
        int i = 0;

        for (double a : x) {

            result[i] = 1. / (1. + Math.exp(-a));
            i++;

            //System.out.print("result: " + result);
        }


        return result;
    }


    double[] sum(double[] a, double[] b) {

        double[] sum = new double[a.length];

//        System.out.println("a" + a);
//        System.out.println("b" + b);

        for (int i = 0; i < a.length; i++) {

            if (i >= a.length) {
                System.out.print("a not long enough");

            }

            if (i >= b.length) {
                System.out.print("b not long enough");

            }

            sum[i] = a[i] + b[i];
        }
        return sum;
    }

    double cross_entropy(double[] y, double[] t) {
        //"" "The cross entropy between y and t.

        //:param y:the output produced by the model (vector)
        //:param t:the target, or actual output (vector)
        //:returns:
        //cross entropy between y and t
        //:rtype:float

        //"" "


        double result = 0;


        double test = 0;

        double[] buffer = new double[t.length];
        double[] buffer2 = new double[t.length];
        double[] buffer3 = new double[t.length];


        for (int i = 0; i < y.length; i++) {

            buffer[i] += Math.log(y[i]);
            buffer2[i] += 1 - t[i];
            buffer3[i] += Math.log(1 - y[i]);

        }
        double term1 = dotproduct(t, buffer);

        result = -(term1 + dotproduct(buffer2, buffer3));

        return result;

    }


    public static double dotproduct(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++)
            sum += a[i] * b[i];
        return sum;
    }


    double[] product(double[] a, double[][] b) {

        //todo test
        if (a.length == 0) return new double[0];
        if (b.length != a.length) {
            System.out.println("length error");
            return null;  //todo check   ; think ok
        }

        double[] product = new double[b[0].length];


        for (int j = 0; j < b[0].length; j++) {

            for (int i = 0; i < a.length; i++) {
                //System.out.print(i+" i ") ;
                product[j] += a[i] * b[i][j];
            }

        }
        return product;
    }


    public static double[][] product(double a[][], double b[][]) {//a[m][n], b[n][p]
        if (a.length == 0) return new double[0][0];
        if (a[0].length != b.length) return null; //invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;

        double ans[][] = new double[m][p];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }


}
