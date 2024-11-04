package employee.batch.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "emp_nbr", nullable = false, unique = true)
	private String empNbr;

	@Column(name = "employee_id")
	private Integer employeeId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "doj", nullable = false)
	private LocalDate doj;

	@Column(name = "salary", nullable = false)
	private BigDecimal salary;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployeePhone> phoneNumbers;

	// Getters and Setters, Constructor, etc.

	public Employee() {
	}

	public Employee(String empNbr, String firstName, String lastName, String email, LocalDate doj, BigDecimal salary) {
		this.empNbr = empNbr;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.doj = doj;
		this.salary = salary;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpNbr() {
		return empNbr;
	}

	public void setEmpNbr(String empNbr) {
		this.empNbr = empNbr;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDoj() {
		return doj;
	}

	public void setDoj(LocalDate doj) {
		this.doj = doj;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public List<EmployeePhone> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<EmployeePhone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

}
