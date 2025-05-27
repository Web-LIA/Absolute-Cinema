package threads;

import java.util.concurrent.Semaphore;

public class Fan extends Thread {

	private int id;
	private int lanchTime;
	private Semaphore S;

	public Fan(int id, int lanchTime, Semaphore S) {
		this.id = id;
		this.lanchTime = lanchTime;
		this.S = S;
	}

	public void eat() {

	}

	public void run() {

	}

}
