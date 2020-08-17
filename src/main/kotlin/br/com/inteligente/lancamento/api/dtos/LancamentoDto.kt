package br.com.inteligente.lancamento.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class LancamentoDto (

        @get:NotEmpty(message = "Data não pode ser vazia.")
        val data: String? = null,

        @get:NotEmpty(message = "Tipo não pode ser vazio")
        val tipo: String? = null,

        val descricao: String? = null,
        val localizacao: String? = null,

        @JsonProperty("funcionario-id")
        val funcionarioId: String? = null,
        var id: String? = null


)