package lv.avg;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ColorTransition extends Transition {
	private static final Duration DURATION = Duration.millis(1000);

	private final Region target;
	private final Color from, to;

	private static final Interpolator SIGMOID = new SigmoidInterpolator(0.13, 0.6);

	{
		setInterpolator(SIGMOID);
	}

	public ColorTransition(Region target, Color from, Color to) {
		this(target, from, to, DURATION);
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
