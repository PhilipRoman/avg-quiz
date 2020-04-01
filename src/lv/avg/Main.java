package lv.avg;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
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
	class AnswerButton extends Button {
		AnswerButton(int index) {
			setVisible(false);
			setMinWidth(350);
			setMaxWidth(350);
			setMinHeight(40);
			setWrapText(true);
			setFont(Font.font("Segoe UI", 16));
			setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 0;");
			setOnMouseClicked(event -> submitAnswer(index));
		}
	}

	class QuestionLabel extends Label {

		QuestionLabel() {
			setFont(Font.font("Segoe Light", 24));
			setWrapText(true);
			setMaxWidth(500);
		}
	}

	private final QuestionLabel questionLabel = new QuestionLabel();
	private final List<Button> answerButtons = IntStream.range(0, 8).mapToObj(AnswerButton::new).collect(Collectors.toList());
	private final List<Question> questions = new QuestionReader(Main.class.getResourceAsStream("data/questions.txt")).list();
	int questionIndex = 0;

	private void setCurrentQuestion(Question q) {
		questionLabel.setText(q.questionText());
		for(var button : answerButtons)
			button.setVisible(false);
		for(int i = 0; i < q.answers().size(); i++) {
			answerButtons.get(i).setText(q.answers().get(i).text());
			answerButtons.get(i).setVisible(true);
		}
	}

	private void submitAnswer(int index) {
		Question current = questions.get(questionIndex);
		if(current.answers().get(index).isCorrect())
			System.out.println("Correct");
		else
			System.out.println("Incorrect");

		if(++questionIndex == questions.size())
			System.exit(0);

		Question next = questions.get(questionIndex);
		setCurrentQuestion(next);
	}

	@Override
	public void start(Stage stage) {
		System.setProperty("prism.lcdtext", "false");
		stage.setTitle("Tests");

		var pane = new FlowPane();
		pane.getChildren().add(questionLabel);
		pane.getChildren().addAll(answerButtons);
		pane.setOrientation(Orientation.VERTICAL);
		pane.setPadding(new Insets(40));
		pane.setVgap(10);

		setCurrentQuestion(questions.iterator().next());

		var scene = new Scene(pane, 720, 640);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}