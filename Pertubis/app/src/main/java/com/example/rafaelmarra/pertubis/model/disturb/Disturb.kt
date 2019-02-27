package com.example.rafaelmarra.pertubis.model.disturb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "disturbs")
data class Disturb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long?,
    @ColumnInfo(name = "nome") var nome: String?,
    @ColumnInfo(name = "como") var como: String?,
    @ColumnInfo(name = "data") var data: String?,
    @ColumnInfo(name = "hora") var hora: String?,
    @ColumnInfo(name = "cep") var cep: String?,
    @ColumnInfo(name = "endereco") var endereco: String?,
    @ColumnInfo(name = "bairro") var bairro: String?,
    @ColumnInfo(name = "cidade") var cidade: String?,
    @ColumnInfo(name = "estado") var estado: String?,
    @ColumnInfo(name = "numero") var numero: String?,
    @ColumnInfo(name = "complemento") var complemento: String?
) : Serializable {

    constructor() : this(
        null,
        "",
        "",
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}