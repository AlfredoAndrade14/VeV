package com.ingressos;

import com.ingressos.service.Gerenciador;
import com.ingressos.models.Show;
import com.ingressos.models.Lote;
import com.ingressos.models.Relatorio;
import com.ingressos.enums.TipoIngresso;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gerenciador gerenciador = new Gerenciador();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Criar Show");
            System.out.println("2. Remover Show");
            System.out.println("3. Buscar Show");
            System.out.println("4. Adicionar Lote ao Show");
            System.out.println("5. Vender Ingressos");
            System.out.println("6. Listar Shows");
            System.out.println("7. Gerar Relátorio");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do artista: ");
                    String artista = scanner.nextLine();
                    System.out.print("Digite a data do show (AAAA-MM-DD): ");
                    String data = scanner.nextLine();
                    System.out.print("Digite as despesas de infraestrutura: ");
                    double despesasInfraestrutura = scanner.nextDouble();
                    System.out.print("Digite o valor do cache: ");
                    double cache = scanner.nextDouble();
                    System.out.print("É uma data especial? (true/false): ");
                    boolean dataEspecial = scanner.nextBoolean();
                    scanner.nextLine();

                    try {
                        gerenciador.criarShow(artista, data, despesasInfraestrutura, cache,
                                dataEspecial);
                        System.out.println("Show criado com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Digite o nome do artista do show a ser removido: ");
                    String artistaRemover = scanner.nextLine();
                    System.out.print("Digite a data do show a ser removido (AAAA-MM-DD): ");
                    String dataRemover = scanner.nextLine();

                    try {
                        Show showRemover = gerenciador.buscarShow(artistaRemover, dataRemover);
                        gerenciador.removerShow(showRemover);
                        System.out.println("Show removido com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Digite o nome do artista: ");
                    String artistaBuscar = scanner.nextLine();
                    System.out.print("Digite a data do show (AAAA-MM-DD): ");
                    String dataBuscar = scanner.nextLine();

                    try {
                        Show showBuscar = gerenciador.buscarShow(artistaBuscar, dataBuscar);
                        System.out.println("Show encontrado: ");
                        System.out.println("Artista: " + showBuscar.getArtista());
                        System.out.println("Data: " + showBuscar.getData());
                        System.out.println("Cache: " + showBuscar.getCache());
                        System.out.println("Despesas Infraestrutura: " +
                                showBuscar.getDespesasInfraestrutura());
                        System.out.println("Data Especial: " + showBuscar.isDataEspecial());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Digite o nome do artista do show: ");
                    String artistaLote = scanner.nextLine();
                    System.out.print("Digite a data do show (AAAA-MM-DD): ");
                    String dataLote = scanner.nextLine();

                    try {
                        System.out.print("Digite a quantidade de ingressos: ");
                        int quantidade = scanner.nextInt();
                        System.out.print("Digite o percentual de ingressos VIP (0.2 a 0.3): ");
                        double percentualVip = scanner.nextDouble();
                        System.out.print("Digite o preço normal dos ingressos: ");
                        double precoNormal = scanner.nextDouble();
                        System.out.print("Digite o desconto (0 a 0.25): ");
                        double desconto = scanner.nextDouble();
                        scanner.nextLine();

                        Lote lote = new Lote(quantidade, percentualVip, precoNormal, desconto);
                        gerenciador.adicionarLoteAoShow(artistaLote, dataLote, lote);
                        System.out.println("Lote adicionado ao show com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Digite o nome do artista do show: ");
                    String artistaVender = scanner.nextLine();
                    System.out.print("Digite a data do show (AAAA-MM-DD): ");
                    String dataVender = scanner.nextLine();
                    System.out.print("Digite o tipo de ingresso (VIP, MEIA_ENTRADA, NORMAL): ");
                    String tipoIngressoStr = scanner.nextLine().toUpperCase();
                    TipoIngresso tipoIngresso = TipoIngresso.valueOf(tipoIngressoStr);
                    System.out.print("Digite a quantidade de ingressos a vender: ");
                    int quantidadeVender = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        gerenciador.venderIngressos(artistaVender, dataVender, tipoIngresso,
                                quantidadeVender);
                        System.out.println("Ingressos vendidos com sucesso!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Lista de Shows:");
                    for (Show s : gerenciador.listarShows()) {
                        System.out.println("Artista: " + s.getArtista() + ", Data: " + s.getData());
                    }
                    break;

                case 7:
                    System.out.print("Digite o nome do artista: ");
                    String artistaRelatorio = scanner.nextLine();
                    System.out.print("Digite a data do show (AAAA-MM-DD): ");
                    String dataRelatorio = scanner.nextLine();

                    try {
                        Relatorio relatorio = gerenciador.gerarRelatorioShow(artistaRelatorio,
                                dataRelatorio);
                        System.out.println(relatorio.toString());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Erro ao gerar relatório: " + e.getMessage());
                    }
                    break;

                case 8:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
