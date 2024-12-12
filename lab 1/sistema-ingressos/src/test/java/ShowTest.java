import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.models.Show;

class ShowTest {

    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show("2024-11-16", "Linkin Park", 1000.0, 2000.0, false);
    }

    @Test
    void testCriarShow() {
        assertEquals("2024-11-16", show.getData());
        assertEquals("Linkin Park", show.getArtista());
        assertEquals(1000.0, show.getCache());
        assertEquals(2000.0, show.getDespesasInfraestrutura());
        assertEquals(false, show.isDataEspecial());
    }

}