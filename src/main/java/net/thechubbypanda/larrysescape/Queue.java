package net.thechubbypanda.larrysescape;

import java.util.EmptyStackException;
import java.util.Stack;

public class Queue<T> extends Stack<T> {

	@Override
	public synchronized T peek() {
		if (size() == 0)
			throw new EmptyStackException();
		return elementAt(0);
	}

	@Override
	public synchronized T pop() {
		T obj;

		obj = peek();
		removeElementAt(0);

		return obj;
	}
}
