package com.smart.home.backend.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class to get the bean of a type.
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor {
	
	private static ConfigurableListableBeanFactory beanFactory;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		SpringUtils.beanFactory = beanFactory;
	}
	
	/**
	 * Retrieving a type's bean.
	 * @param clz class type
	 * @param <T> returned bean's class
	 * @return Found bean
	 */
	public static <T> T getBean(Class<T> clz) {
		return beanFactory.getBean(clz);
	}
}
