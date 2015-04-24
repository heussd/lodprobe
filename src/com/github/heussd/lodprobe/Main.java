package com.github.heussd.lodprobe;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Resource;

public class Main {
	private final static Logger LOGGER = Logger.getLogger("lodprobe");

	private final static Query QUERY_COUNT_UNIQUE_SUBJECTS = QueryFactory.create("SELECT COUNT(distinct ?s) { ?s ?p ?o .}", Syntax.syntaxARQ);
	private final static Query QUERY_COUNT_PROPERTIES = QueryFactory.create("SELECT ?p (COUNT(?p) as ?pCount) { ?s ?p ?o . } GROUP BY ?p ORDER BY ?p", Syntax.syntaxARQ);

	public static void main(String[] args) throws Exception {
		LOGGER.info("T.H. LODprobe");
		
		System.out.print("Enter target file name (.csv will be suffixed):");
		String csvfile = System.console().readLine();
		
		System.out.print("Enter dataset name on local fuseki [DB]:");
		String datasetId = System.console().readLine();

		if (datasetId.trim().isEmpty())
			datasetId = "DB";

		// String datasetId = "people";

		LOGGER.info("Probing \"" + datasetId + "\"...");
		int subjects = getNumberOfUniqueSubjects(datasetId);

		LOGGER.info("  Number of unique subjects: " + subjects);

		// / ##########################################
		PropertyMatrix propertyMatrix = new PropertyMatrix();

		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://localhost:3030/" + datasetId + "/query", QUERY_COUNT_PROPERTIES);
		ResultSet results = queryExecution.execSelect();

		// System.out.println(ResultSetFormatter.asText(results));
		while (results.hasNext()) {
			QuerySolution row = results.next();
			Resource resource = row.getResource("p");
			String count = row.getLiteral("pCount").getString();
			LOGGER.info("  found property " + resource.toString());
			propertyMatrix.add(resource, count);

		}
		LOGGER.info(propertyMatrix.size() + " properties in total");
		String prefixes = getPrefixes(propertyMatrix);

		// System.out.print("Enter dataset name on local fuseki:");
		// datasetId = System.console().readLine();
		for (int i = 0; i < propertyMatrix.size(); i++) {
			Resource x = propertyMatrix.get(i);

			for (int j = 0; j < propertyMatrix.size(); j++) {
				if (j > i) {
					Resource y = propertyMatrix.get(j);

					LOGGER.info(x.getLocalName() + " vs. " + y.getLocalName());

					System.out.println(createVsQuery(x, y, prefixes));
					String result = "";

					try {
						QueryExecution exec = QueryExecutionFactory.sparqlService("http://localhost:3030/" + datasetId + "/query", createVsQuery(x, y, prefixes));
						ResultSet r = exec.execSelect();

						QuerySolution row = r.next();
						result = row.getLiteral(".1").getString();
					} catch (Exception e) {
						e.printStackTrace();
						result = "E";
					} finally {
						queryExecution.close();
					}
					propertyMatrix.put(i, j, result);

				} else {
					propertyMatrix.put(i, j, "-");
				}

			}
		}

		System.out.println("\n\n");
		String output = "Number of unique subjects: " + subjects + propertyMatrix;
		System.out.println(output);

		FileUtils.writeStringToFile(new File("lodprobe/" + csvfile + ".csv"), output);
	}

	private static int getNumberOfUniqueSubjects(String datasetId) {
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://localhost:3030/" + datasetId + "/query", QUERY_COUNT_UNIQUE_SUBJECTS);

		ResultSet results = queryExecution.execSelect();

		QuerySolution row = results.next();
		int numberOfUniqueSubjects = 0;
		try {
			numberOfUniqueSubjects = new Integer(row.getLiteral(".1").getString());
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve number of unique subjects", e);
		} finally {
			queryExecution.close();
		}
		// System.out.println(label.getString());

		// System.out.println(ResultSetFormatter.asText(results));
		//
		// ResultSetFormatter.out(System.out, results);
		return numberOfUniqueSubjects;
	}

	private static Query createVsQuery(Resource x, Resource y, String prefixes) {
		String query = prefixes;
		query += "select COUNT(distinct ?s) { ?s " + getNamespace(x) + ":" + escapeProperty(x.getLocalName()) + " ?o1 . ?s " + getNamespace(y) + ":" + escapeProperty(y.getLocalName()) + " ?o2 . }";

		return QueryFactory.create(query, Syntax.syntaxARQ);
	}

	private static String getNamespace(Resource resource) {
		return "NS" + Math.abs(resource.getNameSpace().toString().hashCode());
	}

	private static String getPrefixes(PropertyMatrix propertyMatrix) {
		String prefixes = "";
		for (int i = 0; i < propertyMatrix.size(); i++) {
			Resource resource = propertyMatrix.get(i);
			prefixes += "\nPREFIX " + getNamespace(resource) + ": <" + resource.getNameSpace() + ">";
		}
		return prefixes;
	}
	
	private static String escapeProperty(final String property) {
		String escapedProperty = property.replaceAll("\\.", "\\\\.");
		return escapedProperty;
	}
}
