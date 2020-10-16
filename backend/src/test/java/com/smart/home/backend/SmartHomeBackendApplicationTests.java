package com.smart.home.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.home.backend.controller.HouseLayoutController;
import com.smart.home.backend.input.HouseLayoutInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmartHomeBackendApplicationTests {

	@Test
	void fakeTest() {
		assertEquals(1, 1);
	}

}
