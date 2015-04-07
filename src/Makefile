#Java Compiler
JC = javac
JFLAGS = -classpath
# Clear any default targets for building.
.SUFFIXES: .java .class
		
# the default make target entry
default: MainClassTwitterStreaming.class


MainClassTwitterStreaming.class: MainClassTwitterStreaming.java
	$(JC) $(JFLAGS) ".:twitter4j_jars/*" $*.java

# RM 
clean:
	$(RM) *.class

