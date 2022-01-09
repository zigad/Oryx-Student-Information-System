package si.deisinger.SIS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.deisinger.SIS.exceptions.NotFoundException;
import si.deisinger.SIS.model.SchoolClass;
import si.deisinger.SIS.model.Students;
import si.deisinger.SIS.repo.SchoolClassRepository;
import si.deisinger.SIS.repo.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	SchoolClassRepository schoolClassRepository;

	/**
	 * GET Request: http://localhost:1598/students
	 * Returns all students available in DB
	 * <p>
	 * Example Response:
	 * <p>
	 * [
	 * {
	 * "studentId": 1,
	 * "firstName": "Žiga",
	 * "lastName": "Deisinger"
	 * },
	 * {
	 * "studentId": 2,
	 * "firstName": "Marjan",
	 * "lastName": "Pršovil"
	 * },
	 * {
	 * "studentId": 3,
	 * "firstName": "Eugenus",
	 * "lastName": "Allmighty"
	 * },
	 * {
	 * "studentId": 4,
	 * "firstName": "Papež",
	 * "lastName": "Svizcev"
	 * },
	 * {
	 * "studentId": 5,
	 * "firstName": "Rokovil",
	 * "lastName": "Škrbac"
	 * }
	 * ]
	 */
	@GetMapping("/students")
	public ResponseEntity getAllStudents() {
		List<Students> studentsList = studentRepository.findAll();
		return ResponseEntity.ok().body(studentsList);
	}

	/**
	 * GET Request: http://localhost:1598/student/4
	 * Returns specific student from DB based on provided ID
	 * <p>
	 * Example Response:
	 * <p>
	 * {
	 * "studentId": 4,
	 * "firstName": "Papež",
	 * "lastName": "Svizcev"
	 * }
	 *
	 * @param id - Student ID
	 */
	@GetMapping("/student/{id}")
	public Students retrieveStudent(@PathVariable long id) {
		Optional<Students> student = studentRepository.findById(id);
		if (!student.isPresent())
			throw new NotFoundException(Long.toString(id));
		return student.get();
	}

	/**
	 * Generate new student and write it to DB
	 * <p>
	 * POST Request: http://localhost:1598/new-student
	 * <p>
	 * Header: Content-Type: application/json
	 * Body:
	 * {
	 * "firstName": "Papež",
	 * "lastName": "Svizcev"
	 * }
	 * <p>
	 * Example Response:
	 * <p>
	 * {
	 * "studentId": 5,
	 * "firstName": "Papež",
	 * "lastName": "Svizcev"
	 * }
	 * <p>
	 * <p>
	 * Note: There are no checks for duplicate values!
	 */
	@PostMapping("/new-student")
	ResponseEntity<?> newStudent(@RequestBody Students newStudent) {
		return ResponseEntity.ok().body(studentRepository.save(newStudent));
	}

	/**
	 * DELETE Request: http://localhost:1598/student/10
	 * Deletes a student from db
	 *
	 * @param id
	 */
	@DeleteMapping("/student/{id}")
	public void deleteStudent(@PathVariable long id) {
		Optional<Students> student = studentRepository.findById(id);
		if (!student.isPresent())
			throw new NotFoundException(Long.toString(id));
		else {
			studentRepository.deleteById(id);
		}
	}

	/**
	 * Get all available classes
	 *
	 * @return
	 */
	@GetMapping("/classes")
	public ResponseEntity getAllClasses() {
		List<SchoolClass> classesList = schoolClassRepository.findAll();
		return ResponseEntity.ok().body(classesList);
	}

	/**
	 * Search for specific class based on requested parameter sent in request
	 *
	 * @param className - name of class in request parameter
	 * @return - all classes matching search
	 */
	@GetMapping("/class")
	public ResponseEntity searchForClass(@RequestParam(name = "className") String className) {
		return ResponseEntity.status(HttpStatus.OK).body(schoolClassRepository.findAllByName(className));
	}

	/**
	 * Generate new class and write it to db
	 *
	 * @param newClass - new class in request body
	 * @return
	 */
	@PostMapping("/new-class")
	public ResponseEntity addClass(@RequestBody SchoolClass newClass) {
		return ResponseEntity.ok().body(schoolClassRepository.save(newClass));
	}

	/**
	 * Apply Students to Class.
	 *
	 * (not enough time to implement, other more important stuff)
	 *
	 */
	@PutMapping("/student/class/{classId}")
	public ResponseEntity applyStudentToClass(@PathVariable Long classId, @RequestBody Students students) {
		Optional<SchoolClass> optionalClasses = schoolClassRepository.findById(classId);
		if (optionalClasses.isPresent()) {
			SchoolClass classes = optionalClasses.get();
			List<Students> studentsList = (List<Students>) classes.getStudents();
			studentsList.add(students);
			classes.setStudents(studentsList);
			schoolClassRepository.save(classes);
			return ResponseEntity.ok().body("Students applied to class successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Remove Students application from specific class
	 *
	 * (not enough time to implement, other more important stuff)
	 *
	 * @param id
	 * @param classId
	 * @return
	 */
	@DeleteMapping("/student/{id}/class/{classId}")
	public ResponseEntity removeStudentFromClass(@PathVariable Long id, @PathVariable Long classId) {
		Optional<Students> optionalStudent = studentRepository.findById(id);
		Optional<SchoolClass> optionalClasses = schoolClassRepository.findById(classId);
		if (optionalStudent.isPresent() && optionalClasses.isPresent()) {
			if (optionalClasses.get().getStudents().contains(optionalStudent.get())) {
				optionalClasses.get().getStudents().remove(optionalStudent.get());
				schoolClassRepository.save(optionalClasses.get());
				return ResponseEntity.ok().body("Students enrollment cancelled");
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
