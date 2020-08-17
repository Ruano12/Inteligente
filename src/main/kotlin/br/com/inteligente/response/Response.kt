package br.com.inteligente.response

import com.fasterxml.jackson.annotation.JsonInclude

data class Response<T> (
        val erros: ArrayList<String> = arrayListOf(),
        var data: T? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var totalItems: Int? = null
)