# calling-to-the-world

Prueba de concepto de creación de dos instancias de TDLib (clientes) en un mismo proceso.

Este proyecto 

## Requisitos 

  * Java 21
  * Make (opcional)

## Compiling

```bash
javac -cp .:lib/tdlib.jar Main.java
```

With `make`:

```bash
make
```

> `make clean` removes all generated artifacts (class files, TDLib database, ...)

## Running

```bash
java -cp .:lib/tdlib.jar -Djava.library.path=lib/ Main
```

With `make`:

```bash
make run
```

> If `.env` file exists, the run target will source it

### Sending the Telegram authentication code

The application starts a Webserver listening on port **8090** to receive the Telegram auth codes.

```
curl -v localhost:8090/tenant1=<code1> 

curl -v localhost:8090/tenant2=<code1> 
```

## Environment variables

The tenant credentials are recovered from ennvironment variables. 


| environment variable     | description                                |
|--------------------------|--------------------------------------------|
| TENANT1_API_ID           | First tenant client instance ID            |
| TENANT1_API_HASH         | First tenant client instance hash          |
| TENANT1_API_PHONE_NUMBER | First tenant client instance phone number  |
| TENANT2_API_ID           | Second tenant client instance ID           |
| TENANT2_API_HASH         | Second tenant client instance hash         |
| TENANT2_API_PHONE_NUMBER | Second tenant client instance phone number |
