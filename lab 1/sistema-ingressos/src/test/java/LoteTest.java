import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;

class LoteTest {

    private Lote lote;

    @BeforeEach
    void setUp() {
        lote = new Lote(100, 0.2, 50.0, 0.1);
    }

    @Test
    void testCriarLote() {
        List<Ingresso> ingressos = lote.getIngressos();

        long vipCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count();
        long meiaCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count();
        long normalCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count();

        assertEquals(20, vipCount);
        assertEquals(10, meiaCount);
        assertEquals(70, normalCount);
    }

    @Test
    void testCriarLoteComQuantidadeNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(null, 0.2, 50.0, 0.1);
        });
        assertEquals("A quantidade de ingressos deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testCriarLoteComPercentualVipNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, null, 50.0, 0.1);
        });
        assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
    }

    @Test
    void testCriarLoteComPrecoNormalNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.2, null, 0.1);
        });
        assertEquals("O preço normal deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testCriarLoteComDescontoNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.2, 50.0, null);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    @Test
    void testCriarLoteComQuantidadeNegativa() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(-10, 0.2, 50.0, 0.1);
        });
        assertEquals("A quantidade de ingressos deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testCriarLoteComPercentualVipForaDoIntervalo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.35, 50.0, 0.1);
        });
        assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
    }

    @Test
    void testCriarLoteComPrecoNormalZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.2, 0.0, 0.1);
        });
        assertEquals("O preço normal deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testCriarLoteComDescontoNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.2, 50.0, -0.1);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    @Test
    void testCriarLoteComDescontoAcimaDe25() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.2, 50.0, 0.3);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    @Test
    void testCriacaoLoteComDescontoValido() {
        Lote lote = new Lote(2, 0.3, 100.0, 0.15);
        for (Ingresso ingresso : lote.getIngressos()) {
            if (ingresso.getTipo() == TipoIngresso.VIP || ingresso.getTipo() == TipoIngresso.NORMAL) {
                double precoEsperado = ingresso.getTipo() == TipoIngresso.VIP ? 200.0 * 0.85 : 100.0 * 0.85;
                assertEquals(precoEsperado, ingresso.getPrecoFinal());
            } else {
                assertEquals(50.0, ingresso.getPrecoFinal());
            }
        }
    }

    @Test
void testVenderIngressosValidos() {
    int vendidos = lote.venderIngressos(TipoIngresso.NORMAL, 10);
    assertEquals(10, vendidos);
    assertEquals(60, lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
}

@Test
void testVenderIngressosAcimaDoDisponivel() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        lote.venderIngressos(TipoIngresso.VIP, 25);
    });
    assertEquals("Não há ingressos suficientes do tipo VIP para vender", exception.getMessage());
}

@Test
void testVenderIngressosTipoInexistente() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        lote.venderIngressos(null, 5);
    });
    assertEquals("Tipo de ingresso inválido", exception.getMessage());
}

@Test
void testVenderIngressosQuantidadeZero() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        lote.venderIngressos(TipoIngresso.MEIA_ENTRADA, 0);
    });
    assertEquals("A quantidade solicitada deve ser maior que zero", exception.getMessage());
}

@Test
void testVenderIngressosTodosDisponiveis() {
    int vendidos = lote.venderIngressos(TipoIngresso.VIP, 20);
    assertEquals(20, vendidos);
    assertEquals(0, lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
}


}
