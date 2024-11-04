package employee.batch.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import employee.batch.entity.Employee;
import employee.batch.entity.EmployeePhone;

public class EmployeeValidator {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	public static boolean validateEmployeeId(Integer employeeId) {
		return employeeId != null && employeeId > 0;
	}

	public static boolean validateName(String name) {
		return name != null && !name.trim().isEmpty();
	}

	public static boolean validateEmail(String email) {
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}

	public static boolean validateDateOfJoining(String doj) {
		try {
			LocalDate date = LocalDate.parse(doj);
			return !date.isAfter(LocalDate.now());
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public static boolean validateSalary(BigDecimal salary) {
		return salary != null && salary.compareTo(BigDecimal.ZERO) > 0;
	}

	public static boolean validatePhoneNumbers(List<String> phoneNumbers) {
		return phoneNumbers != null && !phoneNumbers.isEmpty()
				&& phoneNumbers.stream().allMatch(number -> number.matches("\\d{10}"));
	}

	public static boolean validateEmployee(Employee employee) {
		return validateEmployeeId(employee.getEmployeeId()) && validateName(employee.getFirstName())
				&& validateName(employee.getLastName()) && validateEmail(employee.getEmail())
				&& validateDateOfJoining(employee.getDoj().toString()) && validateSalary(employee.getSalary())
				&& validatePhoneNumbers(
						employee.getPhoneNumbers().stream().map(EmployeePhone::getPhoneNumber).toList());
	}
}
