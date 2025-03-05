package junit5Tests.funcionalTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ingressos.service.Gerenciador;
import com.ingressos.models.Lote;
import com.ingressos.enums.TipoIngresso;

@DisplayName("Testes Funcionais do Gerenciador utilizando Particionamento por Equivalência")
public class GerenciadorTestPE {

    private Gerenciador gerenciador;

    @BeforeEach
    public void setUp() {
        gerenciador = new Gerenciador();
    }

    @ParameterizedTest
    @DisplayName("Falha ao criar show com dados inválidos")
    @NullSource
    void testCriarShowComDadosInvalidos(String valorInvalido) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow(valorInvalido, "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Falha ao criar show com valores numéricos inválidos")
    @ValueSource(doubles = { -100.0, -30.0, 0.0 })
    void testCriarShowComValoresInvalidos(Double valor) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", valor, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Falha ao vender ingressos com quantidade inválida")
    @ValueSource(ints = { 0, -1, -10 })
    void testVendaIngressosQuantidadeInvalida(int quantidade) {
        gerenciador.criarShow("Artista C", "2025-10-05", 1500.0, 800.0, false);
        Lote lote = new Lote(100, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista C", "2025-10-05", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista C", "2025-10-05", TipoIngresso.NORMAL, quantidade);
        });
        assertTrue(exception.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Falha ao criar show duplicado")
    void testCriarShowDuplicado() {
        gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("O show já está registrado no sistema.", exception.getMessage());
    }

    @Test
    @DisplayName("Venda de ingressos válida")
    void testVendaIngressosValida() {
        gerenciador.criarShow("Artista B", "2025-11-10", 1500.0, 800.0, false);
        Lote lote = new Lote(100, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista B", "2025-11-10", lote);

        assertDoesNotThrow(() -> gerenciador.venderIngressos("Artista B", "2025-11-10", TipoIngresso.NORMAL, 30));
        long vendidos = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL && i.isVendido())
                .count();
        assertEquals(30, vendidos);
    }

    @Test
    @DisplayName("Falha ao vender ingressos acima do disponível")
    void testVendaIngressosQuantidadeMaiorQueDisponivel() {
        gerenciador.criarShow("Artista D", "2025-09-15", 1500.0, 800.0, false);
        Lote lote = new Lote(50, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista D", "2025-09-15", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista D", "2025-09-15", TipoIngresso.NORMAL, 60);
        });
        assertTrue(exception.getMessage().contains("Não há ingressos suficientes"));
    }

    @Test
    @DisplayName("Falha ao vender ingressos com tipo nulo")
    void testVendaIngressosTipoNulo() {
        gerenciador.criarShow("Artista E", "2025-08-20", 1500.0, 800.0, false);
        Lote lote = new Lote(50, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista E", "2025-08-20", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista E", "2025-08-20", null, 10);
        });
        assertTrue(exception.getMessage().contains("Tipo de ingresso inválido"));
    }
}