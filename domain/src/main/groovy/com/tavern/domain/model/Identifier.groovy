package com.tavern.domain.model

/**
 * Common class for a string based object identifier
 */
abstract class Identifier {
	private String id

	protected Identifier() {
	}

	protected Identifier(String id) {
		this.id = id
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (getClass() != o.class) return false

		Identifier that = (Identifier) o

		if (id != that.id) return false

		return true
	}

	int hashCode() {
		return (id != null ? id.hashCode() : 0)
	}

	@Override
	String toString() {
		getId()
	}

	String getId() {
		return id
	}

	private void setId(String id) {
		this.id = id
	}

}
