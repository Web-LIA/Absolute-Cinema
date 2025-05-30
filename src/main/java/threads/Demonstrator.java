package main.java.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import main.java.controllers.CinemaController;

public class Demonstrator extends Thread {

	private int filmTime;
	private long frameDuration;
	private int capacidade;

	private CinemaController cinema;
	private final List<String> filmFrames = new ArrayList<>();

	private Semaphore entrada ;
	private Semaphore inicioFilme ;
	private Semaphore cheios;
	private Semaphore saida;

	public Demonstrator(int filmTime, CinemaController cinemaController) {
		this.filmTime = filmTime;
		this.frameDuration = (this.filmTime * 1_000_000_000L) / 10;
		this.capacidade = cinemaController.getPoltronasCinema().getTamanho();
		for (int i = 1; i <= 10; i++) {
			filmFrames.add("-fx-background-image: url(\'/assets/film_frames/film_frame_" + i + ".jpg');");
		}
		this.cinema = cinemaController;
		this.entrada = cinemaController.getEntrada();
		this.inicioFilme = cinemaController.getInicioFilme();
		this.cheios = cinemaController.getCheios();
		this.saida = cinemaController.getSaida();
		setDaemon(true);
	}

	public void showFilm() {
		long lastUpdate = System.nanoTime();
		//System.out.println("filmeee");
		for (int i = 0; i < 10; i++) {
			while (true) { 
				long now = System.nanoTime();
				if (now - lastUpdate >= this.frameDuration) {
					lastUpdate = now;
					cinema.setCinemaScreen(filmFrames.get(i));
					break;
				}

				
			}
				
			
		}
	}
	
	public void waitingFilm(){
		cinema.setCinemaScreen("-fx-background-color: #4C4343;");
	}
	public void endFilm(){
		cinema.setCinemaScreen(" -fx-background-image: url(\'./assets/film_frames/theEnd.jpg\'); ");
	}
	public void capaFilm(){
		cinema.setCinemaScreen( "-fx-background-image: url(\'./assets/film_frames/capa.jpg\'); ");
	}
	
	public void run() {
		while (true) {
			
			try {
				
                // Libera vagas para os fãs entrarem
                entrada.release(capacidade);

				waitingFilm();
                // BLOQUEIA até todos entrarem
                cheios.acquire(capacidade);

                // Libera início do filme para todos
				cinema.setIsFilmRunning(true);
                inicioFilme.release(capacidade);
				cinema.addConsoleText("Demonstrador iniciou o filme");
                // Exibe o filme
				capaFilm();
                showFilm();
				cinema.addConsoleText("Demonstrador encerrou o filme");
				cinema.setIsFilmRunning(false);
				endFilm();
                // Libera fim do filme para todos
                saida.acquire(capacidade);

            } catch (InterruptedException e) {
                break;
            }

		}
	}

}
