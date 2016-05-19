package cec.properties;

import java.util.HashMap;

public class Raz {

	public static void main(String[] args) {

		CECProperties props = new CECProperties(true);
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("lambda1", -5555555);
		hashMap.put("lambda1", -5555555);
		hashMap.put("lambda2", 800);
		props.setDensityGaussian(0, hashMap, 10);
		props.setGeneral(GeneralSettings.GENERAL_ITERATIONS, Integer.toString(2000));
		props.saveChanges();
		System.out.println("Koniec");
	}

}
