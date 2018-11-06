package smokingproblem;

import java.util.Random;
import java.util.concurrent.*;
import java.lang.Math;

public class Agents extends Thread {
	private Semaphore selects;
	private Semaphore tabacco;
	private Semaphore paper;
	private Semaphore matches;
	private Random rn = new Random();
	private int random;

	private int round;
	
	public Agents(Semaphore select, Semaphore t, Semaphore p, Semaphore m) {
		selects = select;
		tabacco = t;
		paper = p;
		matches = m;
	}

	public void run() {
		while (true) {
			try {
				sleep(getPoissonRandom(3)*1000);
				random = rn.nextInt(3);
				// table has chosen ingredients already(block)¡Alet those ingredient awake
				selects.acquire();
				synchronized(this) {
					round++;
					System.out.println("------------------------------");
					System.out.println("Round "+round+":");
				}
				if (random == 0) {
					
					System.out.println("Prepare for paper & Tabacco.");
					sleep(getPoissonRandom(5)*1000);
					paper.release();
					tabacco.release();
				} else if (random == 1) {
					
					System.out.println("Prepare for matches & tabacco.");
					sleep(getPoissonRandom(5)*1000);
					matches.release();
					tabacco.release();
				} else if (random == 2) {
					
					System.out.println("Prepare for matches & paper.");
					sleep(getPoissonRandom(5)*1000);
					matches.release();
					paper.release();
				}
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

	}

	private static int getPoissonRandom(double mean) {
		Random r = new Random();
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * r.nextDouble();
			k++;
		} while (p > L);
		return k - 1;
	}

}
