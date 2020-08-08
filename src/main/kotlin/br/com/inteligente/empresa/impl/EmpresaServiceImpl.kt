package br.com.inteligente.empresa.impl

import br.com.inteligente.empresa.Empresa
import br.com.inteligente.empresa.EmpresaRepository
import br.com.inteligente.empresa.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository): EmpresaService {

    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)

}