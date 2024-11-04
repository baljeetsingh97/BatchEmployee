package employee.batch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import employee.batch.entity.Employee;
import employee.batch.entity.EmployeePhone;

@Repository
public interface EmployeePhoneRepository extends JpaRepository<EmployeePhone, Integer> {

	@Query(value = "select * from employee_phone where employee_id = ?1 and phone_number = ?2", nativeQuery = true)
	List<EmployeePhone> findByEmployeeAndPhoneNumber(Integer employeeId, String emp);

	@Query(value = "select * from employee_phone where employee_id = ?1", nativeQuery = true)
	List<EmployeePhone> findByEmployee(Integer employeeId);

	List<EmployeePhone> findByEmployee(Employee employee);
}
