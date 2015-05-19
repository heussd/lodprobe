writepng <- function(x) {
	d <- daisy(fromlodproberes(x), metric="gower")
	png(width = 15, height = 10, units = 'in', res = 300, paste("../png/", x, ".png", sep = ""))
	plot(hclust(d))
	# rect.hclust(hc, h=0.3)
	dev.off()
}
