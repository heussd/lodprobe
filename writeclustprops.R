writeclustprops <- function(x) {
	m <- read.csv(sep=",",x)
	totalsubjects <- sub(".*subjects..","",names(m[1:1]))

	d <- daisy(fromlodproberes(x), metric="gower")
	maxh <- max(d)
	minh <- min(d)
	totalproperties <- attr(d, "Size")
	if (totalproperties >= 2) {
		# cluster analysis does only work with n >= 2
	clustersh01 <- max(cutree(hclust(d, method="complete"), h=0.1))
	clustersh02 <- max(cutree(hclust(d, method="complete"), h=0.2))
	clustersh03 <- max(cutree(hclust(d, method="complete"), h=0.3))
	clustersh04 <- max(cutree(hclust(d, method="complete"), h=0.4))
	} else {
		clustersh01 <- clustersh02 <- clustersh03 <- clustersh04 <- 0
	}
	write.table(col.names=FALSE, matrix(c(x, totalsubjects, minh, maxh, totalproperties,clustersh01,clustersh02,clustersh03,clustersh04),ncol=9),append=TRUE, "../clusteranalysis.csv", sep=",", row.names=F)
}