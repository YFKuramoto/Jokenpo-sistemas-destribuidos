import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.InputStreamReader;

public class App {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escolha o modo de jogo:");
        System.out.println("1. Jogar contra a maquina");
        System.out.println("2. Jogar online");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 1) {
            Maquina maquina = new Maquina();
            maquina.Jokenpo();
        } else if (escolha == 2) {
            if (args.length < 1) {
                System.out.print("Digite o endereco do servidor: ");
                String servidor = teclado.readLine();

                System.out.print("Digite o numero da porta: ");
                int porta = Integer.parseInt(teclado.readLine());

                Cliente.executar(servidor, porta);
                return;
            }

            String mode = args[0];

            if (mode.equalsIgnoreCase("servidor")) {
                if (args.length < 2) {
                    System.out.println("Uso: java Main servidor <porta>");
                    return;
                }

                int porta = Integer.parseInt(args[1]);
                startServer(porta);
            } else if (mode.equalsIgnoreCase("cliente")) {
                if (args.length < 3) {
                    System.out.println("Uso: java Main cliente <servidor> <porta>");
                    return;
                }

                String servidor = args[1];
                int porta = Integer.parseInt(args[2]);
                startClient(servidor, porta);
            } else {
                System.out.println("Modo inválido. Use 'servidor' ou 'cliente'.");
            }
        } else {
            System.out.println("Escolha inválida. Use 1 para jogar contra a máquina ou 2 para jogar online.");
        }

        scanner.close();
    }

    private static void startServer(int porta) throws IOException {
        new Thread(() -> {
            ServidorMultiplayer.main(new String[] { String.valueOf(porta) });
        }).start();
    }

    private static void startClient(String servidor, int porta) {
        new Thread(() -> {

        }).start();
    }
}
