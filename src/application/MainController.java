package application;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

	@FXML
	private ImageView chavesImg;

	@FXML
	public void initialize() {
		Image img = new Image(getClass().getResourceAsStream("/assets/chaves.jpeg"));
		chavesImg.setImage(img);
	}
}
