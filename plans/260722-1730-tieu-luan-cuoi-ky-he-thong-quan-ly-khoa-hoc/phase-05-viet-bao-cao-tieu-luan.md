---
phase: 5
title: "Viet bao cao tieu luan"
status: todo
priority: P1
effort: "soan khung tu 27/07, hoan thien 30/07 - 31/07/2026"
dependencies: [4]
---

# Phase 5: Viet bao cao tieu luan

## Overview

Viet **3 file bao cao Word** - moi sinh vien mot file - theo dung mau
`001382_LTUD-Java_Mau trinh bay tieu luan.docx` cua truong, nop truoc **31/07/2026**.

**CHOT (theo yeu cau cua nhom):** ba file co **CUNG MOT NOI DUNG**. Chi khac nhau o
**trang bia** (STT, ho ten, MSSV) va **ten file**. Khong tach noi dung rieng cho tung nguoi.

Yeu cau tu thong bao: *"Dat ten file theo cu phap: **STT_Ho va ten_MSSV** (moi sinh vien mot file)"*
- nghia la moi sinh vien nop mot file mang ten minh, khong phai moi nguoi viet mot noi dung khac nhau.

Nen bat dau soan khung tu sau Phase 1 (luc do da co ERD va so do kien truc), khong doi den 29/07.

## Requirements

**Functional:**
- [x] Ban thao noi dung day du: `docs/bao-cao/bao-cao-tieu-luan.md` (11 chuong)
- [ ] 3 file `.docx` dung mau truong: trang bia + phieu cham bai thi + noi dung
- [ ] **Ca 3 file cung noi dung**, chi khac trang bia
- [ ] Dat ten dung cu phap `STT_Ho va ten_MSSV.docx`
- [ ] Chen anh so do ERD, so do kien truc, anh chup man hinh demo
- [ ] Co link GitHub repo

**Non-functional:**
- [ ] Dinh dang bia dung mau: TIEU LUAN (dam, in hoa, co 30); nganh/chuyen nganh (dam, in hoa, co 16); GVHD/SVTH/MSSV/lop hoc phan (dam, in hoa, co 14); dia diem + thang nam (thuong, co 14)
- [ ] Danh so trang, co muc luc
- [ ] Khong sao chep nguyen van tai lieu ngoai ma khong trich dan

## Cau truc noi dung de xuat

Phan **chung cho ca 3 file** (thong nhat noi dung, moi nguoi tu dien lai bang loi minh):

| Chuong | Noi dung | Anh xa tieu chi |
|---|---|---|
| 1. Gioi thieu | Ly do chon de tai, muc tieu, pham vi, doi tuong su dung | - |
| 2. Co so ly thuyet | Spring Boot, IoC container & Dependency Injection, Layered Architecture, JPA/Hibernate, REST API, DTO vs Entity, Design Pattern (Builder, Factory), Docker | 2, 5 |
| 3. Phan tich yeu cau | Yeu cau chuc nang, yeu cau phi chuc nang, so do use case, cac tac nhan | 1 |
| 4. Thiet ke he thong | So do kien truc tang, so do package, **ERD**, mo ta bang + khoa + rang buoc, ly do thiet ke | 3 |
| 5. Cai dat | Cau truc project, luong du lieu 1 request day du, xu ly validate, Global Exception Handler, Transaction, Paging/Sorting, Swagger | 1, 2, 3 |
| 6. Tinh nang nang cao | Danh gia & xep hang, chung chi + job dinh ky, cache + audit log | 1 |
| 7. Kiem thu va trien khai | Danh sach test case, ket qua chay test, Dockerfile, Docker Compose, CI GitHub Actions | 4 |
| 8. Ket qua dat duoc | Anh chup Swagger UI, frontend, ket qua chay test, container dang chay | 1, 4 |
| 9. Danh gia & huong phat trien | Uu diem, han che, de xuat toi uu (vi du: index DB, cache phan tan, phan trang keyset, tach service) | 5 |
| 10. Phan cong cong viec | Bang phan cong 3 nguoi + trich `git shortlog` lam bang chung | 4 |
| 11. Tai lieu tham khao | Spring docs, Baeldung, tai lieu mon hoc Ch0-Ch3, Lab4, Lab5 | - |

**Khong co phan rieng.** Ban thao da gop day du ca ba mang chuyen sau vao dung chuong:

| Mang | Nam o chuong nao trong ban thao |
|---|---|
| Quan he JPA, lazy/eager, N+1, Specification, paging | Chuong 4.2 va 5.6, 5.7 |
| Transaction, rollback, optimistic vs pessimistic locking | Chuong 5.5 |
| IoC/DI, Global Exception Handler, DTO/Mapper, cross-cutting | Chuong 2.2, 5.4, 5.8 |
| Danh gia & xep hang | Chuong 6.1 |
| Chung chi + `@Scheduled` | Chuong 6.2 |
| Cache + audit log (AOP) + JPA Auditing | Chuong 6.3 |
| Docker, Docker Compose, CI | Chuong 7.4, 7.5 |

Luu y khi van dap: **noi dung bao cao giong nhau nhung nguoi trinh bay phai khac nhau**.
Moi thanh vien van chiu trach nhiem tra loi sau ve mang cua minh (xem Phase 6) - bao cao giong
nhau khong co nghia la ai cung tra loi giong nhau.

## Related Code Files

- Created: `docs/bao-cao/bao-cao-tieu-luan.md` - ban thao DUY NHAT, 11 chuong, dung cho ca 3 file
- Create: `docs/bao-cao/anh/` (anh chup man hinh demo)
- Re-use: `docs/database-schema.png`, `docs/architecture.png` (tu Phase 1)
- Output cuoi (KHONG commit vao repo, nop qua Forms): `STT_Ho va ten_MSSV.docx` x 3

## Implementation Steps

1. **Thong tin bia** (da du, chi can dien vao mau Word):

   | Muc | Gia tri |
   |---|---|
   | Nganh | CONG NGHE THONG TIN |
   | Chuyen nganh | KY THUAT PHAN MEM |
   | Lop hoc phan | 020100138201 |
   | Mon | LAP TRINH UNG DUNG VOI JAVA (14113014) |
   | Ten de tai | XAY DUNG HE THONG QUAN LY KHOA HOC |
   | Dia diem, thoi gian | TP. Ho Chi Minh, thang 07 nam 2026 |
   | Giang vien huong dan | LY NGOC HUNG |

   | STT | SVTH | MSSV | Ten file nop |
   |---|---|---|---|
   | 8 | Ho Minh Dao | 24150199 | `8_Ho Minh Dao_24150199.docx` |
   | 45 | Le Dinh Thanh | 24150127 | `45_Le Dinh Thanh_24150127.docx` |
   | 7 | Nguyen Chi Cuong | 24150545 | `7_Nguyen Chi Cuong_24150545.docx` |

   Ten file giu dau tieng Viet dung nhu ho ten trong danh sach lop.
   Kiem tra lai cu phap voi giang vien neu form nop bao loi ky tu dac biet.

2. ~~Soan ban thao~~ **DA XONG**: `docs/bao-cao/bao-cao-tieu-luan.md`, 11 chuong,
   so lieu lay that tu repo (97 file Java, 44 endpoint, 30 test, 9 bang).

3. Ca nhom **doc lai ban thao mot luot**, sua cau chu cho tu nhien, bo sung y neu thieu.

4. **Chup anh man hinh demo**:
   - Swagger UI liet ke endpoint
   - 1 request POST thanh cong + 1 request loi validate (the hien Global Exception Handler)
   - Frontend danh sach khoa hoc + danh gia sao
   - Ket qua `./mvnw clean verify`
   - `docker compose ps` va `docker compose logs` khi app dang chay
   - `git shortlog -sne` (bang chung dong gop 3 nguoi)

5. **Chuyen sang Word**:
   - Mo file mau `001382_LTUD-Java_Mau trinh bay tieu luan.docx`, luu thanh 3 ban.
   - Dien trang bia theo dung dinh dang co chu trong mau.
   - Giu nguyen trang "PHIEU CHAM BAI THI TIEU LUAN" (giang vien dien).
   - Dan noi dung vao phan "NOI DUNG BAI TIEU LUAN".
   - Chen anh so do + anh chup man hinh, danh so hinh ("Hinh 4.1: So do ERD...").
   - Tao muc luc tu dong, danh so trang.

6. **Ra soat truoc khi nop**: du 3 file, moi file dung trang bia cua nguoi do,
   noi dung ba file giong het nhau, mo duoc bang Word, khong loi font tieng Viet.

7. **Doi ten file** dung cu phap `STT_Ho va ten_MSSV.docx` va nop qua link Google/Microsoft Forms
   giang vien gui, **truoc 31/07/2026**.

## Tests / Validation

- Doi chieu tung muc trong bao cao voi 5 tieu chi cham diem - khong tieu chi nao bi bo trong.
- Moi khang dinh ky thuat trong bao cao phai co trong code that (vi du: neu viet "co cache" thi
  phai co `@Cacheable` trong repo).
- Ba file co noi dung GIONG NHAU; chi trang bia khac.
- Ten file dung cu phap, mo duoc bang Word, khong loi font tieng Viet.

## Success Criteria

- [ ] 3 file `.docx` hoan chinh, dung mau, dung cu phap ten file
- [ ] Co du ERD, so do kien truc, anh chup demo, link GitHub
- [ ] Noi dung bao phu du 5 tieu chi cham diem
- [ ] Ba file cung noi dung, chi khac trang bia va ten file
- [ ] Moi so lieu trong bao cao khop voi repo that
- [ ] Da nop truoc 31/07/2026

## Risk Assessment

| Rui ro | Muc do | Giam thieu |
|---|---|---|
| Doi den 29/07 moi bat dau viet -> khong kip | Thap | Ban thao day du **da xong** tu 22/07 |
| Quen doi trang bia o mot file | Trung binh | Checklist truoc khi nop: mo ca 3 file, doi chieu STT/ho ten/MSSV |
| Bao cao mo ta tinh nang ma code khong co | Cao | Buoc validate: moi khang dinh phai doi chieu duoc voi code that |
| Loi font tieng Viet khi chuyen tu markdown sang Word | Trung binh | Danh may truc tiep trong Word cho phan tieng Viet, chi copy phan code |
| Chua co thong tin bia (MSSV, GVHD, lop) | Trung binh | Hoi va chot ngay tu bay gio, dung de den 30/07 |
| Nop nham file / thieu file cua 1 nguoi | Thap | Checklist truoc khi nop: du 3 file, dung ten, mo duoc |
