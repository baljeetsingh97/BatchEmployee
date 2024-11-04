package employee.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import employee.batch.helper.BatchProcessService;

@RestController
public class EmployeeBatchController {

	@Autowired
	BatchProcessService batchProcessService;

//	@PostMapping("/employees")

	@Scheduled(cron = "0 30 11 * * *")
	public void batchEmployeeDetails() throws Exception {

		batchProcessService.processEmployeeFile();

	}
}