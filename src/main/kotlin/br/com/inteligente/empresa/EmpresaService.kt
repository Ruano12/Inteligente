package br.com.inteligente.empresa

interface EmpresaService  {

    fun buscarPorCnpj(cnpj: String): Empresa?

    fun persistir(empresa: Empresa): Empresa
}