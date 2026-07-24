---
title: "Tieu luan cuoi ky - He thong quan ly khoa hoc"
description: "Nang cap repo Course Management System dat toi da rubric 10 diem, chia viec 3 nguoi, kip moc 24/07 - 31/07 - 01/08/2026"
status: pending
priority: P1
effort: "10 ngay (22/07 - 01/08/2026)"
tags: [spring-boot, java, do-an-cuoi-ky, nhom-1]
created: 2026-07-22
---

# Tieu luan cuoi ky - He thong quan ly khoa hoc

## Overview

Do an cuoi ky hoc phan **Lap trinh ung dung voi Java (14113014) - HK3 2025-2026**.
De tai STT 4: **He thong quan ly khoa hoc**. Nhom **1** (3 sinh vien).

Repo `hmddevv/ScientificResearchManagementSystem` da co san mot ung dung Spring Boot
kha hoan chinh (69 file Java, layered architecture, DTO/Mapper, Swagger, Docker Compose,
GitHub Actions CI). Da xac minh `./mvnw clean test` **PASS**.

Ke hoach nay **giu code hien co lam nen**, va:
1. Va cac lo hong so voi rubric cham diem 10 diem cua giang vien.
2. Bo sung 3 tinh nang nang cao, moi nguoi so huu 1 tinh nang de demo va van dap.
3. Tao lich su commit that su cua 3 nguoi (hien tai repo chi co **1 commit duy nhat** - dang mat diem tieu chi 4).
4. Viet bao cao theo mau (3 file, moi sinh vien 1 file).
5. Chuan bi kich ban demo + bo cau hoi van dap cho tung nguoi.

## Moc thoi gian (rang buoc cung)

| Moc | Han | Noi dung |
|---|---|---|
| **Phase 1 - Bao cao tien do** | **24/07/2026** (con 2 ngay) | Nop thiet ke CSDL (schema), so do kien truc du an, source code khung tren GitHub |
| **Nop san pham** | **31/07/2026** | 3 file bao cao (`STT_Ho va ten_MSSV`) + link GitHub. Nop qua Google/Microsoft Forms |
| **Bao ve** | **01/08/2026** | Nhom 1 thuoc dot nhom 1-10. Demo ung dung tai lop |

## Rubric cham diem (10 diem)

| # | Tieu chi | Diem | Phase xu ly |
|---|---|---|---|
| 1 | Chuc nang & Nghiep vu | 3,0 | Phase 2, 3 |
| 2 | Kien truc & Chat luong code | 2,5 | Phase 2 |
| 3 | Co so du lieu & API | 2,0 | Phase 1, 2 |
| 4 | Cong nghe & Cong cu | 1,0 | Phase 4 |
| 5 | Van dap & Hieu biet | 1,5 | Phase 6 |

## Goals

| # | Goal | Priority |
|---|------|----------|
| 1 | Nop du deliverable Phase 1 truoc 24/07 (ERD + so do kien truc + repo khung) | P1 |
| 2 | Va het lo hong rubric tieu chi 1-3 (bug nghiep vu, validate, transaction, paging, Swagger) | P1 |
| 3 | Moi thanh vien so huu 1 tinh nang nang cao demo duoc va giai thich duoc | P1 |
| 4 | Lich su commit deu, message ro rang, co dong gop that cua ca 3 nguoi | P1 |
| 5 | 3 file bao cao dung mau, nop truoc 31/07 | P1 |
| 6 | Ca 3 tra loi duoc data flow, DI/IoC, JPA trong buoi van dap 01/08 | P1 |

## Thanh vien nhom

| Ky hieu | STT | Ho ten | MSSV | GitHub | Ten file bao cao |
|---|---|---|---|---|---|
| **A** | 8 | Ho Minh Dao | 24150199 | `hmddevv` | `8_Ho Minh Dao_24150199.docx` |
| **B** | 45 | Le Dinh Thanh | 24150127 | `thanhhdev` | `45_Le Dinh Thanh_24150127.docx` |
| **C** | 7 | Nguyen Chi Cuong | 24150545 | `cuongdevv` | `7_Nguyen Chi Cuong_24150545.docx` |

- Giang vien huong dan: **Ly Ngoc Hung**
- Nganh: **Cong Nghe Thong Tin**
- Chuyen nganh: **Ky Thuat Phan Mem**
- Lop hoc phan: **020100138201**
- Hoc phan: Lap trinh ung dung voi Java (14113014) - HK3 2025-2026
- Nhom: **1** - bao ve **01/08/2026**
- Repo: `hmddevv/ScientificResearchManagementSystem` → **doi ten thanh `CourseManagementSystem`** (da chot)

## Phan cong 3 nguoi (slice doc - moi nguoi nam tron 1 luong du lieu)

| Nguoi | Mang so huu | Tinh nang nang cao | Chu de van dap rieng |
|---|---|---|---|
| **A - Ho Minh Dao** | Catalog: Category, Instructor, Course, Lesson | Danh gia & xep hang khoa hoc (Review, AVG rating, top courses) | Quan he JPA 1-N/N-1, lazy vs eager, N+1 query, Specification API, Paging & Sorting |
| **B - Le Dinh Thanh** | Enrollment / nghiep vu ghi danh | Chung chi hoan thanh + `@Scheduled` nhac hoc vien | `@Transactional`, Factory + Builder Pattern, rule nghiep vu, optimistic vs pessimistic locking |
| **C - Nguyen Chi Cuong** | Cross-cutting / ha tang | Spring Cache + Audit log (AOP) | DI/IoC container, Layered Architecture, DTO vs Entity, Global Exception Handler, Docker Compose, profile dev/prod |

Ly do gan vai tro nay (co the doi neu nhom muon):
- **A = Dao**: dang so huu repo (`hmddevv`), nam ro cau truc du an nhat.
- **C = Cuong**: mang cross-cutting nang nhat va phai lam **truoc** (sua `BaseEntity` o Phase 3),
  cung la nguoi dang cam chich ke hoach nay.
- **B = Thanh**: mang nghiep vu ghi danh - noi tap trung 3 bug P1 can va o Phase 2.

Nguyen tac: **chia doc, khong chia ngang theo tang**. Neu chia ngang (A lam Controller, B lam Service,
C lam Repository) thi khi thay hoi "luong du lieu chuc nang ghi danh chay the nao?" khong ai tra loi
tron ven duoc - mat diem tieu chi 5.

## Phases

| # | Phase | Thoi gian | Status |
|---|-------|-----------|--------|
| 1 | [Phase 1: Chuan hoa nen & deliverable Phase 1](./phase-01-start.md) | 22/07 → **24/07** | Da lam xong tai lieu, **con cho nop + quyen cho Thanh** |
| 2 | [Phase 2: Va lo hong theo rubric](./phase-02-va-lo-hong-theo-rubric.md) | 24/07 → 26/07 | **Done** |
| 3 | [Phase 3: Tinh nang nang cao 3 nguoi](./phase-03-tinh-nang-nang-cao-3-nguoi.md) | 26/07 → 29/07 | **Code done**, con phan van dap |
| 4 | [Phase 4: Test, Docker, CI va commit history](./phase-04-test-docker-ci-va-commit-history.md) | 29/07 → 30/07 | **Ha tang done**, con commit cua 3 nguoi |
| 5 | [Phase 5: Viet bao cao tieu luan](./phase-05-viet-bao-cao-tieu-luan.md) | 27/07 → **31/07** | Pending |
| 6 | [Phase 6: Chuan bi van dap va demo](./phase-06-chuan-bi-van-dap-va-demo.md) | 31/07 → **01/08** | Pending |

Phu thuoc: 1 → 2 → 3 → 4 → 6. Trong Phase 3, ba nguoi lam song song (nhung nguoi C phai
sua `BaseEntity` xong truoc - xem Phase 3).

**Phase 5 chay song song**, bat dau soan khung tu 27/07 ngay khi da co ERD + so do kien truc tu
Phase 1 va so lieu tu Phase 2. Khong doi den 30/07 moi bat dau viet - do la rui ro tre han lon nhat.

```
22  23  24  25  26  27  28  29  30  31 | 01/08
|-- P1 --|
        |--- P2 ---|
                   |----- P3 -----|
                                  |P4|
              |-------- P5 -----------|
                                      |P6|
        ^nop Phase 1                  ^nop san pham   ^bao ve
```

## Hien trang ky thuat (da xac minh)

| Hang muc | Trang thai |
|---|---|
| Build | `./mvnw clean test` **PASS** (exit 0) |
| Java | pom target 17; may local dang co JDK 21 (chay duoc) |
| Spring Boot | 3.4.1 |
| CSDL | H2 (profile `dev`) / MySQL 8 (profile `prod`) |
| Entity | BaseEntity (id, createdAt, updatedAt, `@Version`), Category, Instructor, Course, Lesson, Student, Enrollment |
| Quan he | Category 1-N Course; Instructor 1-N Course; Course 1-N Lesson (cascade + orphanRemoval); Student N-N Course qua Enrollment; `UNIQUE(student_id, course_id)` |
| Design pattern | Builder (Lombok), Factory (`EnrollmentFactory`) |
| API doc | springdoc-openapi, Swagger UI tai `/swagger-ui.html` |
| Ha tang | Dockerfile multi-stage (non-root user + healthcheck), docker-compose (MySQL 8 + app), GitHub Actions CI |
| Frontend | HTML/CSS/JS thuan goi Fetch API (`src/main/resources/static/`) |
| Test | 2 file test (`CourseManagementApplicationTests`, `EnrollmentServiceTest`) |
| **Git history** | 13 commit, conventional commits. **Van chi co 2 tai khoan** (Dao 1 commit, Cuong 12) - Thanh chua co quyen push |
| Test | **30 test PASS** (truoc: 3) tren 8 file test |
| Tinh nang nang cao | Danh gia & xep hang · Chung chi + job nhac hoc · Cache + Audit log (AOP) - **da chay va demo duoc** |
| Docker | Da chay thu that: MySQL healthy -> app UP, du lieu ton tai qua restart |
| CI | GitHub Actions **xanh** tren moi commit |

## Success Criteria

- [ ] Deliverable Phase 1 nop dung han 24/07: file ERD, so do kien truc, link GitHub repo chay duoc
- [ ] `./mvnw clean verify` PASS va `docker compose up --build` khoi dong duoc app + MySQL
- [ ] Swagger UI liet ke day du endpoint, moi endpoint danh sach co paging + sorting
- [ ] Moi lo hong P1 trong bao cao audit da duoc va va co test chung minh
- [ ] 3 tinh nang nang cao hoat dong, demo duoc qua Swagger va frontend
- [ ] Git log co it nhat 30 commit, phan bo deu tren ca 3 tai khoan, message theo conventional commit
- [ ] 3 file bao cao dung mau truong (bia + phieu cham + noi dung), dat ten `STT_Ho va ten_MSSV`
- [ ] Moi thanh vien tra loi duoc 10 cau hoi van dap thuoc mang minh so huu

## Cau hoi con mo (can tra loi truoc Phase 5)

1. **Thanh (`thanhhdev`) chua duoc moi vao repo** - Cuong (`cuongdevv`) da co quyen Write, Dao la chu repo.
   Day la viec CAN LAM NGAY: khong co quyen push thi Thanh khong the co commit nao,
   mat diem tieu chi 4 va rat kho giai thich trong buoi bao ve.
2. **Truong co yeu cau khai bao su dung AI trong do an khong?** Neu co thi khai bao trong bao cao
   o Phase 5. Dong `Co-Authored-By` trong git da duoc go (xem "Hien trang git" trong Phase 4). Ten hien tai `ScientificResearchManagementSystem` **khong khop de tai** (quan ly khoa hoc).
   De nghi doi thanh `CourseManagementSystem` - GitHub tu redirect URL cu nen khong vo link da nop.
7. **Chinh sach cua truong/giang vien ve su dung AI trong do an la gi?**
   Commit duy nhat trong repo hien co dong `Co-Authored-By: Claude Opus 4.8`. Can biet quy dinh
   de xu ly dung: khai bao neu bat buoc khai bao, giu nguyen neu duoc phep dung AI ho tro.
   Xem phan "Hien trang git" trong [Phase 4](./phase-04-test-docker-ci-va-commit-history.md).

<!-- slug: tieu-luan-cuoi-ky-he-thong-quan-ly-khoa-hoc -->
