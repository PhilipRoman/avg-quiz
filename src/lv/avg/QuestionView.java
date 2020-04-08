package lv.avg;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public final class QuestionView extends FlowPane {
	private static final Image EMPTY_IMAGE = new WritableImage(350, 200);
	/**
	 * The factory that was used to obtain a session
	 */
	private final Supplier<Session> factory;

	private final Session session;

	private final ProgressBar progressBar = new ProgressBar();
	private final Label questionLabel = new Label();
	private final ImageView questionImage = new ImageView();
	private final Label progressLabel = new Label();

	private final BooleanProperty locked = new SimpleBooleanProperty(false);

	{
		progressBar.getStyleClass().add("progress-bar");
		questionLabel.getStyleClass().add("question-label");
		questionImage.getStyleClass().add("question-image");
		questionImage.setImage(EMPTY_IMAGE);
		questionImage.setPreserveRatio(true);
	}

	private final List<Button> answerButtons = new ArrayList<>();

	public QuestionView(Session session, Supplier<Session> sessionFactory) {
		this.session = session;
		this.factory = sessionFactory;

		progressBar.progressProperty().bind(
			Bindings.createDoubleBinding(session::progress, session)
		);
		progressLabel.textProperty().bind(
			Bindings.createObjectBinding(() -> (session.index() + 1) + "/" + session.total(), session)
		);
		questionLabel.textProperty().bind(
			Bindings.createObjectBinding(() -> session.currentQuestion().questionText(), session)
		);
		questionImage.imageProperty().bind(
			Bindings.createObjectBinding(() -> getImageForQuestion(session.currentQuestion()), session)
		);

		for(int i = 0; i < session.currentQuestion().answers().size(); i++) {
			int answerIndex = i;
			Button b = new Button();
			b.getStyleClass().add("answer-button");

			b.textProperty().bind(
				Bindings.createObjectBinding(() -> session.currentQuestion().answers().get(answerIndex).text(), session)
			);
			b.setOnMouseClicked(e -> {
				if(!locked.get()) submitAnswer(answerIndex);
			});

			answerButtons.add(b);
		}

		var answerBox = new VBox();
		answerBox.getChildren().addAll(answerButtons);
		answerBox.getStyleClass().add("answer-container");

		var questionBox = new VBox(
			questionLabel,
			questionImage
		);
		questionBox.getStyleClass().add("question-container");

		getChildren().addAll(List.of(
			new HBox(
				questionBox,
				answerBox
			),
			progressBar,
			progressLabel
		));
		setOrientation(Orientation.VERTICAL);
	}

	@NotNull
	private static Image getImageForQuestion(Question question) {
		String path = question.imagePath();
		return path == null ? EMPTY_IMAGE : new Image(Main.class.getResourceAsStream(path));
	}

	private void submitAnswer(int index) {

		Question current = session.currentQuestion();
		Answer correctAnswer = current.correctAnswer();
		int correctIndex = current.answers().indexOf(correctAnswer);

		// lock down buttons while animations are playing
		locked.setValue(true);

		boolean correct = session.submitAnswer(index);

		// animation for wrong answer (if one exists)
		if(!correct) {
			new ColorTransition(
				answerButtons.get(index), Color.valueOf("#333"), Color.RED
			).play();
		}

		// animation for correct answer (always exists)
		var transition = new ColorTransition(
			answerButtons.get(correctIndex), Color.valueOf("#333"), Color.GREEN
		);
		transition.setOnFinished(e -> {
			// unlock buttons because animation has finished
			locked.setValue(false);
			if(session.hasNext()) {
				session.goToNext();
				getScene().setRoot(new QuestionView(session, factory));
			} else {
				var result = new Result(session.correct(), session.total());
				getScene().setRoot(new ResultView(result, factory));
			}
		});
		transition.play();
	}

}
