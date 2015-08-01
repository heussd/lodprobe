package com.github.heussd.lodprobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class PropertyMatrix {
	private static final String DELIM = ",";
	List<Resource> properties = new ArrayList<Resource>();
	Map<Resource, Integer> propertyCount = new HashMap<Resource, Integer>();
	String[][] crossCompare = null;
	private Random random = null;

	public void add(Resource resource, String count) {
		properties.add(resource);
		propertyCount.put(resource, new Integer(count));
	}

	public Resource get(int i) {
		return properties.get(i);
	}

	public int size() {
		return properties.size();
	}

	public void put(int x, int y, String value) {
		if (crossCompare == null) {
			crossCompare = new String[size()][size()];
		}
		crossCompare[x][y] = value;
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < size(); i++) {
			// Print heading

			if (i == 0) {
				string += DELIM + "Count";
				for (int j = 0; j < size(); j++) {
					string += DELIM + "[" + j + "]";
				}
				string += "\n";
			}

			string += "[" + i + "] " + get(i).getURI() + DELIM + propertyCount.get(get(i));
			for (int j = 0; j < size(); j++) {
				string += DELIM + crossCompare[i][j];
			}
			string += "\n";
		}
		return string;
	}

	public void randomize() {
		if (random == null)
			random = new Random();

		for (int row = 0; row < size(); row++) {
			for (int column = 0; column < size(); column++) {
				if (column > row) {
					put(row, column, "" + Math.round(propertyCount.get(get(row)) * random.nextFloat()));
				}
			}
		}
	}

	/**
	 * This is an add-variant that generates random property-counts. This is
	 * important for the simulation of heterogenic data.
	 * 
	 * @param string
	 * @param norm
	 */
	public void add(String string, float norm) {
		if (random == null)
			random = new Random();

		add((Resource) new ResourceImpl(string), "" + Math.round(norm * random.nextFloat()));
	}
}
