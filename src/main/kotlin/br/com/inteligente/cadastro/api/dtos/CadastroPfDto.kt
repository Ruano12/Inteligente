package br.com.inteligente.cadastro.api.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class CadastroPfDto(
        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Size(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio")
        @get:Size(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        @get:NotEmpty(message = "Senha não pode ser vazia.")
        val senha: String = "",

        @get:NotEmpty(message = "CPF não pode ser vazio.")
        @get:CPF(message = "CNPJ inválido.")
        val cpf: String = "",

        @get:NotEmpty(message = "CNPJ não pode ser vazio.")
        @get:CNPJ(message = "CNPJ inválido.")
        val cnpj: String = "",

        @JsonProperty("empresa-id")
        val empresaId: String = "",

        @JsonProperty("valor-hora")
        val valorHora: String? = null,

        @JsonProperty("qtd-horas-trabalho-dia")
        val qtdHorasTrabalhoDia: String? = null,

        @JsonProperty("qtd-horas-almoco")
        val qtdHorasAlmoco: String? = null,
        val id: String? = null
)