package com.smart.home.backend.config;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HouseLayoutConfig {
	
	@Bean
	public HouseLayoutModel houseLayoutModel() {
		return new HouseLayoutModel();
	}
	
}
