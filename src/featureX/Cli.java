package featureX;

/**
 * Created by dorien on 29/06/16.
 */
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();


    //setting default values
    public int windowsPerBar = 4;
    public int meterUnits = 4;
    public String inputfile = "/home/dorien/workspace/PMusicOR/data/Haydn_half.xml";
    public int windowLength = 0;
    public String outputwindow;
    public String inputwindow;
    public String key = "C";
    public int run;
    public int iters = 1;

    public String runtype;

    public Cli(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("inputfile", "var", true, "Input musicXML file.");
        options.addOption("windowsPerBar", "var", true, "The number of windows per bar used to calculate the tension. (depreciated)");
        options.addOption("windowLength", "var", true, "Length of the windows expressed as 4 (quarter note), 8, 16, etc. (This setting has preference over windowsPerBar.) Default value is 1 eight note.");
        options.addOption("meterUnits", "var", true, "option only used to change the inscore rendering: number of units of meter. Default value is 4.");
        options.addOption("inputwindow", "var", true, "First slice (in midi notes seperated by space");
        options.addOption("outputwindow", "var", true, "Second slice (in midi notes seperated by space");
        options.addOption("runtype", "var", true, "Type of run, just a reference for output labeling");
        options.addOption("key", "var", true, "C or A (is Aminor), this only works on the deepmodel version, not the regular one");
        options.addOption("run", "var", true, "runID");
        options.addOption("iters", "var", true, "iters");

    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("t"))
                help();

            if (cmd.hasOption("inputfile")) {
                log.log(Level.INFO, "Using argument -inputfile=" + cmd.getOptionValue("inputfile"));
                inputfile = cmd.getOptionValue("inputfile");

                // Whatever you want to do with the setting goes here
            } else {
                log.log(Level.SEVERE, "Missing -inputfile option");
                help();
            }


            if (cmd.hasOption("windowsPerBar")) {
                log.log(Level.INFO, "Using argument -windowsPerBar=" + cmd.getOptionValue("windowsPerBar"));
                windowsPerBar = Integer.valueOf(cmd.getOptionValue("windowsPerBar"));


                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowsPer option");
                //help();
            }

            if (cmd.hasOption("windowLength")) {
                log.log(Level.INFO, "Using argument -windowLength=" + cmd.getOptionValue("windowLength"));
                String option = cmd.getOptionValue("windowLength");
                windowLength = Integer.valueOf(option);
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }


            if (cmd.hasOption("run")) {
                log.log(Level.INFO, "Using argument -run=" + cmd.getOptionValue("run"));
                String option = cmd.getOptionValue("run");
                run = Integer.valueOf(option);
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }


            if (cmd.hasOption("inputwindow")) {
                log.log(Level.INFO, "Using argument -inputwindow=" + cmd.getOptionValue("inputwindow"));
                inputwindow = cmd.getOptionValue("inputwindow");
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }

            if (cmd.hasOption("runtype")) {
                log.log(Level.INFO, "Using argument -runtype=" + cmd.getOptionValue("runtype"));
                runtype = cmd.getOptionValue("runtype");
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }


            if (cmd.hasOption("key")) {
                log.log(Level.INFO, "Using argument -key=" + cmd.getOptionValue("key"));
                key = cmd.getOptionValue("key");
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }


            if (cmd.hasOption("iters")) {
                log.log(Level.INFO, "Using argument -iters=" + cmd.getOptionValue("iters"));
                String option = cmd.getOptionValue("windowLength");
                iters = Integer.valueOf(option);

                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }


            if (cmd.hasOption("outputwindow")) {
                log.log(Level.INFO, "Using argument -outputwindow=" + cmd.getOptionValue("outputwindow"));
                outputwindow = cmd.getOptionValue("outputwindow");
                //windowLength = Integer.valueOf(option);
                //windowLength = Integer.getInteger(option);

                int test = 0;
                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }

            if (cmd.hasOption("meterUnits")) {
                log.log(Level.INFO, "Using argument -meterUnits=" + cmd.getOptionValue("meterUnits"));
                meterUnits = Integer.valueOf(cmd.getOptionValue("meterUnits"));


                // Whatever you want to do with the setting goes here
            } else {
                //log.log(Level.SEVERE, "MIssing windowLength option");
                //help();
            }



        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties", e);
            help();
        }


    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }
}
