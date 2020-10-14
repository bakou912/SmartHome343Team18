package com.smart.home.backend.model.simulationParameters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
public class SystemParameters {
    private double outsideTemp;
    private double insideTemp;
    private String location;
    private Date date;
}
