# ForestGuard



Plataforma de predicción del riesgo y simulación de la propagación
de incendios forestales.



## Estructura del proyecto

- services: servicios backend desarrollados con Spring Boot.
- frontend: interfaz web desarrollada con React.
- ml: preparación de datos, entrenamiento y evaluación de modelos.
- infra: configuración de Docker Compose y recursos en la nube.
- docs: decisiones de diseño, instrucciones y resultados de experimentos.



## Estado actual

Semana 1: configuración del entorno de desarrollo, puesta en marcha
de la infraestructura local y creación del primer servicio REST.

## Requisitos

Docker Desktop iniciado con contenedores Linux y Docker Compose

## Configuración

Copiar `.env.example` a `.env` y sustituir el valor de
`POSTGRES_PASSWORD` por una contraseña local.

## Inicio

Desde la raíz del repositorio:

```bash
docker compose up -d --wait
docker compose ps
```
## Servicios

| Servicio | Dirección local | Función |
| --- | --- | --- |
| PostgreSQL 16 + PostGIS 3.5 | localhost:5432 | Almacenamiento y consultas espaciales |
| Redis 7.4 | localhost:6379 | Caché |
| Kafka 4.1.1 | localhost:9092 | Intercambio de eventos |

La base de datos y el usuario de PostgreSQL se llaman `forestguard`.
Los servicios utilizan volúmenes persistentes.