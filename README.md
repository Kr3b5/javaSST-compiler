<h3 align="center">FSU-JavaSST-Compiler</h3>
  <p align="center">
    A JavaSST compiler written in Java as a study project at FSU Jena.
    <br />
    <a href="https://github.com/Kr3b5/javaSST-compiler/issues">Report Bug</a>
  </p>


<!-- ABOUT THE PROJECT -->
## About The Project

### Built With

* [Java 15](https://openjdk.java.net/)
* [Maven](https://maven.apache.org/)


<!-- GETTING STARTED -->
## Getting Started

Build the project with `mvn clean package`

This generates two .jar files in `./target`: 
* `FSUCompiler-1.0.jar` - Compiler with dependencies 
* `original-FSUCompile-1.0.jar` - Compiler without dependencies


<!-- USAGE EXAMPLES -->
## Usage

```
java -jar FSUCompiler-1.0.jar <java-file> <options>

Valid options:
    -dot : Print Dot File
    -debug: print Debug
```

To convert the .dot file to png use [Graphviz](https://graphviz.org/):

`dot -Tpng <filename>.dot -o <filename>.png`

**Example Output:**

![ast](/assets/example_ast.png)


<!-- LICENSE -->
## License
Distributed under the MIT License. See `LICENSE` for more information.


<!-- CONTACT -->
## Contact

* [Kr3b5](https://github.com/Kr3b5)



<p align="right">(<a href="#top">back to top</a>)</p>
