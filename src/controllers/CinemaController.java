package controllers;

import views.FanView;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Fila;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CinemaController {

	private int capacity;
	private int filmTime;
	private Semaphore S;
	private Semaphore cadeiras = new Semaphore(1, true); // para controlar as escolhas das 
	private Semaphore poltronas = new Semaphore(1, true); // para controlar as poltronas do cinema
	private Semaphore filaForaCinema = new Semaphore(1,true);// controlar fila de fora do cinema

	private final AtomicInteger fanId = new AtomicInteger(1);

	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean isRunning;

	private Stage newFanStage;
	private final List<FanView> fanViews = new ArrayList<>();
	
	private Fila poltronasCinema ; // fila para as poltronas do cinema
	private Fila cadeirasPraca;
	private Fila filaCinema;

	private ArrayList<Integer> voidCinema = new ArrayList<>();
	private ArrayList<Integer> voidPraca = new ArrayList<>();

	@FXML
	public void initialize() {
		this.isRunning = true;
	}
	private void initPoltronasCinema() {
		this.poltronasCinema = new Fila(this.capacity);
		double []poltronasY = {271,300,328,355,382,412};
		double []poltronasX = {244,273,300,327,356,384,412,439,468,498,525,554,583};
		int count = 0;

		for(int i = 0; i< poltronasY.length;i++){
			int menos = 3-i > 0 ? 3-i: 0;
			for(int j= menos ; j < poltronasX.length - menos; j++){
				if(count < this.capacity){
					this.poltronasCinema.addXY(poltronasX[j],poltronasY[i], 1);
					count++;
				}
			}
		}
		poltronasCinema.showStatus();
	}

	private void initCadeirasPraca() {
		this.cadeirasPraca = new Fila(13);
		double [][] cadeirasXY = {
			{ 100, 100, 0 }, {200,200, 0}, {300,300, 0}, {400,400, 0},
			{500,500, 0}, {600,600, 0}, {700,700, 0}, {800,800, 0},
			{900,900, 0}, {1000,1000, 0},{800,800, 0},{800,800, 0},{800,800, 0}
		}; 
		for (int i = 0; i < this.cadeirasPraca.getTamanho(); i++) {
			this.cadeirasPraca.addXY(cadeirasXY[i][0], cadeirasXY[i][1] , cadeirasXY[i][2]);  
		}
	}
	private void initFilaCinema(){
		this.filaCinema = new Fila(15);
		double [][] cadeirasXY = {
			{ 100, 100 }, {200,200}, {300,300}, {400,400},
			{500,500}, {600,600}, {700,700}, {800,800},
			{900,900}, {1000,1000}, { 4, 1 }, { 4, 1 }, { 4, 1 }, { 4, 1 }, { 4, 1 }
		}; 
		for (int i = 0; i < this.filaCinema.getTamanho(); i++) {
			this.filaCinema.addXY(cadeirasXY[i][0], cadeirasXY[i][1] ,0);  
		}
	}
	public void getInitialValues(int capacity, int filmTime, Stage stage) {
		this.capacity = capacity;
		this.filmTime = filmTime;
		this.stage = stage;
		this.stage.setOnCloseRequest(event -> {
			isRunning = false;
		});
		initPoltronasCinema();
		initCadeirasPraca();
		initFilaCinema();
	}
	
	public ArrayList<Integer> getVoidCinema() {
		return this.voidCinema;
	}
	public ArrayList<Integer> getVoidPraca() {
		return this.voidPraca;
	}
	public void addVoidCinema(int id) {
		if (!this.voidCinema.contains(id)) {
			this.voidCinema.add(id);
		}
	}
	public void addVoidPraca(int id) {
		if (!this.voidPraca.contains(id)) {
			this.voidPraca.add(id);
		}
	}
	public void removeVoidCinema(int id) {
		this.voidCinema.remove(Integer.valueOf(id));
	}
	public void removeVoidPraca(int id) {
		this.voidPraca.remove(Integer.valueOf(id));
	}
	
	public Fila getFilaCinema(){
		return this.filaCinema;
	}
	public Fila getCadeirasPraca() {
		return this.cadeirasPraca;
	}
	public Semaphore getSemaphore() {
		return this.S;
	}

	public Fila getPoltronasCinema() {
		return this.poltronasCinema;
	}
	public Semaphore getCadeiras() {
		return this.cadeiras;
	}
	public Semaphore getPoltronas() {
		return this.poltronas;
	}
	public Semaphore getFilaForaCinema(){
		return this.filaForaCinema;
	}
	public int getFanId() {
		return this.fanId.getAndIncrement();
	}

	public boolean getIsRunning() {
		return this.isRunning;
	}

	public void switchToInitScene(ActionEvent event) throws IOException {
		this.isRunning = false;
		root = FXMLLoader.load(getClass().getResource("/scenes/init.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public void openNewFanWindow() {
		if (newFanStage != null && newFanStage.isShowing()) {
			newFanStage.toFront();
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/new_fan.fxml"));
			Parent root = loader.load();

			NewFanController controller = loader.getController();
			controller.setCinemaController(this);

			newFanStage = new Stage();
			newFanStage.setTitle("Adicionar Fan");
			newFanStage.setScene(new Scene(root));
			newFanStage.initModality(Modality.APPLICATION_MODAL); // bloqueia interação com a principal
			newFanStage.setOnCloseRequest(e -> newFanStage = null); // se o usuário fechar manualmente
			newFanStage.setResizable(false);
			newFanStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized FanView createFanView(int id) {
		FanView fan = new FanView(id, (Pane) stage.getScene().getRoot());
		//double baseX = 10;
		//double spacing = 50;
		//int position = fanViews.size();
		//fan.moveTo(baseX + spacing * position, 300);
		//double x = stage.getScene().getWidth()/2;
		//System.out.println(""+x);
		//fan.moveTo(stage.getScene().getWidth()/2,  stage.getScene().getHeight()/2);
		fanViews.add(fan);
		return fan;
	}

}
