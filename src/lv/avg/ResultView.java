package lv.avg;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;
import javafx.application.*;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.time.format.*;
import java.util.function.*;

public final class ResultView extends FlowPane {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	public ResultView(Result result, Function<Login, Session> sessionFactory) {
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

		var button = new Button(Boolean.getBoolean("vert") ? "Iziet" : "Mēģināt vēlreiz");
		button.getStyleClass().add("start-button");
		getChildren().add(button);

		if(Boolean.getBoolean("vert")) {
			saveResult(result);
		}

		button.setOnAction(e -> {
			if(Boolean.getBoolean("vert")) {
				Platform.exit();
			} else {
				getScene().setRoot(new QuestionView(sessionFactory.apply(result.login()), sessionFactory));
			}
		});
	}


	private static final void saveResult(Result result) {
		var s = new StringBuilder();

		s.append(result.login().timestamp().format(formatter));
		s.append(" - ");
		s.append(result.login().name());
		s.append(" ");
		s.append(result.login().klase());
		s.append(" - ");
		s.append(result.correct());
		s.append(" no ");
		s.append(result.total());
		s.append(", atzīme: ");
		s.append((int) Math.max(1, result.percent() / 10));
		s.append("\n");

		try {
			Files.write(
				Paths.get("rezultāti.txt"),
				s.toString().getBytes(StandardCharsets.UTF_8),
				StandardOpenOption.APPEND, StandardOpenOption.CREATE
			);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
