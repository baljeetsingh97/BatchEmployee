package employee.batch.utility;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BatchProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer processId;

	private String processName;
	private LocalDateTime startTimestamp;
	private LocalDateTime endTimestamp;
	private String processedFileName;
	private int insertedRecordCount;
	private int updatedRecordCount;
	private int erroredRecordCount;

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public LocalDateTime getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(LocalDateTime startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public LocalDateTime getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(LocalDateTime endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getProcessedFileName() {
		return processedFileName;
	}

	public void setProcessedFileName(String processedFileName) {
		this.processedFileName = processedFileName;
	}

	public int getInsertedRecordCount() {
		return insertedRecordCount;
	}

	public void setInsertedRecordCount(int insertedRecordCount) {
		this.insertedRecordCount = insertedRecordCount;
	}

	public int getUpdatedRecordCount() {
		return updatedRecordCount;
	}

	public void setUpdatedRecordCount(int updatedRecordCount) {
		this.updatedRecordCount = updatedRecordCount;
	}

	public int getErroredRecordCount() {
		return erroredRecordCount;
	}

	public void setErroredRecordCount(int erroredRecordCount) {
		this.erroredRecordCount = erroredRecordCount;
	}

}
