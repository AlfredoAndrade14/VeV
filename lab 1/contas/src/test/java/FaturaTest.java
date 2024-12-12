import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FaturaTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void TestCreateFatura() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        assertEquals("Cliente A", fatura.getCliente());
        assertEquals(sdf.parse("20/02/2023"), fatura.getData());
        assertEquals(1500.00, fatura.getValor(), 0.01);
    }

    
}