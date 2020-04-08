package lv.avg;

import javafx.beans.Observable;
import javafx.beans.*;
import org.slf4j.*;

import java.util.*;

public final class Session implements Observable {
	private static final Logger log = LoggerFactory.getLogger(Session.class);

	private final Question[] questions;
	private int index = 0;
	private int correct = 0, total;

	public Session(List<Question> questions) {
		this.questions = questions.toArray(new Question[0]);
		total = questions.size();
	}

	public Question currentQuestion() {
		return questions[index];
	}

	public void goToNext() {
		log.debug("Advancing {} to next question", this);
		if(++index >= questions.length) {
			throw new NoSuchElementException();
		}
		invalidate();
	}

	public boolean submitAnswer(int answerIndex) {
		Question q = questions[index];
		Answer answer = q.answers().get(answerIndex);
		log.debug("Submitted {} answer (index {}) to {}", answer.isCorrect(), answerIndex, this);
		if(answer.isCorrect()) {
			correct++;
			invalidate();
			return true;
		}
		return false;
	}

	public double progress() {
		return (index + 0.5) / questions.length;
	}

	public int index() {
		return index;
	}

	public boolean hasNext() {
		return index < questions.length - 1;
	}

	public int correct() {
		return correct;
	}

	public int total() {
		return questions.length;
	}

	@Override
	public String toString() {
		return "Session@" + Integer.toHexString(System.identityHashCode(this)) + "(" + index + "/" + total + ")";
	}

	// ========== Observable implementation ==========

	private final List<InvalidationListener> listeners = new ArrayList<>();

	@Override
	public void addListener(InvalidationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		listeners.remove(listener);
	}

	private void invalidate() {
		new ArrayList<>(listeners).forEach(l -> l.invalidated(this));
	}
}
