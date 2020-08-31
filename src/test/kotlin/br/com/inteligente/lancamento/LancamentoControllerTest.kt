package br.com.inteligente.lancamento

import br.com.inteligente.enums.PerfilEnum
import br.com.inteligente.enums.TipoEnum
import br.com.inteligente.funcionario.Funcionario
import br.com.inteligente.funcionario.FuncionarioService
import br.com.inteligente.lancamento.api.dtos.LancamentoDto
import br.com.inteligente.util.SenhaUtils
import br.com.inteligente.util.JsonUtils
import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import java.text.SimpleDateFormat
import java.util.*


@Profile("teste")
@SpringBootTest
@AutoConfigureMockMvc
class LancamentoControllerTest {

    private var mvc: MockMvc? = null;

    @MockBean
    private val lancamentoService: LancamentoService? = null

    @MockBean
    private val funcionarioService: FuncionarioService? = null

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    @Autowired
    private val jsonUtilsUtil: JsonUtils? = null

    private val URL_BASE: String = "/api/lancamentos/"
    private val ID_FUNCIONARIO: String = "1"
    private val ID_LANCAMENTO: String = "1"
    private val TIPO: TipoEnum = TipoEnum.INICIO_TRABALHO
    private val DATA: Date = Date()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @BeforeEach
    fun setup() {
        this.mvc = webAppContextSetup(webApplicationContext!!).build();
    }

    @Test
    @Throws(Exception::class)
    fun testCadastrarLancamento(){
        val lancamento: Lancamento = obterDadosLancamento()
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(ID_FUNCIONARIO))
                .willReturn(funcionario())
        BDDMockito.given(lancamentoService?.persistir(anyObject()))
               .willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tipo").value(TIPO.name))
                .andExpect(jsonPath("$.data.data").value(dateFormat.format(DATA)))
                .andExpect(jsonPath("$.data.funcionario-id").value(ID_FUNCIONARIO))
                .andExpect(jsonPath("$.erros").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testCadastrarLancamentoFuncionarioIdInvalido() {
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(ID_FUNCIONARIO))
                .willReturn(null)

        mvc!!.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros").value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @WithMockUser(username = "admin@admin.com", roles = arrayOf("ADMIN"))
    fun testRemoverLancamento() {
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId(ID_LANCAMENTO))
                .willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.delete(URL_BASE+ID_LANCAMENTO)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }

    private fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

    @Throws(JsonProcessingException::class)
    private fun obterJsonRequisicaoPost():String {
        val lancamentoDto: LancamentoDto = LancamentoDto(
                dateFormat.format(DATA), TIPO.name, "Descrição",
                    "1.234,4.234", ID_FUNCIONARIO)

        return jsonUtilsUtil!!.toJson(lancamentoDto);
    }

    private fun obterDadosLancamento(): Lancamento =
            Lancamento(DATA, TIPO, ID_FUNCIONARIO,
                "Descrição", "11.234,4.234", ID_LANCAMENTO)

    private fun funcionario(): Funcionario =
            Funcionario("Nome", "email@email.com",
                    SenhaUtils().gerarBcrypt("123456"),
                    "23145699876", PerfilEnum.ROLE_USUARIO,
                    ID_FUNCIONARIO)
}