package com.contas;

import java.util.Date;

public class Conta {
    private String codigo;
    private Date data;
    private double valorPago;
    private Fatura fatura;

    public Conta(String codigo, Date data, double valorPago, Fatura fatura) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código não pode ser nulo ou vazio.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula.");
        }
        if (valorPago < 0) {
            throw new IllegalArgumentException("Valor pago não pode ser negativo.");
        }
        if (fatura == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula.");
        }
        
        this.codigo = codigo;
        this.data = data;
        this.valorPago = valorPago;
        this.fatura = fatura;
    }

    public String getCodigo() {
        return codigo;
    }

    public Date getData() {
        return data;
    }

    public double getValorPago() {
        return valorPago;
    }

    public Fatura getFatura() {
        return fatura;
    }
}
