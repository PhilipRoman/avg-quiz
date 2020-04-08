package lv.avg;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.function.*;

public final class TitleView extends FlowPane {
	public TitleView(Supplier<Session> sessionFactory) {
		System.gc();
		setOrientation(Orientation.VERTICAL);
		setVgap(10);

		var button = new Button("Sākt testu!");
		button.getStyleClass().add("start-button");
		getChildren().add(button);
		button.setOnMouseClicked(event -> {
			getScene().setRoot(new QuestionView(sessionFactory.get(), sessionFactory));
		});
	}
}
