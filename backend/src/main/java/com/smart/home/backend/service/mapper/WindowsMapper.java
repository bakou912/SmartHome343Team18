package com.smart.home.backend.service.mapper;

import com.smart.home.backend.constant.Direction;
import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.model.houselayout.Window;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of windows.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WindowsMapper {
	
	/**
	 * Maps window directions to windows.
	 * @param windowDirections List of window directions
	 * @return Mapped windows
	 */
	public static List<Window> map(List<String> windowDirections) {
		List<Window> windows = new ArrayList<>();
		
		for (int i = 0; i < windowDirections.size(); i++) {
			String windowDirection = windowDirections.get(i);
			
			windows.add(
					Window.builder()
							.id(i)
							.state(WindowState.CLOSED)
							.direction(Direction.get(windowDirection))
							.build()
			);
		}
		
		return windows;
	}
	
}
