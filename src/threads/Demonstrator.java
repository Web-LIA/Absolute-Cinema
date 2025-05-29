package threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import controllers.CinemaController;

public class Demonstrator extends Thread {

	private int filmTime;
	private long frameDuration;
	private Semaphore S;
	private CinemaController cinema;
	private final List<String> filmFrames = new ArrayList<>();

	public Demonstrator(int filmTime, CinemaController cinemaController) {
		this.filmTime = filmTime;
		this.frameDuration = (this.filmTime * 1_000_000_000L) / 10;
		// this.S = S;
		for (int i = 1; i <= 10; i++) {
			filmFrames.add("-fx-background-image: url(\'./assets/film_frames/film_frame_" + i + ".jpg');");
		}
		this.cinema = cinemaController;
	}

	public void showFilm() {
		long lastUpdate = System.nanoTime();
		//System.out.println("filmeee");
		for (int i = 0; i < 10; i++) {
			while (cinema.getIsRunning()) {
				long now = System.nanoTime();
				if (now - lastUpdate >= this.frameDuration) {
					lastUpdate = now;
					cinema.setCinemaScreen(filmFrames.get(i));
					System.out.println(i);
					break;
				}

				// tirar depois
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	public void run() {
		this.showFilm();
	}

}
