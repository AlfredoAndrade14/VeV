import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

import com.contas.Fatura;

public class FaturaTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void TestCreateFatura() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        assertEquals("Cliente A", fatura.getCliente());
        assertEquals(sdf.parse("20/02/2023"), fatura.getData());
        assertEquals(1500.00, fatura.getValor(), 0.01);
    }

    @Test
    public void testCreateFaturaWithNullCliente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Fatura(null, sdf.parse("20/02/2023"), 1500.00);
        });

        assertEquals("Cliente não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    public void testCreateFaturaWithEmptyCliente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Fatura("", sdf.parse("20/02/2023"), 1500.00);
        });

        assertEquals("Cliente não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    public void testCreateFaturaWithNullDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Fatura("Cliente A", null, 1500.00);
        });

        assertEquals("Data não pode ser nula.", exception.getMessage());
    }

    @Test
    public void testCreateFaturaWithNonPositiveValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Fatura("Cliente A", sdf.parse("20/02/2023"), 0);
        });

        assertEquals("Valor deve ser positivo.", exception.getMessage());
    }
}