 # Locus Energy Coding Challenge

This is a very basic scaffold project for you to work in for the elevator simulator assignment.

## Instructions

Write an elevator simulator system that runs multiple elevators in a building. Create a test where you run this simulator for a building with 100 floors and 6 elevators. Come up with some creative ways of testing your system (i.e. create more requests than elevators, etc).

While we know there are many college project implementations of elevator simulators, this assignment allows for a variety of solutions to a real-world problem.  Your solution will be reviewed by the engineers you would be working with if you joined Locus Energy.  We are interested in seeing your real-world design, coding, and testing skills, along with workflow interaction with git.

The following interfaces are provided to you as a suggestion (they are also located in the src directory of this repo along with Javadoc). You are free to implement them or come up with your own.

    public interface ElevatorControler {
        Elevator callElevator(int fromFloor, int direction);
    }
    
    public interface Elevator {
        void requestFloor(int floor);
        boolean isBusy();
        int currentFloor();
    }



## Using this scaffold

This scaffold is provided to help you (and us) build your homework code. 
We've included a `pom.xml`, which is a file used by [maven][maven] to build the project and run other commands.   It also contains information on downloading dependent jars needed by your project.  This one contains JUnit, EasyMock and Log4J already, but feel free to change it as you see fit.

    mvn compile      # compiles your code in src/main/java
    mvn test-compile # compile test code in src/test/java
    mvn test         # run tests in src/test/java for files named Test*.java


[maven]:http://maven.apache.org/

## Submission

Please clone the repository and deliver your solution in a separate repository (preferably your own). _Do not submit your solution via pull request to this repository._ If you are unfamiliar with GitHub, here is a quick introduction: https://guides.github.com/activities/hello-world/. As an alternative, you can deliver your solution via email in an archive format of your choice, including all project files. The project is due within 1 calendar week.
