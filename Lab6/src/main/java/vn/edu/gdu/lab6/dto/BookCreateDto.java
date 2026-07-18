package vn.edu.gdu.lab6.dto;

import jakarta.validation.constraints.*;

/**
 * Request DTO tiep nhan du lieu tao/cap nhat sach (Bai 2 - Chuong 6).
 * Khai bao rang buoc Jakarta Bean Validation ngay tren truong du lieu.
 */
public class BookCreateDto {

    @NotBlank(message = "Tiêu đề sách không được phép để trống!")
    @Size(min = 2, max = 150, message = "Tiêu đề phải từ 2 đến 150 ký tự!")
    private String title;

    @NotBlank(message = "Tên tác giả không được phép để trống!")
    private String author;

    @NotBlank(message = "Mã ISBN không được phép để trống!")
    @Pattern(regexp = "^(978|979)?\\d{9}(\\d|X)$",
            message = "Mã ISBN không đúng định dạng (10 hoặc 13 chữ số)!")
    private String isbn;

    @NotNull(message = "Giá tiền không được phép để trống!")
    @Min(value = 10000, message = "Giá tiền tối thiểu phải là 10.000 VNĐ!")
    private Double price;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
