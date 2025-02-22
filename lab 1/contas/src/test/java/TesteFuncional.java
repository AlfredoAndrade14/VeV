import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TesteFuncional {
   // Análise de valor limite
    @Test
    public void testAVL01() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.01);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 0.01, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testAVL02() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 0.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Valor do boleto inválido: menor que 0.01", exception.getMessage());
    }

    @Test
    public void testAVL03() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 5000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testAVL04() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.01);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 5000.01, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Valor do boleto inválido: maior que 5000", exception.getMessage());
    }

    @Test
    public void testAVL05() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testAVL06() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("21/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(1100.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
    }

    @Test
    public void testAVL07() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("05/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testAVL08() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("06/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Cartão não pode ser usado: menos de 15 dias antes", exception.getMessage());
    }

    @Test
    public void testAVL09() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 2000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testAVL10() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    //Partição de equivalência

    @Test
    public void testPE01() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testPE02() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.005);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 0.005, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Valor do boleto inválido: menor que 0.01", exception.getMessage());
    }

    @Test
    public void testPE03() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 6000.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 6000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Valor do boleto inválido: maior que 5000", exception.getMessage());
    }

    @Test
    public void testPE04() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
        Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 2500.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("20/02/2023"), 2500.00, fatura);
        List<Conta> contas = List.of(conta1, conta2);
        List<String> tipos = List.of("boleto", "boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void testPE05() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
        Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 2000.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("20/02/2023"), 2000.00, fatura);
        List<Conta> contas = List.of(conta1, conta2);
        List<String> tipos = List.of("boleto", "boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }

    // Tabela de decisão
    @Test
    public void testTD01() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 2000.00);
        Conta conta = new Conta("001", sdf.parse("01/02/2024"), 2000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testTD02() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 2000.00);
        Conta conta = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testTD03() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 2000.00);
        Conta conta = new Conta("001", sdf.parse("01/02/2024"), 2000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("transferência");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testTD04() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("01/02/2024"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testTD05() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 1000.00);
        Conta conta = new Conta("001", sdf.parse("20/01/2024"), 1000.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Cartão não pode ser usado: menos de 15 dias antes", exception.getMessage());
    }

    @Test
    public void testTD06() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 3000.00);
        Conta conta1 = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("01/02/2024"), 1000.00, fatura);
        List<Conta> contas = List.of(conta1, conta2);
        List<String> tipos = List.of("boleto", "cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void testTD07() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 5000.00);
        Conta conta1 = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("01/02/2024"), 2000.00, fatura);
        Conta conta3 = new Conta("003", sdf.parse("01/02/2024"), 1000.00, fatura);
        List<Conta> contas = List.of(conta1, conta2, conta3);
        List<String> tipos = List.of("boleto", "transferência", "cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
        assertEquals(fatura.isPaga(), true);
    }
}