import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void executar(String servidor, int porta) {
        try (Socket socket = new Socket(servidor, porta);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor de Jokenpo.");

            // Lê a mensagem inicial do servidor
            System.out.println(entrada.readLine());

            while (true) {
                // Lê as instruções do servidor
                String instrucao = entrada.readLine();
                System.out.println(instrucao);

                // Envia a escolha do jogador
                String escolha = teclado.readLine().toUpperCase();
                saida.println(escolha);

                // Se o jogador quiser sair, termina o loop
                if (escolha.equals("SAIR")) {
                    break;
                }

                // Lê o resultado do jogo
                String resultado = entrada.readLine();
                System.out.println(resultado);

                // Lê os contadores
                String contadores = entrada.readLine();
                System.out.println(contadores);
            }

            System.out.println("Você saiu do jogo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
