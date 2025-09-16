package model;



import app.Main;


public class Estudiante implements Runnable {
    private int id;

    public Estudiante(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Simula programar
                Thread.sleep((long) (Math.random() * 4000));

                Main.mutex.acquire();

                // Caso 1: El monitor está libre y dormido
                if (Main.estudiantesEsperando.get() == 0 && Main.estudiantes.availablePermits() == 0) {
                    System.out.println("🔔 Estudiante " + id + " despierta al monitor y entra directo a recibir ayuda.");
                    Main.estudiantes.release(); // Despierta al monitor
                    Main.mutex.release();

                    Main.monitor.acquire(); // Espera que el monitor le atienda
                    System.out.println("Estudiante " + id + " está recibiendo ayuda");

                }
                // Caso 2: El monitor está ocupado, pero hay sillas disponibles
                else if (Main.estudiantesEsperando.get() < Main.NUM_SILLAS) {
                    Main.estudiantesEsperando.incrementAndGet();
                    System.out.println("Estudiante " + id + " espera en la fila. Ocupadas: " + Main.estudiantesEsperando.get());
                    Main.estudiantes.release(); // Notifica al monitor
                    Main.mutex.release();

                    Main.monitor.acquire(); // Espera ser atendido
                    System.out.println("Estudiante " + id + " está recibiendo ayuda");

                }
                // Caso 3: No hay sillas → se va
                else {
                    System.out.println("Estudiante " + id + " no encontró silla, vuelve luego.");
                    Main.mutex.release();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
