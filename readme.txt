Readme

1. Compiling
   To compile the apllication, following are the steps:
	a. Go to the src folder
		cd src
	
	b. A Makefile has been created for compiling the program.
	   The below command will compile and make Java classes.
		make

	c. To clean the src directory of java classes, type
		make clean

2. Running the Application

   The configurable parameters for this applciation are 'K' and 's' seconds.

   To change these parameters before running, we need to edit the run.sh file
   inside src folder.
   The command
	java -classpath ".:twitter4j_jars/*" MainClassTwitterStreaming 15 5
   assumes the first parameter as the number of seconds (or sliding window)
   the program will wait before displaying the top hashtags. The seconds 
   parameter is for configuring 'K'. This will change the number of top-K
   hashtags displayed. E.g. In above command, 15 is number of seconds the
   program wil wait before displaying '5' = 'K' top hashtags.

   To run the application after compilation, run the following commands:
	chmod u+x run.sh
	./run.sh


3. Expected Output

   After running the above command to run the application, the program will
   first show it is connecting to twitter stream. After that, the program
   will wait for number of seconds that were supplied in arguments before
   displaying any top hashtags.

