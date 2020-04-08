package lv.avg;

import org.jetbrains.annotations.*;

import java.util.*;

public final class Question {
	@NotNull
	private final String question;
	@NotNull
	private final List<@NotNull Answer> answers;
	@Nullable
	private final String imagePath;
	@NotNull
	private final transient Answer correct;

	public Question(@NotNull String question, @NotNull List<Answer> answers) {
		this(question, answers, null);
	}

	public Question(@NotNull String question, @NotNull List<Answer> answers, @Nullable String imagePath) {
		this.question = question.strip();
		this.answers = answers;
		this.imagePath = imagePath;
		if(answers.stream().filter(Answer::isCorrect).count() != 1)
			throw new IllegalArgumentException("Question must have exactly 1 correct answer");
		correct = answers.stream().filter(Answer::isCorrect).findAny().orElseThrow();
	}

	@NotNull
	public List<@NotNull Answer> answers() {
		return answers;
	}

	@NotNull
	public String questionText() {
		return question;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Question question1 = (Question) o;
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

	@Nullable
	public String imagePath() {
		return imagePath;
	}

	@NotNull
	public Answer correctAnswer() {
		return correct;
	}
}
