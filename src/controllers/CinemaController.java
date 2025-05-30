package controllers;

import views.FanView;
import threads.Demonstrator;
import threads.Fan;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Fila;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CinemaController {
	
	@FXML
	private Pane cinemaScreen;
	@FXML
	private Label labelFilmTime;
	@FXML
	private Label labelDemonstratorStatus;
	@FXML
	private Label labelIdThread;
	@FXML
	private Label labelEatTimeThread;
	@FXML
	private Label labelStatusThread;
	@FXML
	private Label labelVoidQueue;
	@FXML
	private Label labelVoidRefectory;
	
	@FXML
	private TextArea consoleText;

	private int capacity;
	private int filmTime;
	private Semaphore cadeiras = new Semaphore(1, true); // para controlar as escolhas das 
	private Semaphore poltronas = new Semaphore(1, true); // para controlar as poltronas do cinema
	private Semaphore filaForaCinema = new Semaphore(1,true);// controlar fila de fora do cinema
	private Semaphore filaAndou = new Semaphore(0,true);
	//private Semaphore consoleSemaphore = new Semaphore(1,true);

	private Semaphore entrada = new Semaphore(0, true);
	private Semaphore inicioFilme = new Semaphore(0);
	private Semaphore cheios = new Semaphore(0);
	private Semaphore saida = new Semaphore(0, true);

	private final AtomicBoolean isFilmRunning = new AtomicBoolean(false);

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
	}

	private void initCadeirasPraca() {
		this.cadeirasPraca = new Fila(13);
		double [][] cadeirasXY = {
			{ 169, 410, 1}, {117,410, 1}, {70,410, 1}, 
			{ 171, 372, 0}, {120,372, 0}, {71,372, 0}, 
			{ 172, 330, 1}, {152,330, 1}, {133,330, 1}, {113,330, 1},{94,330, 1},{74,330, 1},{54,330, 1}
		}; 
		for (int i = 0; i < this.cadeirasPraca.getTamanho(); i++) {
			this.cadeirasPraca.addXY(cadeirasXY[i][0], cadeirasXY[i][1] , cadeirasXY[i][2]);  
		}
	}
	private void initFilaCinema(){
		
		double cadeirasX = 679;
		ArrayList<Double> cadeirasY = new ArrayList<>();
		double init = 415;
		while (init > 87) { 
			cadeirasY.add(init);
			init-= 42;
		}
		this.filaCinema = new Fila(cadeirasY.size());
		for (int i = 0; i < this.filaCinema.getTamanho(); i++) {
			this.filaCinema.addXY(cadeirasX, cadeirasY.get(i) ,0);  
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
		Demonstrator demonstrator = new Demonstrator(this.filmTime, this);
		demonstrator.start();
		this.cinemaScreen.setOnMouseEntered(e -> {
			demonstrator.getState();
			this.setDemonstratorLabels(filmTime + "", demonstrator.getState().toString());
		});
		this.cinemaScreen.setOnMouseExited(e ->{
			this.clearDemonstratorLabels();
		});
	}

	public void setCinemaScreen(String style) {
		this.cinemaScreen.setStyle(style);
	}
	public void addConsoleText(String text) {
		Platform.runLater(() -> {
			this.consoleText.setText( text + "\n" + consoleText.getText());
		});
		
		
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
		if(this.voidCinema.contains(id))
			this.voidCinema.remove(Integer.valueOf(id));
	}
	public void removeVoidPraca(int id) {
		if(this.voidPraca.contains(id))
			this.voidPraca.remove(Integer.valueOf(id));
	}
	
	public Fila getFilaCinema(){
		return this.filaCinema;
	}
	public Fila getCadeirasPraca() {
		return this.cadeirasPraca;
	}
	public Semaphore getFilaAndou(){
		return  this.filaAndou;
	}
	public Semaphore getEntrada(){
		return this.entrada;
	}
	public Semaphore getInicioFilme(){
		return this.inicioFilme;
	}
	public Semaphore getCheios(){
		return this.cheios;
	}
	public Semaphore getSaida(){
		return this.saida;
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
	public boolean getIsFilmRunning(){
		return this.isFilmRunning.get();
	}
	public AtomicBoolean getIsFilmRunningObject(){
		return this.isFilmRunning;
	}
	public void setIsFilmRunning(boolean set){
		this.isFilmRunning.set(set);
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
	
	public void setSelectedFanLabels(String id, String eatTime, String status) {
		this.labelIdThread.setText("Id: " + id);
		this.labelEatTimeThread.setText("Tempo de lanche: " + eatTime + "s");
		this.labelStatusThread.setText("Status: " + status);
	}
	
	public void clearSelectedFanLabels() {
		this.labelIdThread.setText("");
		this.labelEatTimeThread.setText("");
		this.labelStatusThread.setText("");
	}
	
	public void setDemonstratorLabels(String filmTime, String status) {
		this.labelFilmTime.setText("Tempo de Exibição: " + filmTime + "s");
		this.labelDemonstratorStatus.setText("Status: " + status);
	}
	
	public void clearDemonstratorLabels() {
		this.labelFilmTime.setText("");
		this.labelDemonstratorStatus.setText("");
	}
	
	public void setLabelVoidQueue(int quantity) {
		String text = quantity > 0 ? quantity + "" : "";
		Platform.runLater(() -> {
			this.labelVoidQueue.setText(text);
		});
		
		
	}
	
	public void clearLabelVoidQueue() {
		this.labelVoidQueue.setText("");
	}
	
	public void setLabelVoidRefectory(int quantity) {
		String text = quantity > 0 ? quantity + "" : "";
		Platform.runLater(() -> {
			this.labelVoidRefectory.setText(text);
		});
	}
	
	public void clearLabelVoidRefectory() {
		this.labelVoidRefectory.setText("");
	}

	public synchronized FanView createFanView(int id, int eatTime, Fan fanThread) {
		FanView fan = new FanView(id, (Pane) stage.getScene().getRoot());
		fan.getImageView().setOnMouseEntered(e -> {
			fanThread.getState();
			this.setSelectedFanLabels(id+"", eatTime+"", fanThread.getState().toString());
		});
		fan.getImageView().setOnMouseExited(e -> {
			this.clearSelectedFanLabels();
		});
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
