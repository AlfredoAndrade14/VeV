package funcionalTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.service.Gerenciador;
import com.ingressos.models.Show;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;
import com.ingressos.models.Relatorio;
import com.ingressos.enums.StatusFinanceiro;

public class DecisionTableTest {

    private Gerenciador gerenciador;

    @BeforeEach
    public void setUp() {
        gerenciador = new Gerenciador();
    }

    // CT-TD-01
    @Test
    public void testCriarShow_Regra1() {
        Show show = gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, true);
        assertNotNull(show);
        assertEquals("Artista A", show.getArtista());
        assertEquals("2025-12-25", show.getData());
    }

    // CT-TD-02
    @Test
    public void testCriarShow_Regra2() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow(null, "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", ex.getMessage());
    }

    // CT-TD-03
    @Test
    public void testCriarShow_Regra3() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", null, 2000.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", ex.getMessage());
    }

    // CT-TD-04
    @Test
    public void testCriarShow_Regra4() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", -100.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", ex.getMessage());
    }

    // CT-TD-05
    @Test
    public void testCriarShow_Regra5() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 0.0, false);
        });
        assertEquals("Dados inválidos para criar o show", ex.getMessage());
    }

    // CT-TD-06
    @Test
    public void testCriarShow_Regra6() {
        gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("O show já está registrado no sistema.", ex.getMessage());
    }

    // Lote
    // CT-TD-07
    @Test
    public void testCriarLote_Regra1() {
        Lote lote = new Lote(500, 0.25, 10.0, 0.15);
        assertNotNull(lote, "O lote deve ser criado com sucesso.");
        List<Ingresso> ingressos = lote.getIngressos();
        assertEquals(500, ingressos.size(), "O lote deve conter 500 ingressos.");
    }

    // CT-TD-08
    @Test
    public void testCriarLote_Regra2_QuantidadeNaoPositiva() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(0, 0.25, 10.0, 0.15);
        });
        assertTrue(ex.getMessage().contains("quantidade"),
                "Deve lançar exceção indicando que a quantidade deve ser positiva.");
    }

    // CT-TD-09
    @Test
    public void testCriarLote_Regra3_PercentualVipInvalido() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(500, 0.19, 10.0, 0.15);
        });
        assertTrue(ex.getMessage().contains("VIP"),
                "Deve lançar exceção indicando que o percentual de VIP está fora do intervalo permitido.");
    }

    // CT-TD-10
    @Test
    public void testCriarLote_Regra4_PrecoNormalNaoPositivo() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(500, 0.25, 0.0, 0.15);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("preço"),
                "Deve lançar exceção indicando que o preço normal deve ser positivo.");
    }

    // CT-TD-11
    @Test
    public void testCriarLote_Regra5_DescontoExcedente() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(500, 0.25, 10.0, 0.30);
        });
        assertTrue(ex.getMessage().contains("Desconto"),
                "Deve lançar exceção indicando que o desconto está fora do intervalo permitido.");
    }

    // Geração de Relatório
    // CT-TD-12
    @Test
    public void testGerarRelatorio_Lucro() {

        Show show = gerenciador.criarShow("Artista F", "2025-12-31", 2000.0, 1000.0, true);
        Lote lote = new Lote(500, 0.20, 10.0, 0.15);
        gerenciador.adicionarLoteAoShow("Artista F", "2025-12-31", lote);
        venderTodosIngressos(show);

        Relatorio relatorio = gerenciador.gerarRelatorioShow("Artista F", "2025-12-31");
        assertEquals(StatusFinanceiro.LUCRO, relatorio.getStatusFinanceiro());
        assertTrue(relatorio.getReceitaLiquida() > 0);
    }

    // CT-TD-13
    @Test
    public void testGerarRelatorio_Estavel() {
        Show show = gerenciador.criarShow("Artista G", "2025-11-30", 400.0, 175.0, false);
        Lote lote = new Lote(50, 0.20, 10.0, 0.0);
        gerenciador.adicionarLoteAoShow("Artista G", "2025-11-30", lote);
        venderTodosIngressos(show);

        Relatorio relatorio = gerenciador.gerarRelatorioShow("Artista G", "2025-11-30");
        assertEquals(StatusFinanceiro.ESTAVEL, relatorio.getStatusFinanceiro());
        assertEquals(0.0, relatorio.getReceitaLiquida(), 0.01);
    }

    // CT-TD-14
    @Test
    public void testGerarRelatorio_Prejuizo() {
        Show show = gerenciador.criarShow("Artista H", "2025-10-15", 1000.0, 500.0, false);
        Lote lote = new Lote(20, 0.20, 10.0, 0.0);
        gerenciador.adicionarLoteAoShow("Artista H", "2025-10-15", lote);
        venderTodosIngressos(show);

        Relatorio relatorio = gerenciador.gerarRelatorioShow("Artista H", "2025-10-15");
        assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro());
        assertTrue(relatorio.getReceitaLiquida() < 0);
    }

    // CT-TD-15
    @Test
    public void testGerarRelatorio_ShowInexistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.gerarRelatorioShow("Artista x", "2025-02-15");
        });
        assertEquals("Nenhum show encontrado para o artista Artista x na data 2025-02-15", exception.getMessage());

    }

    private void venderTodosIngressos(Show show) {
        show.getLotes().forEach(lote -> {
            lote.getIngressos().forEach(ingresso -> {
                if (!ingresso.isVendido()) {
                    ingresso.vender();
                }
            });
        });
    }
}
