package ru.anton.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.anton.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// DAO - data access object
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }
    public Optional<Person> show(String email){
       return jdbcTemplate.query("SELECT * FROM person WHERE email=?", new Object[]{email},
               new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
    public Person show(int id){


        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);

    }
    public void save(Person person){
        jdbcTemplate.update("INSERT INTO person(name, age, email) VALUES(?, ?, ?)", person.getName(), person.getAge()
        , person.getEmail());


    }
    public void update(int id, Person updatedPerson){
        jdbcTemplate.update("UPDATE person SET name=?, age=?, email=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);

    }

    public void remove(int id){
       jdbcTemplate.update("DELETE FROM person WHERE id=?", id);


    }
    ////////////////////
    //testing multiple vs batch updates
    ///////////////////

    public void testMultipleUpdate(){
        long start;
        long end;
        List<Person> people = createThousandPerson();
        start = System.currentTimeMillis();
        for (Person p : people
             ) {
            jdbcTemplate.update("INSERT INTO person VALUES (?, ?, ?, ?)",
                    p.getId(), p.getName(), p.getAge(), p.getEmail());
        }
        end = System.currentTimeMillis();

        long time = end - start;
        System.out.println("Time: "+time);
    }
    public void testBatchUpdate(){
        long start;
        long end;
        List<Person> people = createThousandPerson();
        start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, people.get(i).getId());
                        preparedStatement.setString(2, people.get(i).getName());
                        preparedStatement.setInt(3, people.get(i).getAge());
                        preparedStatement.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });
        end = System.currentTimeMillis();

        long time = end - start;
        System.out.println("Time: "+time);
    }

    private List<Person> createThousandPerson() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i, "name" + i, 30, "name" + i + "@gmail.com"));
        }
        return people;
    }
}
