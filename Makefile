compile:
	@ javac -cp src/:lib/tdlib.jar -d bin/ src/Main.java

clean:
	@ rm -rf bin/ tdlib/

package:
	@ docker build --file build/Dockerfile --tag multitenant-telegram-poc .

run:
	@ [ -f .env ] && source .env ; java -cp bin/:lib/tdlib.jar -Djava.library.path=lib/ Main
