import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessadorDeContasTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void TestCreateEntities() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);

        assertEquals("Cliente A", fatura.getCliente());
        assertEquals(sdf.parse("20/02/2023"), fatura.getData());
        assertEquals(1500.00, fatura.getValor(), 0.01);

        assertEquals("001", conta.getCodigo());
        assertEquals(sdf.parse("20/02/2023"), conta.getData());
        assertEquals(500.00, conta.getValorPago(), 0.01);
        assertEquals(conta.geFatura(), fatura);
    }
}