package vn.edu.gdu.lab6.mapper;

import vn.edu.gdu.lab6.dto.StudentCreateDto;
import vn.edu.gdu.lab6.dto.StudentResponseDto;
import vn.edu.gdu.lab6.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper anh xa Student Entity <-> DTO (Bai tap ve nha - Chuong 6).
 * componentModel = "spring" -> MapStruct sinh StudentMapperImpl la Spring Bean.
 */
@Mapper(componentModel = "spring")
public interface StudentMapper {

    // Entity -> Response DTO (tu bo internalNotes/createdAt vi DTO khong co truong nay)
    StudentResponseDto toResponseDto(Student student);

    // Create DTO -> Entity: bo qua cac truong do server tu quan ly
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "internalNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Student toEntity(StudentCreateDto createDto);

    // Cap nhat Entity co san tu DTO (dung cho PUT): giu nguyen id/createdAt/internalNotes
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "internalNotes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(StudentCreateDto dto, @MappingTarget Student entity);
}
