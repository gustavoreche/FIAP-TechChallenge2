package com.fiap.techchallenge2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge2.model.dto.EstacionamentoIniciaDTO;
import com.fiap.techchallenge2.model.dto.TempoPermanenciaEnum;
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
	public void deveRetornarStatus400_validacoesDosCamposNullsOuVazios(String placa,
																	   String tempo) throws Exception {
		var request = """
				{
					"placa": %s,
					"tempo": %s
				}
				""".formatted(placa, tempo);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post(URL_INICIA)
						.content(request)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers
						.status()
						.isBadRequest()
				);
		//TODO: FAZER VALIDACOES NO BANCO
//		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String placa,
														  TempoPermanenciaEnum tempo) throws Exception {
		var request = new EstacionamentoIniciaDTO(placa, tempo);
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
		//TODO: FAZER VALIDACOES NO BANCO
//		Assertions.assertEquals(0, this.filaAtendimentoRepository.findAll().size());
//		Assertions.assertEquals(0, this.leadRepository.findAll().size());
	}

	private static Stream<Arguments> requestValidandoCamposNullsOuVazios() {
		return Stream.of(
				Arguments.of(null, TempoPermanenciaEnum.MEIA_HORA.name()),
				Arguments.of("", TempoPermanenciaEnum.MEIA_HORA.name()),
				Arguments.of("ABC1234", null),
				Arguments.of("ABC1234", "1"),
				Arguments.of("ABC1234", "texto"),
				Arguments.of("ABC1234", ""),
				Arguments.of(null, null),
				Arguments.of(null, ""),
				Arguments.of("", null),
				Arguments.of("", "")
		);
	}

	private static Stream<Arguments> requestValidandoCampos() {
		return Stream.of(
				Arguments.of("   ", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("a", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("aaaaaaaaaa", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("abcd1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ab1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABCD1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("AB1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("aBcD1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("Abcd1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC12345", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC123", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("1", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("1111111111", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC-1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("abc12345", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("abc123", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("abc-1234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("AbC12345", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("aBc123", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC1DE23", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC1D234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC1dd23", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC1d234", TempoPermanenciaEnum.MEIA_HORA),
				Arguments.of("ABC-1A23", TempoPermanenciaEnum.MEIA_HORA)
		);
	}

}
