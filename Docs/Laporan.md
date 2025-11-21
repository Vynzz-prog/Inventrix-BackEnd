# API SPEC Laporan

## Buat Laporan Masuk
### Endpoint : Post inventrix/laporan/create

- for buat laporan

#### Request Body :
```json
{
  "jenis": "MASUK",
  "supplier": "PT Sinar Jaya",
  "items": [
    {
      "barangId": 8,
      "jumlah": 5,
      "keterangan": "Masuk dari supplier"
    },
    {
      "barangId": 9,
      "jumlah": 10
    }
  ]
}

```

#### Response Body ( success ):
```json
{
  "pesan": "Laporan berhasil dibuat, ID = 2"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Buat Laporan RUSAK
### Endpoint : Post inventrix/laporan/create

- for buat laporan

#### Request Body :
```json
{
  "jenis": "RUSAK",
  "items": [
    {
      "barangId": 1,
      "jumlah": 3,
      "keterangan": "Rusak saat unloading"
    }
  ]
}

```

#### Response Body ( success ):
```json
{
  "pesan": "Laporan berhasil dibuat, ID = 2"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Buat Laporan HILANG
### Endpoint : Post inventrix/laporan/create

- for buat laporan

#### Request Body :
```json
{
  "jenis": "HILANG",
  "items": [
    {
      "barangId": 1,
      "jumlah": 3,
      "keterangan": "Rusak saat unloading"
    }
  ]
}

```

#### Response Body ( success ):
```json
{
  "pesan": "Laporan berhasil dibuat, ID = 2"
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Lihat list laporan
### Endpoint : Post inventrix/laporan/list

- nah dpe request body bole pilih salah satu atonda mo samua rupa ini, bisa juga cuma jenis, nda ada laeng

#### Request Body :
```json
{
  "jenis": "",
  "tanggalMulai": "",
  "tanggalSelesai": "",
  "namaBarang": "",
  "createdBy": "",
  "page": 0,
  "size": 10
}

```

#### Response Body ( success ):
```json
{
  "content": [
    {
      "id": 6,
      "jenis": "MASUK",
      "supplier": "PT Sinar Jaya",
      "createdBy": "owner",
      "tanggal": "2025-11-21T20:10:30.920686",
      "totalItem": 2
    },
    {
      "id": 5,
      "jenis": "HILANG",
      "supplier": null,
      "createdBy": "gudang",
      "tanggal": "2025-11-21T17:17:52.151574",
      "totalItem": 1
    },
    {
      "id": 4,
      "jenis": "RUSAK",
      "supplier": null,
      "createdBy": "gudang",
      "tanggal": "2025-11-21T17:00:18.019003",
      "totalItem": 1
    },
    {
      "id": 3,
      "jenis": "MASUK",
      "supplier": "PT Sinar Jaya",
      "createdBy": "owner",
      "tanggal": "2025-11-21T16:56:42.375158",
      "totalItem": 2
    },
    {
      "id": 2,
      "jenis": "MASUK",
      "supplier": "PT Sinar Jaya",
      "createdBy": "admin",
      "tanggal": "2025-11-21T15:54:05.009718",
      "totalItem": 2
    },
    {
      "id": 1,
      "jenis": "MASUK",
      "supplier": "PT Sinar Jaya",
      "createdBy": "admin",
      "tanggal": "2025-11-21T15:00:34.270381",
      "totalItem": 2
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 6,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 6,
  "empty": false
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```

## Lihat detail laporan
### Endpoint : GET inventrix/laporan/detail/{id}



#### Response Body ( success ):
```json
{
  "id": 1,
  "jenis": "MASUK",
  "supplier": "PT Sinar Jaya",
  "createdBy": "admin",
  "tanggal": "2025-11-21T15:00:34.270381",
  "items": [
    {
      "id": 1,
      "barangId": 8,
      "namaBarang": "bahav",
      "jumlah": 5,
      "keterangan": "Masuk dari supplier"
    },
    {
      "id": 2,
      "barangId": 9,
      "namaBarang": "vwhsv",
      "jumlah": 10,
      "keterangan": null
    }
  ]
}
```

#### Response Body ( Faild ):
```json
{
  "pesan": "error"
}
```