package com.smart.home.backend.model.heating;

import com.smart.home.backend.constant.HeatingZonePeriod;
import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.AbstractBaseModel;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Heating model.
 */
@Getter
@Component
public class HeatingModel extends AbstractBaseModel {
    
    @Setter
    @Builder.Default
    private List<HeatingZone> zones = new ArrayList<>();
    
    private final IdUtil zoneId = new IdUtil();
    private final HouseLayoutModel houseLayoutModel;
    
    /**
     * 1-parameter constructor
     * @param houseLayoutModel house layout model
     */
    @Autowired
    public HeatingModel(HouseLayoutModel houseLayoutModel) {
        this.houseLayoutModel = houseLayoutModel;
    }
    
    /**
     * Adds a zone to the zone list
     * @param heatingZoneInput zone input
     */
    public HeatingZone addZone(HeatingZoneInput heatingZoneInput) {
        if (this.getZones().stream().anyMatch(zone -> zone.getName().equals(heatingZoneInput.getName()))) {
            return null;
        }
        
        HeatingZone newZone = HeatingZone.builder()
                .name(heatingZoneInput.getName())
                .rooms(
                        heatingZoneInput.getRoomLocations().stream().map(
                                roomLocation -> this.getHouseLayoutModel().findRoom(roomLocation)
                        ).collect(Collectors.toList())
                )
                .id(this.getZoneId().newId())
                .build();
        
        this.getZones().forEach(
                zone -> zone.getRooms().removeIf(room -> newZone.getRooms().contains(room))
        );
        
        this.getZones().add(newZone);
        return newZone;
    }
    
    /**
     * Removes a zone from the zone list
     * @param zoneId zone's id
     */
    public HeatingZone removeZone(Integer zoneId) {
        HeatingZone foundZone = this.getZones().stream().filter(zone -> zone.getId().equals(zoneId)).findFirst().orElse(null);
        this.getZones().remove(foundZone);
        return foundZone;
    }
    
    /**
     * Adds a room to a heating zone.
     * @param zoneId zone's id
     * @param roomLocation room's location
     * @return found room
     */
    public Room addRoomToZone(Integer zoneId, LocationPosition roomLocation) {
        HeatingZone heatingZone = this.findZone(zoneId);
        Room foundRoom = this.getHouseLayoutModel().findRoom(roomLocation);
        
        if (heatingZone == null || foundRoom == null) {
            return null;
        }
    
        this.getZones().forEach(
                zone -> zone.getRooms().remove(foundRoom)
        );
        
        heatingZone.addRoom(foundRoom);
        return foundRoom;
    }
    
    /**
     * Modifies some period's target temperature for a specific zone.
     * @param zoneId zone's id
     * @param zonePeriod zone period
     * @param targetTemperature new target temperature
     */
    public HeatingZone setZonePeriodTargetTemperature(Integer zoneId, HeatingZonePeriod zonePeriod, double targetTemperature) {
        HeatingZone heatingZone = this.findZone(zoneId);
        
        if (heatingZone == null) {
            return null;
        }
        
        heatingZone.getPeriods().setTargetTemperature(zonePeriod, targetTemperature);
        return heatingZone;
    }
    
    /**
     * Removes a room from a heating zone.
     * @param zoneId zone's id
     * @param roomLocation room's location
     * @return removed room
     */
    public Room removeRoomFromZone(Integer zoneId, LocationPosition roomLocation) {
        HeatingZone heatingZone = this.findZone(zoneId);
        Room room = this.getHouseLayoutModel().findRoom(roomLocation);
        
        if (heatingZone == null || room == null) {
            return null;
        }
        
        heatingZone.removeRoom(room);
        
        return room;
    }
    
    /**
     * Find a heating zone using the zone id.
     * @param zoneId zone's id
     * @return The found heating zone
     */
    public HeatingZone findZone(Integer zoneId) {
        return this.getZones().stream().filter(zone -> zone.getId().equals(zoneId)).findFirst().orElse(null);
    }

    /**
     * Get temperature for a room
     * @param locationPosition room's position
     * @return temperature of room
     */
    public Double getRoomTemperature(LocationPosition locationPosition){
        return Objects.requireNonNull(this.getHouseLayoutModel().findRoom(locationPosition)).getTemperature();
    }
    
    /**
     * Override a room's temperature
     * @param locationPosition room's location
     * @param overrideTemperature overridden temperature
     * @return the overridden room
     */
    public Room overrideRoomTemperature(LocationPosition locationPosition, Double overrideTemperature){
        Room foundRoom = this.getHouseLayoutModel().findRoom(locationPosition);
        foundRoom.setTemperature(overrideTemperature);
        return foundRoom;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("timeIncrement")) {
            for (HeatingZone zone: zones) {
                zone.adjustRoomTemperatures((LocalDateTime) evt.getNewValue());
            }
        }
    }
    
    @Override
    public void reset() {
        this.setZones(new ArrayList<>());
    }
    
}
