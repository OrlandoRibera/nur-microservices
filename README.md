# Microservicio de Catering

## Descripción

Este servicio es encargado de recibir las recetas y cocinar la comida, cocinar, empaquetar y despachar cada una de las
comidas, actualizar los estados de las comidas y del paquete.

## Diseño diagrama de clases de los modelos del Dominio:

![Diagrama de clases](assets%2FUML%20MS.png)

## Docker
Puedes clonarte la imagen de este microservicio con el siguiente comando:

```bash
docker pull tuniol/catering-app:latest
```

Para correr la imagen:
```bash
docker run -d -p 8080:8080 tuniol/catering-app
```

Link del [repositorio](https://github.com/OrlandoRibera/nur-microservices).
