# HTTP Tester

## Description

GUI program to test different HTTP request methods against any URL. Enter a URL and select a request method (e.g. GET)
and view the response in plain text.

## Installing

You must first compile the source. After compiling you can run the .class or you can create a .jar for convenience.

### Compiling and Creating Jar

```
git clone https://github.com/NanoDano/RestTester
cd RestTester
javac RestTester/*.java
jar cfm RestTester.jar META-INF/MANIFEST.MF RestTester
```

## Running

```
java -jar RestTester.jar
```
