# calling-to-the-world

A proof of concept about create two Telegram client instances (TDLib clients) in the same process.

## Requisitos 

  * Java 21
  * Make (opcional)
  * Docker (optional)

## TL;DR

Build and run the project, using `make`:

```
make && make run
```

Send the authentication token to telegram:

```bash
# send code to tenant1
curl -v localhost:8090/tenant1=[auth-code]

# send code to tenant2
curl -v localhost:8090/tenant1=[auth-code]
```

## Compiling

```bash
javac -cp src/:lib/tdlib.jar -d bin/ src/Main.java
```

With `make`:

```bash
make
```

> `make clean` removes all generated artifacts (class files, TDLib database, ...)

## Running

```bash
java -cp bin/:lib/tdlib.jar -Djava.library.path=lib/ Main
```

With `make`:

```bash
make run
```

> If `.env` file exists, the run target will source it

## Building and running a container

```bash
docker build --file build/Dockerfile --tag multitenant-telegram-poc .
```

There is a target to ease build the docker container:

```bash
make package
```

It creates a container called **multitenant-telegram-poc**.

To run the container:

```bash
docker run \
  --env-file=.env \
  --publish 8090:8091 \
   --net=host \
  -ti multitenant-telegram-poc
```

### Sending the Telegram authentication code

When the application starts it will login to Telegram, and Telegram sends a code to the mobile. To finish the authentication process, we have to send this auth code.

The application is listening HTTP on port **8090** to receive the Telegram auth codes.

```
# Send code to tenant1
curl -v localhost:8090/tenant1=<code1> 

# Send code to tenant2
curl -v localhost:8090/tenant2=<code1> 
```

## Environment variables

The tenant credentials are recovered from ennvironment variables. You can setup the values in a `.env` file.

> Do not push this file to repo, because it contains credentials.

| environment variable     | description                                |
|--------------------------|--------------------------------------------|
| TENANT1_API_ID           | First tenant client instance ID            |
| TENANT1_API_HASH         | First tenant client instance hash          |
| TENANT1_API_PHONE_NUMBER | First tenant client instance phone number  |
| TENANT2_API_ID           | Second tenant client instance ID           |
| TENANT2_API_HASH         | Second tenant client instance hash         |
| TENANT2_API_PHONE_NUMBER | Second tenant client instance phone number |
