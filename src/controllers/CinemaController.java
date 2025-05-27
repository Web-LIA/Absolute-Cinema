package controllers;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Node;

public class CinemaController {

	@FXML
	private Label labelCapacity;
	@FXML
	private Label labelFilmTime;

	private int capacity;
	private int filmTime;
	private Semaphore S;
	private final AtomicInteger fanId = new AtomicInteger(1);

	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean isRunning;

	private Stage newFanStage;

	@FXML
	public void initialize() {
		this.isRunning = true;
	}

	public void getInitialValues(int capacity, int filmTime, Stage stage) {
		this.capacity = capacity;
		this.filmTime = filmTime;
		labelCapacity.setText("" + capacity);
		labelFilmTime.setText("" + filmTime);
		this.stage = stage;
		this.stage.setOnCloseRequest(event -> {
			isRunning = false;
		});
	}

	public Semaphore getSemaphore() {
		return this.S;
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
			newFanStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initFanAnimation(int id) {
		System.out.println("Iniciada animação do fan " + id); // teste
	}

}
