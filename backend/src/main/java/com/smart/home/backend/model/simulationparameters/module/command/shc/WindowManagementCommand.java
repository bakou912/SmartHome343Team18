package com.smart.home.backend.model.simulationparameters.module.command.shc;

import com.smart.home.backend.input.WindowInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.directional.Window;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Window management command.
 */
public class WindowManagementCommand extends SHCAbstractCommand<HouseLayoutModel, WindowInput, Window> {
    
    /**
     * Default Constructor
     */
    public WindowManagementCommand() {
        super("Window management", true);
    }

    @Override
    public ResponseEntity<Window> execute(HouseLayoutModel houseLayoutModel, WindowInput windowInput) {
        Window modifiedWindow = houseLayoutModel.modifyWindowState(windowInput);
    
        if (modifiedWindow == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    
        this.logAction("Changed window state to " + modifiedWindow.getState().toString());
    
        return new ResponseEntity<>(modifiedWindow, HttpStatus.OK);
    }
    
}
