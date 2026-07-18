package vn.edu.gdu.lab6.controller;

import vn.edu.gdu.lab6.dto.BookCreateDto;
import vn.edu.gdu.lab6.dto.BookResponseDto;
import vn.edu.gdu.lab6.entity.Book;
import vn.edu.gdu.lab6.exception.ResourceNotFoundException;
import vn.edu.gdu.lab6.mapper.BookMapper;
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
 * RESTful Controller quan ly Book (Bai 5 - Chuong 6).
 * Ho tro day du GET/POST/PUT/PATCH/DELETE voi HTTP Status Code chuan.
 * Mo phong CSDL bang mot List trong bo nho (in-memory).
 */
@RestController
@RequestMapping(path = "/api/books", produces = "application/json")
@CrossOrigin(origins = "*")
public class BookRestController {

    private final List<Book> mockDatabase = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final BookMapper bookMapper;

    public BookRestController(BookMapper bookMapper) {
        this.bookMapper = bookMapper;

        // Tao san du lieu mau trong DB gia lap
        Book b1 = new Book();
        b1.setId(idGenerator.getAndIncrement());
        b1.setTitle("Spring Boot Start Here");
        b1.setAuthor("Laurentiu Spilca");
        b1.setIsbn("9781617298691");
        b1.setPrice(950000.0);
        b1.setInternalNotes("Giá mua sỉ: 35 USD. Kệ sách A-4.");
        b1.setCreatedAt(LocalDateTime.now());
        mockDatabase.add(b1);
    }

    // 1. GET ALL (200 OK)
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> result = mockDatabase.stream()
                .map(bookMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // 2. GET BY ID (200 OK hoac nem Exception -> 404)
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        Book book = mockDatabase.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cuốn sách có ID: " + id));
        return ResponseEntity.ok(bookMapper.toResponseDto(book));
    }

    // 3. POST (201 Created kem Location Header)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookCreateDto createDto) {
        Book bookEntity = bookMapper.toEntity(createDto);
        bookEntity.setId(idGenerator.getAndIncrement());
        bookEntity.setCreatedAt(LocalDateTime.now());
        bookEntity.setInternalNotes("Sách mới tạo trực tiếp từ REST API");
        mockDatabase.add(bookEntity);

        BookResponseDto responseDto = bookMapper.toResponseDto(bookEntity);

        // Tao URI tro toi tai nguyen moi vua duoc sinh ra
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    // 4. PUT - Cap nhat thay the toan dien (200 OK)
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookCreateDto updateDto) {
        Book existingBook = mockDatabase.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cập nhật thất bại! Không tìm thấy ID sách: " + id));
        bookMapper.updateEntityFromDto(updateDto, existingBook);
        existingBook.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(bookMapper.toResponseDto(existingBook));
    }

    // 5. PATCH - Cap nhat tung phan cu the (200 OK)
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<BookResponseDto> partialUpdateBook(
            @PathVariable Long id,
            @RequestBody BookCreateDto patchDto) {
        Book existingBook = mockDatabase.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cập nhật thất bại! Không tìm thấy ID sách: " + id));

        if (patchDto.getTitle() != null) existingBook.setTitle(patchDto.getTitle());
        if (patchDto.getAuthor() != null) existingBook.setAuthor(patchDto.getAuthor());
        if (patchDto.getIsbn() != null) existingBook.setIsbn(patchDto.getIsbn());
        if (patchDto.getPrice() != null) existingBook.setPrice(patchDto.getPrice());

        existingBook.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(bookMapper.toResponseDto(existingBook));
    }

    // 6. DELETE (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book book = mockDatabase.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không thể xóa! Không tìm thấy ID sách: " + id));
        mockDatabase.remove(book);
        return ResponseEntity.noContent().build();
    }
}
