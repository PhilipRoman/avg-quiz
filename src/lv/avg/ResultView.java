package lv.avg;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;

import java.util.function.*;

public final class ResultView extends FlowPane {
	public ResultView(Result result, Supplier<Session> sessionFactory) {
		setOrientation(Orientation.VERTICAL);
		setVgap(10);

		var percentText = new Text(result.percent() + "%");
		percentText.setStyle("-fx-font-size: 70; -fx-text-align: center;");
		percentText.setFill(Paint.valueOf(result.quality().color()));
		var percentTextFlow = new TextFlow(percentText);
		percentTextFlow.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(percentTextFlow);

		var boldText = new Text(result.correct() + "/" + result.total());
		boldText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
		boldText.setFill(Paint.valueOf(result.quality().color()));
		getChildren().add(new TextFlow(new Text("Tavs rezultāts: "), boldText));

		var button = new Button("Mēģināt vēlreiz");
		button.getStyleClass().add("start-button");
		getChildren().add(button);

		button.setOnMouseClicked(event -> getScene().setRoot(new QuestionView(sessionFactory.get(), sessionFactory)));
	}
}
