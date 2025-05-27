package threads;

import controllers.CinemaController;
import java.util.concurrent.Semaphore;

public class Fan extends Thread {

	private int id;
	private int eatTime;
	private Semaphore S;
	private CinemaController cinema;

	public Fan(int eatTime, CinemaController cinemaController) {
		this.id = cinemaController.getFanId();
		this.S = cinemaController.getSemaphore();
		this.eatTime = eatTime;
		this.cinema = cinemaController;
	}

	public void eat() {

	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				cinema.initFanAnimation(this.id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
