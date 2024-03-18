package com.example.gamevault.SQLite

data class Gamemodel(
    var id: Int = 0,
    var titulo: String = "",
    var distribuidora: String = "",
    var notaMetacritic: Float = 0.0f,
    var anoLancamento: Int = 0,
    var foto: ByteArray = byteArrayOf(),
    var miniTrailer: String = "",
    var resumo: String = "",
    var tempoEstimado: Int = 0
)
