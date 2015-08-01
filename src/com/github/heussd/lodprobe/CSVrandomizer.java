package com.github.heussd.lodprobe;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVrandomizer {

	public static void main(String[] args) throws Exception {
		String dataset = "linkedmdb";
		int numberofprobes = 30000;
		float maxSubjects = 694400;

		Reader in = new FileReader("/Users/th/Public/GitHub/CKANstats/datahub.io/lodprobe-results/" + dataset + ".csv");
		Iterable<CSVRecord> records = new CSVParser(in, CSVFormat.RFC4180.withHeader());
		ArrayList<String> subjects = new ArrayList<>();

		for (CSVRecord record : records) {
			subjects.add(record.get(0).substring(6));
		}

		for (int i = 0; i < numberofprobes; i++) {
			PropertyMatrix matrix = new PropertyMatrix();
			for (String subject : subjects)
				matrix.add(subject, maxSubjects);

			matrix.randomize();
			PrintWriter out = new PrintWriter("random/" + dataset + i + ".csv");
			out.println(matrix.toString());
			out.close();

			if (i % 100 == 0) {
				System.out.println("Processing... " + i + "/" + numberofprobes);
			}
		}

	}
}
