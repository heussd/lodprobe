# LODprobe
... is a simple tool that fires serveral SPARQL-queries to find out basic characteristics of the data loaded in the default graph.

## Characteristics
LODprobe extracts quantities about the following characteristics of a given default graph:

1. The number of distinct subjects.
2. The number of occurences of each property.
3. The number of co-occurences of two properties, for each property.

The results are stored in a CSV file, containing a `number of properties`x`number of properties`-Matrix with the number of co-occurences.

## Technical stuff
LODprobe is designed to work with a local [Apache Fuseki](http://jena.apache.org/documentation/serving_data/) installation.

## Sample output
LODprobe's output of the [New York Times LOD People dataset](http://data.nytimes.com/people.rdf):

Number of unique subjects: 9958|Count|[0]|[1]|[2]|[3]|[4]|[5]|[6]|[7]|[8]|[9]|[10]|[11]|[12]|[13]|[14]|[15]|[16]|[17]|[18]|[19]
----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|
[0] http://creativecommons.org/ns#attributionName|4979|-|4979|4979|0|0|0|4979|0|0|0|4979|4979|4979|4979|0|0|0|0|0|4979
[1] http://creativecommons.org/ns#attributionURL|4979|-|-|4979|0|0|0|4979|0|0|0|4979|4979|4979|4979|0|0|0|0|0|4979
[2] http://creativecommons.org/ns#license|4979|-|-|-|0|0|0|4979|0|0|0|4979|4979|4979|4979|0|0|0|0|0|4979
[3] http://data.nytimes.com/elements/associated\_article_count|4979|-|-|-|-|4281|4281|0|4979|4979|4480|0|0|0|0|4979|4979|885|4979|4979|0
[4] http://data.nytimes.com/elements/first_use|4281|-|-|-|-|-|4281|0|4281|4281|3884|0|0|0|0|4281|4281|878|4281|4281|0
[5] http://data.nytimes.com/elements/latest_use|4281|-|-|-|-|-|-|0|4281|4281|3884|0|0|0|0|4281|4281|878|4281|4281|0
[6] http://data.nytimes.com/elements/mapping_strategy|4979|-|-|-|-|-|-|-|0|0|0|4979|4979|4979|4979|0|0|0|0|0|4979
[7] http://data.nytimes.com/elements/number\_of_variants|4979|-|-|-|-|-|-|-|-|4979|4480|0|0|0|0|4979|4979|885|4979|4979|0
[8] http://data.nytimes.com/elements/search\_api_query|4979|-|-|-|-|-|-|-|-|-|4480|0|0|0|0|4979|4979|885|4979|4979|0
[9] http://data.nytimes.com/elements/topicPage|4480|-|-|-|-|-|-|-|-|-|-|0|0|0|0|4480|4480|885|4480|4480|0
[10] http://purl.org/dc/elements/1.1/creator|4979|-|-|-|-|-|-|-|-|-|-|-|4979|4979|4979|0|0|0|0|0|4979
[11] http://purl.org/dc/terms/created|4979|-|-|-|-|-|-|-|-|-|-|-|-|4979|4979|0|0|0|0|0|4979
[12] http://purl.org/dc/terms/modified|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|4979|0|0|0|0|0|4979
[13] http://purl.org/dc/terms/rightsHolder|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|-|0|0|0|0|0|4979
[14] http://www.w3.org/1999/02/22-rdf-syntax-ns#type|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|4979|885|4979|4979|0
[15] http://www.w3.org/2002/07/owl#sameAs|14884|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|885|4979|4979|0
[16] http://www.w3.org/2004/02/skos/core#definition|885|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|885|885|0
[17] http://www.w3.org/2004/02/skos/core#inScheme|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|4979|0
[18] http://www.w3.org/2004/02/skos/core#prefLabel|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|0
[19] http://xmlns.com/foaf/0.1/primaryTopic|4979|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-

## Analysis using R
Further analysis of LODprobe's CSV files can be done via R.

### Required libraries
	# daisy
	library(cluster)
	
### Reading LODprobe CSV
The function [fromlodproberes()](fromlodproberes.R) allows to read a generated LODprobe CSV and converts it into a (symmetric) matrix object.

	matrix <- fromlodproberes("people.csv")

### Collecting property counts

[writepropertycounts()](writepropertycounts.R) collects `property`-counts in a single CSV. Designed to be called when iterating over a number of LODprobe results.

	writepropertycounts("people.csv")
	
### Cluster Analysis

[writeclustprops()](writeclustprops.R) creates a cluster analysis of a given LODprobe CSV and extracts several metrics. Designed to be called when iterating over a number of LODprobe results.

	writeclustprops("people.csv")
	
In addition, [writepng()](writepng.R) prints the cluster analysis as image file (Dendrogram).

	writepng("people.csv")