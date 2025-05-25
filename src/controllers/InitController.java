package controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InitController {

	@FXML
	private ImageView chavesImg;
	@FXML
	private TextField inputCapacity;
	@FXML
	private TextField inputFilmTime;
	@FXML
	private Label labelError;

	private Stage stage;
	private Scene scene;
	private Parent root;
	private int capacity;
	private int filmTime;

	@FXML
	public void initialize() {
		Image img = new Image(getClass().getResourceAsStream("/assets/chaves.jpeg"));
		chavesImg.setImage(img);
	}

	public void getInputValues() throws Exception {
		try {
			capacity = Integer.parseInt(inputCapacity.getText());
			filmTime = Integer.parseInt(inputFilmTime.getText());			
		} catch (Exception e) {
			throw new Exception("Digite números inteiros");
		}
		if (capacity < 1 || capacity > 10) {
			throw new Exception("Capacidade do autório inválida");
		}
		if (filmTime < 1 || filmTime > 120) {
			throw new Exception("Tempo de exibição inválido");
		}
	}

	public void switchToCinemaScene(ActionEvent event) throws IOException {
		try {
			getInputValues();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/cinema.fxml"));
			root = loader.load();
			CinemaController cinemaController = loader.getController();
			cinemaController.getInitialValues(capacity, filmTime);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			labelError.setText(e.getMessage());
		}
	}
}
