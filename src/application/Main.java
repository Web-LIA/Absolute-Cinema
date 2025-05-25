package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
			Scene scene = new Scene(root);
			Image icon = new Image("/assets/icon.png");

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Absolute Cinema");
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			//primaryStage.setWidth(420);
			//primaryStage.setHeight(420);
			primaryStage.setResizable(false);
			//primaryStage.setX(50);
			//primaryStage.setY(50);
			//primaryStage.setFullScreen(true);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
