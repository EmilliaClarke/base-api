package com.akami.core.hooks;

import io.cucumber.java.After;
import org.junit.Before;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CommonHooks {
    
	@After
	public void delete() {
	}

    @Before
    public void setUp() {
    }
}