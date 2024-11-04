package employee.batch.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_phone")
public class EmployeePhone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "phone_id")
	private Integer phoneId;

	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
	private Employee employee;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	// Getters and Setters, Constructor, etc.

	public EmployeePhone() {
	}

	public EmployeePhone(Employee employee, String phoneNumber) {
		this.employee = employee;

		this.phoneNumber = phoneNumber;
	}

	public Integer getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Integer phoneId) {
		this.phoneId = phoneId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
