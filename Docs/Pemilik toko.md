# API SPEC PEMILIK TOKO

## Tamabah Data Barang
### Endpoint : Post inventrix/barang/tambah

- for tambah data barang baru

#### Request Body :
```json
{
  "kodeBarang": "BRG002",
  "namaBarang": "Mouse Logitech",
  "merekId": "1",
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
    "kodeBarang": "B001",
    "namaBarang": "Kipas Angin",
    "merek": {
      "id": 2,
      "namaMerek": "Panasonic"
    },
    "hargaBeli": 3000.0,
    "hargaJual": 6000.0,
    "stokToko": 0,
    "stokGudang": 0,
    "deskripsi": "Kipas Angin Bagus",
    "imageUrl": "/inventrix/barang/uploads/1762934765632_copy.png",
    "hargaBeliFormatted": "3.000",
    "hargaJualFormatted": "6.000"
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
- contoh API http://localhost:8080/inventrix/barang/edit/1?kodeBarang&namaBarang&merekId&hargaBeli&hargaJual&deskripsi&image

#### Request Body :
- params

  "kodeBarang": "BRG000",
  "namaBarang": "Kulkas Sharp 3 Pintu",
  "merekId": "Sharp",
  "hargaBeli": 2700000,
  "hargaJual": 3100000,
  "deskripsi": "Kulkas hemat listrik dengan sistem pendingin cepat",
  "image": "https://example.com/kulkas.jpg"

#### Response Body ( success ):
```json
{
  "pesan": "Data barang berhasil diperbarui",
  "data": {
    "id": 1,
    "kodeBarang": "BX0001,",
    "namaBarang": "Kipas Angin,",
    "merek": {
      "id": 2,
      "namaMerek": "Panasonic"
    },
    "hargaBeli": 35000.0,
    "hargaJual": 60000.0,
    "stokToko": 0,
    "stokGudang": 0,
    "deskripsi": "pas angin Punya Mercurius,",
    "imageUrl": "/inventrix/barang/uploads/1762939823314_Screenshot 2024-03-15 024822.png",
    "hargaBeliFormatted": "35.000",
    "hargaJualFormatted": "60.000"
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

## Tambah Merek
### Endpoint : Post inventrix/merek/tambah

- for tambah merek di kelola merek
- contoh API http://localhost:8080/inventrix/merek/tambah?namaMerek=Panasonic

#### Request Body :
namaMerek : Panasonic  ( Bentuk Params ini )

#### Response Body ( success ):
```json
{
  "pesan": "Merek berhasil ditambahkan",
  "data": {
    "id": 1,
    "namaMerek": "Panasonic"
  }
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Nama merek tidak boleh kosong"
}
```

## Edit Merek
### Endpoint : PUT inventrix/merek/edit/{id}

- for edit merek di kelola merek
- contoh API http://localhost:8080/inventrix/merek/edit/1?namaBaru=LG

#### Request Body :
namaBaru : Panasonic  ( Bentuk Params ini )

#### Response Body ( success ):
```json
{
  "pesan": "Merek berhasil diperbarui",
  "data": {
    "id": 1,
    "namaMerek": "LG"
  }
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Nama baru tidak boleh kosong"
}
```

## Hapus Merek
### Endpoint : DELETE inventrix/merek/hapus/{id}

- for hapus merek di kelola merek

#### Response Body ( success ):
```json
{
  "pesan": "Merek berhasil dihapus"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "Merek tidak ditemukan"
}
```