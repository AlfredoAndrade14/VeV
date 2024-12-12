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

    @Test
    void testCriarIngressoComTipoNulo() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Ingresso(null);
        });
        assertEquals("O tipo do ingresso não pode ser nulo", exception.getMessage());
    }


    @Test
    void testSetVendido() {
        assertFalse(ingressoVip.isVendido());
        ingressoVip.vender();
        assertTrue(ingressoVip.isVendido());
    }

    @Test
    void testDisponibilizar() {
        ingressoMeiaEntrada.vender();
        assertTrue(ingressoMeiaEntrada.isVendido());
        ingressoMeiaEntrada.disponibilizar();
        assertFalse(ingressoVip.isVendido());
    }




}
