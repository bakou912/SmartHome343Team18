package com.smart.home.backend.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements BeanFactoryPostProcessor {
	
	private static ConfigurableListableBeanFactory beanFactory;
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtils.beanFactory = beanFactory;
	}
	
	public static <T> T getBean(Class<T> clz) throws BeansException {
		return beanFactory.getBean(clz);
	}
}
