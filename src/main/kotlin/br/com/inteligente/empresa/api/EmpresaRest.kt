package br.com.inteligente.empresa.api

import br.com.inteligente.empresa.Empresa
import br.com.inteligente.empresa.EmpresaService
import br.com.inteligente.empresa.api.dtos.EmpresaDto
import br.com.inteligente.response.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaRest (val empresaService: EmpresaService){

    @GetMapping(value = ["/cnpj/{cnpj}"])
    fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDto>>{
        val response: Response<EmpresaDto> = Response<EmpresaDto>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null){
            response.erros.add("Empresa não encontrada para CNPJ ${cnpj}")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterEmpresaDto(empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterEmpresaDto(empresa: Empresa): EmpresaDto =
            EmpresaDto(empresa.razaoSocial, empresa.cnpj, empresa.id)

}