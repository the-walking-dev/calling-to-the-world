FROM eclipse-temurin:21.0.3_9-jdk AS builder
WORKDIR /usr/src/telegram-poc
ADD src/*.java ./
ADD lib/tdlib.jar ./lib/
RUN javac -cp .:lib/tdlib.jar Main.java

FROM eclipse-temurin:21.0.3_9-jre
WORKDIR /usr/lib/telegram-poc
COPY --from=builder /usr/src/telegram-poc/*.class  ./
ADD lib/tdlib.jar lib/libtdjni.so ./lib/
ENTRYPOINT ["java", "-cp", ".:lib/tdlib.jar", "-Djava.library.path=lib/", "Main"]
