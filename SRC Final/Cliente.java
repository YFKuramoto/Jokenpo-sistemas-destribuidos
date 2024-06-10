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

            System.out.println(entrada.readLine());

            while (true) {

                String instrucao = entrada.readLine();
                if (instrucao == null) {
                    System.out.println("Conexão com o servidor perdida.");
                    break;
                }
                System.out.println(instrucao);

                String escolha = teclado.readLine().toUpperCase();
                saida.println(escolha);

                if (escolha.equals("SAIR")) {
                    break;
                }

                String resultado = entrada.readLine();
                if (resultado == null) {
                    System.out.println("Conexão com o servidor perdida.");
                    break;
                }
                System.out.println(resultado);

                String contadores = entrada.readLine();
                if (contadores == null) {
                    System.out.println("Conexão com o servidor perdida.");
                    break;
                }
                System.out.println(contadores);
            }

            System.out.println("Você saiu do jogo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
