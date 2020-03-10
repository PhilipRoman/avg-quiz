package lv.avg;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class QuestionReader implements Iterator<Question> {
	private final Scanner scanner;

	private final String getLine() {
		return scanner.hasNextLine() ? scanner.nextLine() : "";
	}

	public QuestionReader(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public boolean hasNext() {
		return scanner.hasNextLine();
	}

	@Override
	@NotNull
	public Question next() {
		String line;
		do {
			line = getLine().strip();
		} while (line.isBlank() && scanner.hasNextLine());

		String question = line;

		ArrayList<Answer> answers = new ArrayList<>(4);
		while (scanner.hasNextLine()) {
			line = getLine().strip();
			if (line.startsWith("+"))
				answers.add(new Answer(line.substring(1), true));
			else if (line.startsWith("-"))
				answers.add(new Answer(line.substring(1), false));
			else
				break;
		}

		do {
			line = getLine().strip();
		} while (line.isBlank() && scanner.hasNextLine());

		return new Question(question, answers);
	}
}
