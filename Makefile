build:
	@ javac -cp .:lib/tdlib.jar Main.java

clean:
	@ rm -rf *.class tdlib/

run:
	@ [ -f .env ] && . ./.env ; java -cp .:lib/tdlib.jar -Djava.library.path=lib/ Main
