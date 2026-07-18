package vn.edu.gdu.lab6.controller;

import vn.edu.gdu.lab6.dto.StudentCreateDto;
import vn.edu.gdu.lab6.dto.StudentResponseDto;
import vn.edu.gdu.lab6.entity.Student;
import vn.edu.gdu.lab6.exception.ResourceNotFoundException;
import vn.edu.gdu.lab6.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * RESTful Controller quan ly Student (Bai tap ve nha - Chuong 6).
 * Ho tro day du GET/POST/PUT/PATCH/DELETE, dung chung GlobalExceptionHandler (Bai 4).
 * Mo phong CSDL bang mot List trong bo nho (in-memory).
 */
@RestController
@RequestMapping(path = "/api/students", produces = "application/json")
@CrossOrigin(origins = "*")
public class StudentRestController {

    private final List<Student> mockDatabase = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final StudentMapper studentMapper;

    public StudentRestController(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;

        // Tao san du lieu mau trong DB gia lap
        Student s1 = new Student();
        s1.setId(idGenerator.getAndIncrement());
        s1.setStudentCode("SV001");
        s1.setFullName("Nguyễn Văn An");
        s1.setEmail("an.nv@gdu.edu.vn");
        s1.setGpa(3.45);
        s1.setInternalNotes("Sinh viên lớp trưởng. Diện học bổng.");
        s1.setCreatedAt(LocalDateTime.now());
        mockDatabase.add(s1);
    }

    // 1. GET ALL (200 OK)
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        List<StudentResponseDto> result = mockDatabase.stream()
                .map(studentMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // 2. GET BY ID (200 OK hoac nem Exception -> 404)
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        Student student = mockDatabase.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên có ID: " + id));
        return ResponseEntity.ok(studentMapper.toResponseDto(student));
    }

    // 3. POST (201 Created kem Location Header)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentCreateDto createDto) {
        Student studentEntity = studentMapper.toEntity(createDto);
        studentEntity.setId(idGenerator.getAndIncrement());
        studentEntity.setCreatedAt(LocalDateTime.now());
        studentEntity.setInternalNotes("Sinh viên mới tạo trực tiếp từ REST API");
        mockDatabase.add(studentEntity);

        StudentResponseDto responseDto = studentMapper.toResponseDto(studentEntity);

        // Tao URI tro toi tai nguyen moi vua duoc sinh ra
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    // 4. PUT - Cap nhat thay the toan dien (200 OK)
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentCreateDto updateDto) {
        Student existingStudent = mockDatabase.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cập nhật thất bại! Không tìm thấy ID sinh viên: " + id));
        studentMapper.updateEntityFromDto(updateDto, existingStudent);
        return ResponseEntity.ok(studentMapper.toResponseDto(existingStudent));
    }

    // 5. PATCH - Cap nhat tung phan cu the (200 OK)
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<StudentResponseDto> partialUpdateStudent(
            @PathVariable Long id,
            @RequestBody StudentCreateDto patchDto) {
        Student existingStudent = mockDatabase.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cập nhật thất bại! Không tìm thấy ID sinh viên: " + id));

        if (patchDto.getStudentCode() != null) existingStudent.setStudentCode(patchDto.getStudentCode());
        if (patchDto.getFullName() != null) existingStudent.setFullName(patchDto.getFullName());
        if (patchDto.getEmail() != null) existingStudent.setEmail(patchDto.getEmail());
        if (patchDto.getGpa() != null) existingStudent.setGpa(patchDto.getGpa());

        return ResponseEntity.ok(studentMapper.toResponseDto(existingStudent));
    }

    // 6. DELETE (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student student = mockDatabase.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không thể xóa! Không tìm thấy ID sinh viên: " + id));
        mockDatabase.remove(student);
        return ResponseEntity.noContent().build();
    }
}
