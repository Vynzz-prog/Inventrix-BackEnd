# API SPEC USERS

## Login
### Endpoint : Post inventrix/auth/login
### Endpoint Guest : inventrix/auth/guest

- for login

#### Request Body :
```json
{
  "username": "gudang",
  "password": "gudang123"
}
```

#### Response Body ( success ):
```json
{
  "id": 1,
  "pesan": "Login berhasil",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "OWNER",
  "username": "owner1"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Username atau password salah"
}
```

## Lihat List Data Barang
### Endpoint : Get inventrix/barang/list

- for lia list data barang, ada pake parameter dia
- contoh full API inventrix/barang/list?page=0&size=5
- bisa untuk search http://localhost:8080/inventrix/barang/list?search=Pulpen
- boleh juga filter berdasarkan merek http://localhost:8080/inventrix/barang/list?merek=Pilot&page=0&size=10
- ato nda gabung searh deng merek inventrix/barang/list?search=pulpen&merek=pilot&page=0&size=10

#### Response Body ( success ):
```json
{
  "pesan": "Data barang berhasil diambil",
  "totalItems": 2,
  "data": [
    {
      "stokToko": 0,
      "stokGudang": 0,
      "imageUrl": "/inventrix/barang/uploads/1762779274784_barang.jpg",
      "kodeBarang": "qwer",
      "id": 1,
      "namaBarang": "cwcep",
      "hargaJual": 300000.0,
      "merek": "laa"
    },
    {
      "stokToko": 0,
      "stokGudang": 0,
      "imageUrl": "/inventrix/barang/uploads/1762779367976_barang.jpg",
      "kodeBarang": "p",
      "id": 2,
      "namaBarang": "p",
      "hargaJual": 10000.0,
      "merek": "p"
    }
  ],
  "totalPages": 1,
  "currentPage": 0
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Tidak ada data barang ditemukan",
  "totalItems": 0,
  "data": [],
  "totalPages": 0,
  "currentPage": 0
}
```