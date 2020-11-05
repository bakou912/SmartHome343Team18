package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Window;

import org.springframework.http.ResponseEntity;

public class WindowObstructionCommand extends AbstractCommand<HouseLayoutModel, WindowInput, Window> {
	
	public WindowObstructionCommand() {
		super("Window obstruction", true);
	}
	
	@Override
	public ResponseEntity<Window> execute(HouseLayoutModel houseLayoutModel, WindowInput windowInput) {
		return null;
	}

}
