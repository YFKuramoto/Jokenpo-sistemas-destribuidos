import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServidorMultiplayer {

    private static final int PORTA = 12345;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        System.out.println("Servidor de Jokenpo iniciado na porta " + PORTA);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                Socket jogador1 = serverSocket.accept();
                System.out.println("Jogador 1 conectado: " + jogador1.getInetAddress());
                Socket jogador2 = serverSocket.accept();
                System.out.println("Jogador 2 conectado: " + jogador2.getInetAddress());

                pool.execute(new JogoHandler(jogador1, jogador2));
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static class JogoHandler implements Runnable {
        private final Socket jogador1;
        private final Socket jogador2;
        private static int vitoriasJogador1 = 0;
        private static int vitoriasJogador2 = 0;
        private static int empates = 0;

        public JogoHandler(Socket jogador1, Socket jogador2) {
            this.jogador1 = jogador1;
            this.jogador2 = jogador2;
        }

        @Override
        public void run() {
            try (
                    BufferedReader entrada1 = new BufferedReader(new InputStreamReader(jogador1.getInputStream()));
                    PrintWriter saida1 = new PrintWriter(jogador1.getOutputStream(), true);
                    BufferedReader entrada2 = new BufferedReader(new InputStreamReader(jogador2.getInputStream()));
                    PrintWriter saida2 = new PrintWriter(jogador2.getOutputStream(), true)) {
                saida1.println("Bem-vindo ao servidor de Jokenpo! Você é o jogador 1.");
                saida2.println("Bem-vindo ao servidor de Jokenpo! Você é o jogador 2.");

                while (true) {
                    String escolha1 = null;
                    String escolha2 = null;

                    while (true) {
                        saida1.println("Escolha uma opcao: Pedra, Papel, Tesoura ou Sair.");
                        escolha1 = entrada1.readLine();
                        if (escolha1 == null || !isEscolhaValida(escolha1)) {
                            saida1.println("Opção inválida. Escolha entre Pedra, Papel ou Tesoura.");
                        } else {
                            break;
                        }
                    }

                    while (true) {
                        saida2.println("Escolha uma opcao: Pedra, Papel, Tesoura ou Sair.");
                        escolha2 = entrada2.readLine();
                        if (escolha2 == null || !isEscolhaValida(escolha2)) {
                            saida2.println("Opção inválida. Escolha entre Pedra, Papel ou Tesoura.");
                        } else {
                            break;
                        }
                    }

                    if (escolha1 == null || escolha2 == null) {
                        System.out.println("Um dos jogadores desconectou.");
                        break;
                    }

                    String resultado1 = determinarResultado(escolha1, escolha2);
                    String resultado2 = determinarResultado(escolha2, escolha1);

                    if (resultado1.equals("Você ganhou!")) {
                        vitoriasJogador1++;
                    } else if (resultado1.equals("Você perdeu!")) {
                        vitoriasJogador2++;
                    } else if (resultado1.equals("Empate!")) {
                        empates++;
                    }

                    saida1.println(
                            "Você escolheu " + escolha1 + ". Seu oponente escolheu " + escolha2 + ". " + resultado1);
                    saida2.println(
                            "Você escolheu " + escolha2 + ". Seu oponente escolheu " + escolha1 + ". " + resultado2);

                    // Envia o placar atualizado para ambos os jogadores
                    saida1.println("Placar - Vitórias Jogador 1: " + vitoriasJogador1 + ", Vitórias Jogador 2: "
                            + vitoriasJogador2 + ", Empates: " + empates);
                    saida2.println("Placar - Vitórias Jogador 1: " + vitoriasJogador1 + ", Vitórias Jogador 2: "
                            + vitoriasJogador2 + ", Empates: " + empates);
                }
            } catch (IOException e) {
                System.out.println("Erro ao comunicar com os jogadores: " + e.getMessage());
            } finally {
                try {
                    jogador1.close();
                    jogador2.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar a conexão dos jogadores: " + e.getMessage());
                }
            }
        }

        private boolean isEscolhaValida(String escolha) {
            return escolha.equalsIgnoreCase("Pedra") || escolha.equalsIgnoreCase("Papel")
                    || escolha.equalsIgnoreCase("Tesoura");
        }

        private String determinarResultado(String escolha1, String escolha2) {
            if (escolha1.equalsIgnoreCase(escolha2)) {
                return "Empate!";
            }

            switch (escolha1.toLowerCase()) {
                case "pedra":
                    return (escolha2.equalsIgnoreCase("Tesoura")) ? "Você ganhou!" : "Você perdeu!";
                case "papel":
                    return (escolha2.equalsIgnoreCase("Pedra")) ? "Você ganhou!" : "Você perdeu!";
                case "tesoura":
                    return (escolha2.equalsIgnoreCase("Papel")) ? "Você ganhou!" : "Você perdeu!";
                default:
                    return "Escolha inválida!";
            }
        }
    }
}
