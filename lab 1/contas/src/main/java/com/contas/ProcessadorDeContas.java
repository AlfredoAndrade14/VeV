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

        if (contas.size() == 0) {
            throw new IllegalArgumentException("A lista de contas e a lista de tipos não podem ser vazias");
        }

        Fatura faturaReferencia = contas.get(0).getFatura();
        double somaPagamentos = 0.0;
        boolean adicionarPagamento = true;

        for (int i = 0; i < contas.size(); i++) {
            if (!contas.get(i).getFatura().equals(faturaReferencia)) {
                throw new IllegalArgumentException("Todas as contas devem pertencer à mesma fatura.");
            }
            
            TipoPagamento tipo;
            switch (tipos.get(i)) {
                case "boleto":
                    tipo = Pagamento.TipoPagamento.BOLETO;
                    if (contas.get(i).getValorPago() < 0.01 || contas.get(i).getValorPago() > 5000.00) {
                        adicionarPagamento = false;
                        break;
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
                    break; 
                case "cartão":
                    long diasDeDiferenca = (contas.get(i).getFatura().getData().getTime() - contas.get(i).getData().getTime()) / (1000 * 60 * 60 * 24);
                    tipo = Pagamento.TipoPagamento.CARTAO_CREDITO;
                    if (diasDeDiferenca < 15) {
                        adicionarPagamento = false;
                        break;
                    }
                    break;
                case "tranferencia":
                    tipo = Pagamento.TipoPagamento.TRANSFERENCIA_BANCARIA;
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de pagamento inválido: " + tipos.get(i));
            }

            if (adicionarPagamento) {
                pagamentos.add(new Pagamento(tipo, contas.get(i)));
                somaPagamentos += contas.get(i).getValorPago();
            }
        }
        if (somaPagamentos >= faturaReferencia.getValor()) {
            faturaReferencia.setPaga();
        }
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
}
