package br.com.inteligente.funcionario.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class FuncionarioDto (

        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Size(min = 3, max = 200, message = "Nomae deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Size(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message = "Email Inválido.")
        val email:String = "",

        val senha:String? = null,

        @JsonProperty("valor-hora")
        val valorHora: String? = null,

        @JsonProperty("qtd-horas-trabalho-dia")
        val qtdHorasTrabalhoDia: String? = null,

        @JsonProperty("qtd-horas-almoco")
        val qtdHorasAlmoco: String? = null,
        val id:String? = null

)