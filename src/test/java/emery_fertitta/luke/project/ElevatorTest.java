package emery_fertitta.luke.project;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElevatorTest {
	
	/**
	 * Attempt to have a user enter and leave an elevator in various ways,
	 * ensuring proper variable updating and exception throwing.
	 */
	@Test
	public void testElevatorEnterUpdateAndExit() {
		int numFloors = 1;
		int startFloor = 0;
		int moveDelay = 10; // milliseconds to delay elevator movement
		int fromFloor = 0;
		int toFloor = 1;
		int threadWait = 1000; // milliseconds to wait for elevator to do its job
		IElevator elevator = new Elevator(numFloors, startFloor, moveDelay);
		Thread thread = new Thread(elevator);
		thread.start();
		
		// Dummy user for testing methods
		IElevatorUser user = new IElevatorUser() {
			@Override
			public void update(IElevator elevator) {
				assertEquals(fromFloor, elevator.getCurrentFloor());
				assertEquals(true, elevator.isOccupied());
				try {
					elevator.leaveElevator(toFloor + 1, this);
					fail("Invalid floor should throw an exception. ");
				} catch (WrongFloorException e) {
				}
				
				try {
					elevator.leaveElevator(fromFloor, this);
				} catch (WrongFloorException e) {
					fail("Elevator changed floors for no reason.");
				}
				assertEquals(false, elevator.isOccupied());
			}
		};
		
		try {
			elevator.enterElevator(-1, user);
			fail("Invalid floor should throw an exception. ");
		} catch (WrongFloorException e) {
		}
		
		try {
			elevator.enterElevator(fromFloor, user);
		} catch (WrongFloorException e) {
			fail("Elevator changed floors for no reason.");
		}
		
		// Wait for elevator to call update on our user.
		try {
			Thread.sleep(threadWait);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		
		// Stop the elevator and join the threads.
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
	}
	
	/**
	 * Send the elevator up and and down and ensure that it eventually reaches the destination.
	 */
	@Test
	public void testElevatorMovement(){
		int numFloors = 100;
		int startFloor = 0;
		int moveDelay = 1; // milliseconds to delay elevator movement
		int destination1 = 56;
		int destination2 = 30;
		int threadWait = 100; // milliseconds to wait for elevator to do its job
		Elevator elevator = new Elevator(numFloors, startFloor, moveDelay);
		Thread thread = new Thread(elevator);
		thread.start();
		
		// Forcefully send up.
		elevator.addDestination(destination1);
		// Wait for elevator to move.
		try {
			Thread.sleep(threadWait);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		assertEquals(destination1, elevator.getCurrentFloor());
		
		// Forcefully send down.
		elevator.addDestination(destination2);
		// Wait for elevator to move.
		try {
			Thread.sleep(threadWait);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		assertEquals(destination2, elevator.getCurrentFloor());
		
		
		// Make a request to send the elevator back up.
		try {
			elevator.requestFloor(destination1);
			fail("Exception not thrown despite empty elevator.");
		} catch (InvalidStateException e1) {
		}
		// Create dummy user.
		IElevatorUser user = new IElevatorUser() {
			@Override
			public void update(IElevator elevator) {}
		};
		elevator.addRequester(user);
		try {
			elevator.enterElevator(destination2, user);
		} catch (WrongFloorException e2) {
			fail("Exception thrown when user is on the correct floor");
		}
		try {
			elevator.requestFloor(destination1);
		} catch (InvalidStateException e1) {
			fail("Exception not thrown despite empty elevator.");
		}
		// Wait for elevator to move.
		try {
			Thread.sleep(threadWait);
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
		assertEquals(destination1, elevator.getCurrentFloor());

		
		// Stop the elevator and join the threads.
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			fail("Test thread was unexpectedly interrupted.");
		}
	}

}
