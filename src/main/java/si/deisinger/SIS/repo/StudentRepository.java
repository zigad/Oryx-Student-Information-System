package si.deisinger.SIS.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import si.deisinger.SIS.model.Students;

public interface StudentRepository extends JpaRepository<Students, Long> {

}
