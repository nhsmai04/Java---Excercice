# IE303 Lab 4 - Product Store Database

Lab 4 xay dung tiep tu giao dien san pham cua Lab 3 va luu/truy van thong tin san pham bang PostgreSQL.

## Chuc nang

- Hien thi danh sach san pham giay tu co so du lieu PostgreSQL.
- Xem chi tiet san pham khi bam vao card.
- Tim kiem san pham theo ten hoac thuong hieu.
- Tao bang `products` tu dong neu bang chua ton tai.
- Cau hinh ket noi bang file `.env`.

## Cau truc chinh

```text
src/main
├── .env.example
├── .gitignore
├── java/org/lab4
│   ├── Product.java
│   ├── ProductDAO.java
│   └── ProductStoreLab4.java
└── resources/org/lab4
    └── products_seed.sql
```

## Yeu cau

- Java JDK 17 tro len.
- PostgreSQL.
- PostgreSQL JDBC Driver.
- IDE ho tro Java project, vi du IntelliJ IDEA, Eclipse hoac NetBeans.

## Cau hinh PostgreSQL

Tao database:

```sql
CREATE DATABASE lab4;
```

Sao chep `.env.example` thanh `.env`, sau do sua thong tin ket noi:

```env
POSTGRES_URL=jdbc:postgresql://localhost:5432/lab4
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```

File `.env` da duoc dua vao `.gitignore`, khong nen push len GitHub vi co the chua mat khau CSDL.

## Them du lieu mau

Chay file SQL sau trong PostgreSQL:

```text
resources/org/lab4/products_seed.sql
```

File nay tao bang `products` neu chua co va insert du lieu mau. Neu chay lai nhieu lan, du lieu se khong bi trung do da co rang buoc unique.

## Chay chuong trinh

Them PostgreSQL JDBC Driver vao project, sau do chay class:

```text
org.lab4.ProductStoreLab4
```

Khi ung dung khoi dong, `ProductDAO` se doc cau hinh tu bien moi truong hoac file `.env`, ket noi PostgreSQL, tao bang neu can, roi truy van danh sach san pham.

## Ghi chu

- Du lieu san pham khong hard-code trong Java. Du lieu mau nam trong `products_seed.sql`.
- Anh san pham dang dung lai asset cua Lab 3 voi duong dan `/org/lab3/img*.png`.
- Neu khong ket noi duoc database, kiem tra lai PostgreSQL da chay, thong tin `.env`, va PostgreSQL JDBC Driver da duoc them vao project.
