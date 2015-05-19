writepropertycounts <- function(x) {
	m <- read.csv(sep=",",x)
	counts <- m[1:2]
	counts <- as.matrix(counts)
	row.names(counts) <- sub(".* ", "", m[,1])
	countsdf <-  as.data.frame(counts)[2]
	countsdf[2] <- x
	write.table(sep=",",col.names=FALSE, countsdf,append=TRUE, "../counts.csv")
}