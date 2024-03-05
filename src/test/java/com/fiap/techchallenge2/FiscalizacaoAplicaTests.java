package com.fiap.techchallenge2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge2.model.Estacionamento;
import com.fiap.techchallenge2.model.FiscalizacaoAplicaEnum;
import com.fiap.techchallenge2.model.TempoPermanenciaEnum;
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
import java.util.List;
import java.util.stream.Stream;

import static com.fiap.techchallenge2.controller.FiscalizacaoController.URL_FISCALIZACAO_APLICA;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class FiscalizacaoAplicaTests {

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
	public void deveRetornarStatus200_multadoSemRegistroNoBancoDeDados() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(0);

		Assertions.assertEquals(1, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertNull(registroNoBancoDeDados.getEntrada());
		Assertions.assertNull(registroNoBancoDeDados.getSaida());
		Assertions.assertNull(registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_multadoComHoraVencida() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusHours(3);
		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataEntrada)
				.saida(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(false)
				.horaFiscalizacao(null)
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(0);

		Assertions.assertEquals(1, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertEquals(dataEntrada, registroNoBancoDeDados.getEntrada());
		Assertions.assertEquals(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()), registroNoBancoDeDados.getSaida());
		Assertions.assertEquals(new BigDecimal("5.00"), registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_naoMultado() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataMock)
				.saida(dataMock.plusMinutes(TempoPermanenciaEnum.DUAS_HORAS.getMinutos()))
				.valorPago(new BigDecimal("20.00"))
				.multado(false)
				.horaFiscalizacao(null)
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(0);

		Assertions.assertEquals(1, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.NAO_MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getEntrada());
		Assertions.assertEquals(dataMock.plusMinutes(TempoPermanenciaEnum.DUAS_HORAS.getMinutos()), registroNoBancoDeDados.getSaida());
		Assertions.assertEquals(new BigDecimal("20.00"), registroNoBancoDeDados.getValorPago());
		Assertions.assertFalse(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_naoMultadoHoraFiscalizacaoIgualAHoraSaida() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataMock.minusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.saida(dataMock)
				.valorPago(new BigDecimal("5.00"))
				.multado(false)
				.horaFiscalizacao(null)
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(0);

		Assertions.assertEquals(1, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.NAO_MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertEquals(dataMock.minusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()), registroNoBancoDeDados.getEntrada());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getSaida());
		Assertions.assertEquals(new BigDecimal("5.00"), registroNoBancoDeDados.getValorPago());
		Assertions.assertFalse(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_jaMultadoCarroNoMesmoDia() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusHours(3);
		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataEntrada)
				.saida(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(true)
				.horaFiscalizacao(dataEntrada.plusMinutes(60))
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(0);

		Assertions.assertEquals(1, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.JA_MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertEquals(dataEntrada, registroNoBancoDeDados.getEntrada());
		Assertions.assertEquals(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()), registroNoBancoDeDados.getSaida());
		Assertions.assertEquals(new BigDecimal("5.00"), registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataEntrada.plusMinutes(60), registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_multadoCarroJaMultadoEmOutroDia() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusDays(1);
		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataEntrada)
				.saida(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(true)
				.horaFiscalizacao(dataEntrada.plusMinutes(60))
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findAll().get(1);

		Assertions.assertEquals(2, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertNull(registroNoBancoDeDados.getEntrada());
		Assertions.assertNull(registroNoBancoDeDados.getSaida());
		Assertions.assertNull(registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_naoMultadoCarroComDoisRegistros() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusDays(2);
		var estacionamento1 = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataEntrada)
				.saida(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(false)
				.horaFiscalizacao(dataEntrada.plusMinutes(60))
				.build();

		var estacionamento2 = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataMock)
				.saida(dataMock.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(false)
				.horaFiscalizacao(null)
				.build();

		this.repository.saveAll(List.of(estacionamento1, estacionamento2));

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findTop1ByPlacaOrderByIdDesc("ABC1234");

		Assertions.assertEquals(2, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.NAO_MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getEntrada());
		Assertions.assertEquals(dataMock.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()), registroNoBancoDeDados.getSaida());
		Assertions.assertEquals(new BigDecimal("5.00"), registroNoBancoDeDados.getValorPago());
		Assertions.assertFalse(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_multadoCarroComMultaNoRegistroAnterior() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusDays(2);
		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(null)
				.saida(null)
				.valorPago(new BigDecimal("5.00"))
				.multado(true)
				.horaFiscalizacao(dataEntrada.plusMinutes(60))
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findTop1ByPlacaOrderByIdDesc("ABC1234");

		Assertions.assertEquals(2, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertNull(registroNoBancoDeDados.getEntrada());
		Assertions.assertNull(registroNoBancoDeDados.getSaida());
		Assertions.assertNull(registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}

	@Test
	public void deveRetornarStatus200_multadoCarroComSemMultaNoRegistroAnterior() throws Exception {
		var mockData = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
		var dataMock = LocalDateTime.of(2023, 11, 19, 14, 00, 00);
		mockData.when(LocalDateTime::now)
				.thenReturn(dataMock);

		var dataEntrada = dataMock.minusDays(2);
		var estacionamento = Estacionamento
				.builder()
				.placa("ABC1234")
				.entrada(dataEntrada)
				.saida(dataEntrada.plusMinutes(TempoPermanenciaEnum.MEIA_HORA.getMinutos()))
				.valorPago(new BigDecimal("5.00"))
				.multado(false)
				.horaFiscalizacao(null)
				.build();

		this.repository.save(estacionamento);

		var response = this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", "ABC1234"))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(MockMvcResultMatchers
						.status()
						.isOk()
				)
				.andReturn();
		var responseAppString = response.getResponse().getContentAsString();
		var responseApp = this.objectMapper
				.readValue(responseAppString, FiscalizacaoAplicaEnum.class);

		var registroNoBancoDeDados = this.repository.findTop1ByPlacaOrderByIdDesc("ABC1234");

		Assertions.assertEquals(2, this.repository.findAll().size());
		Assertions.assertEquals(FiscalizacaoAplicaEnum.MULTADO, responseApp);
		Assertions.assertEquals("ABC1234", registroNoBancoDeDados.getPlaca());
		Assertions.assertNull(registroNoBancoDeDados.getEntrada());
		Assertions.assertNull(registroNoBancoDeDados.getSaida());
		Assertions.assertNull(registroNoBancoDeDados.getValorPago());
		Assertions.assertTrue(registroNoBancoDeDados.isMultado());
		Assertions.assertEquals(dataMock, registroNoBancoDeDados.getHoraFiscalizacao());

		mockData.close();
	}


	@ParameterizedTest
	@MethodSource("requestValidandoCampos")
	public void deveRetornarStatus400_validacoesDosCampos(String placa) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.put(URL_FISCALIZACAO_APLICA.replace("{placa}", placa))
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
				Arguments.of("   "),
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
