![Titanic ANN](https://raw.githubusercontent.com/nlo-portfolio/nlo-portfolio.github.io/master/style/images/programs/titanic-ann.png "Titanic-ANN")

## Description ##

Titanic-ANN (Artificial Neural Network) is an artificial learning exercise using statistical data from the sinking of the RMS Titanic in 1912. It uses neural networks provided by the Encog machine learning framework to backpropagate the error in passenger survival predictions.

## Dependencies ##

Ubuntu<br>
Java v14+<br>
Encog Machine Learning Framework<br>
JUnit5 (tests)<br>
\* All required components are included in the provided Docker image.

## Usage ##

Titanic-ANN is entirely self-contained. Follow the directions below to see the how the neural network computes a result.<br>
<br>
Ubuntu:<br>
<br>
&emsp;Application:

```
javac -d src/main/bin --class-path src/main/java:src/main/resources/encog-core-3.4.jar src/main/java/*.java
java --class-path src/main/bin:src/main/resources/encog-core-3.4.jar Titanic
```

&emsp;Test:

```
javac -d src/main/bin --class-path src/main/java:src/test/java:src/main/resources/encog-core-3.4.jar:src/test/resources/junit-platform-console-standalone-1.7.2.jar src/main/java/*.java src/test/java/*.java
java -jar src/test/resources/junit-platform-console-standalone-1.7.2.jar --class-path src/main/bin:src/test/bin:src/main/resources/encog-core-3.4.jar:src/test/resources/junit-platform-console-standalone-1.7.2.jar --scan-class-path
```

<br>

Docker:

```
docker-compose build
docker-compose run <titanic-ann | test>
```
