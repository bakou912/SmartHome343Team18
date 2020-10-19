package com.smart.home.backend.service.mapper;

import com.smart.home.backend.model.houselayout.Person;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for a list of persons.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonsMapper {
	
	/**
	 * Maps a number of peoples to people.
	 * @param nbPersons Number of persons
	 * @return Mapped persons
	 */
	public static List<Person> map(Integer nbPersons) {
		List<Person> persons = new ArrayList<>();
		
		for (int i = 0; i < nbPersons; i++) {
			persons.add(
					Person.builder()
							.id(i)
							.build()
			);
		}
		
		return persons;
	}
	
}
