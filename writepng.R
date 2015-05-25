writepng <- function(x) {
	png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, "_dendrogram",".png", sep = ""))
	plot(hclust(daisy(fromlodproberes(x), metric="gower"), method="complete"), cex=0.5)
	dev.off()
    
    png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, "_dendrogram_h03",".png", sep = ""))
    hc <- hclust(daisy(fromlodproberes(x), metric="gower"), method="complete")
    plot(hclust(daisy(fromlodproberes(x), metric="gower"), method="complete"), cex=0.5)
    rect.hclust(hc, h=0.3)
    dev.off()
}
