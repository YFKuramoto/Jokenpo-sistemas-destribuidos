
import java.util.Random;
import java.util.Scanner;

public class Maquina {
    public void Jokenpo() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String[] opcoes = { "Pedra", "Papel", "Tesoura" };
        String resposta = "";
        int contadorv = 0;
        int contadord = 0;
        int contadore = 0;

        while (!resposta.equalsIgnoreCase("sair")) {
            System.out.println("Escolha uma opcao: Pedra, Papel ou Tesoura. Digite 'sair' para encerrar o jogo.");
            resposta = scanner.nextLine().trim();

            if (resposta.equalsIgnoreCase("sair")) {
                System.out.println("Jogo encerrado.");
                break;
            }

            boolean escolhaValida = false;
            for (String opcao : opcoes) {
                if (resposta.equalsIgnoreCase(opcao)) {
                    escolhaValida = true;
                    break;
                }
            }

            if (!escolhaValida) {
                System.out.println("Escolha inválida. Tente novamente.");
                continue;
            }

            int escolhaComputador = random.nextInt(3);
            String escolhaComputadorStr = opcoes[escolhaComputador];

            System.out.println("Computador escolheu: " + escolhaComputadorStr);

            if (resposta.equalsIgnoreCase(escolhaComputadorStr)) {
                System.out.println("Empate!");
                contadore += 1;
            } else if ((resposta.equalsIgnoreCase("Pedra") && escolhaComputadorStr.equals("Tesoura")) ||
                    (resposta.equalsIgnoreCase("Papel") && escolhaComputadorStr.equals("Pedra")) ||
                    (resposta.equalsIgnoreCase("Tesoura") && escolhaComputadorStr.equals("Papel"))) {
                System.out.println("Voce venceu!");
                contadorv += 1;
            } else {
                System.out.println("Computador venceu!");
                contadord += 1;
            }

            System.out.println("Placar: Voce " + contadorv + " x " + contadord + " Computador. Empates: " + contadore);
        }

        scanner.close();
    }

}
