package b3backend.program;

import b3backend.response.ErrorResponse;
import b3backend.response.ProgramListResponse;
import b3backend.response.ProgramResponse;
import b3backend.user.User;
import b3backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/programs")
public class ProgramController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProgramRepository programRepository;

    @PostMapping
    public ResponseEntity<?> createProgram(@RequestBody Program program, @PathVariable int userId) {
        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (program.getProgramExercises() == null) {
            program.setProgramExercises(new ArrayList<>());
        }

        program.setUser(user);

        //uppdatera nytt program i databas
        Program newProgram = this.programRepository.save(program);

        ProgramResponse programResponse = new ProgramResponse();
        programResponse.set(newProgram);

        return new ResponseEntity<>(programResponse, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<?> getAllPrograms(@PathVariable int userId) {
        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Program> allPrograms = user.getPrograms();

        ProgramListResponse programListResponse = new ProgramListResponse();
        programListResponse.set(allPrograms);

        return new ResponseEntity<>(programListResponse, HttpStatus.OK);
    }

    @GetMapping("/{programId}")
    public ResponseEntity<?> getProgramById(@PathVariable int userId, @PathVariable int programId) {
        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Program> allPrograms = user.getPrograms();

        Program program = allPrograms.stream()
                .filter(p -> p.getId() == programId)
                .findFirst()
                .orElse(null);

        if (program == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No program with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        ProgramResponse programResponse = new ProgramResponse();
        programResponse.set(program);

        return new ResponseEntity<>(programResponse, HttpStatus.OK);
    }

    @PutMapping("/{programId}")
    public ResponseEntity<?> updateProgram(@PathVariable int userId, @PathVariable int programId, @RequestBody Program program) {
        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Program> allPrograms = user.getPrograms();

        Program programToBeUpdated = allPrograms.stream()
                .filter(p -> p.getId() == programId)
                .findFirst()
                .orElse(null);

        if (programToBeUpdated == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No program with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        programToBeUpdated.setTitle(program.getTitle());
        programToBeUpdated.setProgramExercises(program.getProgramExercises());

        Program updatedProgram = this.programRepository.save(programToBeUpdated);

        ProgramResponse programResponse = new ProgramResponse();
        programResponse.set(updatedProgram);

        return new ResponseEntity<>(programResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<?> deleteProgram(@PathVariable int userId, @PathVariable int programId) {
        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        List<Program> allPrograms = user.getPrograms();

        Program programToBeDeleted = allPrograms.stream()
                .filter(p -> p.getId() == programId)
                .findFirst()
                .orElse(null);

        if (programToBeDeleted == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No program with that id found.");

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.programRepository.delete(programToBeDeleted);

        ProgramResponse programResponse = new ProgramResponse();
        programResponse.set(programToBeDeleted);

        return new ResponseEntity<>(programResponse, HttpStatus.CREATED);
    }
}