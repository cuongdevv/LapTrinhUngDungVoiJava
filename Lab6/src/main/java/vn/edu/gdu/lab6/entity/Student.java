package vn.edu.gdu.lab6.entity;

import java.time.LocalDateTime;

/**
 * Database Entity dai dien cho sinh vien (Bai tap ve nha - Chuong 6).
 * Chua truong nhay cam "internalNotes" va "createdAt" -> che giau khoi Client
 * thong qua StudentResponseDto.
 */
public class Student {
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private Double gpa;
    private String internalNotes; // Ghi chu noi bo nhay cam - khong tra ve cho Client
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
