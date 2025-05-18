# CryptoArthead 🪙

### Descripción del proyecto 📜

El proyecto consiste en un analizador de AltCoins que estudia distintos parámetros de la moneda basándose en su repositorio de GitHub, con el objetivo de proporcionar a los inversores información detallada y relevante que les permita tomar decisiones más informadas antes de realizar cualquier acción. Además de los datos de GitHub, el sistema utiliza información proveniente de CoinMarketCap, permitiendo obtener datos de calidad de cada moneda. El resultado es un datamart consolidado y actualizado que permite ejecutar modelos predictivos sobre el comportamiento de las criptomonedas emergentes.

### Elección de APIs  🔌 

Hemos escogido la API de GitHub y la de CoinMarketCap, debido al gran auge que han tenido las criptomonedas en los últimos años, ya que es un tema de interés para muchos potenciales usuarios, basandolo en GitHub para relacionar los datos, ya que es otra herramienta mundialmente conocida y usada por la mayoría de gente que se dedica a la programación desde gente amateur o estudiantes hasta profesionales y empresas, como en este caso, las de las AltCoins elegidas.

### Requisitos 🔧

- Instalar el broker de mensajería ActiveMQ e iniciarlo.
- Tener el JDK versión 21.
- Tener Python 3.11.9 en adelante y configurado en las variables de entorno.

### Estructura del datamart 📊

El datamart se estructura a partir de tres archivos CSV. Los primeros dos contienen datos en bruto procedentes de los eventos recogidos en tiempo real desde las APIs de GitHub y CoinMarketCap, los cuales son enviados al broker sin procesar. Posteriormente, estos datos se combinan en un tercer CSV, el datamart definitivo, que unifica la información según el nombre y la fecha de los eventos. Este datamart consolidado contiene todos los eventos históricos procesados hasta el momento, representando una versión integrada y depurada de las fuentes originales.

### Estructura seguida 🧩 

El proyecto ha seguido una arquitectura lambda, donde en cada módulo hemos hecho una clean arquitecture, en este caso una arquitectura hexagonal, con el objetivo de mantener bien separada la lógica de negocio del resto de componentes del sistema, como bases de datos, servicios externos o interfaces de usuario. 

En el proyecto hemos identificado dos principios: 
- Single Responsability Principle: Lo hemos usado para mantener el código lo más modularizado posible, asignando a cada clase o componente una única responsabilidad. Esto facilita la lectura, el mantenimiento y la reutilización del código.
- Open/Close Principle: lo usamos para que el sistema sea fácil de ampliar sin tener que modificar el código ya existente, lo que reduce errores y facilita el mantenimiento a largo plazo, se puede observar al definir puertos para la mensajería (ActiveMQ) y el almacenamiento (SQLite), ya que el núcleo del sistema no depende de detalles concretos.
En nuestro código, este principio se evidencia en los métodos main de los feeders: si sustituimos ActiveMQStore por SQLiteStore, simplemente se modifica la construcción del objeto para utilizar una base de datos en lugar de mensajería, sin alterar la lógica del programa, ya que ambas clases implementan la misma interfaz GithubRepositoryStore. 

```java
GithubRepositoryStore store = new ActiveMQStore(args[1]);
Controller controller = new Controller(provider, store, repositories);
controller.execute();
```

```java
CoinStore store = new ActiveMQStore(args[1]);
Controller controller = new Controller(provider, store);
printSystemInformation(queries);
controller.execute();
```


Consta de 4 módulos:
- GitHubFeeder: alimenta un csv usando los eventos captados de Github, haciendo llamadas cada 5 minutos.
- CoinMarketCapFeeder: hace lo mismo que el anterior, pero para CoinMarketCap
- EventStoreBuilder: Recibe los mensajes de ActiveMQ y los guarda como eventos.
- CoinStatsPredictor: Mediante varios modelos de aprendizaje, procesa el datamart a tiempo real para obtener predicciones de interés para el usuario, que será capaz de visualizar y manejar mediante la CLI proporcionada.

Nuestro sistema usa una arquitectura Lambda porque trabaja tanto con datos en tiempo real como con datos históricos. Recibe eventos al momento desde ActiveMQ y también accede a archivos .events ya guardados con información pasada. Esto nos permite usar los datos recientes y también analizar los antiguos sin tener que volver a procesarlo todo. A diferencia de Kappa, que solo usa streaming, esta forma se adapta mejor a nuestro sistema porque tenemos los históricos guardados y organizados para consultar directamente.

### Diagrama de clases 📐

#### Diagrama de clases de los feeders
  - Los siguientes feeders implementan la lógica de extracción de datos desde las APIs externas y la envían al sistema a través de ActiveMQ o directamente a base de datos. Ambos siguen una estructura modular con responsabilidad única:

    - GitHubFeeder: [Image](https://github.com/user-attachments/assets/40fcbab5-d653-4b05-acf3-8160c59120bc)

    - CoinMarketCapFeeder: [Image](https://github.com/user-attachments/assets/5963070c-2cb4-4969-9c66-ba76cb94770d)

#### Diagrama de clases del EventStoreBuilder

  - El módulo EventStoreBuilder consume eventos desde ActiveMQ y los guarda como archivos .events estructurados por tipo y fecha. Sigue una arquitectura por capas y está diseñado para desacoplar el origen de los datos de su almacenamiento, cumpliendo con el principio de responsabilidad única:
    
    - EventStoreBuilder: [Image](https://github.com/user-attachments/assets/893c8ea9-0b61-4e95-87dc-e277aa4f557c)

#### Diagrama de clases del CoinStatsPredictor

  - El módulo CoinStatsPredictor aplica distintos modelos de aprendizaje automático sobre el datamart generado, permitiendo al usuario lanzar predicciones personalizadas a través de una interfaz CLI. Se compone de varios componentes organizados de forma modular:
    
    - CoinStatsPredictor: [Image](https://github.com/user-attachments/assets/795aad0b-f032-422c-b99d-3e0868618971)

### Compilación y ejecución 📦

El proyecto funciona siguiendo un orden de ejecución definido, utilizando el broker de mensajería ActiveMQ. Primero se lanzan los dos feeders: el de CoinMarketCap y el de GitHub, que se alimentan de datos en intervalos de 5 minutos. Estos datos, aún en bruto, se almacenan como dos CSV independientes. Cada 5 minutos, ambos archivos se unen por nombre y fecha para formar un tercer CSV: el datamart definitivo, que recoge todos los eventos históricos procesados hasta el momento. A partir de este datamart, se aplican distintos modelos de aprendizaje automático que permiten interpretar los datos y ofrecer resultados de interés al usuario, quien puede consultarlos mediante la interfaz de línea de comandos (CLI).

- CoinMarketFeeder: depende si lo tratamos con bases de datos, el primer argumento es la API key y el segundo es la ruta donde quieres que se cree la base de datos. Si lo tratamos con ActiveMQ ponemos el puerto.
  
![Image](https://github.com/user-attachments/assets/006cc3ee-cec2-411f-bfad-fb778c09b627)

![Image](https://github.com/user-attachments/assets/e96a349c-88be-4320-8a70-2203b99f16e7)

- GitHubFeeder: al igual que el otro feeder, el primer argumento es la key y el otro la ruta de la base de datos/puerto del ActiveMQ.
  
![Image](https://github.com/user-attachments/assets/1b7fc91f-b28a-4295-be5b-7660932fc7b5)

- EventStore: aqui proporcionamos el enlace de ActiveMQ, y los events que queremos.
  
![Image](https://github.com/user-attachments/assets/46721ef0-fcdb-41d4-bc6b-aec23933d9dd)

![Image](https://github.com/user-attachments/assets/8384a2d8-9e2d-472a-bfd4-b9f08b18fcc4)

- CoinStatPredictor: CoinStatsPredictor requiere nueve argumentos: la ruta del CSV de GitHub, la ruta del CSV de CoinMarketCap, la ruta del datamart combinado, la ruta donde se guardará el resultado del modelo, el enlace del servidor ActiveMQ, y los nombres de los topics correspondientes a la información de GitHub, los repositorios de GitHub, las monedas y las quotes.
    - En la interfaz de usuario mediante linea de comandos tendrá tres opcizones:
      
      ![Image](https://github.com/user-attachments/assets/8ecac3b5-0016-488e-83f2-483fdb4b3993)
    
        - Solicitar ayuda (1): te muestra la lista de monedas, targets y modelo disponibles.
          
          ![Image](https://github.com/user-attachments/assets/bb380aa1-7f00-42f9-a299-138276adb160)
          
        - Iniciar busqueda (2): Se seleccionan tres elementos (seperados por coma si son mas de uno y no poniendo nada si quieres todos), la/s moneda/s que quieras analizar, el/los target/s que quieras estudiar, y el/los modelos que quieras utilizar.
          Ejemplo de busqueda con dos monedas, con respecto al precio y usando los modelos de regresión lineal,  knn y svm:
          
          ![Image](https://github.com/user-attachments/assets/1c20adc7-219f-49d3-9231-63df1b1110b1)
          
        - Salir (0): Te saca del menú.
          
          ![Image](https://github.com/user-attachments/assets/b4b90d25-269d-4561-91f8-51b49d28a1cf)
          
        - Cuando termines una busqueda, podrás presionar enter para volver al menú de inicio (a veces al darle enter, le tienes que dar otra vez y te muestra el menú dos veces, en principio eso es un problema de la propia IDE).

          ![Image](https://github.com/user-attachments/assets/ce78b7ab-661e-4e7f-9d3e-b460677c0714)

### Validación y tests 🧪
Para garantizar el correcto funcionamiento del sistema, se han implementado múltiples pruebas utilizando JUnit, abarcando tanto la conexión con las APIs como la persistencia en la base de datos. Se han realizado pruebas para tanto CoinMarketCap como GitHub.
- Test de CoinMarketCap:
  - CoinMarketCapConnectionTest: verifica la conexión con la API.
  - CoinMarketCapDeserializerTest: comprueba la correcta interpretación de los datos JSON.
  - CoinMarketCapFetcherTest: testea la recolección de datos de forma periódica.
  - CoinMarketCapProviderTest: valida el flujo completo de obtención y entrega de datos.
  - CoinRepositoryTest: comprueba la inserción y consulta de monedas en SQLite.
  - QuoteRepositoryTest: testea la persistencia de precios y estadísticas.
  - SQLiteConnectionTest: valida la conexión con la base de datos.
  - TableCreatorTest: comprueba que las tablas necesarias se crean correctamente.
  CoinMarketCap proporciona una API key de prueba, lo que facilita la ejecución de tests reales sin exponer información sensible.

- Test de GitHub:
  - GithubConnectionTest: valida la conexión con la API de GitHub.
  - GithubDeserializerTest: asegura la correcta transformación de los datos JSON en objetos del dominio.
  - GithubFetcherTest: verifica que la extracción de datos se realiza correctamente.
  - GithubProviderTest: testea la lógica de integración del proveedor completo.
  - GithubRepositoryTest: gestiona la persistencia de los repositorios en la base de datos.
  - InformationRepositoryTest: asegura que la información adicional de los proyectos se guarda adecuadamente.
  - SQLiteConnectionTest: prueba la conexión a SQLite.
  - TableCreatorTest: comprueba la creación de estructuras de base de datos.
GitHub no proporciona una API key de testeo, por lo que se utilizó .gitignore para proteger las credenciales locales usadas durante las pruebas.

### Autores ✒️
Este proyecto fue creado por [Arthead](https://github.com/ArtHead-Devs), contando con dos integrantes:
- Fabio Nesta Arteaga Cabrera: [NestX10](https://github.com/NestX10)
- Pablo Cabeza Lantigua: [pabcablan](https://github.com/pabcablan)

          

          
