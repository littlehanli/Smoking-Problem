package smokingproblem;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Table extends Thread {
	
	private Semaphore resource;
	private Semaphore[] ingredient_Array;
	private Semaphore mutex;
	private int[] counter;
	
	private int resource_id;
	private String name;
	
	public Table(Semaphore res, Semaphore[] array, Semaphore mutex, int rid, String n, int[] t) {
		resource = res;
		ingredient_Array = array;
		resource_id = rid;
		this.mutex = mutex;
		name = n;
		counter = t;
	}
	
	public static void ArrayClean(Semaphore[] a) {
		for(int i=0;i < a.length; i++) {
			int j = a[i].availablePermits();
			if(j>2) {
				a[i].drainPermits();
			}
		}
	}
	
	public void run() {
		while(true) {
			try {
				// ingredient supply completed(block)
				resource.acquire();
				ArrayClean(ingredient_Array);
				System.out.println(name+" is on the table.");
				try {
					sleep(getPoissonRandom(3)*1000);
				}
				catch(InterruptedException e1) {
					e1.printStackTrace();
				}
				// wait for two ingredients be prepared
				mutex.acquire();
				counter[0] = counter[0] + resource_id;
				// smoker start to smoke(awake)
				ingredient_Array[counter[0]-1].release();	
				mutex.release();
			}
			catch(InterruptedException e1){
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
