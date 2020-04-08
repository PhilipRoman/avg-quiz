package lv.avg;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ColorTransition extends Transition {

	private final Region target;
	private final Color from, to;

	{
		setInterpolator(Interpolator.LINEAR);
	}

	public ColorTransition(Region target, Color from, Color to) {
		this(target, from, to, Duration.millis(600));
	}

	public ColorTransition(Region target, Color from, Color to, Duration duration) {
		this.target = target;
		setCycleDuration(duration);
		this.from = from;
		this.to = to;
	}

	@Override
	protected void interpolate(double frac) {
		Color c = frac == 1.0d ? from : from.interpolate(to, frac);
		target.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
	}
}
