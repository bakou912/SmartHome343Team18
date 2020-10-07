package com.smart.home.backend.service.mapper;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.model.houselayout.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of windows.
 */
public class WindowsMapper {
	
	private WindowsMapper() {
		// Hiding constructor
	}
	
	/**
	 * Maps window directions to windows.
	 * @param windowDirections List of window directions
	 * @return Mapped windows
	 */
	public static List<Window> map(List<String> windowDirections) {
		List<Window> windows = new ArrayList<>();
		
		windowDirections.forEach(
				windowDirection -> windows.add(
						Window.builder()
								.state(WindowState.CLOSED)
								.direction(Direction.get(windowDirection))
								.build()
				)
		);
		
		return windows;
	}
	
}
