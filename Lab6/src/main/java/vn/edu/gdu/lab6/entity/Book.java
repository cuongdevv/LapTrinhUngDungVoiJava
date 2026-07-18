package vn.edu.gdu.lab6.entity;

import java.time.LocalDateTime;

/**
 * Database Entity dai dien cho sach (Bai 2 - Chuong 6).
 * Chua truong nhay cam "internalNotes" va cac truong audit (createdAt/updatedAt)
 * -> se bi che giau khoi Client thong qua BookResponseDto.
 */
public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Double price;
    private String internalNotes; // Ghi chu noi bo nhay cam - khong tra ve cho Client
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
