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
	private Semaphore filaCapacidade;
	private Semaphore filaAndou;
	private Semaphore entrada ;
	private Semaphore inicioFilme ;
	private Semaphore cheios;
	private Semaphore saida ;

	public Fan(int eatTime, CinemaController cinemaController) {
		this.id = cinemaController.getFanId();
		this.S = cinemaController.getSemaphore();
		this.eatTime = eatTime;
		this.cinema = cinemaController;
		this.cadeirasSemaphore = cinemaController.getCadeiras();
		this.poltronasSemaphore = cinemaController.getPoltronas();
		this.filaForaCinema = cinemaController.getFilaForaCinema();
		this.entrada = cinemaController.getEntrada();
		this.inicioFilme = cinemaController.getInicioFilme();
		this.cheios = cinemaController.getCheios();
		this.saida = cinemaController.getSaida();
		this.filaCapacidade = cinemaController.getFilaCapacidade();
		this.filaAndou = cinemaController.getFilaAndou();
		setDaemon(true);
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
			cinema.removeVoidPraca(id);
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
			cinema.removeVoidCinema(id);
			poltronas.addPerson(posicao,id);
			//Thread.sleep(100); // 
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
			if(posicao == 0) {
				poltronas.removeFifo();
			} else {
				
			}
		} catch (Exception e) {
			Thread.currentThread().interrupt(); // Restaura o estado de interrupção
		}finally {
			filaForaCinema.release();
		}
	}
	
	public void run() {
		FanView fan = cinema.createFanView(id,eatTime,this);
		while (true) {
			try {
				fan.invisible();
				fan.moveTo(680, 60);
				cinema.addConsoleText("Fan " + id + " entrou na fila");
				while(true){
					
					escolherFilaCinema();
					if(cinema.getFilaCinema().positionPerson(id) != -1){
						fan.show();
						fan.entryQueueAnimation(cinema.getFilaCinema().getPerson(id)[0], cinema.getFilaCinema().getPerson(id)[1]);
					}
					if(cinema.getFilaCinema().positionPerson(id) == 0)break;
					
					filaAndou.acquire();
				}
				
				//Fã na fila do cinema
				fan.entryAnimation(true);
				entrada.acquire();
				sairFilaForaCinema();
				filaAndou.release(cinema.getFilaCinema().getTamanho() + cinema.getVoidCinema().size());
				
				escolherPoltronaCinema();
				cinema.addConsoleText("Fan " + id + " entrou no cinema");
				fan.goToCinemaChairAnimation( cinema.getPoltronasCinema().getPerson(id)[0], cinema.getPoltronasCinema().getPerson(id)[1], true);
				
				// Animção Fã entrando no cinema
				

				// Fã entra no cinema
				cheios.release();

				//Fã esperando filme
				inicioFilme.acquire();

				fan.watchFilmAnimation(cinema.getIsFilmRunningObject());
				// Animação Fã saindo do cinema
				sairPoltronaCinema();
				fan.goToRefectoryAnimation(true);
				saida.release();
				escolherCadeiraPraca();
				cinema.addConsoleText("Fan " + id + " entrou na praca de alimentacao");
				if(cinema.getCadeirasPraca().positionPerson(id) != -1){
					fan.goToRefectoryChairAnimation(cinema.getCadeirasPraca().getPerson(id)[0], cinema.getCadeirasPraca().getPerson(id)[1], cinema.getCadeirasPraca().getPerson(id)[2]==1 , true);
					Thread.sleep(1000 * eatTime);
					sairCadeiraPraca();
					fan.goFromRefectoryToExitAnimation(true);
				}else {
					fan.goOutToEatAnimation(true);
					Thread.sleep(1000 * eatTime);
					cinema.removeVoidPraca(id);
					fan.goFromOutToExitAnimation(true);
				}
				

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
