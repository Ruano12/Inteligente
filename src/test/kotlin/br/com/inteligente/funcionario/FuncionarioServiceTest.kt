package br.com.inteligente.funcionario

import br.com.inteligente.enums.PerfilEnum
import br.com.inteligente.util.SenhaUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Profile
import java.util.*

@Profile("teste")
@SpringBootTest
class FuncionarioServiceTest {

    @MockBean
    private val funcionarioRepository: FuncionarioRepository? = null

    @Autowired
    val funcionarioService: FuncionarioService? = null

    private val NOME: String = "Marcelo Benjamim"
    private val EMAIL: String = "Marcelo.benjamim@gmail.com"
    private val CPF: String = "09273534074"
    private val ID:String = "1"
    private val SENHA = "123456"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp(){
        val optFuncionario: Optional<Funcionario> = Optional.of(funcionario())

        BDDMockito.given(funcionarioRepository?.save(Mockito.any(Funcionario::class.java)))
                .willReturn(funcionario())
        BDDMockito.given(funcionarioRepository?.findById(ID)).willReturn(optFuncionario)
        BDDMockito.given(funcionarioRepository?.findByEmail(EMAIL)).willReturn(funcionario())
        BDDMockito.given(funcionarioRepository?.findByCpf(CPF)).willReturn(funcionario())
    }

    @Test
    fun testPersistirFuncionario() {
        val funcionario: Funcionario? = this.funcionarioService?.persistir(funcionario())
        Assertions.assertNotNull(funcionario)
    }

    @Test
    fun testBuscarFuncionarioPorId() {
        val funcionario: Funcionario? = this.funcionarioService?.buscarPorId(ID)
        Assertions.assertNotNull(funcionario)
    }

    @Test
    fun testBuscarFuncionarioPorEmail() {
        val funcionario: Funcionario? = this.funcionarioService?.buscarPorEmail(EMAIL)
        Assertions.assertNotNull(funcionario)
    }

    @Test
    fun testBuscarFuncionarioPorCpf() {
        val funcionario: Funcionario? = this.funcionarioService?.buscarPorCpf(CPF)
        Assertions.assertNotNull(funcionario)
    }

    private fun funcionario(): Funcionario =
            Funcionario(NOME, EMAIL, SenhaUtils().gerarBcrypt(SENHA), CPF,
                PerfilEnum.ROLE_USUARIO, ID)
}