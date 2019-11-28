package at.jku.cp.ai.tests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class TestLoopAdd {
	private static List<Integer> source = new ArrayList<>();

	@Before
	public void setup() {
		for (int i = 0; i < 10000000; i++) {
			source.add(i);
		}
	}

	@Test
	public void loopAdd() {
		Queue<Integer> queue = new LinkedList<>();
		for (int x : source) {
			queue.add(x);
		}
	}

	@Test
	public void loopAdd2() {
		Queue<Integer> queue = new LinkedList<>();
		queue.addAll(source);
	}
}
