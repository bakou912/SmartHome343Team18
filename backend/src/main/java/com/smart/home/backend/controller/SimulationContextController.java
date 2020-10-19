/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.home.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smart.home.backend.constant.WindowState;
import com.smart.home.backend.model.houselayout.*;
import com.smart.home.backend.model.houselayout.directional.Window;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CrossOrigin
@RestController
public class SimulationContextController {
    private HouseLayoutModel houseLayoutModel;
    private WindowState state;
        
    /**
     *  put an arbitrary object on a window 
     * 
     * @param rowId row number
     * @param roomId room number
     * @param windowId id of window
     * @return updated houseLayout. returns null if  window, room, or row does not exist.
    */
    /*Pseudocode TO BE REMOVED AFTER TESTING
    public void Put_Arb_Obj_On_Window(){
        if (specificwindow.getState() == "OPEN"){
            specificwindow.getState() = "BLOCKED";
        }
    }*/
    @PutMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
    public ResponseEntity<HouseLayoutModel> Put_Arb_Obj_On_Window(@PathVariable(value = "rowId") int rowId, @PathVariable(value = "roomId") int roomId, @PathVariable(value = "windowId") int windowId){
            Window targetWindow = this.getHouseLayoutModel().findWindow(rowId, roomId, windowId);

            if (targetWindow == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            if (targetWindow.getState() == WindowState.OPEN){
                targetWindow.setState(WindowState.BLOCKED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
    }
    
    /**
     *  put an arbitrary object on a window 
     * 
     * @param rowId row number
     * @param roomId room number
     * @param windowId id of window
     * @return updated houseLayout. returns null if  window, room, or row does not exist.
    */
    /*Pseudocode TO BE REMOVED AFTER TESTING
    public void Remove_Arb_Obj_On_Window(){
        if (specificwindow.getState() == "BLOCKED"){
            specificwindow.getState() = "CLOSED";
        }
    }*/
    @PutMapping("/layout/rows/{rowId}/rooms/{roomId}/windows/{windowId}")
    public ResponseEntity<HouseLayoutModel> Remove_Arb_Obj_On_Window(@PathVariable(value = "rowId") int rowId, @PathVariable(value = "roomId") int roomId, @PathVariable(value = "windowId") int windowId){
            Window targetWindow = this.getHouseLayoutModel().findWindow(rowId, roomId, windowId);

            if (targetWindow == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (targetWindow.getState() == WindowState.BLOCKED){
                targetWindow.setState(WindowState.CLOSED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(this.getHouseLayoutModel(), HttpStatus.OK);
    }
}
