DFXEngine Injector API contains the annotations used by [Injector](../injector). It has been separated from the main
Injector so that other modules can have a soft dependency to the injector without having the whole implementation in the
classpath for projects that do not need dependency injection capabilities. More information is available in the main 
Injector module's [README file](../injector/README.md).