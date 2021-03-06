/*
 * File:	Philosopher.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti (modified by Kostiantyn Kucher and Rashed Qazizada)
 * Date: 	November 2019
 */

import java.util.Random;

public class Philosopher implements Runnable {

	/*
	 * Controls whether logs should be shown on the console or not. Logs should
	 * print events such as: state of the philosopher, and state of the chopstick
	 * for example: philosopher # is eating; philosopher # picked up the left
	 * chopstick (chopstick #)
	 */
	public boolean DEBUG = false;
	private int id;

	private final Chopstick leftChopstick;
	private final Chopstick rightChopstick;

	private Random randomGenerator = new Random();

	private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;

	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;

	public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick, int seed, boolean debug) {
		this.id = id;
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;

		this.DEBUG = debug;

		/*
		 * set the seed for this philosopher. To differentiate the seed from the other
		 * philosophers, we add the philosopher id to the seed. the seed makes sure that
		 * the random numbers are the same every time the application is executed the
		 * random number is not the same between multiple calls within the same program
		 * execution
		 * 
		 * NOTE In order to get the same average values use the seed 100, and set the id
		 * of the philosopher starting from 0 to 4 (0,1,2,3,4). Each philosopher sets
		 * the seed to the random number generator as seed+id. The seed for each
		 * philosopher is as follows: P0.seed = 100 + P0.id = 100 + 0 = 100 P1.seed =
		 * 100 + P1.id = 100 + 1 = 101 P2.seed = 100 + P2.id = 100 + 2 = 102 P3.seed =
		 * 100 + P3.id = 100 + 3 = 103 P4.seed = 100 + P4.id = 100 + 4 = 104 Therefore,
		 * if the ids of the philosophers are not 0,1,2,3,4 then different random
		 * numbers will be generated.
		 */

		randomGenerator.setSeed(id + seed);
	}

	public int getId() {
		return id;
	}

	public double getAverageThinkingTime() {
		/*
		 * TODO Return the average thinking time Add comprehensive comments to explain
		 * your implementation
		 */
		return thinkingTime / numberOfThinkingTurns;
	}

	public double getAverageEatingTime() {
		/*
		 * TODO Return the average eating time Add comprehensive comments to explain
		 * your implementation
		 */

		return eatingTime / numberOfEatingTurns;
	}

	public double getAverageHungryTime() {
		/*
		 * TODO Return the average hungry time Add comprehensive comments to explain
		 * your implementation
		 */
		return hungryTime / numberOfHungryTurns;
	}

	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}

	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}

	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}

	public double getTotalThinkingTime() {
		return thinkingTime;
	}

	public double getTotalEatingTime() {
		return eatingTime;
	}

	public double getTotalHungryTime() {
		return hungryTime;
	}

	@Override
	public void run() {
		/*
		 * TODO (Initialize some additional variables, if necessary)
		 *
		 * Think, Get hungry, Pick up the left and then the right chopstick, Eat,
		 * Release the chopsticks. ^^^ Repeat until the thread is interrupted
		 *
		 * Increment the thinking/hungry/eating turns counter *when each turn starts*.
		 *
		 * Update the duration of each turn *after the turn is completely finished*.
		 * Keep track of total hungry turn durations by getting the current timestamp
		 * with System.currentTimeMillis() when the turn starts, then another
		 * System.currentTimeMillis() after the turn has finished, and subtracting
		 * these. For thinking and eating turns, use the duration generated with
		 * randomGenerator.nextInt(1000).
		 *
		 * If DEBUG is true, print the log messages for each event. Additionally, you
		 * might want to print a message such as "philosopher X has finished" when the
		 * thread terminates (for debugging purposes).
		 *
		 *
		 * Add comprehensive comments to explain your implementation, including deadlock
		 * prevention/detection. You should start with a straightforward implementation,
		 * but you will eventually have to make it more sophisticated w.r.t the order
		 * (and conditions) of the actions and the threads synchronization in order to
		 * pass the tests with the expected results!
		 */
		// Think, hungry and eat loop
		try {
			while (true) {
				// Philosophers intiall state to start with.
				thinking();
				// Hungry method
				hungry();
				// Eating method
				eating();
			}
		} catch (InterruptedException e) {
			 Thread.currentThread().interrupt();

		} finally {
			if (DEBUG) {
				printingPhilosopherStatus(id, PhilosopherStatus.HAS_BEEN_TERMINATED);

			}
		}
	}

	// Philosophers thinking method
	private void thinking() throws InterruptedException {
		numberOfThinkingTurns++;
		// storing the random time in randomThinkingTIme.
		int randomThinkingTIme = randomTime();
		// sleeps the thingk in randomThinkTime
		Thread.sleep(randomThinkingTIme);

		if (DEBUG) {
			printingPhilosopherStatus(id, PhilosopherStatus.THINKING);
		}
		thinkingTime += randomThinkingTIme;
	}

	// Philosophers hungry method
	private void hungry() throws InterruptedException {
		numberOfHungryTurns++;

		if (DEBUG) {
			printingPhilosopherStatus(id, PhilosopherStatus.HUNGRY);
		}

		boolean holdsTwochopsticks = false;//holds boolean value  if both chopsticks are true. 
		
		boolean holdsleftChopStick=false;//holds left chopstick
		boolean holdsrightChopStick=false;//holds right chopstick
		
		long intiallTime = System.currentTimeMillis();
		while (! holdsTwochopsticks&& !Thread.currentThread().isInterrupted()) {

//			The philosopher must have both left and right chopsticks to eat otherwise he must put it down

			// Attemps to pick up the left chopstick.
			if (leftChopstick.pickUp()) {
				holdsleftChopStick =true;
				// chopstick status
				if (DEBUG)
					philosopherStatus(id, " picked up chopstick " + leftChopstick.getId());

				// Attemping to pick up the right chopstick if true goes to eating method
				if (rightChopstick.pickUp()) {
					holdsrightChopStick =true;
				//	holdsTwochopsticks = true;
					// chopstick status
					if (DEBUG) {
						philosopherStatus(id, " picked up chopstick " + rightChopstick.getId());
					}
				}

				// Drops left if the right cannot be picked up
				else {
					leftChopstick.putDown();

					if (DEBUG) {
						philosopherStatus(id, " put down chopstick " + leftChopstick.getId());
					}
				}
				if (holdsrightChopStick && holdsleftChopStick) {
					holdsTwochopsticks = true;	
					}
			}
		}

		// Adds time waiting to hungry time.
		hungryTime += System.currentTimeMillis() - intiallTime;
	}

	// Philosophers eat method
	private void eating() throws InterruptedException {
		numberOfEatingTurns++;
		// Sleeps thread for random time and adds the sleep time to the total eating
		// time.
		int randomEatingTIme = randomTime();
		Thread.sleep(randomEatingTIme);

		if (DEBUG) {
			printingPhilosopherStatus(id, PhilosopherStatus.EATING);

		}

		eatingTime += randomEatingTIme;
		printingPhilosopherStatus(id, PhilosopherStatus.FINISHED_EATING);

		// Puts down both chopsticks when the philosophers has finished eating
		releaseChopsticks();
	}

	// Generates a random time method
	private int randomTime() {
		return randomGenerator.nextInt(1000);
	}

	// Releasing both chopsticks
	private void releaseChopsticks() {

		rightChopstick.putDown();
		leftChopstick.putDown();

		if (DEBUG) {
			philosopherStatus(id, " put down chopstick " + rightChopstick.getId());
			philosopherStatus(id, " put down chopstick " + leftChopstick.getId());
		}

	}

	public void philosopherStatus(int id, String status) {
		System.out.println("Philosopher " + id + " " + status);

	}

	public void printingPhilosopherStatus(int id, PhilosopherStatus status) {
		System.out.println("Philosopher " + id + " " + status);

	}

}