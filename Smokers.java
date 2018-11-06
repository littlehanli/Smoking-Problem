package smokingproblem;

import java.util.Random;
import java.util.concurrent.*;
import java.lang.Math;

public class Smokers extends Thread{

	private Semaphore selects;
	private String name;
	private int[] counter;
	private Semaphore rest;
	private int times;
	
	public Smokers(Semaphore select,int[] test, Semaphore r, String n) {
		selects = select;
		this.counter = test;
		name = n;
		rest = r;
	}
	
	public void run() {
		while(true) {
			// let another two smoker waiting(block)
			try {
				rest.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			counter[0] = 0;
			System.out.println(name+"Smoker starts to smoke.");
			
			try {
				sleep(getPoissonRandom(8)*1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			synchronized(this) {
				times++;
			}
			System.out.println("Done! "+name+"Smoker "+times+" times!");
			try {
				sleep(getPoissonRandom(5)*1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// Done smoking¡Atable start to be called (awake)
			selects.release();
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
