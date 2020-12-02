package com.smart.home.backend.controller;

import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.simulationparameters.module.command.shh.AddHeatingZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.AddRoomToZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.RemoveRoomFromZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.SetZoneTemperatureCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.RemoveHeatingZoneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Smart Home Heating Controller
 */
@RestController
public class SmartHomeHeatingController {
    
    private HeatingModel heatingModel;

    @Autowired
    public SmartHomeHeatingController(HeatingModel heatingModel) {
        this.heatingModel = heatingModel;
    }
    
    /**
     * add heating zone
     * @param heatingZoneInput input for a heating zone
     * @return true if zone was added, false wotherwise
     */
    @PostMapping("/heating/zone")
    public ResponseEntity<HeatingZoneInput> addHeatingZone(@RequestBody HeatingZoneInput heatingZoneInput ){
        return new AddHeatingZoneCommand().execute(heatingModel, heatingZoneInput);
    }

    /**
     * remove heating zone
     * @param zoneId id for a zone
     * @return zone id
     */
    @DeleteMapping("/heating/zone/{zoneId}")
    public ResponseEntity<Integer> removeHeatingZone(@PathVariable Integer zoneId){
        return new RemoveHeatingZoneCommand().execute(heatingModel, zoneId);
    }

    /**
     * add a room to a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @PostMapping("heating/zone/room")
    public ResponseEntity<Integer> addRoomToZone(@RequestBody HeatingZoneRoomInput heatingZoneRoomInput){
        return new AddRoomToZoneCommand().execute(heatingModel, heatingZoneRoomInput);
    }

    /**
     * remove room from a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @DeleteMapping("heating/zone/room")
    public ResponseEntity<Integer> deleteRoomFromZone(@RequestBody HeatingZoneRoomInput heatingZoneRoomInput){
        return new RemoveRoomFromZoneCommand().execute(heatingModel, heatingZoneRoomInput);
    }

    /**
     * set temperature for a zone
     * @param heatingZoneTemperatureInput heating zone temperature input
     * @return heating zone temperature
     */
    @PutMapping("heating/zone/temperature")
    public ResponseEntity<Double> setZoneTemperature(@PathVariable HeatingZoneTemperatureInput heatingZoneTemperatureInput){
        return new SetZoneTemperatureCommand().execute(heatingModel, heatingZoneTemperatureInput);
    }

}
