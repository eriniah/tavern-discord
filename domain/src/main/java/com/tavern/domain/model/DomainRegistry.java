package com.tavern.domain.model;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class DomainRegistry implements ApplicationContextAware {
	private final static DomainRegistry instance = new DomainRegistry();
	public static DomainRegistry getInstance() {
		return instance;
	}

	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (context == null) {
			context = applicationContext;
		}
	}

}
