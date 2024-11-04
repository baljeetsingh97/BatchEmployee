package employee.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import employee.batch.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Employee findByEmail(String email);

	Employee findByEmployeeId(Integer employeeId);
}
