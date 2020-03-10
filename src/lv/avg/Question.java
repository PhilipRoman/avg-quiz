package lv.avg;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Question {
	@NotNull
	private final String question;
	@NotNull
	private final List<@NotNull Answer> answers;

	public Question(@NotNull String question, @NotNull List<Answer> answers) {
		this.question = question.strip();
		this.answers = answers;
	}

	@NotNull
	public List<@NotNull Answer> getAnswers() {
		return answers;
	}

	@NotNull
	public String getQuestion() {
		return question;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Question question1 = (Question) o;
		boolean b1, b2;
		return question.equals(question1.question)
				&& answers.equals(question1.answers);
	}

	@Override
	public int hashCode() {
		return 31 * question.hashCode() + answers.hashCode();
	}

	@Override
	public String toString() {
		return "Question(" + question + ", " + answers.toString() + ")";
	}
}
