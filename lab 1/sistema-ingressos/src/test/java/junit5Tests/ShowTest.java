package junit5Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ingressos.enums.StatusFinanceiro;
import com.ingressos.models.*;

class ShowTest {

    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show("2024-11-16", "Linkin Park", 1000.0, 2000.0, true);
    }

    @AfterEach
    void tearDown() {
        show = null;
    }

    @Nested
    @DisplayName("Testes de Criação de Show")
    class CriacaoShowTest {

        @Test
        @DisplayName("Criar show válido")
        void testCriarShow() {
            assertAll("Verificando atributos do show",
                    () -> assertEquals("2024-11-16", show.getData()),
                    () -> assertEquals("Linkin Park", show.getArtista()),
                    () -> assertEquals(1000.0, show.getCache()),
                    () -> assertEquals(2000.0, show.getDespesasInfraestrutura()),
                    () -> assertTrue(show.isDataEspecial()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Falha ao criar show com data nula ou vazia")
        void testCriarShowComDataInvalida(String data) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Show(data, "Metallica", 50000.0, 20000.0, false));
            assertEquals("A data do show não pode ser nula ou vazia", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Falha ao criar show com artista nulo ou vazio")
        void testCriarShowComArtistaInvalido(String artista) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Show("2024-12-15", artista, 50000.0, 20000.0, false));
            assertEquals("O nome do artista não pode ser nulo ou vazio", exception.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Falha ao criar show com cache inválido")
        @NullSource
        @ValueSource(doubles = { -1.0, -10.0 })
        void testCriarShowComCacheInvalido(Double cache) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Show("2024-12-15", "Coldplay", cache, 20000.0, false));
            assertEquals("O cache deve ser um valor positivo", exception.getMessage());
        }

        @ParameterizedTest
        @DisplayName("Falha ao criar show com despesas de infraestrutura inválidas")
        @NullSource
        @ValueSource(doubles = { -1.0, -10.0 })
        void testCriarShowComDespesasInvalida(Double despesas) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Show("2024-12-15", "Breaking Benjamin", 50000.0, despesas, false));
            assertEquals("As despesas de infraestrutura não podem ser nulas ou negativas", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de Lotes")
    class LoteTest {

        @Test
        @DisplayName("Adicionar um lote ao show")
        void testAdicionarLote() {
            Lote lote = new Lote(500, 0.2, 10.00, 0.15);
            show.adicionarLote(lote);

            assertAll("Verificando lote adicionado",
                    () -> assertEquals(1, show.getLotes().size()),
                    () -> assertTrue(show.getLotes().contains(lote)));
        }
    }

    @Nested
    @DisplayName("Testes de Relatórios")
    class RelatorioTest {

        @Test
        @DisplayName("Gerar relatório sem ingressos vendidos (prejuízo)")
        void testGerarRelatorio() {
            Relatorio relatorio = show.gerarRelatorio();

            assertAll("Verificando relatório inicial",
                    () -> assertEquals(0, relatorio.getIngressosVipVendidos()),
                    () -> assertEquals(0, relatorio.getIngressosMeiaVendidos()),
                    () -> assertEquals(0, relatorio.getIngressosNormaisVendidos()),
                    () -> assertEquals(-3300, relatorio.getReceitaLiquida()),
                    () -> assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro()));
        }

        @Test
        @DisplayName("Gerar relatório com ingressos vendidos (lucro)")
        void testGerarRelatorioComIngressosVendidos() {
            Show show1 = new Show("2024-12-01", "Linkin Park", 8000.0, 4000.0, false);
            Lote lote1 = new Lote(500, 0.3, 100.00, 0.2);
            show1.adicionarLote(lote1);

            lote1.getIngressos().forEach(Ingresso::vender);
            Relatorio relatorio = show1.gerarRelatorio();

            assertAll("Verificando relatório com lucro",
                    () -> assertTrue(relatorio.getReceitaLiquida() > 0),
                    () -> assertEquals(StatusFinanceiro.LUCRO, relatorio.getStatusFinanceiro()));
        }

        @Test
        @DisplayName("Gerar relatório estável (zero lucro)")
        void testGerarRelatorioEstavel() {
            Show show = new Show("2024-10-10", "Imagine Dragons", 21000.0, 25000.0, false);
            Lote lote1 = new Lote(1000, 0.3, 80.00, 0.25);
            show.adicionarLote(lote1);

            for (int i = 0; i < 500; i++) {
                lote1.getIngressos().get(i).vender();
            }

            Relatorio relatorio = show.gerarRelatorio();

            assertAll("Verificando relatório estável",
                    () -> assertEquals(500,
                            relatorio.getIngressosVipVendidos() + relatorio.getIngressosMeiaVendidos()
                                    + relatorio.getIngressosNormaisVendidos()),
                    () -> assertEquals(0, relatorio.getReceitaLiquida(), 0.01),
                    () -> assertEquals(StatusFinanceiro.ESTAVEL, relatorio.getStatusFinanceiro()));
        }
    }
}
