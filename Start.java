package smokingproblem;

import java.util.concurrent.Semaphore;

public class Start {

	public Start() {
		main();
	}
	
	private Semaphore [] array = new Semaphore[6];
	private Semaphore mutex;
	private Semaphore selects;
	private Semaphore tabacco;
	private Semaphore paper;
	private Semaphore matches;
	
	private int [] counter;
	
	private Smokers TabaccoSmoker;
	private Smokers MatchesSmoker;
	private Smokers PaperSmoker;
	
	private Table T_tabacco;
	private Table T_paper;
	private Table T_matches;
	
	private Agents agent;
	
	public void main() {
		
		mutex = new Semaphore(1);
		counter = new int[1];
		counter[0] = 0;
		for(int k=0;k<array.length;k++) {
			array[k] = new Semaphore(0);
		}
	
		paper = new Semaphore(0); // Permits = 0
		matches = new Semaphore(0);
		tabacco = new Semaphore(0);
		selects = new Semaphore(1); // Permits = 1
		
		agent = new Agents(selects, tabacco, paper, matches);
		T_tabacco = new Table(tabacco,array,mutex,4,"Tabacco",counter);
		T_paper = new Table(paper,array,mutex,2,"Paper",counter);
		T_matches = new Table(matches,array,mutex,1,"Matches",counter);
		
		TabaccoSmoker = new Smokers(selects,counter,array[2],"Tabacco");
		PaperSmoker = new Smokers(selects,counter,array[4],"Paper");
		MatchesSmoker = new Smokers(selects,counter,array[5],"Matches");
		
		
		agent.start();
		T_tabacco.start();
		T_paper.start();
		T_matches.start();
		TabaccoSmoker.start();
		PaperSmoker.start();
		MatchesSmoker.start();
	}
}
