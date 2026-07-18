package vn.edu.gdu.lab6.dto;

import jakarta.validation.constraints.*;

/**
 * Request DTO tiep nhan du lieu tao/cap nhat sinh vien (Bai tap ve nha - Chuong 6).
 */
public class StudentCreateDto {

    @NotBlank(message = "Mã sinh viên không được phép để trống!")
    @Size(min = 5, max = 10, message = "Mã sinh viên phải từ 5 đến 10 ký tự!")
    private String studentCode;

    @NotBlank(message = "Họ tên không được phép để trống!")
    @Size(min = 2, max = 50, message = "Họ tên phải từ 2 đến 50 ký tự!")
    private String fullName;

    @NotBlank(message = "Email không được phép để trống!")
    @Email(message = "Email không đúng định dạng!")
    private String email;

    @NotNull(message = "GPA không được phép để trống!")
    @DecimalMin(value = "0.0", message = "GPA tối thiểu là 0.0!")
    @DecimalMax(value = "4.0", message = "GPA tối đa là 4.0!")
    private Double gpa;

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
}
