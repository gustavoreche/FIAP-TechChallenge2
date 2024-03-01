package com.fiap.techchallenge2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.fiap.techchallenge2.controller.EstacionamentoController.URL_FINALIZA;
import static com.fiap.techchallenge2.controller.FiscalizacaoController.URL_FISCALIZACAO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FiscalizacaoTests {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String placa,
														  String diaEHoraInicio,
														  String diaEHoraFim) throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(URL_FISCALIZACAO)
				.contentType(MediaType.APPLICATION_JSON);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("placa", placa)
						.queryParam("diaEHoraInicio", diaEHoraInicio)
						.queryParam("diaEHoraFim", diaEHoraFim)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				).andReturn();
		//TODO: FAZER VALIDACOES NO BANCO
//		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("   ", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("a", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("aaaaaaaaaa", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("abcd1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ab1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABCD1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("AB1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("aBcD1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("Abcd1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC12345", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC123", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("1", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("1111111111", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC-1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("abc12345", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("abc123", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("abc-1234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("AbC12345", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("aBc123", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC1DE23", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC1D234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC1dd23", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC1d234", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC-1A23", LocalDateTime.now().toString(), LocalDateTime.now().toString())
		);
	}

}
