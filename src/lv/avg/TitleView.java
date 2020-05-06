package lv.avg;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.*;
import javafx.geometry.*;

import java.util.function.*;

public final class TitleView extends FlowPane {
	public TitleView(Function<Login, Session> sessionFactory) {
		setOrientation(Orientation.VERTICAL);
		setVgap(10);
		setAlignment(Pos.CENTER);

		var text = new Text("Fizikas Tests");
		text.getStyleClass().add("title-text");

		var subtext = new Text("Komanda: Romāns, Vētra");
		subtext.getStyleClass().add("title-subtext");

		TextField vārds = new TextField();
		vārds.setPromptText("Vārds");
		TextField klase = new TextField();
		klase.setPromptText("Klase");


		var button = new Button("Sākt testu!");
		button.getStyleClass().add("start-button");
		button.setOnAction(event -> {
			var login = Boolean.getBoolean("vert")
				? new Login(vārds.getText(), klase.getText())
				: Login.anonymous();
			getScene().setRoot(new QuestionView(sessionFactory.apply(login), sessionFactory));
		});

		var box = new VBox(
			20,
			text, 
			subtext
		);
		if(Boolean.getBoolean("vert")) {
			box.getChildren().add(new Text("Lai sāktu testu, ieraksti savu vārdu un klasi!"));
			box.getChildren().add(vārds);
			box.getChildren().add(klase);
		}
		box.getChildren().add(button);

		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(60));
		box.getStyleClass().add("title-box");

		getStyleClass().add("title-pane");
		getChildren().add(box);
		
	}
}
