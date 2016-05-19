package cec.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


public class CECProperties {

	private Properties applicationProps;

	public CECProperties(boolean useDefault) {

		Properties defaultProps = new Properties();

		if(!useDefault){
			try(FileInputStream in = new FileInputStream("cecconfig_default.properties")){
				defaultProps.load(in);

				// create application properties with default
				applicationProps = new Properties(defaultProps);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
				applicationProps = new Properties();
		}
			// now load properties from last invocation
			try(FileInputStream im = new FileInputStream("cecconfig.properties")){
			applicationProps.load(im);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setDensityGaussian(int numer, HashMap<String, Integer> lambdaValues, int repeatTimes) {

		if(lambdaValues != null){
		applicationProps.setProperty("density."+numer+".lambda1", lambdaValues.get("lambda1").toString());
		applicationProps.setProperty("density."+numer+".lambda2", lambdaValues.get("lambda2").toString());
		}

		applicationProps.setProperty("density."+numer+".repeat", Integer.toString(repeatTimes));
	}

	public String getDensityGaussian (int numer, String value){

		return applicationProps.getProperty("density."+numer +"."+ value);
	}

	public void setGeneral(GeneralSettings settings, String value){

		applicationProps.setProperty(settings.getParameter(), value);
	}

	public String getGeneral(GeneralSettings settings, String value){

		return applicationProps.getProperty(settings.getParameter(), "");
	}

	public void saveChanges(){

			try(FileOutputStream out = new FileOutputStream("cecconfig.properties")){
					applicationProps.store(out,"");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}




