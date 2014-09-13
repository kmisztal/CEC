package cec.options;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import nl.chess.it.util.config.Config;
import nl.chess.it.util.config.ConfigValidationResult;

/**
 *
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
    private CECConfig(String resourceName) {
        super(read(resourceName));
        
        ConfigValidationResult configResult = this.validateConfiguration();

        if (configResult.thereAreErrors()) {
            System.out.println("Errors in configuration");

            for (Iterator iter = configResult.getErrors().iterator(); iter.hasNext();) {
                System.out.println(" > " + iter.next());
            }

            System.exit(1);
        }
        
        
    }

    private static Properties read(final String filename) {
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
        return prop;
    }

    public int getGeneralCores(){
        return getInt("general.cores");
    }
    
    public int getGeneralRuns(){
        return getInt("general.runs");
    }
    
    public int getGeneralEvents(){
        return getInt("general.events");
    }
    
    public String getGeneralDataType(){
        return getString("general.data.type");
    }
    
    public int getGeneralDataDimension(){
        return getInt("general.data.dimension");
    }
    
    public String getGeneralOutputDirectory(){
        return getString("general.output.directory");
    }
    
    public String getGeneralOutputFilename(){
        return getString("general.output.filename");
    }
    
    public String getGeneralInputFilename(){
        return getString("general.input.filename");
    }
    
    public String getGeneralInputFileType(){
        return getString("general.input.type");
    }
    
    public static void main(String[] args) {
        CECConfig op = getInstance(null);
        System.out.println(op.densities);
    }
    
    private static void load(){
        ConfigValidationResult configResult = singleton.validateConfiguration();
        
        if (configResult.thereAreUnusedProperties()) {
//            System.out.println("Unused properties");
            densities = Options.getInstance();
            for (Iterator iter = configResult.getUnusedProperties().iterator(); iter.hasNext();) {
                final String name = (String) iter.next();                
//                System.out.println(" > " + name + " " + config.getString(name));
                final int beg = name.indexOf(".", 0);
                final int end = name.indexOf(".", beg+1);
                final String type = name.substring(beg+1, end);
                densities.addValue(type, name.substring(end+1), singleton.getString(name));
            }
        }else{
            System.out.println("Please complete properties file");           
        }
    }    

    public static CECConfig getInstance(String file) {
        if(singleton == null){
            CECConfig.singleton = new CECConfig(file == null ? RESOURCE_NAME : file);
            load();
        }
        return singleton;
    }    
}
