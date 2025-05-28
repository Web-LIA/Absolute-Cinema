package controllers;

import views.FanView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CinemaController {

	private int capacity;
	private int filmTime;
	private Semaphore S;
	private final AtomicInteger fanId = new AtomicInteger(1);

	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean isRunning;

	private Stage newFanStage;
	private final List<FanView> fanViews = new ArrayList<>();

	@FXML
	public void initialize() {
		this.isRunning = true;
	}

	public void getInitialValues(int capacity, int filmTime, Stage stage) {
		this.capacity = capacity;
		this.filmTime = filmTime;
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
