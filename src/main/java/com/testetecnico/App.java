package com.testetecnico;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean rodando = true;

        System.out.println("\n");
        System.out.println("--------------------------------------------------------------");
        System.out.println("         Eduardo Ruiz - Teste Tecnico - Intuitive Care        ");
        System.out.println("--------------------------------------------------------------");

        while (rodando) {
            System.out.println("\n");
            System.out.println(
                    "Digite:\n 1 para fazer webscraping\n 2 para fazer transformacao de dados\n 3 para sair: ");

            try {
                int opcao = scanner.nextInt();

                if (opcao == 1) {
                    WebScrapping.run();
                } else if (opcao == 2) {
                    TransformationData.run();
                } else if (opcao == 3) {
                    System.out.println("Saindo...");
                    rodando = false;
                } else {
                    System.out.println("Opcao invalida. Tente novamente.");
                }

            } catch (Exception e) {
                System.err.println("Opcao invalida. Saindo do programa.");
                rodando = false;
            }
        }

        scanner.close();
    }
}
