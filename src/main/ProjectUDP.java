package main;

import java.io.IOException;

// unused imports here to make sure the classes we are running will get compiled
import elevator.ElevatorSubsystem;
import scheduler.Scheduler;
import floor.FloorSubsystem;

/**
 * Class setup to run the system as seperate processes, by spawning them from a single process (like fork())
 * Useful for running the system with UDP without needing to start 3 processes
 * @author Martin Dimitrov
 */
@SuppressWarnings("unused")
public class ProjectUDP {
	
	public static void main(String[] args) {
		// create the processes
		ProcessBuilder elevator = new ProcessBuilder("java", "-cp", "bin", "elevator.ElevatorSubsystem");
		ProcessBuilder floor = new ProcessBuilder("java","-cp", "bin", "floor.FloorSubsystem");
		ProcessBuilder scheduler = new ProcessBuilder("java", "-cp", "bin", "scheduler.Scheduler");
		
		// set their stdin, stderr, stdout to the same as this process
		// TODO: perhaps add some config to get this output in some txt files in /logs?
		// TODO: can also set the stdout to be a object in this class for tests (so we can read input internally)
		elevator.inheritIO();
		floor.inheritIO();
		scheduler.inheritIO();
		
		Process[] processes = new Process[3];
		
		// start them all
		try {
			processes[2] = elevator.start();
			processes[1] = scheduler.start();
			processes[0] = floor.start();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		// make sure the processes get killed after (otherwise they will be stuck on the OS)
		try {
			processes[0].waitFor();
		} catch (InterruptedException e) {
			System.out.println("Making sure all processes are killed");
			for (Process p: processes) {
				p.destroy();
			}
		}
	}

}