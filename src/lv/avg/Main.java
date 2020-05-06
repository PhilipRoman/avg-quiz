package lv.avg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.*;

import java.util.*;
import java.io.*;
import java.util.function.*;

/**
 * JavaFX App
 */
public class Main extends Application {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static final String STYLESHEET = Main.class.getResource("style.css").toExternalForm();

	private static final int WIDTH = 1000, HEIGHT = 640;

	@Override
	public void start(Stage stage) throws IOException {
		System.setProperty("prism.lcdtext", "false");
		// if you wish to disable the logger
		// System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
		stage.setTitle("Fizikas Tests");
		stage.setResizable(true);
		stage.setMinWidth(WIDTH);
		stage.setMinHeight(HEIGHT);
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom16.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom32.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom64.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom128.ico")));
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom256.ico")));
		stage.setWidth(WIDTH);
		stage.setWidth(HEIGHT);

		Random random = new Random();
		if(System.getProperty("lv.avg.seed") != null) {
			long seed = Long.getLong("lv.avg.seed");
			random.setSeed(seed);
			log.info("Set random seed to {} because property \"lv.avg.seed\" was set", seed);
		}

		if(Boolean.getBoolean("vert")) {
			log.info("Property \"vert\" was set to true");
		} else {
			log.info("Property \"vert\" not detected");
		}

		final QuestionReader questionReader;
		if(System.getProperty("lv.avg.file") != null) {
			String fileName = System.getProperty("lv.avg.file");
			questionReader = new QuestionReader(new FileInputStream(fileName));
			log.info("Set question file to {} because property \"lv.avg.file\" was set", fileName);
		} else if (Boolean.getBoolean("vert")) {			
			questionReader = new QuestionReader(Main.class.getResourceAsStream("data/questions-vert.txt"));
		} else {			
			questionReader = new QuestionReader(Main.class.getResourceAsStream("data/questions.txt"));
		}

		List<Question> questions = questionReader.list();

		Function<Login, Session> sessionFactory = login -> {
			Collections.shuffle(questions, random);
			return new Session(questions, login);
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
