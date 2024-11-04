package employee.batch.helper;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import employee.batch.entity.Employee;
import employee.batch.entity.EmployeePhone;
import employee.batch.repository.BatchErrorsRepository;
import employee.batch.repository.BatchProcessRepository;
import employee.batch.repository.EmployeePhoneRepository;
import employee.batch.repository.EmployeeRepository;
import employee.batch.utility.BatchErrors;
import employee.batch.utility.BatchProcess;
import employee.batch.vo.EmployeeCsvDto;
import jakarta.transaction.Transactional;

@Service
public class BatchProcessService {

	private static final Logger log = LoggerFactory.getLogger(BatchProcessService.class);

	@Autowired
	private BatchProcessRepository batchProcessRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeePhoneRepository employeePhoneRepository;

	@Autowired
	private BatchErrorsRepository batchErrorsRepository;

	@Value("${fileDirectory}")
	private String fileDirectory;

	private static final String DIRECTORY_PATH = "/path/to/directory";
	private static final String FILE_NAME = "employee.csv";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	public void processEmployeeFile() {
		File file = new File(fileDirectory, FILE_NAME);
		if (!file.exists()) {
			log.error("File not found: " + file.getPath());
			return;
		}

		BatchProcess batchProcess = new BatchProcess();
		batchProcess.setProcessName("Employee Upsert batch process");
		batchProcess.setStartTimestamp(LocalDateTime.now());
		batchProcessRepository.save(batchProcess);

		int insertedCount = 0;
		int updatedCount = 0;
		int errorCount = 0;

		try (FileReader fileReader = new FileReader(file)) {
			List<EmployeeCsvDto> employeeCsvDtos = parseCsv(file);
			for (EmployeeCsvDto employeeCsvDto : employeeCsvDtos) {

				log.info("EmpID: " + employeeCsvDto.getEmployeeId());
				try {
					Employee employee = employeeRepository.findByEmployeeId(employeeCsvDto.getEmployeeId());
					if (employee == null) {
						employee = new Employee();
						insertedCount++;
					} else {
						updatedCount++;
					}
					employee.setEmployeeId(employeeCsvDto.getEmployeeId());
					employee.setFirstName(employeeCsvDto.getFirstName());
					employee.setLastName(employeeCsvDto.getLastName());
					employee.setEmail(employeeCsvDto.getEmail());
					employee.setDoj(employeeCsvDto.getDoj());
					employee.setSalary(employeeCsvDto.getSalary());

					employeeRepository.save(employee);

					List<String> phoneNumbers = Arrays.stream(employeeCsvDto.getPhoneNumbers().split(",\\s*"))
							.collect(Collectors.toList());

					for (String phoneNumber : phoneNumbers) {
						List<EmployeePhone> existingPhone = employeePhoneRepository
								.findByEmployeeAndPhoneNumber(employee.getEmployeeId(), phoneNumber);
						if (existingPhone.isEmpty()) {
							EmployeePhone phone = new EmployeePhone();
							phone.setPhoneNumber(phoneNumber);
							phone.setEmployee(employee);
							employeePhoneRepository.save(phone);
						}
					}

					removeOldEmployeePhones(employee, phoneNumbers);

				} catch (Exception ex) {
					ex.printStackTrace();
					errorCount++;
					logError(employeeCsvDto.getEmployeeId(), ex.getMessage());
				}
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yy-HH-mm-ss"));
			File processedFile = new File(DIRECTORY_PATH, "processed-employees-" + timestamp + ".csv");
			file.renameTo(processedFile);

			batchProcess.setEndTimestamp(LocalDateTime.now());
			batchProcess.setInsertedRecordCount(insertedCount);
			batchProcess.setUpdatedRecordCount(updatedCount);
			batchProcess.setErroredRecordCount(errorCount);
			batchProcess.setProcessedFileName(processedFile.getName());
			batchProcessRepository.save(batchProcess);

		} catch (Exception e) {
			log.error("Error processing batch file: " + e.getMessage());
			logError(null, "Error processing batch file: " + e.getMessage());
		}
	}

	private void updateOrAddEmployeePhones(Employee employee, List<String> newPhoneNumbers) {
		for (String phoneNumber : newPhoneNumbers) {
			List<EmployeePhone> existingPhones = employeePhoneRepository
					.findByEmployeeAndPhoneNumber(employee.getEmployeeId(), phoneNumber);
			if (existingPhones.isEmpty()) {
				EmployeePhone phone = new EmployeePhone();
				phone.setPhoneNumber(phoneNumber);
				phone.setEmployee(employee);
				employeePhoneRepository.save(phone);
			}
		}
	}

	private void removeOldEmployeePhones(Employee employee, List<String> newPhoneNumbers) {
		List<EmployeePhone> existingPhones = employeePhoneRepository.findByEmployee(employee);
		for (EmployeePhone existingPhone : existingPhones) {
			if (!newPhoneNumbers.contains(existingPhone.getPhoneNumber())) {
				employeePhoneRepository.delete(existingPhone);
			}
		}
	}

	private void logError(Integer employeeId, String errorMessage) {
		BatchErrors error = new BatchErrors();
		error.setFilename(FILE_NAME);
		error.setMessage("Error processing Employee ID: " + employeeId);
		error.setErrorTimestamp(LocalDateTime.now());
		batchErrorsRepository.save(error);
	}

	private List<EmployeeCsvDto> parseCsv(File file) {
		List<EmployeeCsvDto> employees = null;

		try (Reader reader = new FileReader(file)) {
			CsvToBean<EmployeeCsvDto> csvToBean = new CsvToBeanBuilder<EmployeeCsvDto>(reader)
					.withType(EmployeeCsvDto.class).withIgnoreLeadingWhiteSpace(true).build();

			employees = csvToBean.parse();
			if (!employees.isEmpty())
				for (EmployeeCsvDto employee : employees) {
					if (employee.getEmployeeId() == null) {
						logError(employee.getEmployeeId(), "EmployeeID cannot be null");
					}

					if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
						logError(employee.getEmployeeId(), "First Name cannot be null");
					}

					if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
						logError(employee.getEmployeeId(), "Last Name cannot be null");
					}

					if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
						logError(employee.getEmployeeId(), "Email cannot be null");
					} else if (!isValidEmail(employee.getEmail())) {
						logError(employee.getEmployeeId(), "Email is not valid");
					}

					if (employee.getPhoneNumbers() == null || employee.getPhoneNumbers().trim().isEmpty()) {
						logError(employee.getEmployeeId(), "Phone number Error");
					} else {
						String[] phones = employee.getPhoneNumbers().split(",\\s*");
						for (String phone : phones) {
							if (!phone.matches("\\d{10}")) {
								logError(employee.getEmployeeId(), "EmployeeID cannot be null");

							}
						}
					}

					if (employee.getDoj() == null || !isValidDate(employee.getDoj())) {
						logError(employee.getEmployeeId(), "DOJ cannot be null");
					}

					if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
						logError(employee.getEmployeeId(), "Salary cannot be null");
					}
				}
			log.info("Parsed {} employees from CSV file", employees.size());
		} catch (Exception e) {
			log.error("Error occurred while parsing employees.csv", e);
		}
		return employees;
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return email.matches(emailRegex);
	}

	private boolean isValidDate(LocalDate date) {
		return date != null;
	}
}
