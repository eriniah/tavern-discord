package com.asm.tavern.discord.discord.command.parser


class CommandTokenizer {
	final Stack<String> tokens

	CommandTokenizer(String message) {
		this.tokens = createMessageStack(message)
	}

	private Stack<String> createMessageStack(String message) {
		String[] parts = message.trim().split("\\s+")
		if (!parts.length) {
			return new Stack<String>()
		}

		Stack<String> tokens = new Stack<>()
		for (int i = parts.length - 1; i >= 0; i--) {
			String part = parts[i]
			if (!part.isEmpty()) {
				tokens.add(part)
			}
		}
		tokens
	}

	boolean hasNext() {
		!tokens.empty()
	}

	String peek() {
		return tokens.peek()
	}

	String pop()
		throws IllegalAccessException {
		if (!hasNext()) {
			throw new IllegalAccessException("No more message tokens in the stack")
		}

		tokens.pop()
	}

	List<String> popRemaining() {
		List<String> remainingTokens = new ArrayList<>(tokens)
		tokens.clear()
		remainingTokens
	}

}
