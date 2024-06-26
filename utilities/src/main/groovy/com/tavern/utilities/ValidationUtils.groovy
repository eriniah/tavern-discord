package com.tavern.utilities

class ValidationUtils {
	static void requireNonBlank(String value, String errorMessage)
		throws NullPointerException {
		if (StringUtils.isNullOrBlank(value)) {
			throw new NullPointerException(errorMessage)
		}
	}
}
