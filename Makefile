build:
	@ javac -cp .:lib/tdlib.jar Main.java

clean:
	@ rm -rf *.class tdlib/

package:
	@ docker build -t multitenant-telegram-poc .

run:
	@ [ -f .env ] && export $(grep -v '^#' .env | xargs -d '\n') > /dev/null ; java -cp .:lib/tdlib.jar -Djava.library.path=lib/ Main
