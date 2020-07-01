import GUI.MainWindow;
import GUI.OptionsWindow;
import Handle.Handler;
import Rendering.Renderer;

import javax.swing.*;

public class Main {

    /**
     * Předmět: PGRF2
     * Autor: Jiří Bönsch
     * Zadání: L-systém
     * Datum poslední úpravy programu: 3. Května 2020
     */

    public static void main(String[] args) {

        Handler handler = new Handler();
        //handler.running = true;

        Thread thread2 = new Thread() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        OptionsWindow options = new OptionsWindow(handler);
                        handler.options = options;
                    }
                });
            }
        };
        Thread thread1 = new Thread() {
            public void run() {
                MainWindow main = new MainWindow(new Renderer(handler), handler);
                handler.main = main;


            }
        };

        thread1.start();
        thread2.start();

    }
}
