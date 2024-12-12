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
        ingressoVip = new Ingresso(TipoIngresso.VIP, 200.00, 0.0);
        ingressoNormal = new Ingresso(TipoIngresso.NORMAL, 100.00, 0.0);
        ingressoMeiaEntrada = new Ingresso(TipoIngresso.MEIA_ENTRADA, 50.00, 0.0);
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
            new Ingresso(null, 200.00, 0.0);
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

    @Test
    void testNaoPermitirVenderIngressosJaVendidos() {
        ingressoNormal.vender();
        IllegalStateException exception = assertThrows(IllegalStateException.class, ingressoNormal::vender);
        assertEquals("O ingresso já foi vendido", exception.getMessage());
    }

    @Test
    void testNaoPermitirDisponibilizarIngressosJaDisponiveis() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                ingressoVip::disponibilizar);
        assertEquals("O ingresso já está disponível", exception.getMessage());
    }

}
