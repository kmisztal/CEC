package cec.properties;

public enum GeneralSettings {

	GENERAL_CORES("general.cores"),
	GENERAL_NSTART("general.nstart"),
	GENERAL_ITERATIONS("general.iterations"),
	GENERAL_INPUT_FILENAME("general.input.filename"),
	GENERAL_INPUT_TYPE("general.input.type"),
	GENERAL_OUTPUT_DIRECTORY("general.output.directory"),
	GENERAL_OUTPUT_FILENAME("general.output.filename");

	private String mappedValue;

	GeneralSettings(){}

	GeneralSettings(String mapping){
		mappedValue = mapping;
	}

	public String getMappedValue(){
		return mappedValue;
	}
}
