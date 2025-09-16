package app;

import model.Estudiante;
import model.Monitor;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    // Semáforos
    public static final Semaphore estudiantes = new Semaphore(0);
    public static final Semaphore monitor = new Semaphore(0);
    public static final Semaphore mutex = new Semaphore(1);
    public static volatile boolean monitorDormido = true;


    // Variables compartidas
    public static final int NUM_SILLAS = 3;
    public static AtomicInteger estudiantesEsperando = new AtomicInteger(0);

    public static void main(String[] args) {
        int totalEstudiantes = 8;

        // Crear monitor
        Thread monitorThread = new Thread(new Monitor());
        monitorThread.start();

        // Crear estudiantes y guardarlos en un arreglo
        Thread[] estudiantesThreads = new Thread[totalEstudiantes];
        for (int i = 0; i < totalEstudiantes; i++) {
            estudiantesThreads[i] = new Thread(new Estudiante(i + 1));
            estudiantesThreads[i].start();
        }

        // Esperar a que todos los estudiantes terminen
        for (Thread t : estudiantesThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Cuando todos los estudiantes terminan, apagamos el monitor
        System.out.println("✅ Todos los estudiantes fueron atendidos. El programa termina.");
        monitorThread.interrupt();
    }
}
