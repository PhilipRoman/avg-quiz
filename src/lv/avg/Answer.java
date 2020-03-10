package lv.avg;

import org.jetbrains.annotations.NotNull;

public final class Answer {
	@NotNull
	private final String text;
	private final boolean correct;

	public Answer(@NotNull String text, boolean correct) {
		this.text = text.strip();
		this.correct = correct;
	}

	@NotNull
	public String getText() {
		return text;
	}

	public boolean isCorrect() {
		return correct;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Answer answer = (Answer) o;

		return correct == answer.correct
				&& text.equals(answer.text);

	}

	@Override
	public int hashCode() {
		return 31 * text.hashCode() + (correct ? 1 : 0);
	}

	@Override
	public String toString() {
		return "Answer(" + text + ", " + (correct ? "correct" : "incorrect") + ")";
	}
}
