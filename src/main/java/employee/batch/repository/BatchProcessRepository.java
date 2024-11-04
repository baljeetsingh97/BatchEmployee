package employee.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import employee.batch.utility.BatchProcess;

@Repository
public interface BatchProcessRepository extends JpaRepository<BatchProcess, Integer> {

}
