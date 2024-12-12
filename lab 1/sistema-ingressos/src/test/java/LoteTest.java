import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
