package com.smart.home.backend.model.heating;

import com.smart.home.backend.input.HeatingZoneInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Room;
import com.smart.home.backend.model.simulationparameters.location.LocationPosition;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;


/**
 * Heating model.
 */
@Getter
@Component
public class HeatingModel {
    
    
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
        Timer timer = new Timer();
        // TODO: implement delay using the simulation's modifyiable speed
        timer.scheduleAtFixedRate(new TemperatureAdjustmentTask(this.getZones()), 1000L, 1000L);
    }
    
    /**
     * Adds a zone to the zone list
     * @param heatingZoneInput zone input
     */
    public boolean addZone(HeatingZoneInput heatingZoneInput) {
        if (this.getZones().stream().anyMatch(zone -> zone.getName().equals(heatingZoneInput.getName()))) {
            return false;
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
        
        return this.getZones().add(newZone);
    }
    
    /**
     * Removes a zone from the zone list
     * @param zoneId zone's id
     */
    public boolean removeZone(Integer zoneId) {
        return this.getZones().removeIf(zone -> zone.getId().equals(zoneId));
    }
    
    /**
     * Adds a room to a heating zone.
     * @param zoneId zone's id
     * @param roomLocation room's location
     * @return Whether the room was added to the zone or not
     */
    public boolean addRoomToZone(Integer zoneId, LocationPosition roomLocation) {
        HeatingZone heatingZone = this.findZone(zoneId);
        Room room = this.getHouseLayoutModel().findRoom(roomLocation);
        
        if (heatingZone == null || room == null) {
            return false;
        }
        
        return heatingZone.addRoom(room);
    }
    
    /**
     * Removes a room from a heating zone.
     * @param zoneId zone's id
     * @param roomLocation room's location
     * @return Whether the room was removed from the zone or not
     */
    public boolean removeRoomFromZone(Integer zoneId, LocationPosition roomLocation) {
        HeatingZone heatingZone = this.findZone(zoneId);
        Room room = this.getHouseLayoutModel().findRoom(roomLocation);
        
        if (heatingZone == null || room == null) {
            return false;
        }
        
        return heatingZone.removeRoom(room);
    }
    
    /**
     * Find a heating zone using the zone id.
     * @param zoneId zone's id
     * @return The found heating zone
     */
    public HeatingZone findZone(Integer zoneId) {
        return this.getZones().stream().filter(zone -> zone.getId().equals(zoneId)).findFirst().orElse(null);
    }
    
}
