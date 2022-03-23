<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]



<h3 align="center">FSU-JavaSST-Compiler</h3>
  <p align="center">
    A JavaSST compiler written in Java as a study project at FSU Jena.
    <br />
    <a href="https://github.com/Kr3b5/FSU-JavaSST-Compiler/issues">Report Bug</a>
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
java -jar FSUCompile-1.0.jar <java-file> <options>

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



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/Kr3b5/FSU-JavaSST-Compiler/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/Kr3b5/FSU-JavaSST-Compiler/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/Kr3b5/FSU-JavaSST-Compiler/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/Kr3b5/FSU-JavaSST-Compiler/issues
