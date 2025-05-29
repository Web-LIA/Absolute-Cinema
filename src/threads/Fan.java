package threads;

import controllers.CinemaController;
import java.util.concurrent.Semaphore;
import utils.Fila;
import views.FanView;

public class Fan extends Thread {

	private int id;
	private int eatTime;
	private Semaphore S;
	private CinemaController cinema;
	private Semaphore cadeirasSemaphore;
	private Semaphore poltronasSemaphore;
	private Semaphore filaForaCinema;
	public Fan(int eatTime, CinemaController cinemaController, Semaphore cadeiras, Semaphore poltronas,Semaphore filaCinema) {
		this.id = cinemaController.getFanId();
		this.S = cinemaController.getSemaphore();
		this.eatTime = eatTime;
		this.cinema = cinemaController;
		this.cadeirasSemaphore = cadeiras;
		this.poltronasSemaphore = poltronas;
		this.filaForaCinema = filaCinema;
	}

	public void eat() {

	}
	public void escolherPoltronaCinema() {
		try {
			poltronasSemaphore.acquire();
			// Simula a escolha da cadeira
			// Aqui você pode adicionar lógica para escolher uma cadeira específica
			Fila poltronas = cinema.getPoltronasCinema();
			int posicao = poltronas.positionEmpty();
			int posicaoPessoa = poltronas.positionPerson(id);
			if(posicaoPessoa != -1){
				return;
			}
			if(posicao == -1) {
				return;
				
			}
			if(posicao < 0 ) {
				return;
			}
			poltronas.addPerson(posicao,id);
			//Thread.sleep(100); // Simula o tempo de escolha da cadeira
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		} finally {
			poltronasSemaphore.release();
		}
	}
	public void sairPoltronaCinema(){
		try {
			poltronasSemaphore.acquire();
			Fila poltronas = cinema.getPoltronasCinema();
			int posicao = poltronas.positionPerson(id);
			if(posicao != -1) {
				poltronas.removePerson(posicao);
			} else {
			}
			
		} catch (Exception e) {
			Thread.currentThread().interrupt();
		} finally{
			poltronasSemaphore.release();
		}
		
	}
	
	public void escolherCadeiraPraca(){
		try {
			cadeirasSemaphore.acquire();
			// Simula a escolha da cadeira
			// Aqui você pode adicionar lógica para escolher uma cadeira específica
			Fila cadeiras = cinema.getCadeirasPraca();
			int posicao = cadeiras.positionEmpty();
			int posicaoPessoa = cadeiras.positionPerson(id);
			if(posicaoPessoa != -1){
				
				return;
			}
			if(posicao == -1) {
				
				cinema.addVoidPraca(id);
				return;
				
			}
			if(posicao < 0 ) {
				
				return;
			}
			cadeiras.addPerson(posicao,id);
			
			//Thread.sleep(100); // Simula o tempo de escolha da cadeira
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		} finally {
			cadeirasSemaphore.release();
		}
	}
	public void sairCadeiraPraca(){
		try {
			cadeirasSemaphore.acquire();
			Fila poltronas = cinema.getCadeirasPraca();
			int posicao = poltronas.positionPerson(id);
			if(posicao != -1) {
				poltronas.removePerson(posicao);
				
			} else {
				
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		}finally {
			cadeirasSemaphore.release();
		}
		

	}
	
	public void escolherFilaCinema(){
		try {
			filaForaCinema.acquire();
			// Simula a escolha da cadeira
			// Aqui você pode adicionar lógica para escolher uma cadeira específica
			Fila poltronas = cinema.getFilaCinema();
			int posicao = poltronas.positionEmpty();
			int posicaoPessoa = poltronas.positionPerson(id);
			if(posicaoPessoa != -1){
				return;
			}
			if(posicao == -1) {
				cinema.addVoidCinema(id);
				return;
				
			}
			if(posicao < 0 ) {
				return;
			}
			poltronas.addPerson(posicao,id);
			//Thread.sleep(100); // Simula o tempo de escolha da cadeira
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		} finally {
			filaForaCinema.release();
		}
	}
	public void sairFilaForaCinema(){
		try {
			filaForaCinema.acquire();
			Fila poltronas = cinema.getFilaCinema();
			int posicao = poltronas.positionPerson(id);
			if(posicao != -1) {
				poltronas.removePerson(posicao);
				
			} else {
				
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		}finally {
			filaForaCinema.release();
		}
	}
	
	public void run() {
		FanView fan = cinema.createFanView(id);
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms

		while (cinema.getIsRunning()) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				fan.walk();
				escolherFilaCinema();
				cinema.getFilaCinema().showStatus();
				System.out.println(cinema.getVoidCinema());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
