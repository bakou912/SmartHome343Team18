package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.input.PersonInput;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;

/**
 * Class for outside the house.
 */
@Getter
@Setter
public class OutsideLocation extends Location {
	
	/**
	 * 1-parameter constructor.
	 * @param name location's name
	 */
	public OutsideLocation(String name) {
		super(name, new Light(), new ArrayList<>());
	}
	
	/**
	 * Finds a person with the corresponding id.
	 * @param id Searched person's id
	 * @return Found person
	 */
	@Nullable
	public Person findPerson(int id) {
		return this.getPersons()
				.stream()
				.filter(person -> person.getId() == id)
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Adds a person to the person list
	 * @param personInput person input
	 */
	public Integer addPerson(PersonInput personInput) {
		Integer id = this.getPersonId().newId();
		
		this.getPersons().add(
				Person.builder()
						.id(id)
						.name(personInput.getName())
						.build()
		);
		
		return id;
	}
}
