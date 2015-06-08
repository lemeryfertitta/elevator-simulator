# Locus Energy Coding Challenge Solution

This is my solution to the Locus Energy Coding challenge, which I started on 2015-06-05 and submitted on 2015-06-08.

## Approach

This solution is heavily based upon a concurrent implementation of the elevators. Once the controller starts the simulation, elevators will move concurrently, with a specified delay between each time the elevator decides to move up, down, or stay on the same floor. Users may call an elevator via the controller. The controller then decides which elevator shoud service the user's request, and the user is added to the list of people that the elevator notifies when it switches floors. Users may then attempt to enter the elevator, request a floor, and exit the elevator at the appropriate times. 

## Future

Some additional features that would improve the simulator: 

- Opening and closing of elevator doors.
- Smarter notification for users that are waiting on or inside an elevator.
- Handling floor requests that are not in the direction of travel.
- Continuous elevator travel (i.e. implemnting basic physics), instead of the more artifical Thread.sleep() delay.
- Allow for various algorithms to dictate elevator movement instead of only selection.
- Improve the selection and movement algorithms.

## Using this solution

The solution can be built with [maven][maven].  

    mvn compile      
    mvn test-compile 
    mvn test         

The current set of JUnit tests has basic functionality tests, as well as three runs of a 100 floor, 6 elevator building to test the three selection algorithms that I have included.


[maven]:http://maven.apache.org/
