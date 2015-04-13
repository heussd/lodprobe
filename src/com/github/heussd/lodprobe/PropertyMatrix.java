package com.github.heussd.lodprobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Resource;

public class PropertyMatrix {
	private static final String DELIM = ",";
	List<Resource> properties = new ArrayList<Resource>();
	Map<Resource, Integer> propertyCount = new HashMap<Resource, Integer>();
	String[][] crossCompare = null;

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
}
