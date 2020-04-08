package lv.avg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.*;

/**
 * JavaFX App
 */
public class Main extends Application {
	private static final String STYLESHEET = Main.class.getResource("style.css").toExternalForm();

	private static final int WIDTH = 800, HEIGHT = 640;

	@Override
	public void start(Stage stage) {
		System.setProperty("prism.lcdtext", "false");
		// if you wish to disable the logger
		// System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
		stage.setTitle("Fizikas Tests");
		stage.setResizable(true);
		stage.setMinWidth(WIDTH);
		stage.setMinHeight(HEIGHT);
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom16.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom32.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom64.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom256.ico")));
		stage.setWidth(WIDTH);
		stage.setWidth(HEIGHT);

		List<Question> questions = new QuestionReader(Main.class.getResourceAsStream("data/questions.txt")).list();

		Random random = new Random();
		if(System.getProperty("lv.avg.seed") != null)
			random.setSeed(Long.getLong("lv.avg.seed"));

		Supplier<Session> sessionFactory = () -> {
			Collections.shuffle(questions, random);
			return new Session(questions);
		};

		var scene = new Scene(new TitleView(sessionFactory), WIDTH, HEIGHT);
		scene.getStylesheets().add(STYLESHEET);

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}