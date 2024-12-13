import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.enums.StatusFinanceiro;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;
import com.ingressos.models.Relatorio;
import com.ingressos.models.Show;

class ShowTest {

    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show("2024-11-16", "Linkin Park", 1000.0, 2000.0, true);
    }

    @Test
    void testCriarShow() {
        assertEquals("2024-11-16", show.getData());
        assertEquals("Linkin Park", show.getArtista());
        assertEquals(1000.0, show.getCache());
        assertEquals(2000.0, show.getDespesasInfraestrutura());
        assertEquals(true, show.isDataEspecial());
    }

    @Test
    void testCriarShowComDataNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show(null, "Metalica", 50000.0, 20000.0, false);
        });
        assertEquals("A data do show não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testCriarShowComDataVazia() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("   ", "Twenty One Pilots", 50000.0, 20000.0, false);
        });
        assertEquals("A data do show não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testCriarShowComArtistaNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", null, 50000.0, 20000.0, false);
        });
        assertEquals("O nome do artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCriarShowComArtistaVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "   ", 50000.0, 20000.0, false);
        });
        assertEquals("O nome do artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCriarShowComCacheNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Imagine Dragons", null, 20000.0, false);
        });
        assertEquals("O cache deve ser um valor positivo", exception.getMessage());
    }

    @Test
    void testCriarShowComCacheNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Coldplay", -10000.0, 20000.0, false);
        });
        assertEquals("O cache deve ser um valor positivo", exception.getMessage());
    }

    @Test
    void testCriarShowComDespesasInfraestruturaNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Cage The Elephant", 50000.0, null, false);
        });
        assertEquals("As despesas de infraestrutura não podem ser nulas ou negativas", exception.getMessage());
    }

    @Test
    void testCriarShowComDespesasInfraestruturaNegativas() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Breaking Benjamin", 50000.0, -5000.0, false);
        });
        assertEquals("As despesas de infraestrutura não podem ser nulas ou negativas", exception.getMessage());
    }

    @Test
    void testAdicionarLote() {
        Lote lote = new Lote(500, 0.2, 10.00, 0.15);
        show.adicionarLote(lote);

        assertEquals(1, show.getLotes().size());
        assertTrue(show.getLotes().contains(lote));
    }

    @Test
    void testGerarRelatorio() {
        Relatorio relatorio = show.gerarRelatorio();
        assertEquals(0, relatorio.getIngressosVipVendidos());
        assertEquals(0, relatorio.getIngressosMeiaVendidos());
        assertEquals(0, relatorio.getIngressosNormaisVendidos());
        assertEquals(-3300, relatorio.getReceitaLiquida());
        assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro());
    }

    @Test
    void testGerarRelatorioComIngressosVendidos() {
        Show show1 = new Show("2024-12-01", "Linkin Park", 8000.0, 4000.0, false);
        Lote lote1 = new Lote(500, 0.3, 100.00, 0.2);
        show1.adicionarLote(lote1);

        for (Ingresso ingresso : lote1.getIngressos()) {
            ingresso.vender();
        }

        Relatorio relatorio = show1.gerarRelatorio();

        assertTrue(relatorio.getReceitaLiquida() > 0);
        assertEquals(StatusFinanceiro.LUCRO, relatorio.getStatusFinanceiro());
        assertEquals("Relatório do Show\n" +
                "Data: 2024-12-01\n" +
                "Artista: Linkin Park\n" +
                "Ingressos VIP vendidos: 150\n" +
                "Ingressos Meia vendidos: 50\n" +
                "Ingressos Normais vendidos: 300\n" +
                "Receita líquida: R$ 38500,00\n" +
                "Status financeiro: LUCRO", relatorio.toString());
    }

    @Test
    void testRelatorioComLotesMultiplos() {
        Show show = new Show("2024-12-31", "Linkin Park", 70000.0, 30000.0, false);
        Lote lote1 = new Lote(1000, 0.3, 600.00, 0.2);
        Lote lote2 = new Lote(500, 0.2, 750.00, 0.1);

        for (Ingresso ingresso : lote1.getIngressos()) {
            ingresso.vender();
        }

        for (int i = 0; i < 250; i++) {
            lote2.getIngressos().get(i).vender();
        }

        show.adicionarLote(lote1);
        show.adicionarLote(lote2);

        Relatorio relatorio = show.gerarRelatorio();

        assertEquals(1250,
                relatorio.getIngressosVipVendidos() + relatorio.getIngressosMeiaVendidos()
                        + relatorio.getIngressosNormaisVendidos());
        assertEquals("Relatório do Show\n" +
                "Data: 2024-12-31\n" +
                "Artista: Linkin Park\n" +
                "Ingressos VIP vendidos: 400\n" +
                "Ingressos Meia vendidos: 150\n" +
                "Ingressos Normais vendidos: 700\n" +
                "Receita líquida: R$ 727250,00\n" +
                "Status financeiro: LUCRO", relatorio.toString());
    }

    @Test
    void testGerarRelatorioPrejuizo() {
        Show show = new Show("2024-11-15", "Coldplay", 15000.0, 20000.0, false);
        Lote lote1 = new Lote(300, 0.3, 50.00, 0.25);

        show.adicionarLote(lote1);

        for (int i = 0; i < 100; i++) {
            lote1.getIngressos().get(i).vender();
        }

        Relatorio relatorio = show.gerarRelatorio();

        assertEquals(100, relatorio.getIngressosVipVendidos() + relatorio.getIngressosMeiaVendidos()
                + relatorio.getIngressosNormaisVendidos());
        assertTrue(relatorio.getReceitaLiquida() < 0);
        assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro());
        assertEquals("Relatório do Show\n" +
                "Data: 2024-11-15\n" +
                "Artista: Coldplay\n" +
                "Ingressos VIP vendidos: 90\n" +
                "Ingressos Meia vendidos: 10\n" +
                "Ingressos Normais vendidos: 0\n" +
                "Receita líquida: R$ -28000,00\n" +
                "Status financeiro: PREJUÍZO", relatorio.toString());
    }

}