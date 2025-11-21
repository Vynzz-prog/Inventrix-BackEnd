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