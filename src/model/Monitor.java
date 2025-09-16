package model;
import app.Main;
public class Monitor implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                // Si no hay estudiantes, el monitor se duerme
                if (Main.estudiantes.availablePermits() == 0 && Main.estudiantesEsperando.get() == 0) {
                    System.out.println("El monitor se duerme, no hay estudiantes.");
                }

                // Espera hasta que llegue un estudiante
                Main.estudiantes.acquire();

                Main.mutex.acquire();
                Main.estudiantesEsperando.decrementAndGet();
                System.out.println("\nMonitor ayudando a un estudiante...");
                Main.monitor.release(); // Deja entrar al estudiante
                Main.mutex.release();

                // Simula tiempo de ayuda
                Thread.sleep((long) (Math.random() * 3000));
                System.out.println("Monitor terminó de ayudar\n");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
