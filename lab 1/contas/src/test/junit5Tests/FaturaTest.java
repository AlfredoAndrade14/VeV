import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.contas.Fatura;

@DisplayName("Testes da classe Fatura")
public class FaturaTest {
    private SimpleDateFormat sdf;
    private Date dataValida;

    @BeforeEach
    void setup() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataValida = sdf.parse("20/02/2023");
    }

    @Nested
    @DisplayName("Testes de criação válida")
    class CriacaoValida {
        @Test
        @DisplayName("Deve criar uma fatura com parâmetros válidos")
        void testCreateFatura() {
            Fatura fatura = new Fatura("Cliente A", dataValida, 1500.00);

            assertEquals("Cliente A", fatura.getCliente(), "Cliente deve ser igual ao informado");
            assertEquals(dataValida, fatura.getData(), "Data deve ser igual à informada");
            assertEquals(1500.00, fatura.getValor(), 0.01, "Valor deve ser igual ao informado");
        }
    }

    @Nested
    @DisplayName("Testes de validação de parâmetros")
    class ValidacaoParametros {
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Deve lançar exceção para cliente nulo ou vazio")
        void testCreateFaturaWithInvalidCliente(String cliente) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Fatura(cliente, dataValida, 1500.00);
            }, "Deve lançar IllegalArgumentException para cliente inválido");

            assertEquals("Cliente não pode ser nulo ou vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para data nula")
        void testCreateFaturaWithNullDate() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Fatura("Cliente A", null, 1500.00);
            }, "Deve lançar IllegalArgumentException para data nula");

            assertEquals("Data não pode ser nula.", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -1.0, -100.0})
        @DisplayName("Deve lançar exceção para valor não positivo")
        void testCreateFaturaWithNonPositiveValue(double valor) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Fatura("Cliente A", dataValida, valor);
            }, "Deve lançar IllegalArgumentException para valor não positivo");

            assertEquals("Valor deve ser positivo.", exception.getMessage());
        }
    }
}