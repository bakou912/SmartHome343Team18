package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Window;
import org.springframework.http.ResponseEntity;

public class WindowManagementCommand extends AbstractCommand<HouseLayoutModel, WindowInput, Window> {
    
    /**
     * Default Constructor
     */
    public WindowManagementCommand() {
        super("Window management", true);
    }

    @Override
    public ResponseEntity<Window> execute(HouseLayoutModel houseLayoutModel, WindowInput doorInput) {
        return null;
    }
    
}
