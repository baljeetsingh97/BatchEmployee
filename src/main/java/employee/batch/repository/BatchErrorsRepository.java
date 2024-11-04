package employee.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import employee.batch.utility.BatchErrors;

@Repository
public interface BatchErrorsRepository extends JpaRepository<BatchErrors, Integer> {

}
