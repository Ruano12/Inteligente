package br.com.inteligente

import br.com.inteligente.empresa.Empresa
import br.com.inteligente.empresa.EmpresaRepository
import br.com.inteligente.enums.PerfilEnum
import br.com.inteligente.funcionario.Funcionario
import br.com.inteligente.funcionario.FuncionarioRepository
import br.com.inteligente.lancamento.LancamentoRepository
import br.com.inteligente.util.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.validation.constraints.Email

@SpringBootApplication
class InteligenteApplication

fun main(args: Array<String>) {
	runApplication<InteligenteApplication>(*args)
}
