package cec.options;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import nl.chess.it.util.config.Config;
import nl.chess.it.util.config.ConfigValidationResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Krzysztof
 */
public class CECConfig extends Config {
    private static CECConfig singleton;
    /**
     * Name of the file we are looking for to read the configuration.
     */
    private static final String RESOURCE_NAME = "cecconfig.properties";
    private static Options densities;

    /**
     * Creates a new CECConfig object.
     * check if the are same mistakes in file
     *
     * @param resourceName
     * @throws java.io.IOException
     */
    private CECConfig(String resourceName, String[] args) throws IOException {
        super(read(resourceName, args));

        ConfigValidationResult configResult = this.validateConfiguration();

        if (configResult.thereAreErrors()) {
            System.out.println("Errors in configuration");

            for (Object o : configResult.getErrors()) {
                System.out.println(" > " + o);
            }

            System.exit(1);
        }


    }

    private static Properties read(final String filename, String[] args) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        overwriteProperties(prop, args);
        return prop;
    }

    private static void overwriteProperties(Properties prop, String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        argsMap.put("cores", "general.cores");
        argsMap.put("nstart", "general.nstart");
        argsMap.put("iterations", "general.iterations");
        argsMap.put("infile", "general.input.filename");
        argsMap.put("intype", "general.input.type");
        argsMap.put("outdir", "general.output.directory");
        argsMap.put("outfile", "general.output.filename");

        OptionParser optionParser = new OptionParser();

        for (String availableArg : argsMap.keySet())
            optionParser.accepts(availableArg).withRequiredArg();

        OptionSet options = optionParser.parse(args);

        for (Map.Entry<String, String> entry : argsMap.entrySet()) {
            if (options.has(entry.getKey())){
                prop.setProperty(entry.getValue(), options.valueOf(entry.getKey()).toString());
            }
        }
    }

    public int getGeneralCores() {
        return getInt("general.cores");
    }

    public int getNStart() {
        return getInt("general.nstart");
    }

    public int getIterations() {
        return getInt("general.iterations");
    }

    public String getGeneralOutputDirectory() {
        return getString("general.output.directory");
    }

    public String getGeneralOutputFilename() {
        return getString("general.output.filename");
    }

    public String getGeneralInputFilename() {
        return getString("general.input.filename");
    }

    public String getGeneralInputFileType() {
        return getString("general.input.type");
    }

    public static void main(String[] args) throws IOException {
        CECConfig op = getInstance(null, null);
        System.out.println(densities);
    }

    private static void load() {
        ConfigValidationResult configResult = singleton.validateConfiguration();

        if (configResult.thereAreUnusedProperties()) {
//            System.out.println("Unused properties");
            densities = Options.getInstance();
            for (Object o : configResult.getUnusedProperties()) {
                final String name = (String) o;
//                System.out.println(" > " + name + " " + config.getString(name));
                final int beg = name.indexOf(".", 0);
                final int end = name.indexOf(".", beg + 1);
                final String type = name.substring(beg + 1, end);
                densities.addValue(type, name.substring(end + 1), singleton.getString(name));
            }
        } else {
            System.out.println("Please complete properties file");
        }
    }

    public static CECConfig getInstance(String file, String[] args) throws IOException {
        if (singleton == null) {
            CECConfig.singleton = new CECConfig(file == null ? RESOURCE_NAME : file, args);
            load();
        }
        return singleton;
    }

    private void argsOverwrite(String[] args) {

    }
}
