library("cluster")

fromlodproberes <- function(x) {
    m <- read.csv(sep=",",x)
    # Interpret LODprobe output
    row.names(m) <- m[,1]
    # Remember count col
    counts <- m[2:2]
    counts <- as.matrix(counts)
    # Cut off meta
    m <- m[3:length(m)]
    # names(m) <- row.names(m)
    names(m) <- 0:(ncol(m)-1)
    #m[,1] <- 0
    #m[as.matrix(m)=='-'] <- 0
    #m[as.matrix(m)=='E'] <- 0
    # Convert to numeric matrix
    m <- as.matrix(m)
    # Write remembered counts into diagonale
    m[row(m) == col(m)] <- counts[row(m)]
    
    mode(m) <- "numeric"
    require("gdata")
    lowerTriangle(m) <- lowerTriangle(t(as.matrix(m)))
    
    #m[is.na(m)] <- 0
    
    return (as.matrix(m))
}

writepng <- function(x) {
    require(graphics)
	png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, "_dendrogram",".png", sep = ""))
	plot(hclust(daisy(fromlodproberes(x), metric="gower"), method="complete"), cex=0.5)
	dev.off()
    
    png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, "_dendrogram_h02",".png", sep = ""))
    hc <- hclust(daisy(fromlodproberes(x), metric="gower"), method="complete")
    plot(hclust(daisy(fromlodproberes(x), metric="gower"), method="complete"), cex=0.5)
    rect.hclust(hc, h=0.2)
    dev.off()
    
    png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, "_dendrogram_h02",".png", sep = ""))
    hc <- hclust(daisy(fromlodproberes(x), metric="gower"), method="complete")
    plot(hclust(daisy(fromlodproberes(x), metric="gower"), method="complete"), cex=0.5)
    rect.hclust(hc, h=0.2)
    dev.off()
}
