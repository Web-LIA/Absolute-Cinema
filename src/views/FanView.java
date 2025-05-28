package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class FanView {
	private final int id;
	private final ImageView imageView;
	private double x, y;
	private final Pane root;
	private final List<Image> fanFrames = new ArrayList<>();
	private int walkFrame = 1;
	private int viewDirection = 3; // 1=up, 2=rigth, 3=down, 4=left

	public FanView(int id, Pane root) {
		this.id = id;
		this.root = root;
		this.x = 680;
		this.y = 60;

		for (int i = 1; i <= 68; i++) {
			fanFrames.add(new Image("/assets/chaves_frames/chaves_frame_" + i + ".png"));
		}

		imageView = new ImageView(fanFrames.get(0));
		imageView.setFitWidth(50);
		imageView.setFitHeight(50);
		imageView.setLayoutX(x);
		imageView.setLayoutY(y);
		// testando mudar a animação ao clicar
		imageView.setOnMouseClicked(e -> {
			if (this.viewDirection == 4)
				this.changeDirection(1);
			else
				this.changeDirection(this.viewDirection + 1);
		});

		// root.getChildren().add(imageView);
		Platform.runLater(() -> root.getChildren().add(imageView));
	}

	public void walk() {
		switchFrame(walkFrame);
		int walkFrame0 = (this.viewDirection - 1) * 9 + 33;
		if (this.walkFrame >= walkFrame0 && this.walkFrame < walkFrame0 + 8)
			this.walkFrame++;
		else
			this.walkFrame = walkFrame0;

		if (viewDirection == 1)
			moveTo(x, y - 2);
		if (viewDirection == 2)
			moveTo(x - 2, y);
		if (viewDirection == 3)
			moveTo(x, y + 2);
		if (viewDirection == 4)
			moveTo(x + 2, y);
	}
	
	public void run() {
		switchFrame(walkFrame);
		int walkFrame0 = (this.viewDirection - 1) * 8 + 1;
		if (this.walkFrame >= walkFrame0 && this.walkFrame < walkFrame0 + 7)
			this.walkFrame++;
		else
			this.walkFrame = walkFrame0;

		if (viewDirection == 1)
			moveTo(x, y - 2);
		if (viewDirection == 2)
			moveTo(x - 2, y);
		if (viewDirection == 3)
			moveTo(x, y + 2);
		if (viewDirection == 4)
			moveTo(x + 2, y);
	}

	public void changeDirection(int newDirection) {
		if (newDirection > 0 && newDirection < 5) {
			this.viewDirection = newDirection;
		}
	}

	public void switchFrame(int newFrameId) {
		this.imageView.setImage(fanFrames.get(newFrameId - 1));
	}

	public void moveTo(double newX, double newY) {
		Platform.runLater(() -> {
			this.x = newX;
			this.y = newY;
			imageView.setLayoutX(x);
			imageView.setLayoutY(y);
		});
	}

	public int getId() {
		return id;
	}

	public void remove() {
		Platform.runLater(() -> root.getChildren().remove(imageView));
	}
}
