package threads;

import java.util.concurrent.Semaphore;

public class Demonstrator extends Thread {

	private int filmTime;
	private Semaphore S;

	public Demonstrator(int filmTime, Semaphore S) {
		this.filmTime = filmTime;
		this.S = S;
	}

	public void showFilm() {
		
	}

	public void run() {

	}

}
