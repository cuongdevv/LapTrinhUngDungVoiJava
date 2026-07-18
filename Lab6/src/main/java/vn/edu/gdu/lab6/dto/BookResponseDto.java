package vn.edu.gdu.lab6.dto;

/**
 * Response DTO gui du lieu ra Client (Bai 2 - Chuong 6).
 * Loai bo internalNotes, createdAt, updatedAt de bao mat thong tin noi bo.
 */
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Double price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
