package br.com.inteligente.cadastro.api

import br.com.inteligente.cadastro.api.dtos.CadastroPjDto
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
@RequestMapping("/api/cadastrar-pj")
class CadastroPJRest(val empresaService: EmpresaService,
                     val funcionarioService:FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPjDto: CadastroPjDto,
                  result: BindingResult): ResponseEntity<Response<CadastroPjDto>> {
        val response: Response<CadastroPjDto> = Response<CadastroPjDto>()

        validarDadosExistentes( cadastroPjDto, result)
        if(result.hasErrors()){
            for(erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        var empresa: Empresa = converterDtoParaEmpresa(cadastroPjDto)
        empresa = empresaService.persistir(empresa)

        var funcionario: Funcionario = converterDtoParaFuncionario(cadastroPjDto, empresa)
        funcionario = funcionarioService.persistir(funcionario)
        response.data = converterCadastroPJDto(funcionario, empresa)

        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistentes(cadastroPjDto: CadastroPjDto, result: BindingResult){
        val empresa:Empresa? = empresaService.buscarPorCnpj(cadastroPjDto.cnpj)
        if(empresa != null){
            result.addError(ObjectError("empresa", "Empresa ja existente"))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPjDto.cpf)
        if(funcionarioCpf != null){
            result.addError(ObjectError("funcionario", "CPF já existente"))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorEmail(cadastroPjDto.email)
        if(funcionarioEmail != null){
            result.addError(ObjectError("funcionario", "Email ja existente"))
        }
    }

    private fun converterDtoParaEmpresa(cadastroPjDto: CadastroPjDto): Empresa =
            Empresa(cadastroPjDto.razaoSocial, cadastroPjDto.cnpj)

    private fun converterDtoParaFuncionario(cadastroPjDto: CadastroPjDto, empresa: Empresa) =
            Funcionario(cadastroPjDto.nome, cadastroPjDto.email,
            SenhaUtils().gerarBcrypt(cadastroPjDto.senha), cadastroPjDto.cpf,
            PerfilEnum.ROLE_ADMIN, empresa.id.toString())

    private fun converterCadastroPJDto(funcionario: Funcionario, empresa: Empresa): CadastroPjDto =
            CadastroPjDto(funcionario.nome, funcionario.email, "", funcionario.cpf,
            empresa.cnpj, empresa.razaoSocial, funcionario.id)
}