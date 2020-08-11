package br.com.inteligente.response

data class Response<T> (
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? null
)