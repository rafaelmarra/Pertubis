package com.example.rafaelmarra.pertubis.model.cep

interface ServiceListener {

    fun onSucess(obtained: Any)

    fun onError(throwable: Throwable)
}