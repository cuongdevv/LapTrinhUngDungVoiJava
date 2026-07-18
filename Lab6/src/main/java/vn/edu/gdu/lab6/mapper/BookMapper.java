package vn.edu.gdu.lab6.mapper;

import vn.edu.gdu.lab6.dto.BookCreateDto;
import vn.edu.gdu.lab6.dto.BookResponseDto;
import vn.edu.gdu.lab6.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper anh xa Entity <-> DTO (Bai 3 - Chuong 6).
 * componentModel = "spring" -> MapStruct sinh BookMapperImpl va dang ky la Spring Bean.
 * Code Impl duoc sinh luc compile tai:
 *   target/generated-sources/annotations/vn/edu/gdu/lab6/mapper/BookMapperImpl.java
 */
@Mapper(componentModel = "spring")
public interface BookMapper {

    // Entity -> Response DTO (tu dong bo qua internalNotes/audit vi DTO khong co truong nay)
    BookResponseDto toResponseDto(Book book);

    // Create DTO -> Entity: bo qua cac truong do server tu quan ly
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "internalNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toEntity(BookCreateDto createDto);

    // Cap nhat Entity co san tu DTO (dung cho PUT): giu nguyen id/audit/internalNotes
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "internalNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(BookCreateDto dto, @MappingTarget Book entity);
}
