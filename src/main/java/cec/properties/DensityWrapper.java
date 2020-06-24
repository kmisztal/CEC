package cec.properties;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import cec.cluster.types.TypeOptions;

public class DensityWrapper {

	private String type;
	private int repeat;
	private TypeOptions typeOptions;
	private String options; //for displaying purposes

	public DensityWrapper(String t, int rep, TypeOptions typeOpt){
		type = t;
		repeat = rep;
		typeOptions = typeOpt;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setTypeOptions(TypeOptions typeOptions) {
		this.typeOptions = typeOptions;
	}

	public TypeOptions getTypeOptions() {
		return typeOptions;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		options = "";
		typeOptions.forEach(new BiConsumer<String, Object>() {

			@Override
			public void accept(String t, Object u) {

				if(u instanceof int[]){
					String byComma = Arrays.stream((int[])u)
						.mapToObj(Integer::toString)
						.collect(Collectors.joining(","));
					options+= t + "=" + byComma + "\n";
				}
				else {
					options+= t + "=" +u+"\n";
				}
			}
		});
		if(typeOptions.size() > 0)
			return "type=" + type + "\nrepeat=" + repeat + "\n" + options;
		else
			return "type=" + type + "\nrepeat=" + repeat + "\n";
	}
}
