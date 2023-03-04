package dao.impl;

import dao.EmployeeDAO;
import model.City;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    private final Connection connection;

    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addEmployee(Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO employee (first_name, last_name, gender, age, city_id) " +
                        "VALUES ((?), (?), (?), (?), (?))"
        )) {
            statement.setString(1, employee.getFirst_name());
            statement.setString(2, employee.getLast_name());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        Employee employee = new Employee();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM employee INNER JOIN city " +
                "ON employee.city_id = city.city_id WHERE id = (?)")) {
            statement.setInt(1, id);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employee.setFirst_name(resultSet.getString("first_name"));
                employee.setLast_name(resultSet.getString("last_name"));
                employee.setGender(resultSet.getString("gender"));
                employee.setAge(resultSet.getInt("age"));
                employee.setCity(new City(resultSet.getInt("city_id"), resultSet.getString("city_name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employee;
    }


        @Override
    public List<Employee> getAllEmployees() {
            List<Employee> employees = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM employee INNER JOIN city " +
                            "ON employee.city_id = city.city_id")) {

                final ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = Integer.parseInt(resultSet.getString("id"));
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String gender = resultSet.getString("gender");
                    int age = Integer.parseInt(resultSet.getString("age"));
                    City city = new City(resultSet.getInt("city_id"),
                            resultSet.getString("city_name"));

                    employees.add(new Employee(id, first_name, last_name, gender, age, city));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return employees;
        }

    @Override
    public void updateEmployeeById(int id, Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE employee SET first_name = (?), " +
                        "last_name = (?), gender = (?), age = (?), city_id = (?) WHERE id = (?)"
        )) {
            statement.setString(1, employee.getFirst_name());
            statement.setString(2, employee.getLast_name());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getId());
            statement.setInt(6, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmployeeById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM employee WHERE id = (?)"
        )) {
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
