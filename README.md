# Transacciones
Esta aplicación Android permite a los usuarios realizar varias operaciones relacionadas con 
transacciones para Credibanco, incluyendo autorización, cancelación, búsqueda por número de recibo 
y listado de todas las transacciones autorizadas. Se comunica con un servidor backend que ejecuta 
un microservicio basado en Kotlin con Spring Boot a través de APIs HTTP.

## Funcionalidades
1. **Autorización de Transacciones:** Los usuarios pueden autorizar transacciones llenando los detalles necesarios y enviándolos. Al autorizarse con éxito, la transacción se almacena localmente usando la biblioteca de persistencia Room.
2. **Buscar Transacciones por Número** de Recibo: Los usuarios pueden buscar transacciones utilizando sus números de recibo. Si se encuentran, los usuarios pueden ver los detalles de la transacción y proceder a cancelarla si es necesario.
3. **Listar Todas las Transacciones Autorizadas:** Muestra una lista de todas las transacciones autorizadas almacenadas localmente en el dispositivo, junto con sus detalles.
4. **Cancelación de Transacciones:** Los usuarios pueden cancelar transacciones autorizadas buscándolas utilizando sus números de recibo. Tras la confirmación, el estado de la transacción se actualiza localmente y no se puede cancelar de nuevo.

## Demo
|Prueba Uno|Prueba Dos|
|---|---|
|<img src="https://github.com/jsebastianbarretoj99/transactions/assets/63886423/35610f28-6fdc-4fc6-87bc-a348dc4cfae9" width="300"/>|<img src="https://github.com/jsebastianbarretoj99/transactions/assets/63886423/823b6cca-9a58-429f-906b-a0ccccd657c1" width="300"/>|

## Stack Tecnológico
- Kotlin
- Jetpack Compose
- MVVM Arquitectura
- DataStore para almacenamiento de preferencias
- Room para persistencia de datos
- Retrofit para comunicación HTTP
- Koin para inyección de dependencias
- Coroutines para programación asíncrona

## Requisitos
- Android SDK 26+
- Kotlin 1.9.23
- DataStore 1.0.0
- Room 2.6.1
- Retrofit 2.9.0
- Jetpack Compose 1.6.4
- Koin 3.5.0
- Coroutines 1.8.0

## Contribuidores
- [Juan Sebastián Barreto Jiménez](https://github.com/jsebastianbarretoj99)
