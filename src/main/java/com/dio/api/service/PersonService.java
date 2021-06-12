package com.dio.api.service;

import com.dio.api.dto.request.PersonDTO;
import com.dio.api.entity.Person;
import com.dio.api.exception.PersonNotFoundException;
import com.dio.api.repository.PersonRepository;
import lombok.AllArgsConstructor;
import com.dio.api.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper mapper = PersonMapper.INSTANCE;

    public String createPerson(PersonDTO personDTO) {
        Person person = personDTO.toEntity();

        person = personRepository.save(person);

        return "Created person with ID " + person.getId();
    }

    public List<PersonDTO> listAll() {
        List<Person> personList = personRepository.findAll();

        return personList
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = this.verifyIfExists(id);

        return mapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        this.verifyIfExists(id);

        personRepository.deleteById(id);
    }

    public String updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        this.verifyIfExists(id);

        Person person = mapper.toModel(personDTO);

        person.setId(id);

        person = personRepository.save(person);

        return "Updated person with ID " + person.getId();
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
