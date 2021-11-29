# RecargaPay Users API


* [Overview](#overview)
* [Docker](#docker)

##Overview
Nombre      |  URL                        | Requiere autenticación
----------|-----------------------------|-------------
Sign up usuarios   |  GET /user/register | No
Login usuarios | POST /user/authenticate | No
Sumar dos números | GET /add?x={x}&y={y} | Sí
Historial de llamados a los endpoints | GET /history | Sí
Logout usuarios | GET /logout | Sí

Una vez registrado el usuario se podrá autenticar a la API a través del Login, el cual si fue exitoso devolverá un token.

Para ejecutar los endpoints securizados se deberá agregar el header de autorización "Bearer token" con el token obtenido en el login.

Podés encontrar una copia de la API en [Postman](challenge-app/src/main/resources/challenge.postman_collection.json)


##Docker

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

```
docker pull gonisebas/tenpo-challenge-gonisebas
```
