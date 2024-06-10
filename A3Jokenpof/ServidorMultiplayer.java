import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMultiplayer {
    private static int porta;
    private static final int MAX_JOGADORES = 2;
    private static final ExecutorService pool = Executors.newFixedThreadPool(MAX_JOGADORES);
    private static final ConcurrentLinkedQueue<Socket> jogadoresConectados = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Digite a porta do servidor: ");
            porta = Integer.parseInt(teclado.readLine());
        } catch (IOException e) {
            System.out.println("Erro ao ler a porta do servidor: " + e.getMessage());
            return;
        }

        System.out.println("Servidor de Jokenpo iniciado na porta " + porta);

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            while (true) {
                Socket jogador = serverSocket.accept();
                System.out.println("Novo jogador conectado: " + jogador.getInetAddress());
                jogadoresConectados.add(jogador);

                if (jogadoresConectados.size() >= MAX_JOGADORES) {
                    iniciaJogo();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void iniciaJogo() {
        Socket jogador1 = jogadoresConectados.poll();
        Socket jogador2 = jogadoresConectados.poll();
        System.out.println("Iniciando jogo entre " + jogador1.getInetAddress() + " e " + jogador2.getInetAddress());
        pool.execute(new JogoHandler(jogador1, jogador2));
    }

    private static class JogoHandler implements Runnable {
        private final Socket jogador1;
        private final Socket jogador2;

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
                PrintWriter saida2 = new PrintWriter(jogador2.getOutputStream(), true)
            ) {
                saida1.println("Bem-vindo ao servidor de Jokenpo! Você é o jogador 1.");
                saida2.println("Bem-vindo ao servidor de Jokenpo! Você é o jogador 2.");

                // Loop do jogo
                while (true) {
                    saida1.println("Escolha uma opção: Pedra, Papel, Tesoura ou Sair");
                    saida2.println("Escolha uma opção: Pedra, Papel, Tesoura ou Sair");

                    String escolha1 = entrada1.readLine();
                    String escolha2 = entrada2.readLine();

                    if (escolha1 == null || escolha2 == null || escolha1.equalsIgnoreCase("SAIR") || escolha2.equalsIgnoreCase("SAIR")) {
                        break;
                    }

                    if (!validaEscolha(escolha1) || !validaEscolha(escolha2)) {
                        saida1.println("Escolha inválida. Tente novamente.");
                        saida2.println("Escolha inválida. Tente novamente.");
                        continue;
                    }

                    String resultado1 = determinarResultado(escolha1, escolha2);
                    String resultado2 = determinarResultado(escolha2, escolha1);

                    saida1.println("Você escolheu " + escolha1 + ". Seu oponente escolheu " + escolha2 + ". " + resultado1);
                    saida2.println("Você escolheu " + escolha2 + ". Seu oponente escolheu " + escolha1 + ". " + resultado2);

                    saida1.println("Vitórias Jogador 1: " + vitorias1 + ", Derrotas Jogador 1: " + derrotas1 + ", Empates: " + empates);
                    saida2.println("Vitórias Jogador 2: " + vitorias2 + ", Derrotas Jogador 2: " + derrotas2 + ", Empates: " + empates);
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

        private boolean validaEscolha(String escolha) {
            return escolha.equalsIgnoreCase("PEDRA") || escolha.equalsIgnoreCase("PAPEL") || escolha.equalsIgnoreCase("TESOURA");
        }

        private String determinarResultado(String escolha1, String escolha2) {
            if (escolha1.equalsIgnoreCase(escolha2)) {
                empates++;
                return "Empate!";
            }

            if ((escolha1.equalsIgnoreCase("PEDRA") && escolha2.equalsIgnoreCase("TESOURA")) ||
                (escolha1.equalsIgnoreCase("PAPEL") && escolha2.equalsIgnoreCase("PEDRA")) ||
                (escolha1.equalsIgnoreCase("TESOURA") && escolha2.equalsIgnoreCase("PAPEL"))) {
                vitorias1++;
                derrotas2++;
                return "Você ganhou!";
            } else {
                derrotas1++;
                vitorias2++;
                return "Você perdeu!";
            }
        }

        private int vitorias1 = 0;
        private int derrotas1 = 0;
        private int vitorias2 = 0;
        private int derrotas2 = 0;
        private int empates = 0;
    }
}