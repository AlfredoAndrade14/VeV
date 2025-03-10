import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes da classe Conta")
public class ContaTest {
    private SimpleDateFormat sdf;
    private Fatura fatura;
    private Date dataValida;

    @BeforeEach
    void setup() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataValida = sdf.parse("20/02/2023");
        fatura = new Fatura("Cliente A", dataValida, 1500.00);
    }

    @Nested
    @DisplayName("Testes de criação válida")
    class CriacaoValida {
        @Test
        @DisplayName("Deve criar uma conta com parâmetros válidos")
        void testCreateConta() throws ParseException {
            Conta conta = new Conta("001", dataValida, 500.00, fatura);

            assertEquals("001", conta.getCodigo(), "Código deve ser igual ao informado");
            assertEquals(dataValida, conta.getData(), "Data deve ser igual à informada");
            assertEquals(500.00, conta.getValorPago(), 0.01, "Valor pago deve ser igual ao informado");
            assertEquals(fatura, conta.getFatura(), "Fatura deve ser igual à informada");
        }
    }

    @Nested
    @DisplayName("Testes de validação de parâmetros")
    class ValidacaoParametros {
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Deve lançar exceção para código nulo ou vazio")
        void testCreateContaWithInvalidCodigo(String codigo) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Conta(codigo, dataValida, 500.00, fatura);
            }, "Deve lançar IllegalArgumentException para código inválido");

            assertEquals("Código não pode ser nulo ou vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para data nula")
        void testCreateContaWithNullDate() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Conta("001", null, 500.00, fatura);
            }, "Deve lançar IllegalArgumentException para data nula");

            assertEquals("Data não pode ser nula.", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(doubles = {-500.00, -0.01})
        @DisplayName("Deve lançar exceção para valor pago negativo")
        void testCreateContaWithNegativeValorPago(double valorPago) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Conta("001", dataValida, valorPago, fatura);
            }, "Deve lançar IllegalArgumentException para valor pago negativo");

            assertEquals("Valor pago não pode ser negativo.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para fatura nula")
        void testCreateContaWithNullFatura() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Conta("001", dataValida, 500.00, null);
            }, "Deve lançar IllegalArgumentException para fatura nula");

            assertEquals("Fatura não pode ser nula.", exception.getMessage());
        }
    }
}