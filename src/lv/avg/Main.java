package lv.avg;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.*;

/**
 * JavaFX App
 */
public class Main extends Application {

	@Override
	public void start(Stage stage) {
		System.setProperty("prism.lcdtext", "false");
		stage.setTitle("Tests");

		var question = new Label("");
		List<Question> questions = List.of(
			new Question("Kādas ierīces jāizvēlas, lai noteiktu kāds lādiņa lielums elektriskajā ķēdē izplūst " +
				"caur vadītāja šķērsgriezuma laukumu?", List.of(
				new Answer("Bīdmērs", false),
				new Answer("Ampērmetrs un bīdmērs", true),
				new Answer("Ampērmetrs un hronometrs", false),
				new Answer("Hronometrs un bīdmērs", false)
			))
		);
		question.setText(questions.get(0).getQuestion());
		question.setFont(Font.font("Segoe Light", 24));
		question.setWrapText(true);
		question.setMaxWidth(500);

		List<Button> buttons = Stream.generate(Button::new).limit(8).collect(Collectors.toList());
		for(Button b : buttons) {
			b.setVisible(false);
			b.setMinWidth(350);
			b.setMaxWidth(350);
			b.setMinHeight(40);
			b.setFont(Font.font("Segoe UI", 16));
			b.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 0;");
		}

		for(int i = 0; i < questions.get(0).answers().size(); i++) {
			buttons.get(i).setText(questions.get(0).answers().get(i).text());
			buttons.get(i).setVisible(true);
		}

		var pane = new FlowPane(
			Orientation.VERTICAL,
			Stream.concat(Stream.of(question), buttons.stream()).toArray(Node[]::new));
		pane.setPadding(new Insets(40));
		pane.setVgap(10);

		var scene = new Scene(pane, 720, 640);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}