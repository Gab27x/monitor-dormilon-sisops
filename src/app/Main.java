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

    // Variables compartidas
    public static final int NUM_SILLAS = 3;
    public static AtomicInteger estudiantesEsperando = new AtomicInteger(0);

    public static void main(String[] args) {
        // Crear monitor
        Thread monitorThread = new Thread(new Monitor());
        monitorThread.start();

        // Crear estudiantes
        for (int i = 1; i <= 8; i++) {
            new Thread(new Estudiante(i)).start();
        }
    }
}