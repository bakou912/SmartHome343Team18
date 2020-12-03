package com.smart.home.backend.controller;

import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.module.command.shh.AddHeatingZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.AddRoomToZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.OverrideRoomTemperatureCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.RemoveRoomFromZoneCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.SetZoneTemperatureCommand;
import com.smart.home.backend.model.simulationparameters.module.command.shh.RemoveHeatingZoneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.Getter;
import lombok.Setter;

/**
 * Smart Home Heating Controller
 */
@Getter
@Setter
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
     * @return heating zone name
     */
    @PostMapping("/heating/zone")
    public ResponseEntity<String> addHeatingZone(@RequestBody HeatingZoneInput heatingZoneInput ){
        return new AddHeatingZoneCommand().execute(this.heatingModel, heatingZoneInput);
    }

    /**
     * remove heating zone
     * @param zoneId id for a zone
     * @return zone id
     */
    @DeleteMapping("/heating/zone/{zoneId}")
    public ResponseEntity<Integer> removeHeatingZone(@PathVariable Integer zoneId){
        return new RemoveHeatingZoneCommand().execute(this.heatingModel, zoneId);
    }

    /**
     * add a room to a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @PostMapping("heating/zone/room")
    public ResponseEntity<Room> addRoomToZone(@RequestBody HeatingZoneRoomInput heatingZoneRoomInput){
        return new AddRoomToZoneCommand().execute(this.heatingModel, heatingZoneRoomInput);
    }

    /**
     * remove room from a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @DeleteMapping("heating/zone/room")
    public ResponseEntity<Integer> deleteRoomFromZone(@RequestBody HeatingZoneRoomInput heatingZoneRoomInput){
        return new RemoveRoomFromZoneCommand().execute(this.heatingModel, heatingZoneRoomInput);
    }

    /**
     * set temperature for a zone
     * @param heatingZoneTemperatureInput heating zone temperature input
     * @return heating zone temperature
     */
    @PutMapping("heating/zone/temperature")
    public ResponseEntity<Double> setZoneTemperature(@PathVariable HeatingZoneTemperatureInput heatingZoneTemperatureInput){
        return new SetZoneTemperatureCommand().execute(this.heatingModel, heatingZoneTemperatureInput);
    }

    /**
     * read temperature from a given room
     * @param heatingZoneRoomInput heating zone room input
     * @return temperature of room
     */
    @GetMapping("heating/room/temperature")
    public ResponseEntity<Double> readRoomTemperature(@PathVariable HeatingZoneRoomInput heatingZoneRoomInput){
        
        double roomTemperature = this.heatingModel.getRoomTemperature(heatingZoneRoomInput.getZoneId(), heatingZoneRoomInput.getRoomId());
        return new ResponseEntity<Double>(roomTemperature, HttpStatus.OK);
    }

    /**
     * Override room's temperature
     * @param heatingZoneRoomTemperature
     * @return
     */
    @PutMapping("heating/room/temperature")
    public ResponseEntity<Double> overrideRoomTemperature(@PathVariable HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature){
        return new OverrideRoomTemperatureCommand().execute(this.heatingModel, heatingZoneRoomTemperature);
    }

}
