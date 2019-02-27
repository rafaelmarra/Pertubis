package com.example.rafaelmarra.pertubis.model.cep

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("{CEP}/json/")
    fun getCep(@Path("CEP") CEP : String) : Call<Cep>
}