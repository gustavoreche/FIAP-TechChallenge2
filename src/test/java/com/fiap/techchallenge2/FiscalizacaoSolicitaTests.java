package com.fiap.techchallenge2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge2.model.Estacionamento;
import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.model.dto.RelatorioFiscalizacaoDTO;
import com.fiap.techchallenge2.repository.EstacionamentoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge2.controller.FiscalizacaoController.URL_FISCALIZACAO;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FiscalizacaoSolicitaTests {

    @Autowired
    private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	EstacionamentoRepository repository;

	@BeforeEach
	void inicializaLimpezaDoDatabase() {
		this.repository.deleteAll();
	}

	@AfterAll
	void finalizaLimpezaDoDatabase() {
		this.repository.deleteAll();
	}


	@Test
	public void deveRetornarStatus200_periodoDe30Dias() throws Exception {
		var estacionamento1 = Estacionamento.builder()
				.placa("ABC1234")
				.entrada(LocalDateTime.of(2022, 12, 13, 00, 00))
				.saida(LocalDateTime.of(2022, 12, 13, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento2 = Estacionamento.builder()
				.placa("DEF1234")
				.entrada(LocalDateTime.of(2023, 01, 15, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 15, 02, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento3 = Estacionamento.builder()
				.placa("GHI1234")
				.entrada(LocalDateTime.of(2023, 01, 20, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 20, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		this.repository.saveAll(List.of(estacionamento1, estacionamento2, estacionamento3));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("diaEHoraInicio", LocalDateTime.of(2023, 01, 13, 00, 00).toString())
						.queryParam("diaEHoraFim", LocalDateTime.of(2023, 02, 12, 00, 00).toString())
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseAppArray = this.objectMapper
				.readValue(responseAppString, RelatorioFiscalizacaoDTO[].class);
		List<RelatorioFiscalizacaoDTO> responseApp = Arrays.stream(responseAppArray).toList();

		Assertions.assertEquals(3, this.repository.findAll().size());
		Assertions.assertEquals(2, responseApp.size());
	}

	@Test
	public void deveRetornarStatus200_ultimos15DiasSemPassarParametroDeDatas() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 01, 25, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var estacionamento1 = Estacionamento.builder()
				.placa("ABC1234")
				.entrada(LocalDateTime.of(2022, 12, 13, 00, 00))
				.saida(LocalDateTime.of(2022, 12, 13, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento2 = Estacionamento.builder()
				.placa("DEF1234")
				.entrada(LocalDateTime.of(2023, 01, 15, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 15, 02, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento3 = Estacionamento.builder()
				.placa("GHI1234")
				.entrada(LocalDateTime.of(2023, 01, 20, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 20, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		this.repository.saveAll(List.of(estacionamento1, estacionamento2, estacionamento3));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseAppArray = this.objectMapper
				.readValue(responseAppString, RelatorioFiscalizacaoDTO[].class);
		List<RelatorioFiscalizacaoDTO> responseApp = Arrays.stream(responseAppArray).toList();

		Assertions.assertEquals(3, this.repository.findAll().size());
		Assertions.assertEquals(2, responseApp.size());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_passandoSomenteDataInicioESemDataFinal() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 01, 25, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var estacionamento1 = Estacionamento.builder()
				.placa("ABC1234")
				.entrada(LocalDateTime.of(2022, 12, 13, 00, 00))
				.saida(LocalDateTime.of(2022, 12, 13, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento2 = Estacionamento.builder()
				.placa("DEF1234")
				.entrada(LocalDateTime.of(2023, 01, 15, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 15, 02, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento3 = Estacionamento.builder()
				.placa("GHI1234")
				.entrada(LocalDateTime.of(2023, 01, 20, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 20, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		this.repository.saveAll(List.of(estacionamento1, estacionamento2, estacionamento3));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("diaEHoraInicio", LocalDateTime.of(2023, 01, 13, 00, 00).toString())
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseAppArray = this.objectMapper
				.readValue(responseAppString, RelatorioFiscalizacaoDTO[].class);
		List<RelatorioFiscalizacaoDTO> responseApp = Arrays.stream(responseAppArray).toList();

		Assertions.assertEquals(3, this.repository.findAll().size());
		Assertions.assertEquals(2, responseApp.size());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_passandoPlaca() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 01, 25, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var estacionamento1 = Estacionamento.builder()
				.placa("ABC1234")
				.entrada(LocalDateTime.of(2022, 12, 13, 00, 00))
				.saida(LocalDateTime.of(2022, 12, 13, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento2 = Estacionamento.builder()
				.placa("DEF1234")
				.entrada(LocalDateTime.of(2023, 01, 15, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 15, 02, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		var estacionamento3 = Estacionamento.builder()
				.placa("GHI1234")
				.entrada(LocalDateTime.of(2023, 01, 20, 00, 00))
				.saida(LocalDateTime.of(2023, 01, 20, 01, 00))
				.horaFiscalizacao(null)
				.multado(false)
				.valorPago(new BigDecimal("5.00"))
				.build();
		this.repository.saveAll(List.of(estacionamento1, estacionamento2, estacionamento3));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("placa", "DEF1234")
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				).andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseAppArray = this.objectMapper
				.readValue(responseAppString, RelatorioFiscalizacaoDTO[].class);
		List<RelatorioFiscalizacaoDTO> responseApp = Arrays.stream(responseAppArray).toList();

		Assertions.assertEquals(3, this.repository.findAll().size());
		Assertions.assertEquals(1, responseApp.size());

		mockData.close();
	}

	@ParameterizedTest
	@MethodSource("requestPeriodoMaiorQue30Dias")
	public void deveRetornarStatus500_periodoMaiorQue30Dias(String diaEHoraInicio,
															String diaEHoraFim) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("diaEHoraInicio", diaEHoraInicio)
						.queryParam("diaEHoraFim", diaEHoraFim)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				).andReturn();
		Assertions.assertEquals(0, this.repository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestDataInicioMaiorQueDataFim")
	public void deveRetornarStatus500_dataInicioMaiorQueDataFim(String diaEHoraInicio,
																String diaEHoraFim) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get(URL_FISCALIZACAO)
						.queryParam("diaEHoraInicio", diaEHoraInicio)
						.queryParam("diaEHoraFim", diaEHoraFim)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isInternalServerError()
				).andReturn();
		Assertions.assertEquals(0, this.repository.findAll().size());
	}

	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String placa,
														  String diaEHoraInicio,
														  String diaEHoraFim) throws Exception {
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
		Assertions.assertEquals(0, this.repository.findAll().size());
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
				Arguments.of("ABC-1A23", LocalDateTime.now().toString(), LocalDateTime.now().toString()),
				Arguments.of("ABC1A23", "2024-03-03", LocalDateTime.now().toString()),
				Arguments.of("ABC1A23", "19:13:15.724979663", LocalDateTime.now().toString()),
				Arguments.of("ABC1A23", "texto", LocalDateTime.now().toString()),
				Arguments.of("ABC1A23", LocalDateTime.now().toString(), "2024-03-03"),
				Arguments.of("ABC1A23", LocalDateTime.now().toString(), "19:13:15.724979663"),
				Arguments.of("ABC1A23", LocalDateTime.now().toString(), "texto")
		);
	}

	private static Stream<Arguments> requestPeriodoMaiorQue30Dias() {
		return Stream.of(
				Arguments.of(LocalDateTime.of(2023, 01, 13, 00, 00).toString(),
						LocalDateTime.of(2023, 02, 13, 00, 00).toString()),
				Arguments.of(LocalDateTime.of(2023, 01, 13, 00, 00).toString(),
						"")
		);
	}

	private static Stream<Arguments> requestDataInicioMaiorQueDataFim() {
		return Stream.of(
				Arguments.of(LocalDateTime.of(2023, 01, 15, 00, 00).toString(),
						LocalDateTime.of(2023, 01, 14, 00, 00).toString()),
				Arguments.of("",
						LocalDateTime.now().minusDays(16).toString()),
				Arguments.of(LocalDateTime.of(2023, 01, 15, 01, 00).toString(),
						LocalDateTime.of(2023, 01, 15, 00, 00).toString()),
				Arguments.of(LocalDateTime.of(2023, 01, 15, 00, 01).toString(),
						LocalDateTime.of(2023, 01, 15, 00, 00).toString()),
				Arguments.of(LocalDateTime.of(2023, 01, 15, 00, 00, 01).toString(),
						LocalDateTime.of(2023, 01, 15, 00, 00, 00).toString())
		);
	}

}
