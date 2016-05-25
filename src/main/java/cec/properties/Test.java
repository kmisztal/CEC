package cec.properties;

import java.util.Map.Entry;

import cec.cluster.types.TypeOption;
import cec.cluster.types.TypeOptions;

public class Test {

	public static void main(String[] args) {

		//create CECProperties instance and set it's default filepath;  choose if willing to preserve comments
		CECProperties props = new CECProperties("cecconfig.properties",true);

		//get some general settings using 'GeneralSettings' enum
		System.out.println(props.getGeneralSettings().get(GeneralSettings.GENERAL_INPUT_FILENAME));
		System.out.println(props.getGeneralSettings().get(GeneralSettings.GENERAL_ITERATIONS));
		System.out.println();

		//get particular density settings using 'Densities' enum
		System.out.println(props.getDensities().get(Densities.Gaussian));

		//list through all of settings
		for (Entry<Densities, DensityWrapper> pair : props.getDensities().entrySet()) {
			System.out.println("Enum key: " + pair.getKey());
			System.out.println(pair.getValue());
		}

		//make some changes in properties
		props.getGeneralSettings().put(GeneralSettings.GENERAL_INPUT_FILENAME, "new_name_of_file");
		props.getGeneralSettings().put(GeneralSettings.GENERAL_ITERATIONS, Integer.toString(8000) );

		DensityWrapper gaussianWrapper = props.getDensities().get(Densities.Gaussian);
		gaussianWrapper.setRepeat(180);
		TypeOptions tOptions = gaussianWrapper.getTypeOptions();
		tOptions.put("beta", 2010);
		gaussianWrapper.setTypeOptions(tOptions);

		//comments won't be preserved correctly if you add TypeOptions to wrapper that didn't exist in the loaded config file
		//tOptions.put("new_element", "will_destroy_comments");

		//save content to a specific file
		props.save("cecconfig-result-test.properties", true);

		//you can also create brand new Properties set
		props = new CECProperties();
		props.getGeneralSettings().put(GeneralSettings.GENERAL_NSTART, Integer.toString(28));
		tOptions = new TypeOptions(new TypeOption[]{
				TypeOption.add("one", "sth"),
				new TypeOption("two", "anything")
		});
		//make sure the 'type' of DensityWrapper fits the 'Densities' enum (or you will get errors while reading the file back)
		props.getDensities().put(Densities.Gaussian, new DensityWrapper("Gaussian", 5, tOptions));
		props.save("cecconfig-from-empty-set.properties.",true);
	}
}
