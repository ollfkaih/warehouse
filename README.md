[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/javafx-template)
# Javafx template

A repository with three variants of a javafx projects, with maven setup for Java 16 and JavaFX 16, and JUnit 5 (Jupiter) and TestFX for testing.

To make the project(s) more interesting, it is the start of an [RPN](https://en.wikipedia.org/wiki/Reverse_Polish_notation) calculator (look for `// TODO`) markers). The core logic is almost implemented (in [Calc.java](javafx-template/src/main/java/app/Calc.java)), the fxml file (in [App.fxml](javafx-template/src/main/resources/app/App.fxml) is almost complete, but the controller class (in [AppController.java](javafx-template/src/main/java/app/AppController.java) is pretty limited. And last, but not least, there is a TestFX-based test (in [AppTest.java](javafx-template/src/test/java/app/AppTest.java), see the [README](javafx-template/src/test/java/app/README.md) for details about what it tests).

## javafx-template

Template for  single-module, single-package javafx project.

## packages-template

Template for  single-module, multi-package javafx project.

## modules-template

Template for  multi-module, multi-package javafx project.

## Trying it out

All projects can be tried out by cd-ing into the corresponding folder and using `mvn`:

- compile with `mvn compile` (after `cd javafx-template` of course)
- test with `mvn test` (it should fail until you complete the RPN calculator)
- run with `mvn javafx:run` (it should open, but not work properly)
