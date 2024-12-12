import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;

import static org.junit.jupiter.api.Assertions.*;

class IngressoTest {

    private Ingresso ingressoVip;
    private Ingresso ingressoNormal;
    private Ingresso ingressoMeiaEntrada;

    @BeforeEach
    void setUp() {
        ingressoVip = new Ingresso(TipoIngresso.VIP);
        ingressoNormal = new Ingresso(TipoIngresso.NORMAL);
        ingressoMeiaEntrada = new Ingresso(TipoIngresso.MEIA_ENTRADA);
    }

    @Test
    void testCriarIngresso() {
        assertEquals(TipoIngresso.VIP, ingressoVip.getTipo());
        assertFalse(ingressoVip.isVendido());

        assertEquals(TipoIngresso.NORMAL, ingressoNormal.getTipo());
        assertFalse(ingressoNormal.isVendido());

        assertEquals(TipoIngresso.MEIA_ENTRADA, ingressoMeiaEntrada.getTipo());
        assertFalse(ingressoMeiaEntrada.isVendido());
    }

}
