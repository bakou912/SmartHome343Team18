package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.model.ModelObject;
import com.smart.home.backend.service.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for a room.
 */
@Getter
@SuperBuilder
@AllArgsConstructor
public class Location extends ModelObject {
	
	@Setter
	private String name;
	@Setter
	@Builder.Default
	private Light light = new Light();
	@Setter
	@Builder.Default
	private List<Person> persons = new ArrayList<>();
	
	private final IdUtil personId = new IdUtil();
	
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
