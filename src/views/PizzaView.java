package views;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PizzaView {

	private final ImageView imageView;
	private double x, y;
	private final Pane root;
	private final List<Image> pizzaFrames = new ArrayList<>();
	private int pizzaFrame = 1;

	public PizzaView(double x, double y, Pane root) {
		this.root = root;
		this.x = x;
		this.y = y + 10;
		for (int i = 1; i <= 5; i++) {
			pizzaFrames.add(new Image("/assets/pizza_frames/pizza_frame_" + i + ".png"));
		}

		imageView = new ImageView(pizzaFrames.get(0));
		imageView.setFitWidth(20);
		imageView.setFitHeight(20);
		imageView.setLayoutX(x);
		imageView.setLayoutY(y);
		Platform.runLater(() -> root.getChildren().add(imageView));
	}

	public void pizzaAnimation(int eatTime) {
		long init = System.nanoTime();
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000;
		while (init + eatTime * 1_000_000_000L > System.nanoTime()) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.switchFrame(pizzaFrame);
				if (this.pizzaFrame < 5)
					pizzaFrame++;
				else
					pizzaFrame = 1;
			}
		}
		remove();
	}

	public void switchFrame(int newFrameId) {
		Platform.runLater(() -> {
			if (newFrameId >= 1 && newFrameId <= pizzaFrames.size()) {
				this.imageView.setImage(pizzaFrames.get(newFrameId - 1));
			}
		});
	}
	
	public void remove() {
		Platform.runLater(() -> root.getChildren().remove(imageView));
	}

}
