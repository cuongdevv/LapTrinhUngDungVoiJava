package vn.edu.gdu.lab6.dto;

/**
 * Response DTO gui du lieu sinh vien ra Client (Bai tap ve nha - Chuong 6).
 * Loai bo internalNotes, createdAt de bao mat thong tin noi bo.
 */
public class StudentResponseDto {
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private Double gpa;

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
}
