package br.com.inteligente.cadastro.api

import br.com.inteligente.cadastro.api.dtos.CadastroPfDto
import br.com.inteligente.empresa.Empresa
import br.com.inteligente.empresa.EmpresaService
import br.com.inteligente.enums.PerfilEnum
import br.com.inteligente.funcionario.Funcionario
import br.com.inteligente.funcionario.FuncionarioService
import br.com.inteligente.response.Response
import br.com.inteligente.util.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPFRest(val empresaService: EmpresaService,
                     val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPfDto: CadastroPfDto,
                  result: BindingResult): ResponseEntity<Response<CadastroPfDto>> {
        val response: Response<CadastroPfDto> = Response<CadastroPfDto>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPfDto.cnpj)
        validarDadosExistentes(cadastroPfDto, empresa, result)

        if (result.hasErrors()) {
            for (erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        var funcionario: Funcionario =  converterDtoParaFuncionario(cadastroPfDto, empresa!!)

        funcionario = funcionarioService.persistir(funcionario)
        response.data = converterCadastroPFDto(funcionario, empresa!!)
        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistentes(cadastroPfDto: CadastroPfDto, empresa: Empresa?,
                                        result: BindingResult){

        if(empresa == null){
            result.addError(ObjectError("empresa", "Empresa não cadastrada"))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPfDto.cpf)
        if(funcionarioCpf != null){
            result.addError(ObjectError("funcionario", "CPF já existente"))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPfDto.email)
        if(funcionarioEmail != null){
            result.addError(ObjectError("funcionario", "Email ja existente"))
        }
    }

    private fun converterDtoParaFuncionario(cadastroPfDto: CadastroPfDto, empresa: Empresa) =
            Funcionario(cadastroPfDto.nome, cadastroPfDto.email,
            SenhaUtils().gerarBcrypt(cadastroPfDto.senha), cadastroPfDto.cpf,
            PerfilEnum.ROLE_USUARIO, empresa.id.toString(),
            cadastroPfDto.valorHora?.toDouble(), cadastroPfDto.qtdHorasTrabalhoDia?.toFloat(),
            cadastroPfDto.qtdHorasAlmoco?.toFloat(), cadastroPfDto.id)


    private fun converterCadastroPFDto(funcionario: Funcionario, empresa: Empresa): CadastroPfDto =
            CadastroPfDto(funcionario.nome, funcionario.email, "", funcionario.cpf,
                        empresa.cnpj, empresa.id.toString(), funcionario.valorHora.toString(),
                        funcionario.qtdHorasTrabalhoDia.toString(),
                        funcionario.qtdHorasTrabalhoDia.toString(),
                        funcionario.id)
}