package com.example.rafaelmarra.pertubis.model.cep

import com.google.gson.annotations.SerializedName

data class Cep(

    @SerializedName("cep")
    val cep: String,

    @SerializedName("logradouro")
    val endereco: String,

    @SerializedName("bairro")
    val bairro: String,

    @SerializedName("localidade")
    val cidade: String,

    @SerializedName("uf")
    val estado: String,

    @SerializedName("erro")
    val erro  : Boolean
)