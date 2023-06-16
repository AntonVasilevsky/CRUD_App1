package ru.anton.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.anton.springcourse.models.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// DAO - data access object
@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>(Arrays.asList(
                new Person(++PEOPLE_COUNT, "Tom", 23, "qwe@gmail.com"),
                new Person(++PEOPLE_COUNT, "Tim", 33, "asd@gail.com"),
                new Person(++PEOPLE_COUNT, "Bob", 44, "zxc@gmail.com"),
                new Person(++PEOPLE_COUNT, "Sam", 55, "rty@gmail.com"),
                new Person(++PEOPLE_COUNT, "Dan", 66, "bnm@gmail.com")

        ));

    }

    public List<Person> index(){
        return people;
    }

    public Person show(int id){
        return people.stream().filter(person -> person.getId()==id).findAny().orElse(null);
    }
    public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }
    public void update(int id, Person updatedPerson){
        Person personToUpdate = show(id);
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setAge(updatedPerson.getAge());
        personToUpdate.setEmail(updatedPerson.getEmail());

    }

    public void remove(int id){
        people.removeIf(person -> person.getId()==id);
    }
}
