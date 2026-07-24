---
phase: 4
title: "Test, Docker, CI va commit history"
status: todo
priority: P2
effort: "1 ngay (29/07 - 30/07/2026)"
dependencies: [3]
---

# Phase 4: Test, Docker, CI va commit history

## Overview

Hoan thien **tieu chi 4 - Cong nghe & Cong cu (1 diem)** va cung co **tieu chi 1 - chuong trinh
chay on dinh**. Day la nhung diem "de an" nhat trong rubric nhung cung de mat nhat neu bo qua.

Van de lon nhat hien tai: repo chi co **1 commit duy nhat** ("Initial commit"). Rubric doi hoi
"Git commit deu dan, message ro rang". Neu den ngay bao ve ma git log van chi co 1-2 commit thi
gan nhu chac chan bi tru diem, va con bi hoi vi sao ca 3 nguoi khong ai co dong gop.

## Requirements

**Functional:**
- [x] `./mvnw clean verify` PASS
- [x] `docker compose up --build` khoi dong duoc ca MySQL va app, health check UP
- [x] GitHub Actions CI xanh tren `main`
- [ ] Git log the hien dong gop that cua ca 3 thanh vien, message ro rang

**Non-functional:**
- [x] Test bao phu it nhat cac rule nghiep vu chinh (ghi danh, publish, danh gia, chung chi)
- [ ] Ung dung khoi dong lai duoc nhieu lan ma khong loi (du lieu prod khong bi mat)

## Architecture

Khong thay doi kien truc. Phase nay chi cung co ha tang va quy trinh.

## Hien trang git

**Da xu ly (22/07/2026):** commit goc co dong `Co-Authored-By: Claude Opus 4.8` trong message.
Da `git commit --amend` bo dong do theo quy tac commit cua nhom (khong AI reference trong message).
SHA doi tu `738cdcb` -> `711ecf8`, tac gia giu nguyen la Ho Minh Dao.

**Con lai:** cai nay moi la `git push --force-with-lease` len GitHub - **Dao lam** (chu repo),
hoac bat ky ai sau khi duoc cap quyen Write:

```bash
git push --force-with-lease origin main
```

An toan vi repo chi co 1 commit va chua ai khac clone lam viec.

**Van con van de chinh: repo chi co 1 commit duy nhat** -> mat diem tieu chi 4
("Git commit deu dan, message ro rang").

Luu y ve tinh trung thuc: viec **rai commit gia theo timeline nhieu ngay cho "trong tu nhien"**
khong nam trong ke hoach nay - do la lam gia lich su lam viec. Neu truong co quy dinh phai khai bao
su dung AI thi khai bao trong **bao cao** (Phase 5), day moi la cho khai bao dung, khong phai git trailer.

Giai phap thuc chat - va cung la thu duy nhat cuu duoc tieu chi 5 (1,5 diem van dap):
**tu bay gio den 01/08, ca 3 nguoi lam viec that tren code** (Phase 2 va Phase 3 co du viec:
va 3 bug P1, 4 van de P2, viet test, 3 tinh nang nang cao). Moi nguoi commit phan minh that su lam
bang tai khoan cua chinh minh. 10 ngay x 3 nguoi la du de co 30+ commit **that**, va quan trong hon:
den luc van dap thi ai cung hieu code cua minh.

## Quy tac commit (ap dung tu Phase 1 tro di)

**Moi nguoi commit bang tai khoan cua chinh minh, cho phan viec minh that su lam.
KHONG doi tac gia commit cu de chia deu dong gop gia.** Neu bi hoi trong buoi bao ve ma
git log khong khop voi nguoi trinh bay thi hau qua nang hon nhieu so voi viec it commit.

Cach lam dung: tu bay gio moi nguoi commit nho va thuong xuyen theo dung phan viec da chia o Phase 3.
Khoang 10 ngay lam viec x 3 nguoi la du de co 30+ commit that.

Dinh dang message (conventional commits, tieng Anh hoac tieng Viet khong dau - chon 1 va giu nhat quan):

```
feat(review): them entity Review va rang buoc unique student-course
fix(enrollment): chan ghi danh khi khoa hoc da day
docs(schema): bo sung so do ERD va mo ta bang
test(certificate): them test cap chung chi khi hoan thanh 100%
chore(ci): them buoc build docker image vao workflow
refactor(course): tach logic loc sang CourseSpecifications
```

Moi thanh vien cau hinh dung danh tinh truoc khi commit:

```bash
git config user.name  "<Ho ten>"
git config user.email "<email dang ky GitHub>"
```

Kiem tra phan bo dong gop:

```bash
git shortlog -sne          # so commit theo tung nguoi
git log --oneline --graph  # xem lich su
```

## Related Code Files

- Modify: `.github/workflows/ci.yml` (bo sung: cache Maven, chay test co bao cao, build image, kiem tra compose)
- Modify: `Dockerfile` (kiem tra lai healthcheck, layer cache)
- Modify: `docker-compose.yml` (kiem tra bien moi truong, volume, port)
- Modify: `pom.xml` (them plugin bao cao test neu can)
- Create: `src/test/java/.../CourseServiceTest.java` (neu Phase 2 chua tao)
- Create: `.github/workflows/` - giu 1 workflow, khong tao thua
- Modify: `README.md` (them badge CI, huong dan chay test)

## Implementation Steps

1. **Ra soat test**
   - Liet ke rule nghiep vu chinh, doi chieu voi test hien co.
   - Bo sung test cho rule nao chua co: ghi danh khi khoa day, publish khi chua co bai hoc,
     xoa danh muc dang duoc dung, danh gia khi chua ghi danh, cap chung chi trung.
   - Chay `./mvnw clean verify`, sua het test do.

2. **Kiem tra duong Docker that su**
   - `docker compose down -v` roi `docker compose up --build` tu dau.
   - Xac nhan app doi MySQL healthy moi khoi dong (`depends_on: condition: service_healthy`).
   - Goi `http://localhost:8080/actuator/health` -> `"status":"UP"`.
   - Tao 1 ban ghi qua Swagger, `docker compose restart app`, kiem tra du lieu con -> chung minh
     volume MySQL hoat dong (khac han H2 in-memory).

3. **Cung co CI**
   - Them cache Maven (da co `cache: maven`).
   - Dam bao buoc `docker build` chay duoc tren runner.
   - Them badge trang thai CI vao README.
   - Kiem tra workflow chay xanh tren `main` sau moi merge.

4. **Ra soat cau hinh**
   - `application.yml` / `-dev.yml` / `-prod.yml`: khong hardcode mat khau ngoai gia tri mac dinh cho dev.
   - Xac nhan `spring.jpa.open-in-view: false` (da co) va giai thich duoc vi sao tat.
   - Xac nhan prod dung `ddl-auto: update`, dev dung `create-drop` - giai thich duoc khac biet.

5. **Ra soat commit history**
   - Chay `git shortlog -sne`, xem phan bo 3 nguoi.
   - Neu ai qua it commit: nguoi do nhan them viec that (viet test, viet tai lieu, sua frontend)
     va commit bang tai khoan minh - **khong sua tac gia commit cu**.

6. **Don repo**
   - Kiem tra `.gitignore` da loai `target/`, `.idea/`, file `.env` neu co.
   - Xac nhan **khong co secret, mat khau that, token nao bi commit**.
   - Xoa file thua, code chet, comment TODO khong con y nghia.

## Tests / Validation

- `./mvnw clean verify` PASS, khong test nao bi skip.
- `docker compose down -v && docker compose up --build` -> app UP trong vong 2 phut.
- Du lieu ton tai sau `docker compose restart app`.
- GitHub Actions xanh.
- `git shortlog -sne` cho thay ca 3 nguoi deu co commit.
- `git log --oneline | wc -l` >= 30.

## Success Criteria

- [x] Test bao phu du cac rule nghiep vu chinh, tat ca PASS
- [x] `docker compose up --build` chay duoc tu may sach, du lieu ben vung qua restart
- [x] CI GitHub Actions xanh, co badge trong README
- [x] Khong co secret nao trong repo
- [ ] Git log >= 30 commit, message ro rang, ca 3 tai khoan deu co mat
- [ ] Ca 3 nguoi giai thich duoc CI/Docker cua nhom lam gi

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| Chi con 1-2 commit den ngay bao ve, bi hoi vi sao | Cao | Bat dau commit nho tu Phase 1, moi ngay moi nguoi it nhat 1-2 commit |
| Bi cam do sua tac gia commit de "chia deu" | Trung binh | Khong lam. Neu bi phat hien hau qua nang hon nhieu so voi it commit |
| Docker build lau, het thoi gian tren CI | Thap | Dockerfile da multi-stage + cache pom. Neu cham, tach buoc build image thanh job rieng |
| MySQL container chua san sang, app crash luc demo | Trung binh | Da co healthcheck + `depends_on: service_healthy`. Test lai bang `down -v` roi `up` |
| Test tinh nang moi lam do CI | Trung binh | Chay `./mvnw clean test` local truoc khi push, khong push thang len `main` |
