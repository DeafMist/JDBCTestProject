package dao;

import model.Employee;

import java.util.List;

public interface EmployeeDAO {
    void addEmployee(Employee employee);

    Employee getEmployeeById(int id);

    List<Employee> getAllEmployees();

    void updateEmployeeById(int id, Employee employee);

    void deleteEmployeeById(int id);
}
