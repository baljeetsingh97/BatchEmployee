package employee.batch.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import employee.batch.utility.ListConverter;
import employee.batch.utility.LocalDateConverter;

public class EmployeeCsvDto {

	@CsvBindByName(column = "EmployeeID")
	private Integer employeeId;

	@CsvBindByName(column = "FirstName")
	private String firstName;

	@CsvBindByName(column = "LastName")
	private String lastName;

	@CsvBindByName(column = "Email")
	private String email;

	@CsvBindByName(column = "PhoneNumber")
	private String phoneNumbers;

	@CsvCustomBindByName(column = "DOJ", converter = LocalDateConverter.class)
	private LocalDate doj;

	@CsvBindByName(column = "Salary")
	private BigDecimal salary;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
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

	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
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

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
