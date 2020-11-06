package com.smart.home.backend.model.simulationparameters.module.command;

import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.model.houselayout.HouseLayoutModel;
import com.smart.home.backend.model.houselayout.Person;
import org.springframework.http.ResponseEntity;

public class PersonManagementCommand extends AbstractCommand<HouseLayoutModel, PersonInput, Person> {

    public PersonManagementCommand() {
        super("Person Management", true);
    }

    @Override
    public ResponseEntity<Person> execute(HouseLayoutModel houseLayoutModel, PersonInput personInput) {
        return null;
    }

}
