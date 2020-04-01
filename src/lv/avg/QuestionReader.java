package lv.avg;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class QuestionReader implements Iterator<Question> {
	private final ArrayDeque<String> queue = new ArrayDeque<>(64);

	public QuestionReader(Reader reader) {
		this(new Scanner(reader));
	}

	public QuestionReader(InputStream stream) {
		this(new Scanner(stream, StandardCharsets.UTF_8));
	}

	public QuestionReader(Scanner scanner) {
		while(scanner.hasNextLine()) {
			queue.add(scanner.nextLine().strip());
		}
	}

	@Override
	public boolean hasNext() {
		skipEmpty();
		return !queue.isEmpty();
	}

	@Override
	@NotNull
	public Question next() {
		skipEmpty();

		String question = queue.removeFirst();
		String imagePath = null;

		ArrayList<Answer> answers = new ArrayList<>(4);
		while(!queue.isEmpty()) {
			String line = queue.removeFirst();
			if(line.isEmpty())
				break;
			else if(line.startsWith("+"))
				answers.add(new Answer(line.substring(1), true));
			else if(line.startsWith("-"))
				answers.add(new Answer(line.substring(1), false));
			else if(line.startsWith("[") && line.endsWith("]"))
				imagePath = line.substring(1, line.length() - 1);
			else if(answers.isEmpty())
				question += ' ' + line;
			else
				throw new IllegalArgumentException("Bad data format: " + line);
		}
		return new Question(question, answers, imagePath);
	}

	private void skipEmpty() {
		while(!queue.isEmpty() && queue.peekFirst().isEmpty())
			queue.removeFirst();
	}

	public List<Question> list() {
		var list = new ArrayList<Question>(queue.size() / 5);
		while(hasNext())
			list.add(next());
		return list;
	}
}
