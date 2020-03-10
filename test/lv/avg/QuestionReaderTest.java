package lv.avg;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class QuestionReaderTest {
	@Test
	public void testEmpty() {
		assertFalse(new QuestionReader(new Scanner("")).hasNext());
	}

	@Test
	public void testSingleQuestion() {
		assertEquals(new QuestionReader(new Scanner("Question?   \n" +
						"+   correct\n" +
						"- incorrect\n\n")).next(),
				new Question("Question?", List.of(
						new Answer("correct", true),
						new Answer("incorrect", false))
				));
	}
}