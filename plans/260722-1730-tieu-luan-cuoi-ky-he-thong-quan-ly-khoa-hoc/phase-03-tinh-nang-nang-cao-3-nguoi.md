---
phase: 3
title: "Tinh nang nang cao 3 nguoi"
status: todo
priority: P1
effort: "3 ngay (26/07 - 29/07/2026), 3 nguoi lam song song"
dependencies: [2]
---

# Phase 3: Tinh nang nang cao 3 nguoi

## Overview

Bo sung 3 tinh nang nang cao - moi thanh vien so huu tron ven 1 tinh nang: tu entity, repository,
service, controller den DTO va test. Muc tieu kep:

1. **Tieu chi 1 (3d)**: "Co them cac tinh nang nang cao".
2. **Tieu chi 5 (1,5d)**: moi nguoi co mot mang rieng de tra loi van dap, khong trung nhau,
   va tra loi duoc tron ven luong du lieu cua tinh nang minh lam.

Ba tinh nang duoc chon vi **de giai thich khi van dap** va **khong dam chan nhau ve file**.

> Ky hieu nguoi phu trach: **A = Ho Minh Dao** (`hmddevv`) · **B = Le Dinh Thanh** (`thanhhdev`) · **C = Nguyen Chi Cuong** (`cuongdevv`)

## Requirements

**Functional:**
- [x] A: Hoc vien danh gia khoa hoc (1-5 sao + nhan xet); he thong tinh diem trung binh va xep hang khoa hoc
- [x] B: Hoc vien hoan thanh 100% duoc cap chung chi; job dinh ky nhac hoc vien lau khong hoat dong
- [x] C: Cache du lieu it doi de giam truy van DB; ghi nhat ky thao tac (audit log) tra cuu duoc

**Non-functional:**
- [x] Moi tinh nang co it nhat 2 unit test / integration test
- [x] Moi endpoint moi deu co Swagger annotation, tra `ApiResponse`, endpoint danh sach co paging + sorting
- [x] Khong pha vo layered architecture: Controller khong goi thang Repository
- [ ] Moi nguoi commit bang tai khoan GitHub cua chinh minh

## Architecture

### Thu tu bat buoc (tranh xung dot file)

`BaseEntity` la lop cha cua moi entity nen bat ky thay doi nao o day deu anh huong ca A va B.

```
Buoc 0 (C lam truoc, MOT MINH)  ->  C sua BaseEntity + bat @EnableJpaAuditing, commit, push
Buoc 1 (A va B pull ve roi lam song song)
```

Sau buoc 0, A va B khong con dung file chung ngoai `Course.java` (A sua) va
`EnrollmentServiceImpl.java` (B sua) - hai file khac nhau, khong xung dot.

### A - Danh gia & xep hang

```
POST /api/courses/{courseId}/reviews
  ReviewController  -> @Valid ReviewRequest
  ReviewService     -> kiem tra: hoc vien DA ghi danh khoa nay chua? da danh gia chua?
  ReviewRepository  -> save
  -> cap nhat diem trung binh khoa hoc (tinh bang aggregate query, KHONG luu trung lap)

GET /api/courses/top-rated?page=0&size=10&sort=avgRating,desc
  -> JPQL aggregate: SELECT c, AVG(r.rating) FROM Course c LEFT JOIN c.reviews r GROUP BY c
```

Rang buoc: `UNIQUE(student_id, course_id)` tren bang `reviews` - moi hoc vien danh gia 1 lan / khoa.
`rating` gioi han 1..5 bang Bean Validation `@Min(1) @Max(5)` + CHECK constraint o DB.

### B - Chung chi + job nhac hoc

```
PATCH /api/enrollments/{id}/progress  (progressPercent = 100)
  EnrollmentService -> status = COMPLETED, completedAt = now
                    -> CertificateService.issue(enrollment)   [trong cung @Transactional]
  CertificateFactory -> sinh ma chung chi: CERT-{courseId}-{studentId}-{yyyyMMdd}-{6 ky tu ngau nhien}
  CertificateRepository -> save

@Scheduled(cron = "0 0 8 * * *")   // 8h sang moi ngay
  EnrollmentReminderScheduler
    -> tim Enrollment ACTIVE co updatedAt < now - 7 ngay
    -> ghi log nhac nho (khong gui email that - tranh phu thuoc SMTP khi demo)
```

Rang buoc: `UNIQUE(enrollment_id)` tren `certificates` - 1 ghi danh chi cap 1 chung chi.
Chi cap chung chi khi `status = COMPLETED`.

### C - Cache + Audit log

```
Cache:
  @EnableCaching (CacheConfig)
  @Cacheable("categories")       tren CategoryServiceImpl.findAll
  @Cacheable("courseStatistics") tren CourseServiceImpl.getStatistics
  @CacheEvict(allEntries = true) tren cac ham create/update/delete tuong ung

Audit log:
  BaseEntity + @EntityListeners(AuditingEntityListener.class)
  AuditorAwareImpl -> tra ve nguoi thao tac (giai doan nay: "system" hoac header X-User)
  AuditAspect (@Aspect, @AfterReturning quanh cac ham @Transactional trong service)
    -> ghi AuditLog { entityName, entityId, action, actor, timestamp, detail }
  GET /api/audit-logs?page&size&sort  (chi doc, co paging + sorting)
```

## Related Code Files

### Nguoi C - lam TRUOC (buoc 0)
- Modify: `src/main/java/com/university/coursemanagement/entity/BaseEntity.java` (them `createdBy`, `updatedBy`, `@EntityListeners`)
- Create: `config/JpaAuditingConfig.java` (`@EnableJpaAuditing`), `config/AuditorAwareImpl.java`
- **Commit + push xong buoc 0 moi bao A va B pull ve.**

### Nguoi C - phan con lai
- Create: `config/CacheConfig.java` (`@EnableCaching`)
- Create: `entity/AuditLog.java`, `entity/enums/AuditAction.java`
- Create: `repository/AuditLogRepository.java`
- Create: `aspect/AuditAspect.java`
- Create: `service/AuditLogService.java`, `service/impl/AuditLogServiceImpl.java`
- Create: `controller/AuditLogController.java`
- Create: `dto/response/AuditLogResponse.java`, `dto/mapper/AuditLogMapper.java`
- Modify: `service/impl/CategoryServiceImpl.java`, `service/impl/CourseServiceImpl.java` (them `@Cacheable` / `@CacheEvict`)
- Modify: `pom.xml` (them `spring-boot-starter-aop`, `spring-boot-starter-cache`)
- Create: `src/test/java/.../AuditAspectTest.java`, `.../CacheConfigTest.java`

### Nguoi A
- Create: `entity/Review.java`
- Create: `repository/ReviewRepository.java`
- Create: `service/ReviewService.java`, `service/impl/ReviewServiceImpl.java`
- Create: `controller/ReviewController.java`
- Create: `dto/request/ReviewRequest.java`, `dto/response/ReviewResponse.java`, `dto/response/CourseRatingResponse.java`, `dto/mapper/ReviewMapper.java`
- Modify: `entity/Course.java` (them `@OneToMany reviews`)
- Modify: `dto/response/CourseResponse.java`, `dto/mapper/CourseMapper.java` (them `averageRating`, `reviewCount`)
- Modify: `repository/CourseRepository.java` (them query aggregate top-rated)
- Modify: `config/DataSeeder.java` (them review mau de demo co du lieu)
- Create: `src/test/java/.../ReviewServiceTest.java`

### Nguoi B
- Create: `entity/Certificate.java`
- Create: `repository/CertificateRepository.java`
- Create: `service/CertificateService.java`, `service/impl/CertificateServiceImpl.java`
- Create: `factory/CertificateFactory.java`
- Create: `controller/CertificateController.java`
- Create: `dto/response/CertificateResponse.java`, `dto/mapper/CertificateMapper.java`
- Create: `scheduler/EnrollmentReminderScheduler.java`
- Create: `config/SchedulingConfig.java` (`@EnableScheduling`)
- Modify: `service/impl/EnrollmentServiceImpl.java` (goi cap chung chi khi hoan thanh 100%)
- Modify: `repository/EnrollmentRepository.java` (query tim ghi danh lau khong hoat dong)
- Create: `src/test/java/.../CertificateServiceTest.java`, `.../EnrollmentReminderSchedulerTest.java`

### Chung (ai cham nhat thi sua sau, tranh conflict)
- Modify: `src/main/resources/static/index.html`, `js/app.js`, `css/style.css` - them UI cho 3 tinh nang.
  **Quy tac: moi nguoi lam 1 tab/section rieng trong frontend, merge tuan tu, khong lam song song.**
- Modify: `README.md` muc "API chinh" - moi nguoi bo sung dong cua minh.

## Implementation Steps

1. **C lam buoc 0 truoc** (nua ngay): sua `BaseEntity` + bat JPA Auditing, chay `./mvnw test`, commit, push, bao nhom.
2. **A va B pull ve**, tao branch rieng: `feature/review-rating` (A), `feature/certificate-scheduler` (B).
   C tiep tuc tren `feature/cache-audit`.
3. **A**: tao entity Review -> repository -> service (rule: phai da ghi danh moi duoc danh gia, moi hoc vien 1 lan)
   -> controller -> DTO/Mapper -> aggregate query top-rated co paging -> test -> seed du lieu mau.
4. **B**: tao entity Certificate -> factory sinh ma -> service cap chung chi -> hook vao
   `EnrollmentServiceImpl` khi progress = 100 -> controller tra cuu chung chi -> scheduler nhac hoc -> test.
5. **C**: bat `@EnableCaching`, gan `@Cacheable`/`@CacheEvict` -> entity AuditLog -> AuditAspect (AOP)
   -> service + controller tra cuu audit log co paging -> test.
6. **Merge tuan tu** ve `main` theo thu tu C -> A -> B, moi lan merge chay lai `./mvnw clean test`.
7. **Frontend**: sau khi backend merge xong, chia 3 luot sua `static/` de tranh conflict.
8. **Cap nhat README + Swagger**: moi nguoi bo sung endpoint cua minh vao bang API trong README.

## Tests / Validation

- `./mvnw clean test` PASS sau moi lan merge.
- Swagger UI hien du endpoint moi: `/api/courses/{id}/reviews`, `/api/courses/top-rated`,
  `/api/certificates/...`, `/api/audit-logs`.
- Test nghiep vu bat buoc phai co:
  - A: danh gia khi chua ghi danh -> tra 400/409, khong tao ban ghi; danh gia lan 2 -> bi chan boi unique constraint.
  - B: progress = 100 -> co ban ghi Certificate; goi lai lan 2 -> khong tao chung chi trung.
  - C: goi `findAll` categories 2 lan -> lan 2 khong sinh SQL (kiem qua log `org.hibernate.SQL`);
       tao 1 category -> co ban ghi trong `audit_logs`.
- Demo tren frontend: xem sao danh gia, xem chung chi, xem nhat ky thao tac.

## Success Criteria

- [x] 3 tinh nang chay duoc, demo duoc qua ca Swagger va frontend
- [x] Moi tinh nang co it nhat 2 test, tat ca PASS
- [x] Khong endpoint nao tra ve Entity truc tiep (deu qua DTO)
- [x] Endpoint danh sach moi deu co paging + sorting
- [ ] Git log cho thay ca 3 tai khoan deu co commit trong phase nay
- [ ] Moi nguoi tu giai thich duoc luong du lieu tinh nang cua minh ma khong can nhin code

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| C sua `BaseEntity` trong khi A/B dang code -> conflict toan repo | Cao | Ep thu tu: C lam buoc 0 va push TRUOC, A/B chi bat dau sau khi pull ve |
| Ba nguoi cung sua `static/js/app.js` -> conflict frontend | Trung binh | Merge tuan tu sau khi backend xong, moi nguoi 1 section |
| AOP audit lam cham moi request, hoac ghi log qua nhieu | Trung binh | Chi audit cac ham create/update/delete, khong audit ham doc |
| Cache tra du lieu cu khi demo -> thay tuong bug | Trung binh | `@CacheEvict(allEntries = true)` tren moi ham ghi; demo phai chuan bi kich ban ro |
| Scheduler chay luc demo gay nhieu log | Thap | Dat cron 8h sang; demo goi truc tiep method qua test hoac endpoint thu cong |
| Lam qua nhieu, khong kip 31/07 | Cao | Neu tre, cat theo thu tu: bo Scheduler (B) truoc, roi bo Audit AOP (C). Giu Review (A) va Certificate (B) |
