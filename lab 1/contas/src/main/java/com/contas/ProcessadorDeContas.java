package com.contas;

import java.util.ArrayList;
import java.util.List;

import com.contas.Pagamento.TipoPagamento;

public class ProcessadorDeContas {
    private List<Pagamento> pagamentos;

    public ProcessadorDeContas() {
        this.pagamentos = new ArrayList<>();
    }

    public void processar(List<Conta> contas, List<String> tipos) {
        if (contas.size() != tipos.size()) {
            throw new IllegalArgumentException("A lista de contas e a lista de tipos devem ter o mesmo tamanho.");
        }
        Fatura faturaReferencia = contas.get(0).getFatura();
        double somaPagamentos = 0.0;

        for (int i = 0; i < contas.size(); i++) {
            if (!contas.get(i).getFatura().equals(faturaReferencia)) {
                throw new IllegalArgumentException("Todas as contas devem pertencer à mesma fatura.");
            }
            
            TipoPagamento tipo;
            switch (tipos.get(i)) {
                case "boleto":
                    if (contas.get(i).getValorPago() < 0.01 || contas.get(i).getValorPago() > 5000.00) {
                        throw new IllegalArgumentException("Pagamentos por boleto devem estar entre R$ 0,01 e R$ 5.000,00.");
                    }
        
                    if (contas.get(i).getData().after(contas.get(i).getFatura().getData())) {
                        double valorAcrescido = contas.get(i).getValorPago() * 1.10;
                        Conta contaAtualizada = new Conta(
                            contas.get(i).getCodigo(),
                            contas.get(i).getData(),
                            valorAcrescido,
                            contas.get(i).getFatura()
                        );
                    
                        contas.set(i, contaAtualizada);
                    }
                    tipo = Pagamento.TipoPagamento.BOLETO;
                    break; 
                case "cartão":
                    long diasDeDiferenca = (contas.get(i).getFatura().getData().getTime() - contas.get(i).getData().getTime()) / (1000 * 60 * 60 * 24);
                    if (diasDeDiferenca < 15) {
                        throw new IllegalArgumentException("Pagamentos por cartão de crédito só podem ser incluídos se a data da conta for pelo menos 15 dias anteriores à data da fatura.");
                    }
                    tipo = Pagamento.TipoPagamento.CARTAO_CREDITO;
                    break;
                case "tranferencia":
                    tipo = Pagamento.TipoPagamento.TRANSFERENCIA_BANCARIA;
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de pagamento inválido: " + tipos.get(i));
            }

            pagamentos.add(new Pagamento(tipo, contas.get(i)));
        }
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
}
