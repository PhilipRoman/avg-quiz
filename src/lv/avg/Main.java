package lv.avg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Main extends Application {

	@Override
	public void start(Stage stage) {
		var systemInfo = new SystemInfo();
		var javaVersion = systemInfo.javaVersion();
		var javafxVersion = systemInfo.javaFXVersion();

		var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
		var scene = new Scene(new StackPane(label), 900, 640);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}