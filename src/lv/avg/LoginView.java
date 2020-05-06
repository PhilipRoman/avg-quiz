package lv.avg;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.function.*;

@Deprecated
public class LoginView extends FlowPane {

	public LoginView(Function<Login, Session> sessionFactory) {
		setOrientation(Orientation.VERTICAL);
		setVgap(10);
		setAlignment(Pos.CENTER);

		TextField vārds = new TextField();
		vārds.setPromptText("Vārds");
		TextField klase = new TextField();
		klase.setPromptText("Klase");

		Button submit = new Button("Sākt testu");
		submit.setOnAction(e -> {
			var login = new Login(vārds.getText(), klase.getText());
			getScene().setRoot(new QuestionView(sessionFactory.apply(login), sessionFactory));
		});

		getChildren().add(vārds);
		getChildren().add(klase);
		getChildren().add(submit);
	}
}
