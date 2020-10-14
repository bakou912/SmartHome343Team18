package com.smart.home.backend.config;

import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the house layout.
 */
@Configuration
public class HouseLayoutConfig {
	
	/**
	 * Shared house layout model bean.
	 * @return HouseLayoutModel
	 */
	@Bean
	public HouseLayoutModel houseLayoutModel() {
		return new HouseLayoutModel();
	}
	
	
}
