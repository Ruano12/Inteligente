package br.com.inteligente.lancamento

import br.com.inteligente.enums.TipoEnum
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.*
import kotlin.collections.ArrayList

@Profile("teste")
@SpringBootTest
class LancamentoServiceTest {

    @MockBean
    private val lancamentoRepository: LancamentoRepository? = null

    @Autowired
    private val lancamentoService: LancamentoService? = null

    private val ID: String = "1"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp(){
        BDDMockito
                .given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId(ID, PageRequest.of(0, 10)))
                .willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById(ID)).willReturn(Optional.of(lancamento()))
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java)))
                .willReturn(lancamento())
    }

    @Test
    fun testBuscarLancamentoPorFuncionarioId() {
        val lancamento: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(ID, PageRequest.of(0, 10))
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testBuscarLancamentoPorId() {
        val lancamento: Lancamento? = lancamentoService?.buscarPorId(ID)
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testPersistirLancamento(){
        val lancamento: Lancamento? = lancamentoService?.persistir(lancamento())
        Assertions.assertNotNull(lancamento)
    }

    private fun lancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_TRABALHO, ID)
}