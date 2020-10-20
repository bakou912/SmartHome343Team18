package com.smart.home.backend.model.houselayout;

import com.smart.home.backend.input.PersonInput;
import com.smart.home.backend.service.util.IdUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for outside the house.
 */
@Getter
@Setter
public class Outside {
	
	@NonNull
	private List<Person> persons = new ArrayList<>();
	
	private final IdUtil personId = new IdUtil();
	
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
