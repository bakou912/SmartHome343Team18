package com.smart.home.backend.controller;

import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.input.HeatingZoneTemperatureInput;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.input.HeatingZoneRoomInput;
import com.smart.home.backend.input.HeatingZoneRoomTemperatureInput;
import com.smart.home.backend.model.heating.HeatingModel;
import com.smart.home.backend.model.heating.HeatingZone;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
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

import java.util.List;

/**
 * Smart Home Heating Controller
 */
@Getter
@Setter
@RestController
public class HeatingController {
    
    private HeatingModel heatingModel;

    @Autowired
    public HeatingController(HeatingModel heatingModel) {
        this.heatingModel = heatingModel;
    }
    
    /**
     * add heating zone
     * @param heatingZoneInput input for a heating zone
     * @return heating zone name
     */
    @PostMapping("/heating/zone")
    public ResponseEntity<HeatingZone> addHeatingZone(@RequestBody HeatingZoneInput heatingZoneInput) {
        return new AddHeatingZoneCommand().execute(this.heatingModel, heatingZoneInput);
    }
    
    /**
     * Retrieving all heating zones
     * @return found zone
     */
    @GetMapping("/heating/zone")
    public ResponseEntity<List<HeatingZone>> getHeatingZones() {
        return new ResponseEntity<>(this.getHeatingModel().getZones(), HttpStatus.OK);
    }
    
    /**
     * Retrieving a heating zone
     * @param zoneId id for a zone
     * @return found zone
     */
    @GetMapping("/heating/zone/{zoneId}")
    public ResponseEntity<HeatingZone> getHeatingZone(@PathVariable Integer zoneId) {
        HeatingZone foundZone = this.getHeatingModel().findZone(zoneId);
        
        if (foundZone == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(foundZone, HttpStatus.OK);
    }

    /**
     * remove heating zone
     * @param zoneId id for a zone
     * @return zone id
     */
    @DeleteMapping("/heating/zone/{zoneId}")
    public ResponseEntity<Integer> removeHeatingZone(@PathVariable Integer zoneId) {
        return new RemoveHeatingZoneCommand().execute(this.heatingModel, zoneId);
    }

    /**
     * add a room to a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @PostMapping("heating/zone/{zoneId}/room")
    public ResponseEntity<Room> addRoomToZone(@PathVariable Integer zoneId, @RequestBody HeatingZoneRoomInput heatingZoneRoomInput) {
        heatingZoneRoomInput.setZoneId(zoneId);
        return new AddRoomToZoneCommand().execute(this.heatingModel, heatingZoneRoomInput);
    }

    /**
     * remove room from a zone
     * @param heatingZoneRoomInput heating zone room input
     * @return room id
     */
    @DeleteMapping("heating/zone/room")
    public ResponseEntity<Integer> deleteRoomFromZone(@RequestBody HeatingZoneRoomInput heatingZoneRoomInput) {
        return new RemoveRoomFromZoneCommand().execute(this.heatingModel, heatingZoneRoomInput);
    }

    /**
     * set temperature for a zone
     * @param heatingZoneTemperatureInput heating zone temperature input
     * @return heating zone temperature
     */
    @PutMapping("heating/zone/{zoneId}/{period}/temperature")
    public ResponseEntity<Double> setZoneTemperature(
            @PathVariable Integer zoneId,
            @PathVariable HeatingZonePeriod period,
            @RequestBody HeatingZoneTemperatureInput heatingZoneTemperatureInput
    ) {
        heatingZoneTemperatureInput.setZoneId(zoneId);
        heatingZoneTemperatureInput.setHeatingZonePeriod(period);
        return new SetZoneTemperatureCommand().execute(this.heatingModel, heatingZoneTemperatureInput);
    }

    /**
     * read temperature from a given room
     * @param locationPosition room's position
     * @return temperature of room
     */
    @GetMapping("heating/row/{rowId}/room/{roomId}/temperature")
    public ResponseEntity<Double> readRoomTemperature(LocationPosition locationPosition) {
        double roomTemperature = this.heatingModel.getRoomTemperature(locationPosition);
        return new ResponseEntity<>(roomTemperature, HttpStatus.OK);
    }

    /**
     * Override room's temperature
     * @param locationPosition room's location
     * @param heatingZoneRoomTemperature heatingZoneRoomTemperature input
     * @return overridden temperature
     */
    @PutMapping("heating/row/{rowId}/room/{roomId}/temperature")
    public ResponseEntity<Double> overrideRoomTemperature(LocationPosition locationPosition, @RequestBody HeatingZoneRoomTemperatureInput heatingZoneRoomTemperature) {
        heatingZoneRoomTemperature.setLocationPosition(locationPosition);
        return new OverrideRoomTemperatureCommand().execute(this.heatingModel, heatingZoneRoomTemperature);
    }

}
