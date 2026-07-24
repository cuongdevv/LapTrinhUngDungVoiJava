---
phase: 2
title: "Va lo hong theo rubric"
status: todo
priority: P1
effort: "2 ngay (24/07 - 26/07/2026)"
dependencies: [1]
---

# Phase 2: Va lo hong theo rubric

## Overview

Va cac lo hong da phat hien khi audit toan bo 69 file Java, frontend, config, Docker/CI va git log.

**Diem uoc luong hien tai: ~6,65 / 8,5** (chua tinh tieu chi 5 van dap).
Neu va het loi P1 + P2 thi len khoang **7,8 - 8,2 / 8,5**.

Phase nay chi sua code da co, **khong them tinh nang moi** (tinh nang moi o Phase 3).

> Ky hieu nguoi phu trach: **A = Ho Minh Dao** (`hmddevv`) · **B = Le Dinh Thanh** (`thanhhdev`) · **C = Nguyen Chi Cuong** (`cuongdevv`)

## Diem hien tai theo rubric

| # | Tieu chi | Uoc luong | Toi da | Nguyen nhan mat diem |
|---|---|---|---|---|
| 1 | Chuc nang & Nghiep vu | 2,2 | 3,0 | Race condition ghi danh; thieu guard xoa Student; guard xoa Course khong day du; test qua mong |
| 2 | Kien truc & Chat luong code | 2,2 | 2,5 | N+1 query khong nhat quan voi chinh nguyen tac thiet ke da ghi trong `CourseMapper` |
| 3 | Co so du lieu & API | 1,7 | 2,0 | N+1 anh huong hieu nang; `getStatistics()` dung `findAll().size()`; khong co migration tool |
| 4 | Cong nghe & Cong cu | 0,55 | 1,0 | Chi 1 commit duy nhat trong git |
| 5 | Van dap & Hieu biet | - | 1,5 | Phu thuoc nguoi trinh bay (xu ly o Phase 6) |

## Requirements

**Functional:**
- [x] Sua het loi P1 (bug nghiep vu that su)
- [x] Sua het loi P2 (hieu nang + chat luong)
- [x] Moi loi da sua deu co test chung minh

**Non-functional:**
- [x] `./mvnw clean test` PASS sau moi lan sua
- [x] Khong pha vo API contract hien co (frontend van chay duoc)

## Lo hong P1 - phai sua

### P1-1. Race condition khi ghi danh gan day chi tieu

- **File:** `src/main/java/com/university/coursemanagement/service/impl/EnrollmentServiceImpl.java:73-83`
- **Van de:** dem so ghi danh ACTIVE roi so sanh `active >= capacity`, sau do moi `save()`.
  Giua hai buoc khong co khoa. Hai request dong thoi vao **cho cuoi cung** deu doc thay `active < capacity`
  va deu insert thanh cong -> vuot chi tieu.
- **Luu y quan trong:** `@Version` o `BaseEntity.java:39-41` **KHONG** chan duoc loi nay.
  `@Version` chi chong ghi de dong thoi tren chinh ban ghi do, khong bao ve dieu kien dem tren bang khac.
  Day la cho rat de bi hoi van dap - phai hieu dung.
- **Cach sua:** them method co khoa vao `CourseRepository`:
  ```java
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Course c WHERE c.id = :id")
  Optional<Course> findByIdForUpdate(@Param("id") Long id);
  ```
  Trong `enroll()`, lay Course bang `findByIdForUpdate` **truoc** khi dem va save.
- **Nguoi phu trach:** B
- **Test bat buoc:** test da luong - N thread cung ghi danh vao khoa hoc con 1 cho ->
  dung 1 thread thanh cong, N-1 thread bi tu choi.

### P1-2. Thieu guard nghiep vu khi xoa Student

- **File:** `src/main/java/com/university/coursemanagement/service/impl/StudentServiceImpl.java:75-78`
- **Van de:** `delete()` khong kiem tra hoc vien con ghi danh hay khong. Khac han
  `CategoryServiceImpl.delete():89-91` va `InstructorServiceImpl.delete():83-85` deu co check.
  Hau qua: DB nem `DataIntegrityViolationException` (FK `enrollments.student_id NOT NULL`,
  `Enrollment.java:44`), roi vao handler chung `GlobalExceptionHandler.java:65-70` tra message mo ho
  "Vi pham rang buoc du lieu" thay vi thong bao nghiep vu ro rang.
- **Cach sua:** them `boolean existsByStudentId(Long studentId)` vao `EnrollmentRepository`,
  check trong `StudentServiceImpl.delete()`, nem `BusinessException` co thong diep ro rang.
- **Nguoi phu trach:** B
- **Rui ro van dap:** giang vien doc song song 4 file `delete()` la thay ngay su bat nhat.

### P1-3. Guard xoa Course khong day du

- **File:** `src/main/java/com/university/coursemanagement/service/impl/CourseServiceImpl.java:149-156`
- **Van de:** chi chan khi con Enrollment `ACTIVE`. Neu khoa hoc chi con Enrollment
  `CANCELLED`/`COMPLETED` thi code cho qua check, nhung `courseRepository.delete(course)` van vo FK
  vi `Course.enrollments` (`Course.java:84-86`) khong co cascade -> tra loi 409 chung chung.
- **Cach sua:** doi dieu kien thanh `enrollmentRepository.existsByCourseId(courseId)` (them method nay),
  chan xoa khi con **bat ky** lich su ghi danh nao. Thong diep: "Khoa hoc da co lich su ghi danh,
  khong the xoa - can nhac dung archive thay vi delete".
- **Nguoi phu trach:** B

## Lo hong P2 - nen sua

### P2-4. N+1 query khi liet ke Instructor / Student

- **File:** `InstructorServiceImpl.java:67-70,74-77` va `StudentServiceImpl.java:61-64,68-71`
- **Van de:** goi `i.getCourses().size()` / `s.getEnrollments().size()` tren collection LAZY
  (`Instructor.java:41`, `Student.java:38`) ben trong `.map()` cua tung trang -> moi entity kich hoat
  1 SELECT rieng. **Trai nguoc voi chinh comment thiet ke** trong `CourseMapper.java:8-9`
  ("truyen tu Service bang count query de tranh N+1").
- **Cach sua:** them `long countByInstructorId(Long id)` vao `CourseRepository` (da co
  `existsByInstructorId` nhung chua co count) va `long countByStudentId(Long id)` vao
  `EnrollmentRepository`; dung count query giong cach `CourseServiceImpl.toResponse()` dang lam dung.
- **Nguoi phu trach:** A

### P2-5. N+1 query khi tim kiem khoa hoc

- **File:** `CourseServiceImpl.java:187-191` (qua `CourseMapper.java:25-28`), quan he LAZY tai `Course.java:71-77`
- **Van de:** moi trang 10 khoa hoc co the sinh toi 1 + 10x4 = **41 query**
  (category + instructor + lessonCount + activeEnrollments cho tung dong).
- **Cach sua (chon 1):**
  - Them `@EntityGraph(attributePaths = {"category", "instructor"})` cho
    `CourseRepository.findAll(Specification, Pageable)` (can override), **hoac**
  - Them `spring.jpa.properties.hibernate.default_batch_fetch_size: 20` vao `application.yml`.
  - Khuyen nghi: lam **ca hai** - `@EntityGraph` cho endpoint search, `batch_fetch_size` lam luoi an toan chung.
- **Nguoi phu trach:** A
- **Cach chung minh:** bat `org.hibernate.SQL: DEBUG`, dem so dong SQL truoc va sau khi sua.
  **Chup lai lam bang chung cho bao cao va van dap** - day la diem cong rat manh o tieu chi 5.

### P2-6. `getStatistics()` khong dung count query

- **File:** `CourseServiceImpl.java:160-162`
- **Van de:** `findAll(spec).size()` tai toan bo entity vao memory chi de dem.
- **Cach sua:** `courseRepository.count(CourseSpecifications.hasStatus(CourseStatus.PUBLISHED))`
  (`JpaSpecificationExecutor` da co san `count`).
- **Nguoi phu trach:** A

### P2-7. `ddl-auto: update` o profile prod

- **File:** `application-prod.yml:19`
- **Van de:** khong co Flyway/Liquibase, schema tu dong cap nhat theo entity o production.
- **Cach sua (tuy chon, uu tien thap nhat trong P2):** them Flyway, doi `ddl-auto: validate`.
- **Quyet dinh:** neu khong con thoi gian thi **giu nguyen nhung phai hieu de tra loi van dap**
  (cau hoi 29 trong Phase 6). Day la de xuat toi uu tot de trinh bay o chuong 9 bao cao.
- **Nguoi phu trach:** C

## Lo hong P3 - sua neu con thoi gian

| # | File | Van de | Nguoi |
|---|---|---|---|
| P3-8 | `EnrollmentRepository.java:17` | `existsByStudentIdAndCourseIdAndStatus` khai bao nhung khong noi nao goi (dead code) | B |
| P3-9 | `static/js/app.js:43,146,197` | Ham `esc()` khong escape dau nhay don `'`; chuoi nhung vao `onclick="...('${esc(...)}')"` -> tieu de chua `'` lam vo thuoc tinh. Sua bang `data-*` + `addEventListener` thay vi noi chuoi vao `onclick` | C |
| P3-10 | `docker-compose.yml:14-16,36-38` | Mat khau plaintext trong file commit. Chuyen sang `.env` (da co `.gitignore`) | C |
| P3-11 | `src/test/` | Chi co 1 file test nghiep vu + 1 smoke test. Thieu test cho Course/Category/Instructor/Student/Lesson service, thieu `@WebMvcTest`, `@DataJpaTest` | Ca 3 |

## Related Code Files

- Modify: `service/impl/EnrollmentServiceImpl.java`, `service/impl/StudentServiceImpl.java`, `service/impl/CourseServiceImpl.java`, `service/impl/InstructorServiceImpl.java`
- Modify: `repository/CourseRepository.java`, `repository/EnrollmentRepository.java`
- Modify: `dto/mapper/InstructorMapper.java`, `dto/mapper/StudentMapper.java` (neu doi chu ky)
- Modify: `src/main/resources/application.yml` (`default_batch_fetch_size`)
- Modify: `src/main/resources/static/js/app.js`
- Modify: `docker-compose.yml`, tao `.env.example`
- Create: `src/test/java/.../EnrollmentConcurrencyTest.java`, `.../StudentServiceTest.java`, `.../CourseServiceTest.java`

## Implementation Steps

1. **B sua P1-1** (race condition): them `findByIdForUpdate` vao `CourseRepository`, dung trong `enroll()`.
   Viet test da luong truoc, xac nhan test **do** voi code cu, roi sua cho test **pass**.
2. **B sua P1-2 va P1-3**: them `existsByStudentId`, `existsByCourseId`, cap nhat 2 ham `delete()`,
   viet test cho ca hai truong hop.
3. **A sua P2-4, P2-5, P2-6**: them count query, `@EntityGraph`, `batch_fetch_size`, doi `findAll().size()` -> `count()`.
   Bat `org.hibernate.SQL: DEBUG`, dem so query truoc/sau, chup man hinh luu vao `docs/bao-cao/anh/`.
4. **C xu ly P2-7** (quyet dinh Flyway hay giu nguyen) va **P3-9, P3-10**.
5. **Ca 3 bo sung test** (P3-11), moi nguoi viet test cho service thuoc mang minh.
6. Chay `./mvnw clean test`, sua het test do.
7. Moi nguoi commit rieng phan cua minh, message ro rang theo quy tac o Phase 4.

## Tests / Validation

- Test da luong ghi danh: N thread vao 1 cho -> dung 1 thanh cong.
- Xoa Student dang co ghi danh -> tra loi nghiep vu ro rang (khong phai "Vi pham rang buoc du lieu").
- Xoa Course co Enrollment COMPLETED -> bi chan voi thong diep dung.
- So query cua `GET /api/courses?page=0&size=10` giam ro ret (do bang log `org.hibernate.SQL`).
- `./mvnw clean test` PASS.
- Frontend van chay binh thuong sau khi sua `esc()`.

## Success Criteria

- [x] 3 loi P1 da sua, moi loi co test chung minh
- [x] 4 loi P2 da xu ly (hoac P2-7 co quyet dinh ro rang kem ly do)
- [x] So query endpoint search giam, co so lieu truoc/sau lam bang chung
- [x] Test coverage mo rong sang it nhat 4 service
- [x] `./mvnw clean test` PASS
- [x] API contract khong doi, frontend van chay

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| Pessimistic lock gay deadlock hoac cham | Trung binh | Chi lock dung hang Course trong ham `enroll()`, giu transaction ngan. Test da luong de xac nhan |
| Test da luong khong on dinh (flaky) tren CI | Trung binh | Dung `CountDownLatch` + `ExecutorService` co dinh, khong dua vao `Thread.sleep` |
| `@EntityGraph` lam vo query Specification hien co | Trung binh | Chay lai toan bo test sau khi doi; kiem tra ky endpoint search voi moi to hop filter |
| Sua nhieu file cung luc -> conflict giua A va B | Trung binh | A va B sua cac file khac nhau (A: Course/Instructor/Student mapper + repository; B: Enrollment + delete guards). Chi `CourseServiceImpl.java` la ca hai deu dung -> A sua truoc, B pull ve roi sua sau |
| Sua xong quen chay lai frontend | Thap | Buoc validate bat buoc mo `http://localhost:8080` kiem tra |
