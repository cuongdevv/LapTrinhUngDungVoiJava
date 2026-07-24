---
phase: 6
title: "Chuan bi van dap va demo"
status: todo
priority: P1
effort: "1 ngay (31/07 - 01/08/2026)"
dependencies: [5]
---

# Phase 6: Chuan bi van dap va demo

## Overview

**Tieu chi 5 - Van dap & Hieu biet: 1,5 diem** - la tieu chi duy nhat khong the "lam bu" bang code.
Day cung la cho de mat diem nhat khi du an duoc xay dung nhanh: code chay tot nhung khong giai
thich duoc thi mat trang 1,5 diem, va con anh huong den danh gia cac tieu chi khac.

Nhom 1 bao ve ngay **01/08/2026** (dot nhom 1-10).

> Ky hieu: **A = Ho Minh Dao** (24150199) · **B = Le Dinh Thanh** (24150127) · **C = Nguyen Chi Cuong** (24150545)

Rubric hoi 4 nhom cau:
1. Hieu ve chuc nang cua he thong
2. Tra loi duoc cau hoi ve luong di cua du lieu (data flow)
3. Giai thich ro rang ve co che **DI, IoC, JPA**
4. De xuat duoc phuong an toi uu hon cho he thong

## Requirements

- [ ] Kich ban demo 8-10 phut, tap it nhat 2 lan, co phuong an du phong khi mang/Docker loi
- [ ] Moi thanh vien tra loi duoc 10 cau hoi thuoc mang minh so huu
- [ ] Ca 3 nguoi deu tra loi duoc 5 cau chung ve DI/IoC/JPA va data flow
- [ ] Moi nguoi chuan bi 2 de xuat toi uu he thong, giai thich duoc danh doi

## Kich ban demo (8-10 phut)

| Phut | Noi dung | Nguoi trinh bay |
|---|---|---|
| 0-1 | Gioi thieu de tai, kien truc tong quan (mo `docs/architecture.png`) | C |
| 1-2 | `docker compose up` da chay san; mo `actuator/health` UP; mo Swagger UI | C |
| 2-4 | Tao danh muc + giang vien + khoa hoc; them bai hoc; publish khoa hoc; tim kiem co loc + paging + sorting | A |
| 4-5 | Demo **loi validate** (gui du lieu sai -> Global Exception Handler tra JSON loi co `fieldErrors`) | C |
| 5-7 | Ghi danh hoc vien; demo chan ghi danh trung + chan khi khoa da day; cap nhat tien do 100% -> sinh chung chi | B |
| 7-8 | Danh gia khoa hoc, xem xep hang top-rated | A |
| 8-9 | Mo audit log xem nhat ky thao tac vua roi; noi ve cache | C |
| 9-10 | Ket luan, huong phat trien | Ca 3 |

**Chuan bi truoc:**
- Chay `docker compose up --build` **truoc gio bao ve**, khong build tai lop.
- Chuan bi san du lieu mau (DataSeeder) de khong phai go nhieu.
- Luu san cac request mau trong Swagger hoac file `.http` / Postman collection.
- Phuong an du phong: neu Docker loi -> chay `./mvnw spring-boot:run` profile `dev` (H2, khong can DB ngoai).
- Phuong an du phong 2: video quay san man hinh demo.

## Bo cau hoi van dap

### Cau hoi chung - ca 3 nguoi deu phai tra loi duoc

1. **IoC la gi? DI la gi? Khac nhau cho nao?**
   IoC (Inversion of Control) la nguyen ly: doi tuong khong tu tao phu thuoc cua minh ma de
   container tao va dua vao. DI la mot cach hien thuc IoC. Trong du an: Spring container quet
   `@Service`, `@Repository`, `@RestController`, tao bean va tiem qua **constructor injection**.

2. **Vi sao dung constructor injection thay vi `@Autowired` tren field?**
   Bat buoc phai co phu thuoc khi tao doi tuong (khong bao gio null), cho phep khai bao `final`,
   de viet unit test (truyen mock truc tiep), phat hien duoc phu thuoc vong tron ngay khi khoi dong.

3. **Ke lai luong du lieu day du cua mot request** (vi du `POST /api/enrollments`):
   HTTP request -> DispatcherServlet -> `EnrollmentController` -> Bean Validation `@Valid` tren DTO
   -> `EnrollmentService` (mo transaction, kiem tra rule: khoa PUBLISHED, con cho, chua ghi danh trung)
   -> `EnrollmentFactory` tao entity -> `EnrollmentRepository.save()` -> Hibernate sinh SQL INSERT
   -> commit transaction -> Mapper chuyen Entity sang `EnrollmentResponse` -> boc trong `ApiResponse`
   -> Jackson serialize thanh JSON -> tra ve client.

4. **Vi sao phai dung DTO ma khong tra ve Entity truc tiep?**
   Tranh lo cau truc DB ra ngoai; tranh loi lazy loading khi serialize (`LazyInitializationException`);
   kiem soat duoc truong nao duoc lo ra; API doi duoc ma khong phai doi schema DB va nguoc lai.

5. **JPA va Hibernate khac nhau the nao?**
   JPA la dac ta (interface, annotation) cua Jakarta EE. Hibernate la ban hien thuc cu the.
   Spring Data JPA la lop tien ich phia tren, tu sinh cai dat repository tu ten phuong thuc.

### Nguoi A - Catalog & quan he JPA

6. Quan he giua Course va Category la gi? Khoa ngoai nam o bang nao? Vi sao?
7. `FetchType.LAZY` va `EAGER` khac nhau the nao? Trong du an dung cai nao, vi sao?
8. **Van de N+1 query la gi?** Xay ra o dau trong du an? Cach khac phuc? (`JOIN FETCH`, `@EntityGraph`, batch size)
9. Vi sao Course - Lesson dat `cascade = ALL` va `orphanRemoval = true`, con Course - Enrollment thi khong?
10. `Specification` API dung de lam gi? Vi sao khong viet san nhieu phuong thuc `findByXxxAndYyy`?
11. Paging hoat dong the nao? `Pageable` sinh ra SQL gi? `PageResponse` chua nhung gi?
12. Neu bang courses co 10 trieu ban ghi thi phan trang offset co van de gi? Giai phap? (keyset pagination, index)

### Nguoi B - Nghiep vu ghi danh, transaction, pattern

13. `@Transactional` lam gi? Dat o tang nao? Vi sao khong dat o Controller?
14. Neu trong mot phuong thuc `@Transactional` xay ra exception thi dieu gi xay ra? Loai exception nao
    mac dinh gay rollback? (`RuntimeException` va `Error`; checked exception mac dinh **khong** rollback)
15. `@Version` (optimistic locking) giai quyet van de gi? Ke mot tinh huong that trong du an.
    (Hai nguoi cung ghi danh vao cho cuoi cung -> mot nguoi bi `OptimisticLockException`)
16. Optimistic locking va pessimistic locking khac nhau the nao? Khi nao dung cai nao?
17. Builder Pattern giai quyet van de gi? Vi sao khong dung constructor nhieu tham so?
18. Factory Pattern trong `EnrollmentFactory` lam gi? Loi ich so voi `new Enrollment(...)` truc tiep?
19. Rule nghiep vu nao dang duoc kiem tra khi ghi danh? Kiem tra o tang nao? Vi sao khong kiem o Controller?
20. `@Scheduled` chay tren thread nao? Neu co nhieu instance cua ung dung thi job co chay trung khong? Xu ly the nao?

### Nguoi C - Kien truc, cross-cutting, ha tang

21. Layered Architecture gom nhung tang nao? Tang nao duoc phep goi tang nao?
22. Global Exception Handler hoat dong the nao? `@RestControllerAdvice` + `@ExceptionHandler` chay o dau trong luong xu ly?
23. Su khac nhau giua profile `dev` va `prod` trong du an? Cau hinh nao khac nhau va vi sao?
24. `spring.jpa.open-in-view: false` nghia la gi? Vi sao nen tat?
25. `@Cacheable` hoat dong the nao? Khi nao cache bi xoa? Rui ro cua cache la gi?
26. AOP la gi? `AuditAspect` chay vao thoi diem nao? Spring hien thuc AOP bang co che gi? (proxy)
27. Dockerfile multi-stage co loi gi? Vi sao chay bang user khong phai root?
28. `docker compose` dam bao app khong khoi dong truoc khi MySQL san sang bang cach nao?
29. `ddl-auto: update` co van de gi khi len production that? Nen dung gi thay the? (Flyway / Liquibase)
30. CI cua nhom chay nhung buoc nao? Neu test do thi dieu gi xay ra?

### Cau "de xuat toi uu" - moi nguoi chuan bi 2 y

Goi y (chon 2, phai giai thich duoc danh doi, khong doc thuoc long):
- Thay `ddl-auto: update` bang **Flyway migration** de kiem soat phien ban schema.
- Them **index** cho cac cot loc thuong xuyen (`courses.category_id`, `courses.status`, `enrollments.student_id`).
- Chuyen offset pagination sang **keyset pagination** khi du lieu lon.
- Thay cache in-memory bang **Redis** khi trien khai nhieu instance.
- Them **Spring Security + JWT** de phan quyen ADMIN / INSTRUCTOR / STUDENT.
- Dung `@EntityGraph` / `JOIN FETCH` de dep N+1 query o cac endpoint danh sach.
- Tach **read model** cho thong ke (`/api/courses/statistics`) neu truy van nang.
- Them **rate limiting** va **idempotency key** cho endpoint ghi danh.
- Chuyen job `@Scheduled` sang hang doi (message queue) khi can gui email that.

## Implementation Steps

1. Moi nguoi tu tra loi 10 cau hoi cua minh **bang loi cua minh**, viet ra giay, khong copy tai lieu.
2. Nhom hoi cheo nhau: A hoi B, B hoi C, C hoi A - moi lan 10 cau, khong nhin ghi chu.
3. Bat ky cau nao khong tra loi duoc -> mo code thuc te ra doc lai dung cho do, khong hoc thuoc dinh nghia suong.
4. Chay thu kich ban demo tu dau den cuoi, bam gio, it nhat 2 lan.
5. Chuan bi phuong an du phong: profile `dev` chay bang H2, va video quay san.
6. Kiem tra lai thiet bi truoc buoi bao ve: may, cap HDMI, Docker da khoi dong, image da build san.

## Success Criteria

- [ ] Moi nguoi tra loi duoc 10 cau hoi mang minh, khong nhin ghi chu
- [ ] Ca 3 nguoi tra loi duoc 5 cau chung ve IoC/DI/JPA/data flow/DTO
- [ ] Kich ban demo chay tron trong 10 phut, da tap 2 lan
- [ ] Co phuong an du phong khi Docker/mang loi
- [ ] Moi nguoi co 2 de xuat toi uu va giai thich duoc danh doi

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| Code chay tot nhung khong ai giai thich duoc -> mat 1,5 diem | Cao | Buoc 2: hoi cheo nhau, khong tra loi duoc thi doc lai code that |
| Bi hoi doan code minh khong viet | Cao | Moi nguoi phai doc va hieu it nhat toan bo slice cua minh; slice chia doc nen kha thi |
| Demo loi tai lop (Docker/mang) | Trung binh | Build truoc, co phuong an H2, co video du phong |
| Tra loi kieu hoc thuoc, bi hoi nguoc lai thi tac | Trung binh | Tra loi gan voi code cu the trong du an, dan duoc ten file/class |
| Het gio demo truoc khi kip trinh bay phan cua minh | Trung binh | Bam gio khi tap; uu tien demo nghiep vu ghi danh + validate (an diem tieu chi 1) |
