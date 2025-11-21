# API SPEC Permintaan

## Buat Permintaan (ADMIN)
### Endpoint : Post inventrix/permintaan/create

- for permintaan ke gudang

#### Request Body :
```json
{
  "barangId": 8,
  "jumlah": 50,
  "keterangan": "Permintaan stok toko"
}


```

#### Response Body ( success ):
```json
{
  "pesan": "Permintaan dibuat, ID = 1"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Gudang Konfirmasi Permintaan
### Endpoint : Post inventrix/permintaan/kirim/{id}
- id disini itu id permintaan
- pas gudang klik tombol kirim

#### Response Body ( success ):
```json
{
  "pesan": "Permintaan ID 1 telah dikirim"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Lihat Permintaan 
### Endpoint : GET inventrix/permintaan/owner/diminta  ( for OWNR )
### Endpoint : GET inventrix/permintaan/warehouse/diminta ( for GUDANG )
- lihat list permintaan



#### Response Body ( success ):
```json
[
  {
    "id": 3,
    "barang": {
      "id": 8,
      "kodeBarang": "bshsvdv",
      "namaBarang": "bahav",
      "merek": {
        "id": 2,
        "namaMerek": "Panasonic"
      },
      "hargaBeli": 976850.0,
      "hargaJual": 7678449.0,
      "stokToko": 51,
      "stokGudang": 9,
      "deskripsi": "bylly",
      "imageUrl": "/inventrix/barang/uploads/1763212725731_barang.jpg",
      "hargaBeliFormatted": "976.850",
      "hargaJualFormatted": "7.678.449"
    },
    "jumlah": 1,
    "keterangan": "Permintaan stok toko",
    "status": "DIPINTA",
    "createdBy": "owner",
    "processedBy": null,
    "completedBy": null,
    "createdAt": "2025-11-21T22:11:07.056333",
    "processedAt": null,
    "completedAt": null
  }
]
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Lihat Permintaan yang sedang Dikirim
### Endpoint : GET inventrix/permintaan/dikirim


#### Response Body ( success ):
```json
[
  {
    "id": 1,
    "barang": {
      "id": 8,
      "kodeBarang": "bshsvdv",
      "namaBarang": "bahav",
      "merek": {
        "id": 2,
        "namaMerek": "Panasonic"
      },
      "hargaBeli": 976850.0,
      "hargaJual": 7678449.0,
      "stokToko": 0,
      "stokGudang": 10,
      "deskripsi": "bylly",
      "imageUrl": "/inventrix/barang/uploads/1763212725731_barang.jpg",
      "hargaJualFormatted": "7.678.449",
      "hargaBeliFormatted": "976.850"
    },
    "jumlah": 50,
    "keterangan": "Permintaan stok toko",
    "status": "DIKIRIM",
    "createdBy": "owner",
    "processedBy": "gudang",
    "completedBy": null,
    "createdAt": "2025-11-21T19:50:20.358005",
    "processedAt": "2025-11-21T20:00:31.622934",
    "completedAt": null
  }
]
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Konfirmasi Selesai ( ADMIN )
### Endpoint : Post permintaan/selesai/{id}


#### Response Body ( success ):
```json
{
  "pesan": "Permintaan ID 1 selesai. Stok toko telah diperbarui."
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```