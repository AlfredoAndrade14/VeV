import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoteTest {

    private Lote lote;

    @BeforeEach
    void setUp() {
        lote = new Lote();
    }

    @Test
    void testCriarLote() {
        assertEquals(1, lote.getId());
    }

}
