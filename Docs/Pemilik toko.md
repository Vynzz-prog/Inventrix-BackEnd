# API SPEC PEMILIK TOKO

## Tamabah Data Barang
### Endpoint : Post inventrix/barang/tambah

- for tambah data barang baru

#### Request Body :
```json
{
  "kodeBarang": "BRG002",
  "namaBarang": "Mouse Logitech",
  "merek": "Logitech",
  "kategori": "Elektronik",
  "hargaBeli": 80000,
  "hargaJual": 120000,
  "deskripsi": "Mouse wireless 2.4GHz",
  "imageUrl": "https://example.com/mouse.jpg"
}
```

#### Response Body ( success ):
```json
{
  "pesan": "Barang berhasil ditambahkan",
  "data": {
    "id": 1,
    "kodeBarang": "BRG002",
    "namaBarang": "Mouse Logitech",
    "merek": "Logitech",
    "kategori": "Elektronik",
    "hargaBeli": 80000.0,
    "hargaJual": 120000.0,
    "stokToko": 0,
    "stokGudang": 0,
    "deskripsi": "Mouse wireless 2.4GHz",
    "imageUrl": "https://example.com/mouse.jpg"
  }
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Kode Barang Sudah ada"
}
```

## Edit Data Barang
### Endpoint : Put inventrix/barang/edit/{id}

- for edit data barang baru 
- contoh API inventrix/barang/edit/3

#### Request Body :
```json
{
  "kodeBarang": "BRG000",
  "namaBarang": "Kulkas Sharp 3 Pintu",
  "merek": "Sharp",
  "kategori": "Elektronik",
  "hargaBeli": 2700000,
  "hargaJual": 3100000,
  "deskripsi": "Kulkas hemat listrik dengan sistem pendingin cepat",
  "imageUrl": "https://example.com/kulkas.jpg"
}

```

#### Response Body ( success ):
```json
{
  "pesan": "Data barang berhasil diperbarui",
  "data": {
    "id": 1,
    "kodeBarang": "BRG000",
    "namaBarang": "Kulkas Sharp 3 Pintu",
    "merek": "Sharp",
    "kategori": "Elektronik",
    "hargaBeli": 2700000.0,
    "hargaJual": 3100000.0,
    "stokToko": 0,
    "stokGudang": 0,
    "deskripsi": "Kulkas hemat listrik dengan sistem pendingin cepat",
    "imageUrl": "https://example.com/kulkas.jpg"
  }
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Kode Barang Sudah ada"
}
```

## Tampil detail Data Barang
### Endpoint : Put inventrix/barang/detail/{id}

- for liat detail data barang baru
- contoh API inventrix/barang/detail/3


#### Response Body ( success ):
```json
{
  "pesan": "Data barang ditemukan",
  "data": {
    "id": 11,
    "kodeBarang": "B001",
    "namaBarang": "Kipas Angin",
    "merek": "Panasonic",
    "hargaBeli": 3000.0,
    "hargaJual": 6000.0,
    "stokToko": 0,
    "stokGudang": 0,
    "deskripsi": "Kipas Angin Bagus",
    "imageUrl": "http://192.168.1.5:8080/inventrix/barang/uploads/1762867381501_copy.png"
  }
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Barang tidak ditemukan"
}
```