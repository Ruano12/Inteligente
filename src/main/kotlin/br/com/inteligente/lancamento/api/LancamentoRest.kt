package br.com.inteligente.lancamento.api

import br.com.inteligente.enums.TipoEnum
import br.com.inteligente.funcionario.Funcionario
import br.com.inteligente.funcionario.FuncionarioService
import br.com.inteligente.lancamento.Lancamento
import br.com.inteligente.lancamento.LancamentoService
import br.com.inteligente.lancamento.api.dtos.LancamentoDto
import br.com.inteligente.response.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoRest(val lancamentoService: LancamentoService,
                     val funcionarioService: FuncionarioService){

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto,
                    result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        validarFuncionario(lancamentoDto, result)

        if(result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        var lancamento:Lancamento = converterDtoParaLancamento(lancamentoDto, result)
        lancamento = lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = ["/{id}"])
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if(Objects.isNull(lancamento)){
            response.erros.add("Lançamento não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoDto(lancamento!!)
        return ResponseEntity.ok(response)
    }

    @GetMapping(value = ["/funcionario/{funcionarioId}"])
    fun listarPorFuncionarioId(@PathVariable("funcionarioId") funcionarioId: String,
                                @RequestParam(value = "pag", defaultValue = "0") pag:Int,
                                @RequestParam(value = "ord", defaultValue = "id") ord: String,
                                @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
            ResponseEntity<Response<List<LancamentoDto>>>{

        val response: Response<List<LancamentoDto>> = Response<List<LancamentoDto>>()

        val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lancamentos: Page<Lancamento> =
                lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

        val lancamentosDto: List<LancamentoDto> =
                lancamentos.toList().map { lancamento -> converterLancamentoDto(lancamento) }

        response.data = lancamentosDto
        response.totalItems = lancamentos.totalElements.toInt()
        return ResponseEntity.ok(response)
    }

    @PutMapping(value = ["/{id}"])
    fun atualizar(@PathVariable("id") id:String, @Valid @RequestBody lancamentoDto: LancamentoDto,
                  result: BindingResult): ResponseEntity<Response<LancamentoDto>> {

        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        println(lancamentoDto)
        validarFuncionario(lancamentoDto, result)
        lancamentoDto.id = id
        var lancamento: Lancamento = converterDtoParaLancamento(lancamentoDto, result)

        if(result.hasErrors()) {
            for(erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        lancamento = lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping(value = ["/{id}"])
    fun remover(@PathVariable("id") id:String): ResponseEntity<Response<String>> {
        val response: Response<String> = Response<String>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if(lancamento == null) {
            response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)
        return ResponseEntity.ok(response)
    }

    private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if(lancamentoDto.funcionarioId == null){
            result.addError(ObjectError("funcionario", "Funcionário não informado."))

            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if(funcionario == null){
            result.addError(ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."))
        }
    }

    private fun converterDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {
        if(lancamentoDto.id != null){
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDto.id!!)
            if(lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado"))
        }

        return Lancamento(dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!),
                lancamentoDto.funcionarioId!!, lancamentoDto.descricao,
                lancamentoDto.localizacao, lancamentoDto.id)
    }

    private fun converterLancamentoDto(lancamento: Lancamento): LancamentoDto =
            LancamentoDto(dateFormat.format(lancamento.data), lancamento.tipo.toString(),
            lancamento.descricao, lancamento.localizacao,
            lancamento.funcionarioId, lancamento.id)

}