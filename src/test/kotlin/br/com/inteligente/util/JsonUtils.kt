package br.com.inteligente.util

import br.com.inteligente.lancamento.api.dtos.LancamentoDto
import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.mock.http.MockHttpOutputMessage

@Configuration
class JsonUtils {

    @SuppressWarnings("rawtypes")
    @Autowired
    private var mappingJackson2HttpMessageConverter: HttpMessageConverter<Any>? = null

    @Throws(JsonProcessingException::class)
    fun toJson(o:Any):String {
        var mockHttpOutputMessage = MockHttpOutputMessage()
        mappingJackson2HttpMessageConverter!!.write(o,
                MediaType.APPLICATION_JSON, mockHttpOutputMessage)

        return mockHttpOutputMessage.getBodyAsString();
    }
}