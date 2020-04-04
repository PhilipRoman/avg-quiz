package lv.avg;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.*;

/**
 * JavaFX App
 */
public class Main extends Application {
	private static final String STYLESHEET = Main.class.getResource("style.css").toExternalForm();
	private Stage stage;

	private Button createAnswerButton(int index) {
		var button = new Button();
		button.setVisible(false);
		button.setOnMouseClicked(event -> submitAnswer(index));
		button.getStyleClass().add("answer-button");
		return button;
	}

	private final ProgressBar progressBar = new ProgressBar();
	private final Label questionLabel = new Label();

	{
		progressBar.setVisible(false);
		progressBar.getStyleClass().add("progress-bar");

		questionLabel.getStyleClass().add("question-label");
	}

	private final List<Button> answerButtons = IntStream.range(0, 5).mapToObj(this::createAnswerButton).collect(Collectors.toList());
	private final List<Question> questions = new QuestionReader(Main.class.getResourceAsStream("data/questions.txt")).list();
	int questionIndex = 0;
	int score = 0;

	private void submitAnswer(int index) {
		Question current = questions.get(questionIndex);
		if(current.answers().get(index).isCorrect()) {
			System.out.println("Correct");
			score++;
		} else {
			System.out.println("Incorrect");
		}

		if(++questionIndex == questions.size()) {
			progressBar.setVisible(false);
			stage.setScene(createResultScene());
			return;
		}

		progressBar.setProgress((questionIndex + 0.5) / questions.size());

		Question next = questions.get(questionIndex);
		setCurrentQuestion(next);
	}

	private void setCurrentQuestion(Question q) {
		questionLabel.setText(q.questionText());
		for(var button : answerButtons)
			button.setVisible(false);
		for(int i = 0; i < q.answers().size(); i++) {
			answerButtons.get(i).setText(q.answers().get(i).text());
			answerButtons.get(i).setVisible(true);
		}
	}

	@Override
	public void start(Stage stage) {
		System.setProperty("prism.lcdtext", "false");
		this.stage = stage;
		stage.setTitle("Tests");
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("data/atom.ico")));

		stage.setScene(createOpeningScene());
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	private Scene createQuestionScene() {
		score = 0;
		questionIndex = 0;
		progressBar.setVisible(true);
		progressBar.setProgress(0.5 / questions.size());
		Collections.shuffle(questions);
		questions.stream().map(Question::answers).forEach(Collections::shuffle);

		var vBox = new VBox();
		vBox.getChildren().addAll(answerButtons);
		vBox.getStyleClass().add("answer-container");

		var pane = new FlowPane(questionLabel, vBox, progressBar);
		pane.setOrientation(Orientation.VERTICAL);

		setCurrentQuestion(questions.get(0));

		var scene = new Scene(pane, 720, 640);
		scene.getStylesheets().add(STYLESHEET);
		return scene;
	}

	private Scene createOpeningScene() {
		var pane = new FlowPane();
		pane.setOrientation(Orientation.VERTICAL);
		pane.setVgap(10);
		var button = new Button("Sākt testu!");
		button.getStyleClass().add("start-button");
		pane.getChildren().add(button);
		var scene = new Scene(pane, 720, 640);
		scene.getStylesheets().add(STYLESHEET);
		button.setOnMouseClicked(event -> {
			stage.setScene(createQuestionScene());
		});
		return scene;
	}

	private Scene createResultScene() {
		var result = new Result(score, questions.size());

		var pane = new FlowPane();
		pane.setOrientation(Orientation.VERTICAL);
		pane.setVgap(10);

		var percentText = new Text(result.percent() + "%");
		percentText.setStyle("-fx-font-size: 70; -fx-text-align: center;");
		percentText.setFill(Paint.valueOf(result.quality().color()));
		var percentTextFlow = new TextFlow(percentText);
		percentTextFlow.setTextAlignment(TextAlignment.CENTER);
		pane.getChildren().add(percentTextFlow);

		var boldText = new Text(result.correct() + "/" + result.total());
		boldText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		boldText.setFill(Paint.valueOf(result.quality().color()));
		pane.getChildren().add(new TextFlow(new Text("Tavs rezultāts: "), boldText));

		var button = new Button("Mēģināt vēlreiz");
		button.getStyleClass().add("start-button");
		pane.getChildren().add(button);

		var scene = new Scene(pane, 720, 640);
		scene.getStylesheets().add(STYLESHEET);

		button.setOnMouseClicked(event -> ((Stage) scene.getWindow()).setScene(createQuestionScene()));
		return scene;
	}
}