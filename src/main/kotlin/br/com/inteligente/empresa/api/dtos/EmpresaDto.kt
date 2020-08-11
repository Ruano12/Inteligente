package br.com.inteligente.empresa.api.dtos

data class EmpresaDto (
        val razaoSocial: String,
        val cnpj: String,
        val id:String? = null
)