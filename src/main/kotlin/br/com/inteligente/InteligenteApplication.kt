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
class InteligenteApplication(val empresaRepository: EmpresaRepository,
							 val funcionarioRepository: FuncionarioRepository,
							 val lancamentoRepository: LancamentoRepository): CommandLineRunner{

	override fun run(vararg args: String?){
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		var empresa: Empresa = Empresa("Empresa", "35360040000129")
		empresa = empresaRepository.save(empresa)

		var admin: Funcionario = Funcionario("Admin", "admin@empresa.com",
					SenhaUtils().gerarBcrypt("123456"), "52272453040",
					PerfilEnum.ROLE_ADMIN, empresa.id!!)
		admin = funcionarioRepository.save(admin)

		var funcionario: Funcionario = Funcionario("Funcionario",
						"funcionario@empresa.com", SenhaUtils().gerarBcrypt("123456"),
						"46497736018", PerfilEnum.ROLE_USUARIO, empresa.id!!)
		funcionario = funcionarioRepository.save(funcionario)

		println("Empresa ID: "+empresa.id)
		println("Admin ID: "+ admin.id)
		println("Funcionario ID "+funcionario.id)

	}
}

fun main(args: Array<String>) {
	runApplication<InteligenteApplication>(*args)
}
