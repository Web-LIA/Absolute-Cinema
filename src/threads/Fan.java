package threads;

import controllers.CinemaController;
import views.FanView;
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
		FanView fan = cinema.createFanView(id);
		long lastUpdate = System.nanoTime();
		final long frameDuration = 42_000_000; // 42 ms

		while (cinema.getIsRunning()) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				fan.walk();
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
