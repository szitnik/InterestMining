#FUNCTION
draw <- function(As, Bs, Cs, title, dataNames, file) {
pdf(file = file, width=7, height=5)
  
pchs <- c(21,24,23)

plot(As[,1], As[,2],
     main=paste(title), 
     ylab="Precision@N", 
     xlab="Cut-off value N", 
     ylim = c(0.0, 0.9),
     xlim = c(5, 31),
     col = "red",
     type="l",
     xaxt = "n",
     yaxt = "n",
     lty = 2)
axis(1, at = c(0, 5, 10, 15, 20, 30, 40))
axis(2, at = seq(0.0, 0.9, by=0.1))
lines(Bs[,1], Bs[,2], col = "black", lty = 3)
lines(Cs[,1], Cs[,2], col = "blue", lty = 4)
legend("bottomright", dataNames, 
       col = c("red", "black", "blue"), cex = 0.80, lty = c(2,3,4), pch=pchs)

yOffset <- -0.04
xOffset <- -0.9

points(As[,1], As[,2], pch = pchs[1])
text(As[,1] + xOffset, As[,2] + yOffset, As[,2], cex=0.8, pos=4, col="black")

points(Bs[,1], Bs[,2], pch = pchs[2])
text(Bs[,1] , Bs[,2], Bs[,2], cex=0.8, pos=4, col="black")

points(Cs[,1], Cs[,2], pch = pchs[3])
text(Cs[,1] + xOffset, Cs[,2] - yOffset, Cs[,2], cex=0.8, pos=4, col="black")

dev.off()
}


#TYPE 1 - Questions - Categories
As <- matrix(c(5, 10, 15, 20, 30, 0.24, 0.4, 0.58, 0.62, 0.7), 5, 2)
Bs <- matrix(c(5, 10, 15, 20, 30, 0.29, 0.46, 0.59, 0.64, 0.74), 5, 2)
Cs <- matrix(c(5, 10, 15, 20, 30, 0.29, 0.48, 0.59, 0.71, 0.8), 5, 2)
title <- "Type 1 - Questions - CE&Cat 1,2,3 concepts"
dataNames <- c("I&I-STSS", "LinSTSS", "P2Q")
file <- "type1questions_PAtN.pdf"
draw(As, Bs, Cs, title, dataNames, file)

#TYPE 2 - Answers - Categories
As <- matrix(c(5, 10, 15, 20, 30, 0.33, 0.48, 0.59, 0.69, 0.87), 5, 2)
Bs <- matrix(c(5, 10, 15, 20, 30, 0.11, 0.11, 0.11, 0.11, 0.11), 5, 2) #TODO!!!!!!!!!
Cs <- matrix(c(5, 10, 15, 20, 30, 0.27, 0.39, 0.54, 0.63, 0.79), 5, 2)
title <- "Type 2 - Answers - Categories evidences"
dataNames <- c("I&I-STSS", "LinSTSS", "P2Q")
file <- "type2answers_PAtN.pdf"
draw(As, Bs, Cs, title, dataNames, file)

#TYPE 3 - Answers - Categories
As <- matrix(c(5, 10, 15, 20, 30, 0.23, 0.39, 0.5, 0.55, 0.64), 5, 2)
Bs <- matrix(c(5, 10, 15, 20, 30, 0.33, 0.41, 0.51, 0.59, 0.65), 5, 2) #TODO!!!!!!!!!
Cs <- matrix(c(5, 10, 15, 20, 30, 0.35, 0.46, 0.61, 0.66, 0.77), 5, 2)
title <- "Type 3 - Answers - CE&Cat 1,2,3 concepts"
dataNames <- c("I&I-STSS", "LinSTSS", "P2Q")
file <- "type3answers_PAtN.pdf"
draw(As, Bs, Cs, title, dataNames, file)
