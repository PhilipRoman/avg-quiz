package lv.avg;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.Event;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public final class QuestionView extends FlowPane {
	private static final Color GREEN = Color.valueOf("4A0");
	private static final Color RED = Color.valueOf("E00");

	private static final Image EMPTY_IMAGE = new WritableImage(350, 200);
	private static final Image RIGHT_ARROW = new Image(Main.class.getResourceAsStream("data/right-arrow.png"));
	/**
	 * The factory that was used to obtain a session
	 */
	private final Supplier<Session> factory;

	private final Session session;

	private final ProgressBar progressBar = new ProgressBar();
	private final Label questionLabel = new Label();
	private final ImageView questionImage = new ImageView();
	private final Label progressLabel = new Label();
	private final Text resultReveal = new Text();
	private final ImageView nextButton = new ImageView(RIGHT_ARROW);
	private final List<ImageView> icons = new ArrayList<>(4);
	private final Button hintButton = new Button("?");
	private final Button invisibleBox = new Button();

	private final BooleanProperty locked = new SimpleBooleanProperty(false);

	{
		progressBar.getStyleClass().add("progress-bar");
		questionLabel.getStyleClass().add("question-label");
		progressLabel.getStyleClass().add("progress-label");
		resultReveal.getStyleClass().add("result-reveal");
		resultReveal.setWrappingWidth(280);
		nextButton.getStyleClass().add("next-button");
		nextButton.setVisible(false);
		nextButton.setFitWidth(40);
		nextButton.setFitHeight(130);
		nextButton.setPickOnBounds(true);
		questionImage.getStyleClass().add("question-image");
		questionImage.setPreserveRatio(true);
		invisibleBox.getStyleClass().add("square-button");
		invisibleBox.setVisible(false);
		hintButton.getStyleClass().add("square-button");
	}

	private final List<Button> answerButtons = new ArrayList<>();

	public QuestionView(Session session, Supplier<Session> sessionFactory) {
		this.session = session;
		this.factory = sessionFactory;

		progressBar.setProgress(session.progress());
//		progressBar.progressProperty().bind(
//			Bindings.createDoubleBinding(session::progress, session)
//		);
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

		nextButton.setOnMouseClicked(e -> {
			if(session.hasNext()) {
				session.goToNext();
				getScene().setRoot(new QuestionView(session, factory));
			} else {
				var result = new Result(session.correct(), session.total());
				getScene().setRoot(new ResultView(result, factory));
			}
		});

		var answerButtonBox = new VBox();
		for(int i = 0; i < answerButtons.size(); i++) {
			Button b = answerButtons.get(i);
			var icon = new ImageView();
			icon.getStyleClass().add("answer-icon");
			boolean correct = session.currentQuestion().answers().get(i).isCorrect();
			icon.setImage(new Image(Main.class.getResourceAsStream(
				correct ? "data/correct.png" : "data/incorrect.png"
			)));
			icon.setFitWidth(40);
			icon.setFitHeight(40);
			icon.setSmooth(true);
			icon.setVisible(false);
			icons.add(icon);
			var hbox = new HBox(10, icon, b);
			hbox.setAlignment(Pos.CENTER);
			answerButtonBox.getChildren().add(hbox);
		}
		answerButtonBox.getStyleClass().add("answer-container");

		var questionBox = new VBox(
			questionLabel,
			questionImage
		);
		questionBox.setAlignment(Pos.TOP_CENTER);
		questionBox.getStyleClass().add("question-container");
		getChildren().addAll(List.of(
			vBox(
				hBox(
					questionBox,
					vBox(
						hBox(
							answerButtonBox
						),
						hBox(
							invisibleBox,
							resultReveal,
							hintButton
						)
					)
				),
				progressBar,
				progressLabel
			),
			nextButton
		));
		VBox.setMargin(progressBar, new Insets(50, 0, 0, 0));
		setMargin(nextButton, new Insets(0, 0, 0, 70));
		setAlignment(Pos.BOTTOM_CENTER);
		setOrientation(Orientation.HORIZONTAL);
		setTranslateY(60);
		setTranslateX(20);
		nextButton.setTranslateY(-getTranslateY());
	}

	private static VBox vBox(Node... nodes) {
		var box = new VBox(nodes);
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	private static HBox hBox(Node... nodes) {
		var box = new HBox(nodes);
		box.setSpacing(20);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	@NotNull
	private static Image getImageForQuestion(Question question) {
		String path = question.imagePath();
		return path == null ? EMPTY_IMAGE : new Image(Main.class.getResourceAsStream(path));
	}

	private void submitAnswer(int index) {

		// lock down buttons while animations are playing
		locked.setValue(true);

		boolean correct = session.submitAnswer(index);

		for(Button b : answerButtons) {
			b.addEventFilter(MouseEvent.ANY, Event::consume);
			b.setCursor(Cursor.DEFAULT);
		}

		for(ImageView icon : icons)
			icon.setVisible(true);

		resultReveal.setText(correct ? "Pareizi!" : "Nepareizi");
		resultReveal.setFill(correct ? GREEN : RED);
		resultReveal.setVisible(true);
		hintButton.setVisible(false);

		var progressBarAnimation = new ValueTransition(
			progressBar::setProgress,
			session.progress(),
			session.nextProgress()
		);
		progressBarAnimation.play();

		// animation for chosen answer
		var transition = new ColorTransition(
			answerButtons.get(index), Color.valueOf("#333"), correct ? GREEN : RED
		);
		transition.setOnFinished(e -> {
			nextButton.setVisible(true);
		});
		transition.play();
	}

}
