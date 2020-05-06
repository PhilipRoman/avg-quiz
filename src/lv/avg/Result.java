package lv.avg;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Result {
	private final int correct, total;

	private final Login login;

	public enum Quality {
		BAD("red"), AVERAGE("orange"), GOOD("lime"), EXCELLENT("forestgreen");
		private final String color;

		Quality(String color) {
			this.color = color;
		}

		public String color() {
			return color;
		}
	}

	private static final NavigableMap<Double, Quality> table = new TreeMap<>(Map.of(
		0.0d, Quality.BAD,
		0.4d, Quality.AVERAGE,
		0.7d, Quality.GOOD,
		0.9d, Quality.EXCELLENT
	));

	Result(Login login, int correct, int total) {
		this.login = login;
		if(total <= 0)
			throw new IllegalArgumentException("Total number of answers must be greater than 0");
		if(correct > total)
			throw new IllegalArgumentException("Cannot have more correct answers than total");
		this.correct = correct;
		this.total = total;
	}

	Quality quality() {
		return table.floorEntry(ratio()).getValue();
	}

	int percent() {
		return (int) Math.round(ratio() * 100.0);
	}

	private double ratio() {
		return correct / (double) total;
	}

	int correct() {
		return correct;
	}

	int total() {
		return total;
	}

	Login login() {
		return login;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Result result = (Result) o;
		return correct == result.correct && total == result.total && login.equals(result.login);
	}

	@Override
	public int hashCode() {
		return (31 * correct + total) ^ login.hashCode();
	}
}
