package com.fiap.techchallenge2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge2.model.dto.EstacionamentoIniciaDTO;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static com.fiap.techchallenge2.controller.EstacionamentoController.URL_INICIA;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class EstacionamentoIniciaTests {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@ParameterizedTest
	@MethodSource("requestValidandoCamposNullsOuVazios")
	public void deveRetornarStatus400_validacoesDosCamposNullsOuVazios(String placa) throws Exception {
		var request = new EstacionamentoIniciaDTO(placa);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_INICIA)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				).andReturn();
		Assertions.assertEquals("{\"placa\":\"A placa nao pode ser vazia\"}", response.getResponse().getContentAsString());
		//TODO: FAZER VALIDACOES NO BANCO
//		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String placa) throws Exception {
		var request = new EstacionamentoIniciaDTO(placa);
		var objectMapper = this.objectMapper
				.writer()
				.withDefaultPrettyPrinter();
		var jsonRequest = objectMapper.writeValueAsString(request);
		var response = this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_INICIA)
						.content(jsonRequest)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				).andReturn();
		Assertions.assertEquals("{\"placa\":\"A placa inserida nao esta no padrao antigo, nem no novo padrao\"}", response.getResponse().getContentAsString());
		//TODO: FAZER VALIDACOES NO BANCO
//		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCamposNullsOuVazios() {
		return Stream.of(
				Arguments.of("null"),
				Arguments.of(""),
				Arguments.of(" ")
		);
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("a"),
				Arguments.of("aaaaaaaaaa"),
				Arguments.of("abcd1234"),
				Arguments.of("ab1234"),
				Arguments.of("ABCD1234"),
				Arguments.of("AB1234"),
				Arguments.of("aBcD1234"),
				Arguments.of("Abcd1234"),
				Arguments.of("ABC12345"),
				Arguments.of("ABC123"),
				Arguments.of("1"),
				Arguments.of("1111111111"),
				Arguments.of("ABC-1234"),
				Arguments.of("abc12345"),
				Arguments.of("abc123"),
				Arguments.of("abc-1234"),
				Arguments.of("AbC12345"),
				Arguments.of("aBc123"),
				Arguments.of("ABC1DE23"),
				Arguments.of("ABC1D234"),
				Arguments.of("ABC1dd23"),
				Arguments.of("ABC1d234"),
				Arguments.of("ABC-1A23")
		);
	}

}
