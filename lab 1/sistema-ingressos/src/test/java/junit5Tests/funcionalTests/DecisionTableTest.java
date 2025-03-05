package junit5Tests.funcionalTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.ingressos.service.Gerenciador;
import com.ingressos.models.*;
import com.ingressos.enums.StatusFinanceiro;

@DisplayName("Testes de Tabela de Decisão para Gerenciador de Shows")
public class DecisionTableTest {

    private Gerenciador gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new Gerenciador();
    }

    @Nested
    @DisplayName("Testes de criação de Show")
    class CriacaoShowTests {

        @Test
        @DisplayName("Criar show válido")
        void testCriarShow_Valido() {
            Show show = gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, true);
            assertNotNull(show);
            assertEquals("Artista A", show.getArtista());
            assertEquals("2025-12-25", show.getData());
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Falha ao criar show com artista nulo")
        void testCriarShow_ArtistaNulo(String artista) {
            Exception ex = assertThrows(IllegalArgumentException.class,
                    () -> gerenciador.criarShow(artista, "2025-12-25", 2000.0, 1000.0, false));
            assertEquals("Dados inválidos para criar o show", ex.getMessage());
        }

        @ParameterizedTest
        @CsvSource({ "-100.0", "0.0" })
        @DisplayName("Falha ao criar show com despesas inválidas")
        void testCriarShow_DespesasInvalidas(double despesas) {
            Exception ex = assertThrows(IllegalArgumentException.class,
                    () -> gerenciador.criarShow("Artista A", "2025-12-25", despesas, 1000.0, false));
            assertEquals("Dados inválidos para criar o show", ex.getMessage());
        }

        @Test
        @DisplayName("Falha ao criar show duplicado")
        void testCriarShow_Duplicado() {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
            Exception ex = assertThrows(IllegalArgumentException.class, () -> {
                gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
            });
            assertEquals("O show já está registrado no sistema.", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de criação de Lote")
    class CriacaoLoteTests {

        @ParameterizedTest
        @CsvSource({ "0", "-10" })
        @DisplayName("Falha ao criar lote com quantidade inválida")
        void testCriarLote_QuantidadeInvalida(int quantidade) {
            Exception ex = assertThrows(IllegalArgumentException.class, () -> new Lote(quantidade, 0.25, 10.0, 0.15));
            assertTrue(ex.getMessage().contains("quantidade"));
        }

        @ParameterizedTest
        @ValueSource(doubles = { 0.19, 0.50 })
        @DisplayName("Falha ao criar lote com percentual VIP inválido")
        void testCriarLote_PercentualVipInvalido(double percentualVip) {
            Exception ex = assertThrows(IllegalArgumentException.class, () -> new Lote(500, percentualVip, 10.0, 0.15));
            assertTrue(ex.getMessage().contains("VIP"));
        }
    }

    @Nested
    @DisplayName("Testes de Relatórios Financeiros")
    class GeracaoRelatorioTests {

        @Test
        @DisplayName("Gerar relatório de lucro")
        void testGerarRelatorio_Lucro() {
            Show show = gerenciador.criarShow("Artista F", "2025-12-31", 2000.0, 1000.0, true);
            Lote lote = new Lote(500, 0.20, 10.0, 0.15);
            gerenciador.adicionarLoteAoShow("Artista F", "2025-12-31", lote);
            venderTodosIngressos(show);

            Relatorio relatorio = gerenciador.gerarRelatorioShow("Artista F", "2025-12-31");
            assertEquals(StatusFinanceiro.LUCRO, relatorio.getStatusFinanceiro());
            assertTrue(relatorio.getReceitaLiquida() > 0);
        }

        @Test
        @DisplayName("Gerar relatório de prejuízo")
        void testGerarRelatorio_Prejuizo() {
            Show show = gerenciador.criarShow("Artista H", "2025-10-15", 1000.0, 500.0, false);
            Lote lote = new Lote(20, 0.20, 10.0, 0.0);
            gerenciador.adicionarLoteAoShow("Artista H", "2025-10-15", lote);
            venderTodosIngressos(show);

            Relatorio relatorio = gerenciador.gerarRelatorioShow("Artista H", "2025-10-15");
            assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro());
            assertTrue(relatorio.getReceitaLiquida() < 0);
        }

        @Test
        @DisplayName("Gerar relatório de show inexistente")
        void testGerarRelatorio_ShowInexistente() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> gerenciador.gerarRelatorioShow("Artista X", "2025-02-15"));
            assertEquals("Nenhum show encontrado para o artista Artista X na data 2025-02-15", exception.getMessage());
        }
    }

    private void venderTodosIngressos(Show show) {
        show.getLotes().forEach(lote -> lote.getIngressos().forEach(Ingresso::vender));
    }
}
