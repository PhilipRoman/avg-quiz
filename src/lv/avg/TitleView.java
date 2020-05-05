package lv.avg;

import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.function.*;

public final class TitleView extends FlowPane {
	public TitleView(Supplier<Session> sessionFactory) {
		System.gc();
		setOrientation(Orientation.VERTICAL);
		setVgap(10);

		var button = new Button("Sākt testu!");
		button.getStyleClass().add("start-button");

		var text = new Text("Fizikas Tests");
		text.getStyleClass().add("title-text");

		var subtext = new Text("Komanda: Filips, Verners, Imants");
		subtext.getStyleClass().add("title-subtext");

		var box = new VBox(
			30,
			text,
			subtext,
			button
		);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(60));
		box.getStyleClass().add("title-box");

		getStyleClass().add("title-pane");
		getChildren().add(box);
		button.setOnMouseClicked(event -> {
			getScene().setRoot(new QuestionView(sessionFactory.get(), sessionFactory));
		});
	}
}
