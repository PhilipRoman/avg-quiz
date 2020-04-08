package lv.avg;

import javafx.animation.Interpolator;

/**
 * See <a href=https://www.desmos.com/calculator/3zhzwbfrxd>https://www.desmos.com/calculator/3zhzwbfrxd</a> for
 * a visualisation of the function.
 */
public class SigmoidInterpolator extends Interpolator {
	private final double p, s;


	/**
	 * Roughly speaking, p controls the horizontal offset of the sigmoid and
	 * s controls how sharp the transition is (s = 0 is always a straight diagonal line,
	 * s = 0.999 is horizontal except for a sharp vertical step at x = p).
	 *
	 * Good default values could be p = 0.5, s = 0.5
	 *
	 * @param p Parameter for the equation, should be in range [0 ... 1] inclusive
	 * @param s Parameter for the equation, should be in range [0 ... 1] exclusive
	 */
	public SigmoidInterpolator(double p, double s) {
		this.p = p;
		this.s = s;
	}

	@Override
	protected double curve(double x) {
		double c = 2 / (1 - s) - 1;
		return x <= p
			? Math.pow(x, c) / Math.pow(p, c - 1)
			: 1 - Math.pow(1 - x, c) / Math.pow(1 - p, c - 1);
	}
}
