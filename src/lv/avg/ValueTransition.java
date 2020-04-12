package lv.avg;

import javafx.animation.*;
import javafx.util.Duration;

import java.util.function.*;

public class ValueTransition extends Transition {
	private static final Duration DURATION = Duration.millis(800);

	private final DoubleConsumer property;
	private final double from;

	private final double diff;

	{
		setInterpolator(Interpolator.EASE_BOTH);
	}

	public ValueTransition(DoubleConsumer property, double from, double to) {
		this(property, from, to, DURATION);
	}

	public ValueTransition(DoubleConsumer property, double from, double to, Duration duration) {
		this.property = property;
		this.from = from;
		diff = to - from;
		setCycleDuration(duration);
	}

	@Override
	protected void interpolate(double frac) {
		property.accept(from + diff*frac);
	}
}
