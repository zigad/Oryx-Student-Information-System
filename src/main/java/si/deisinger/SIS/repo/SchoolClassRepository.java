package si.deisinger.SIS.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import si.deisinger.SIS.model.SchoolClass;

import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
	Optional<SchoolClass> findAllByName(String className);
}
