compile:
	@ javac -cp .:lib/tdlib.jar Main.java

run:
	@ java -cp .:lib/tdlib.jar -Djava.library.path=lib/ Main

