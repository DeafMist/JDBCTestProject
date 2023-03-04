import dao.EmployeeDAO;
import dao.impl.EmployeeDAOImpl;
import model.City;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private static final String USER = "*****";
    private static final String PASSWORD = "*****";
    private static final String URL = "jdbc:postgresql://localhost:5432/skypro";

    public static void main(String[] args) {
        int id = 1;

        try (final Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);

            Employee employee = new Employee("Sasha", "Pupkin", "male", 18,
                    new City(2, "Saint-Petersburg"));

            employeeDAO.addEmployee(employee);

            employeeDAO.getEmployeeById(id);

            employeeDAO.updateEmployeeById(1, new Employee("Genshin", "Impact", "female", 22,
                    new City(3, "Kaliningrad")));

            employeeDAO.deleteEmployeeById(5);

            List<Employee> employees = new ArrayList<>(employeeDAO.getAllEmployees());

            for (Employee elem : employees) {
                System.out.println(elem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void task1(int id, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(
                     "SELECT first_name, last_name, gender, age, city_name FROM employee INNER JOIN city " +
                             "ON employee.city_id = city.city_id WHERE id = (?)")) {
            statement.setInt(1, id);

            final ResultSet resultSet = statement.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String first_name = "First name: " + resultSet.getString("first_name");
                String last_name = "Last name: " + resultSet.getString("last_name");
                String gender = "Gender: " + resultSet.getString("gender");
                String age = "Age: " + resultSet.getString("age");
                String city = "City: " + resultSet.getString("city_name");
                sb.append(first_name).append("\n")
                        .append(last_name).append("\n")
                        .append(gender).append("\n")
                        .append(age).append("\n")
                        .append(city).append("\n");
            }

            System.out.println(sb.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
