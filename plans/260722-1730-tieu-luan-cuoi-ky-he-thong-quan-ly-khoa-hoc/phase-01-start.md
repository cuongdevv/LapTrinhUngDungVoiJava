---
phase: 1
title: "Chuan hoa nen & deliverable Phase 1"
status: todo
priority: P1
effort: "2 ngay (22/07 - 24/07/2026)"
dependencies: []
---

# Phase 1: Chuan hoa nen & deliverable Phase 1

## Overview

Chuan bi moi truong lam viec cho ca 3 nguoi va nop dung han **bao cao tien do Phase 1 ngay
24/07/2026**: thiet ke CSDL, so do kien truc du an, va source code khung tren GitHub chay duoc.

Day la phase gap nhat - chi con **2 ngay**. Uu tien tuyet doi: co du 3 deliverable nop dung han,
khong sa da vao viet them tinh nang.

## Requirements

**Functional (theo yeu cau giang vien muc IV.1):**
- [x] Ban thiet ke CSDL (Database schema) - dang so do quan he + DDL doc duoc
- [x] So do kien truc du an - the hien layered architecture + luong du lieu
- [x] Source code khung tren GitHub - repo public, build duoc, chay duoc

**Non-functional:**
- [ ] Ca 3 thanh vien clone va chay duoc du an tren may minh (H2 profile dev, khong can cai MySQL)
- [ ] Ca 3 thanh vien co quyen push vao repo
- [x] Tai lieu viet bang tieng Viet, dat trong `docs/` cua repo de nop kem link GitHub

## Architecture

So do kien truc can the hien dung 4 tang hien co trong code:

```
Client (Browser / Swagger UI)
        |  HTTP + JSON
        v
  Controller  (@RestController)   - nhan request, @Valid, tra ApiResponse
        v
  Service     (@Service, @Transactional) - nghiep vu, rule, transaction
        v
  Repository  (Spring Data JPA)   - truy van DB, Specification
        v
  Entity / Database (JPA + H2/MySQL)

  DTO <-> Mapper <-> Entity  (tach lop API khoi lop CSDL)
```

Mo hinh du lieu (lay tu code hien co, khong bia them):

- `Category` 1 --- N `Course` (FK `category_id`, NOT NULL)
- `Instructor` 1 --- N `Course` (FK `instructor_id`, NOT NULL)
- `Course` 1 --- N `Lesson` (cascade ALL + orphanRemoval)
- `Student` N --- N `Course` qua bang trung gian `Enrollment` (mang them `status`, `progress_percent`, `enrolled_at`, `completed_at`)
- `UNIQUE(student_id, course_id)` tren `enrollments` (`uk_enrollment_student_course`)
- Moi bang ke thua `BaseEntity`: `id`, `created_at`, `updated_at`, `version` (optimistic locking)

Enum: `CourseLevel`, `CourseStatus` (DRAFT / PUBLISHED / ARCHIVED), `EnrollmentStatus` (ACTIVE / COMPLETED / CANCELLED).

## Related Code Files

- Create: `docs/database-schema.md` (so do ERD Mermaid + giai thich tung bang, khoa, rang buoc)
- Create: `docs/database-schema.png` (anh xuat tu Mermaid, de dan vao bao cao Word)
- Create: `docs/architecture.md` (so do kien truc + so do luong du lieu 1 request)
- Create: `docs/architecture.png`
- Create: `docs/schema.sql` (DDL sinh tu Hibernate, dung lam bang chung thiet ke DB)
- Modify: `README.md` (them muc "Tai lieu thiet ke" tro toi `docs/`)
- Modify: `D:\GDU\Nam2-HK3\LapTrinhUngDungVoiJava\.gitignore` (them `TieuLuan/` de repo mon hoc khong nested-commit repo do an)
- Read-only tham chieu: `src/main/java/com/university/coursemanagement/entity/*.java`

## Implementation Steps

1. **Chot workspace** (da lam mot phan)
   - Repo da clone ve `D:\GDU\Nam2-HK3\LapTrinhUngDungVoiJava\TieuLuan`.
   - Them dong `TieuLuan/` vao `.gitignore` cua repo mon hoc cha de tranh commit long nhau.

2. **Chot quyen truy cap GitHub** (Dao lam - chu tai khoan `hmddevv`)
   - Moi `cuongdevv` va `thanhhdev` vao repo voi quyen **Write**
     (Settings > Collaborators and teams > Add people).
   - **Doi ten repo** `ScientificResearchManagementSystem` -> `CourseManagementSystem` (da chot):
     Settings > General > Repository name > Rename.
     GitHub tu redirect URL cu nen khong lam vo link nao da chia se.
   - Sau khi doi ten, ca 3 nguoi cap nhat remote local:
     ```bash
     git remote set-url origin https://github.com/hmddevv/CourseManagementSystem.git
     git remote -v   # kiem tra lai
     ```
   - Kiem tra repo dang **Public** (giang vien phai xem duoc).

3. **Xac minh moi truong cua ca 3 nguoi**
   - Moi nguoi chay `./mvnw spring-boot:run` (profile `dev`, H2 in-memory, khong can cai MySQL).
   - Mo `http://localhost:8080/swagger-ui.html` va `http://localhost:8080` (frontend) de xac nhan chay duoc.
   - Nguoi C chay them `docker compose up --build` de xac nhan duong prod (MySQL) hoat dong.

4. **Sinh DDL that tu Hibernate** (khong viet tay - de dam bao so do khop code)
   - Chay app o profile `dev` voi thuoc tinh sinh script:
     `jakarta.persistence.schema-generation.scripts.action=create` va
     `...create-target=docs/schema.sql`.
   - Doc file sinh ra, don dep dinh dang, luu thanh `docs/schema.sql`.
   - Doi chieu lai voi entity: moi FK phai `NOT NULL`, `enrollments` phai co unique constraint.

5. **Viet `docs/database-schema.md`**
   - So do ERD bang Mermaid (`erDiagram`) ve du 6 bang + quan he + cardinality.
   - Bang mo ta tung cot: ten cot, kieu, rang buoc, y nghia nghiep vu.
   - Muc "Ly do thiet ke": vi sao dung bang trung gian `Enrollment` thay vi `@ManyToMany` thuan
     (vi can mang them trang thai + tien do), vi sao co `@Version`, vi sao `Lesson` cascade con
     `Enrollment` thi khong.

6. **Viet `docs/architecture.md`**
   - So do tang (Controller / Service / Repository / Entity + DTO-Mapper).
   - So do package (`controller`, `service`, `repository`, `entity`, `dto`, `exception`, `factory`, `common`, `config`).
   - So do tuan tu (Mermaid `sequenceDiagram`) cho **1 luong du lieu day du**: `POST /api/enrollments`
     - Controller nhan + `@Valid` DTO
     - Service kiem tra khoa hoc PUBLISHED, con cho, chua ghi danh trung
     - `EnrollmentFactory` tao ban ghi
     - Repository save, `@Transactional` commit
     - Mapper -> `EnrollmentResponse` -> JSON boc trong `ApiResponse`
   - Muc "Cong nghe su dung" + "Profile dev/prod".

7. **Xuat anh PNG**
   - Render Mermaid ra PNG (`docs/database-schema.png`, `docs/architecture.png`) de dan vao file Word bao cao o Phase 5.

8. **Cap nhat README + push**
   - Them muc "Tai lieu thiet ke" link toi `docs/database-schema.md` va `docs/architecture.md`.
   - Commit theo tung viec nho (khong gop 1 commit): xem quy tac commit o Phase 4.
   - Push len GitHub, kiem tra CI GitHub Actions chay xanh.

9. **Nop Phase 1**
   - Nop link GitHub + 2 file tai lieu theo huong dan giang vien truoc **24/07/2026**.

## Tests / Validation

- `./mvnw clean test` PASS tren may ca 3 nguoi.
- `docker compose up --build` khoi dong duoc, `http://localhost:8080/actuator/health` tra `"status":"UP"`.
- `docs/schema.sql` chua du 6 bang, moi FK deu `NOT NULL`, co `uk_enrollment_student_course`.
- Mo `docs/database-schema.md` va `docs/architecture.md` tren GitHub - so do Mermaid render duoc.
- GitHub Actions badge xanh.

## Success Criteria

- [x] `TieuLuan/` da vao `.gitignore` cua repo mon hoc cha
- [ ] Ca 3 thanh vien co quyen push va da chay duoc du an tren may minh
- [x] Repo doi ten khop de tai va dang Public
- [x] `docs/database-schema.md` + `.png` hoan chinh, so do khop 100% voi entity trong code
- [x] `docs/architecture.md` + `.png` co du so do tang + so do package + sequence diagram luong ghi danh
- [x] `docs/schema.sql` sinh tu Hibernate, khong viet tay
- [x] README tro toi tai lieu thiet ke
- [x] CI GitHub Actions xanh
- [ ] Da nop Phase 1 truoc 24/07/2026

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| Chi con 2 ngay, khong kip lam ca tai lieu | Cao | Uu tien tuyet doi 3 deliverable. KHONG viet them tinh nang trong phase nay. Tinh nang de Phase 3 |
| Thanh vien khac chua cai JDK/Maven, mat thoi gian setup | Trung binh | Da co Maven Wrapper (`mvnw`) - chi can JDK 17+. Ai khong cai duoc thi dung Docker |
| Doi ten repo lam vo link da chia se | Thap | GitHub tu redirect URL cu. Van nen doi truoc khi nop link cho giang vien |
| So do ve tay lech so voi code | Trung binh | Sinh DDL truc tiep tu Hibernate (buoc 4), doi chieu lai voi entity truoc khi ve |
| Docker chua cai tren may cua nguoi C | Thap | Docker da co san tren may (da xac minh: Docker 29.6.1) |
