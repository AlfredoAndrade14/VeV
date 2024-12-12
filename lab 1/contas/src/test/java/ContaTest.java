import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContaTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void TestCreateConta() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);

        assertEquals("001", conta.getCodigo());
        assertEquals(sdf.parse("20/02/2023"), conta.getData());
        assertEquals(500.00, conta.getValorPago(), 0.01);
        assertEquals(conta.getFatura(), fatura);
    }

    @Test
    public void testCreateContaWithNullCodigo() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Conta(null, sdf.parse("20/02/2023"), 500.00, fatura);
        });

        assertEquals("Código não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    public void testCreateContaWithEmptyCodigo() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Conta("", sdf.parse("20/02/2023"), 500.00, fatura);
        });

        assertEquals("Código não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    public void testCreateContaWithNullDate() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Conta("001", null, 500.00, fatura);
        });

        assertEquals("Data não pode ser nula.", exception.getMessage());
    }

    @Test
    public void testCreateContaWithNegativeValorPago() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Conta("001", sdf.parse("20/02/2023"), -500.00, fatura);
        });

        assertEquals("Valor pago não pode ser negativo.", exception.getMessage());
    }

    @Test
    public void testCreateContaWithNullFatura() throws ParseException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Conta("001", sdf.parse("20/02/2023"), 500.00, null);
        });

        assertEquals("Fatura não pode ser nula.", exception.getMessage());
    }
}