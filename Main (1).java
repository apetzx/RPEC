import java.util.*;

class Node {
    int data;
    Node next;

    public Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class Pilha {
    Node top;

    public Pilha() {
        this.top = null;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void push(int data) {
        Node newNode = new Node(data);
        newNode.next = top;
        top = newNode;
    }

    public int pop() {
        if (isEmpty()) {
            return -1;
        }
        int data = top.data;
        top = top.next;
        return data;
    }

    public int peek() {
        if (isEmpty()) {
            System.out.println("Erro: A pilha está vazia.");
            return -1;
        }
        return top.data;
    }

    public int size() {
        int count = 0;
        Node current = top;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Informe o tamanho das pilhas: ");
        int tamanhoPilhas = scanner.nextInt();
        System.out.print("Deseja ordenar em ordem crescente (1) ou decrescente (2): ");
        int escolha = scanner.nextInt();
        boolean ordemCrescente = (escolha == 1);
        
        
        Pilha pilha1 = new Pilha();
        Pilha pilha2 = new Pilha();
        Pilha pilha3 = new Pilha();
        
        Random random = new Random();
        for (int i = 0; i < tamanhoPilhas; i++) {
            pilha1.push(random.nextInt(100) + 1);
        }
        System.out.println("\nPilha 1: " + Imprimir(pilha1));
        System.out.println("Pilha 2: " + Imprimir(pilha2));
        System.out.println("Pilha 3: \n " + Imprimir(pilha3));

        System.out.print("Deseja jogar manualmente (1), automaticamente (2) ou sair do jogo (0): ");
        int modoJogo = scanner.nextInt();

        int jogadas = 0;
        
        if(modoJogo==1){
            long startTime = System.currentTimeMillis();
            while (true) {

                System.out.println("\nPilha 1: " + Imprimir(pilha1));
                System.out.println("Pilha 2: " + Imprimir(pilha2));
                System.out.println("Pilha 3: " + Imprimir(pilha3));

                System.out.println("\nEscolha a pilha de origem (1, 2, 3) ou 0 para sair: ");
                int origem = scanner.nextInt();

                if (origem == 0) {
                    System.out.println("Jogo encerrado.");
                    break;
                } else if (origem < 1 || origem > 3) {
                    System.out.println("Pilha de origem inválida. Escolha entre 1, 2 ou 3.");
                    continue;
                }

                System.out.println("Escolha a pilha de destino (1, 2, 3): ");
                int destino = scanner.nextInt();

                if (destino < 1 || destino > 3) {
                    System.out.println("Pilha de destino inválida. Escolha entre 1, 2 ou 3.");
                    continue;
                }

                Pilha pilhaOrigem, pilhaDestino;
                if (origem == 1) pilhaOrigem = pilha1;
                else if (origem == 2) pilhaOrigem = pilha2;
                else pilhaOrigem = pilha3;

                if (destino == 1) pilhaDestino = pilha1;
                else if (destino == 2) pilhaDestino = pilha2;
                else pilhaDestino = pilha3;

                if (pilhaOrigem.isEmpty()) {
                    System.out.println("Pilha de origem está vazia.");
                    continue;
                }

                int numeroMovido = pilhaOrigem.pop();
                if ((!ordemCrescente && !pilhaDestino.isEmpty() && numeroMovido < pilhaDestino.peek()) ||
                    (ordemCrescente && !pilhaDestino.isEmpty() && numeroMovido > pilhaDestino.peek())) {
                    System.out.println("Movimento inválido. O número deve estar na ordem correta em relação à pilha de destino.");
                    pilhaOrigem.push(numeroMovido);
                } else {
                    pilhaDestino.push(numeroMovido);
                    jogadas++;

                    if (pilhaDestino.size() == tamanhoPilhas && isOrdenada(pilhaDestino, ordemCrescente)) {
                        System.out.println("\nPilha 1: " + Imprimir(pilha1));
                        System.out.println("Pilha 2: " + Imprimir(pilha2));
                        System.out.println("Pilha 3:" + Imprimir(pilha3));
                        System.out.println();
                        System.out.println("Ordenaçao concluída em " + jogadas + " jogadas.");
                        long endTime = System.currentTimeMillis();
                        long tempoDecorrido = endTime - startTime;
                        long tempoSegundos  = tempoDecorrido/1000;
                        if (tempoSegundos >= 60) {
                            long minutos = tempoSegundos / 60;
                            long segundosRestantes = tempoSegundos % 60;
                            System.out.println("Tempo decorrido: " + minutos + " minutos e " + segundosRestantes + " segundos");
                        } else {
                            System.out.println("Tempo decorrido: " + tempoSegundos + " segundos");
                        }
                        
                        scanner.close();
                        break;
                    }
                }
            }
        }
        else if(modoJogo == 2){
            
            int a = solucaoAutomatica( pilha1, pilha3, pilha2, tamanhoPilhas, ordemCrescente);
            System.out.println("\nPilha 1: " + Imprimir(pilha1));
            System.out.println("Pilha 2: " + Imprimir(pilha2));
            System.out.println("Pilha 3:" + Imprimir(pilha3));
            System.out.println(a + " Passos necessarios\n");

        }   

        else if(modoJogo == 0){
            System.out.println("Falou cria");
            scanner.close();
            return;
        }

        else{
            System.out.println("Modo de jogo inválido.");
        }
        scanner.close();
    }
    
    public static int solucaoAutomatica(Pilha origem, Pilha destino, Pilha intermediaria, int tamanho, boolean ordemCrescente) {
        int jogadas = 0;
        if (tamanho > 0) {
            solucaoAutomatica(origem, intermediaria, destino, tamanho - 1, ordemCrescente);

            int numero = origem.pop();
            destino.push(numero);
            jogadas++;

            jogadas+=ordenarPilhas(origem, destino, intermediaria, ordemCrescente);

            jogadas += solucaoAutomatica(intermediaria, destino, origem, tamanho - 1, ordemCrescente);
        }
        return jogadas;
    }
    
    public static int ordenarPilhas(Pilha origem, Pilha destino, Pilha intermediaria, boolean ordemCrescente) {
        int jogadas = 0;
        while (!origem.isEmpty()) {
            int numero = origem.pop();
            if (ordemCrescente) {
                while (!destino.isEmpty() && destino.peek() < numero) {
                    intermediaria.push(destino.pop());
                    jogadas++;
                }
            } else {
                while (!destino.isEmpty() && destino.peek() > numero) {
                    intermediaria.push(destino.pop());
                    jogadas++;
                }
            }
            destino.push(numero);
            while (!intermediaria.isEmpty()) {
                destino.push(intermediaria.pop());
                jogadas++;
            }
        }
        return jogadas;
    }
    private static boolean isOrdenada(Pilha pilha, boolean ordemCrescente) {
        Pilha aux = new Pilha();
        int prev = ordemCrescente ? -1 : 101;

        while (!pilha.isEmpty()) {
            int numero = pilha.pop();
            aux.push(numero);
            
            if (ordemCrescente && numero < prev) return false;
            if (!ordemCrescente && numero > prev) return false;
            
            prev = numero;
        }

        while (!aux.isEmpty()) {
            pilha.push(aux.pop());
        }

        return true;
    }
    
    private static String Imprimir(Pilha pilha) {
        StringBuilder sb = new StringBuilder();
        Pilha aux = new Pilha();
        while (!pilha.isEmpty()) {
            int numero = pilha.pop();
            if(numero != -1){
                sb.append(numero).append(" ");
                aux.push(numero);
            }
        }
        while (!aux.isEmpty()) {
            pilha.push(aux.pop());
        }
        return sb.toString();
    }
    
}