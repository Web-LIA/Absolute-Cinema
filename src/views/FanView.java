package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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

		for (int i = 1; i <= 92; i++) {
			fanFrames.add(new Image("/assets/chaves_frames/chaves_frame_" + i + ".png"));
		}

		imageView = new ImageView(fanFrames.get(51));
		imageView.setFitWidth(50);
		imageView.setFitHeight(50);
		imageView.setLayoutX(x);
		imageView.setLayoutY(y);
		// testando mudar a animação ao clicar
		imageView.setOnMouseClicked(e -> {
			// if (this.viewDirection == 4)
			// this.changeDirection(1);
			// else
			// this.changeDirection(this.viewDirection + 1);
			System.out.println("X: " + this.x + "Y: " + this.y);
		});

		// root.getChildren().add(imageView);
		Platform.runLater(() -> root.getChildren().add(imageView));
	}

	public void entryQueueAnimation(double x, double y){
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (true) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;	
				if(this.x> x)
					walk();
				else if (this.y < y){
					this.viewDirection = 3;
					walk();
				}else 
					break;
			}
		}
	}

	public void entryAnimation(boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 3;
				if (this.y < 415)
					walk();
				else if (this.x > 640) {
					this.viewDirection = 2;
					walk();
				} else
					break;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void goToCinemaChairAnimation(double chairX, double chairY, boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;
				if (this.x > chairX)
					walk();
				else if (this.y > chairY) {
					this.viewDirection = 1;
					walk();
				} else {
					this.switchFrame(69);
					this.walkFrame = 69;
					break;
				}
			}
		}
	}

	public void watchFilmAnimation(AtomicBoolean isFilmRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 100_000_000; // 42 ms
		while (isFilmRunning.get()) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				switchFrame(walkFrame);
				int walkFrame0 = 81;
				if (this.walkFrame >= walkFrame0 && this.walkFrame < walkFrame0 + 2)
					this.walkFrame++;
				else
					this.walkFrame = walkFrame0;
				
				
			}
		}
	}

	public void goToRefectoryAnimation(boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;
				if (this.x > 222)
					walk();
				else if (this.y < 415) {
					this.viewDirection = 3;
					walk();
				} else {
					this.viewDirection = 2;
					walk();
					walk();
					break;
				}
			}
		}
	}

	public void goToRefectoryChairAnimation(double chairX, double chairY, boolean backwards, boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;
				if (this.x > 194)
					walk();
				else if (this.y > chairY) {
					this.viewDirection = 1;
					walk();
				} else if (this.x > chairX) {
					this.viewDirection = 2;
					walk();
				} else {
					if (backwards)
						this.switchFrame(71);
					else
						this.switchFrame(77);
					break;
				}
			}
		}
	}

	public void goOutToEatAnimation(boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;
				if (this.x > 46)
					walk();
				else if (this.y < 474) {
					this.viewDirection = 3;
					walk();
				} else
					break;
			}
		}
	}

	public void goFromRefectoryToExitAnimation(boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 2;
				if (this.x > 26)
					walk();
				else if (this.y > 250) {
					this.viewDirection = 1;
					walk();
				} else
					break;
			}
		}
	}

	public void goFromOutToExitAnimation(boolean isRunning) {
		long lastUpdate = System.nanoTime();
		final long frameDuration = 65_000_000; // 42 ms
		while (isRunning) {
			long now = System.nanoTime();
			if (now - lastUpdate >= frameDuration) {
				lastUpdate = now;
				this.viewDirection = 1;
				if (this.y < 326)
					walk();
				else {
					this.goFromRefectoryToExitAnimation(isRunning);
					break;
				}
			}
		}
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
		Platform.runLater(() -> {
			if (newFrameId >= 1 && newFrameId <= fanFrames.size()) {
				this.imageView.setImage(fanFrames.get(newFrameId - 1));
			}
		});
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
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public ImageView getImageView () {
		return this.imageView;
	}

	public void remove() {
		Platform.runLater(() -> root.getChildren().remove(imageView));
	}
	public void invisible(){
		imageView.setOpacity(0);
	}
	public void show(){
		imageView.setOpacity(100);
	}
}
