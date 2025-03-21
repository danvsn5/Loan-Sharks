[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/5pVslkgH)

# SOFTENG 306: Project 1 Starter

Welcome to your first project for SOFTENG 306.

The project specification is available [here](https://canvas.auckland.ac.nz/courses/105857/files/13471322). The four deliverables for this project are:

- [Design Documentation](https://canvas.auckland.ac.nz/courses/105857/assignments/400876)
- [Demo](https://canvas.auckland.ac.nz/courses/105857/assignments/400878)
- [Code](https://canvas.auckland.ac.nz/courses/105857/assignments/400879)
- [Quality Report](https://canvas.auckland.ac.nz/courses/105857/assignments/404409)

## Contributors

- Allan Xu - axu732
- Jamie Lee - JamieLeeNZ
- Chulshin Kim - ckim349
- Daniil Vasan - danvsn5

## Notes From Contributors

- To run application, type `./mvnw javafx:run` into terminal after cloning
- Login on main page is Moana and Password
- To run unit tests in VSCode, navigate to Testing Tab and run all LAVS tests. For minimal test errors, run each test class individually instead of running the entire LAVS folder. NOTE: an HTTP connection to the mainframe is not required for the tests to succeed.
- Ensure there is a lavs.db file within the folder [\src\main\resources\db](\src\main\resources\db). In the event it is not there, create a file called lavs.db
- Assumes that first phone number, email and address are primary
- If loan creation does not work, reset the lavs.db by running ResetDatabase.java [\src\main\java\uoa\lavs\backend\sql](\src\main\java\uoa\lavs\backend\sql)
- Read all instructions in the Welcome page for the application process to run as intended

## Initial Repository Code

The repository contains some initial Java code to help your project. These are under the [src/main/java/uoa/lavs](src/main/java/uoa/lavs) folder. The following code is supplied:

- [mainframe](src/main/java/uoa/lavs/mainframe): the interfaces for sending requests to and receiving responses from the mainframe. You will need to use the [Connection](src/main/java/uoa/lavs/mainframe/Connection.java) interface in your code: we have provided two implementations of this interface in the [simulator](src/main/java/uoa/lavs/mainframe/simulator/) folder.
- [Utility](src/main/java/uoa/lavs/utility/): a utility for calculating loan repayments.

  In addition, there are some unit tests in [src/test/java/uoa/lavs/](src/test/java/uoa/lavs/) folder.
