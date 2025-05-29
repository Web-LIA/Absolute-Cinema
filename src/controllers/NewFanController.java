package controllers;

import threads.Fan;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewFanController {

	@FXML
	private TextField inputEatTime;

	private CinemaController cinemaController;

	@FXML
	public void initialize() {

	}

	public void setCinemaController(CinemaController controller) {
		this.cinemaController = controller;
	}

	@FXML
	private void addFan() throws Exception {
		try {
			int eatTime = Integer.parseInt(inputEatTime.getText());
			if (eatTime < 1 || eatTime > 120) {
				throw new Exception("");
			}

			Fan fan = new Fan(eatTime, cinemaController,cinemaController.getCadeiras() , cinemaController.getPoltronas(),cinemaController.getFilaForaCinema());
			fan.start();

			Stage stage = (Stage) inputEatTime.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			inputEatTime.setText("");
		}
	}

}
