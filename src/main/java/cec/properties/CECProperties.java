package cec.properties;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cec.cluster.types.TypeOption;
import cec.cluster.types.TypeOptions;

public class CECProperties {

	private String filepath;
	private int lineNumber = -1;
	private LinkedHashMap<Densities,DensityWrapper> densities = new LinkedHashMap<>();
	private LinkedHashMap<Integer, String> comments = new LinkedHashMap<>();
	private LinkedHashMap<GeneralSettings, String> generals = new LinkedHashMap<>();

	public HashMap<Densities,DensityWrapper> getDensities(){
		return densities;
	}

	public LinkedHashMap<GeneralSettings, String> getGeneralSettings() {
		return generals;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public CECProperties(){}

	public CECProperties(String pathToFile, boolean preserveComments){
		filepath = pathToFile;
		load(pathToFile,preserveComments);
	}

	//load properties into list of DensityWrapper class
	public void load(String pathToFile, boolean preserveComments){

		try(Scanner sc = new Scanner(new FileInputStream(pathToFile))){
			String next = "";

			while(sc.hasNext()){
				next = sc.nextLine();
				lineNumber++;

				if(next.startsWith("#")){
					if(preserveComments){
					comments.put(lineNumber, next);
					}
					continue;
				}

				if(next.isEmpty()){
					if(!sc.hasNext()){
						break;
					}
				} else  {
					if(next.matches("general.+")){
						String[] keyValue = next.split("=");
						try {
							SetGeneral(keyValue[0], keyValue[1]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else if (next.matches("type.+")){
						String[] keyValue = next.split("=");
						try {
							SetDensity(sc, keyValue[1], preserveComments);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void save(boolean includeComments){

		save(filepath,includeComments);
	}

	public void save(String pathToFile, boolean includeComments){

		try(BufferedWriter fs = new BufferedWriter(new FileWriter(pathToFile))){

			if(includeComments && comments.size() != 0){
				Iterator<Integer> comms = comments.keySet().iterator();
				save(fs, true, comms);
			}else {
				save(fs, false, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void save(BufferedWriter bw, boolean saveComments, Iterator<Integer> comms) throws IOException{

		lineNumber = 0;
		CommentResponseValues values = new CommentResponseValues(0,true,"");

		if(saveComments){
			for (Entry<GeneralSettings, String> gen : generals.entrySet()) {
				saveCommentsIfNeeded(bw,comms,values);
				saveGenerals(bw, gen);
			}
			bw.newLine();
			lineNumber++;

			int count = densities.values().size();
			int curr = 0;
			for(DensityWrapper density: densities.values()){
				saveDensities(bw, density, comms, values);
				curr++;

				if(curr < count){
					bw.newLine();
					lineNumber++;
				}
			}

			while(!values.isCommentSaved || comms.hasNext()){
				saveCommentsIfNeeded(bw, comms, values);
				bw.newLine();
				lineNumber++;
			}

		}else {
			for (Entry<GeneralSettings, String> gen : generals.entrySet()) {
				saveGenerals(bw, gen);
			}
			bw.newLine();
			lineNumber++;

			for(DensityWrapper density: densities.values()){
				bw.write(density.toString());
				bw.newLine();
				lineNumber++;
			}
		}
	}

	private void saveGenerals(BufferedWriter bw, Entry<GeneralSettings, String> gen) throws IOException{

		bw.write(gen.getKey().getMappedValue()+"="+gen.getValue()+"\n");
		lineNumber++;
	}

	private void saveDensities(BufferedWriter bw, DensityWrapper density, Iterator<Integer> comms, CommentResponseValues values) throws IOException{

		saveCommentsIfNeeded(bw, comms, values);
		bw.write("type="+density.getType()+"\n");
		lineNumber++;

		saveCommentsIfNeeded(bw, comms, values);
		bw.write("repeat="+density.getRepeat()+"\n");
		lineNumber++;

		for (Entry<String, Object> option : density.getTypeOptions().entrySet()) {

			saveCommentsIfNeeded(bw, comms, values);
			if(option.getValue() instanceof int[]){
				String byComma = Arrays.stream((int[])option.getValue())
						.mapToObj(Integer::toString)
						.collect(Collectors.joining(","));
				bw.write(option.getKey()+"="+byComma+"\n");
				lineNumber++;
			}else {
				bw.write(option.getKey()+"="+option.getValue()+"\n");
				lineNumber++;
			}
		}
	}

	private void saveCommentsIfNeeded(BufferedWriter bw,Iterator<Integer> comms, CommentResponseValues values) throws IOException{

		while(true){
			if(comms.hasNext() && values.isCommentSaved){
				values.nextCommentNumber = comms.next();
				values.nextComment = comments.get(values.nextCommentNumber);
				values.isCommentSaved = false;
			}
			if(lineNumber == values.nextCommentNumber){
				bw.write(values.nextComment+"\n");
				lineNumber++;
				values.isCommentSaved =true;

				if(comments.containsKey(values.nextCommentNumber + 1)){
					continue;
				}else {
					break;
				}
			}else {
				if(lineNumber > values.nextCommentNumber){
					values.isCommentSaved = true;
					comms = Collections.emptyIterator();
				}
				break;
			}
		}
	}

	private void SetGeneral(String key, String value) throws Exception{

			switch (key) {
			case "general.cores":
				generals.put(GeneralSettings.GENERAL_CORES, value);
				break;
			case "general.nstart":
				generals.put(GeneralSettings.GENERAL_NSTART, value);
				break;
			case "general.iterations":
				generals.put(GeneralSettings.GENERAL_ITERATIONS, value);
				break;
			case "general.input.filename":
				generals.put(GeneralSettings.GENERAL_INPUT_FILENAME, value);
				break;
			case "general.input.type":
				generals.put(GeneralSettings.GENERAL_INPUT_TYPE, value);
				break;
			case "general.output.directory":
				generals.put(GeneralSettings.GENERAL_OUTPUT_DIRECTORY, value);
				break;
			case "general.output.filename":
				generals.put(GeneralSettings.GENERAL_OUTPUT_FILENAME, value);
				break;
			default:
				throw new Exception("Invalid parameter: "+ key+": "+value);
			}
	}

	private void SetDensity(Scanner sc, String key, boolean preserveComments) throws IllegalArgumentException, Exception{

		String repeat = "";
		boolean found = false;
		ArrayList<TypeOption> options = new ArrayList<>();

		//check for 'repeat'
		while(sc.hasNext()){
			repeat = sc.nextLine();
			lineNumber++;

			if(repeat.startsWith("#")){
				if(preserveComments){
					comments.put(lineNumber, repeat);
				}
				continue;
			}else if(!repeat.isEmpty()){
				found = true;
				break;
			}else{
				throw new Exception("Unexpected new line after 'type': "
				+ key +" declaration. 'repeat' argument required.");
			}
		}
		if(!found)
			throw new Exception("Wrong sequence found. Expected argument 'repeat' not found.");

		//collect info about parameters
		if(repeat.matches("repeat.+")){
			String[] keyValue = repeat.split("=");
			String next = "";

			while(sc.hasNext()){
			next = sc.nextLine();
			lineNumber++;

			if(next.isEmpty()){
				break;
			}

			if(next.startsWith("#")){
				if(preserveComments){
					comments.put(lineNumber, next);
				}
				if(!sc.hasNext()){
					break;
				}
			} else  {
					String[] nextPair = next.split("=");

					if(nextPair != null && nextPair.length == 2){
						//check for multiple parameters
						String[] multiValue = nextPair[1].split(",");

						if(multiValue != null){
							if(multiValue.length > 1){
								int[] mValues = Arrays.stream(multiValue).mapToInt(s->Integer.parseInt(s)).toArray();
								options.add(new TypeOption(nextPair[0], mValues));
							}
							else {
								options.add(new TypeOption(nextPair[0], Integer.parseInt(multiValue[0])));
							}
						}
					}else {
						throw new Exception("Wrong parameter format. Expected: name=value[,value2,...]");
					}
				}
			}
			Object[] opts = options.toArray();
			try{
			densities.put(Densities.valueOf(key) ,new DensityWrapper(key, Integer.parseInt(keyValue[1]),
					new TypeOptions(Arrays.copyOf(opts, opts.length, TypeOption[].class))));
			}
			catch (IllegalArgumentException ex){
				throw new IllegalArgumentException("Wartoœc: '" + key + "' nie zdefiniowana w typie wyliczeniowym 'Densities'.");
			}
		}
		else {
			throw new Exception("Expected 'repeat' for the type: "
					+ key + " not found (current occurence: {"
					+ repeat.split("=")== null? "empty":(repeat.split("=")[0])+"}");
		}
	}

	//helper class for saving operation
	private class CommentResponseValues{

		public int nextCommentNumber;
		public boolean isCommentSaved;
		public String nextComment;

		public CommentResponseValues(int numb, boolean saved, String comment){
			nextCommentNumber = numb;
			isCommentSaved = saved;
			nextComment = comment;
		}
	}
}




