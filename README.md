# RecargaPay Users API


* [Overview](#overview)
* [Deployment Overview](#deployment-overview)
* [Docker](#docker)

## Overview
Nombre      |  URL                        | Requiere autenticación
----------|-----------------------------|-------------
Sign up usuarios   |  GET /user/register | No
Login usuarios | POST /user/authenticate | No
Sumar dos números | GET /add?x={x}&y={y} | Sí
Historial de llamados a los endpoints | GET /history | Sí
Logout usuarios | GET /logout | Sí

Una vez registrado el usuario se podrá autenticar a la API a través del Login, el cual si fue exitoso devolverá un token.

Para ejecutar los endpoints securizados se deberá agregar el header de autorización "Bearer token" con el token obtenido en el login.

Podés encontrar una copia de la API en [Postman](src/main/resources/challenge.postman_collection.json)

## Deployment overview

Esta es una aplicación web que está desarrollada usando Java, Spring, Postgres y Redis
- Postgres es usado para mantener la información de los usuarios registrados y el historial de operaciones.
- Redis mantiene de forma temporal los tokens jwt que fueron blacklisteados, esta estrategia permite invalidar los tokens que aún no expiraron pero que el usuario decidió desloguearse.

## Docker

La app está desarrollada para correr sobre un contenedor Docker. 
En docker-compose.yml se pueden configurar las siguientes properties:

```
db:
  - POSTGRES_USER=compose-postgres
  - POSTGRES_PASSWORD=compose-postgres
```
```
app:
    ports:
      - 8080:8080
    environment:
      - SPRING_DATASOURCE_USERNAME=compose-postgres
      - SPRING_DATASOURCE_PASSWORD=compose-postgres
```
Para poder ejecutar la aplicación usando docker-compose se debe descargar la imagen:
```
docker pull gonisebas/tenpo-challenge-gonisebas
```

Luego, para iniciarla corremos el comando (defecto corre en el puerto 8080):
 ```
  docker-compose up --build
 ```

