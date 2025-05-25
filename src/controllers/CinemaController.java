package controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	public void initialize() {
		
	}
	
	public void getInitialValues(int capacity, int filmTime) {
		this.capacity = capacity;
		this.filmTime = filmTime;
		labelCapacity.setText(""+capacity);
		labelFilmTime.setText(""+filmTime);
	}

	public void switchToInitScene(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/scenes/init.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
