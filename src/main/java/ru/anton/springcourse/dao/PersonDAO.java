package ru.anton.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.anton.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// DAO - data access object
@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private static final String URL = "jdbc:postgresql://localhost:5433/local_spring_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";
    private static Connection connection;
    static{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Person> index(){

        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM person";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Person p: people
             ) {
            System.out.println(p);
        }
        return people;
    }

    public Person show(int id){

       // return people.stream().filter(person -> person.getId()==id).findAny().orElse(null);
        return new Person();
    }
    public void save(Person person){
        try{
            Statement statement = connection.createStatement();
            String SQL = String.format("insert into person values(%d, '%s', %d, '%s')",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void update(int id, Person updatedPerson){
//        Person personToUpdate = show(id);
//        personToUpdate.setName(updatedPerson.getName());
//        personToUpdate.setAge(updatedPerson.getAge());
//        personToUpdate.setEmail(updatedPerson.getEmail());

    }

    public void remove(int id){
        //people.removeIf(person -> person.getId()==id);
    }
}
