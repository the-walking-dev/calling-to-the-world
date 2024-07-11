compile:
	@ javac -cp src/:lib/tdlib.jar -d bin/ src/Main.java

clean:
	@ rm -rf bin/ tdlib/

package:
	@ docker build --file build/Dockerfile --tag multitenant-telegram-poc .

run:
	@ [ -f .env ] && export $(grep -v '^#' .env | xargs -d '\n') > /dev/null ; java -cp bin/:lib/tdlib.jar -Djava.library.path=lib/ Main
