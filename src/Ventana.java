import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Ventana extends JFrame {
    private int rows = 2;
    private int cols = 3;
    private ArrayList<ImageIcon> imagenes; 
    private JPanel panelBotones;
    private ImageIcon imagenInicial;
    private JButton primerBoton, segundoBoton;
    private boolean esperandoSegundoClick = false;
    private int paresEncontrados = 0; 

    public Ventana() {
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout()); 

        imagenInicial = new ImageIcon("src//images//DORSOCARTA.jpg");
        Image imagenGrande = imagenInicial.getImage();
        imagenGrande = imagenGrande.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        imagenInicial = new ImageIcon(imagenGrande);

        // Cargo las imagenes en el ArrayList
        cargarImagenes();

        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(rows, cols));

        // Crear botones y agregarlos al panel
        for (int i = 0; i < rows * cols; i++) {
            JButton boton = new JButton(imagenInicial);
            boton.setActionCommand(String.valueOf(i)); // Esto le da una especie de ID para identificar el boton
            boton.addActionListener(new ManejadorBoton()); // Le agrego el ActioListener personalizado al boton
            // Las dos lineas siguientes es para que no tengan fondo
            boton.setContentAreaFilled(false);  
            boton.setOpaque(false);
            panelBotones.add(boton); 
        }

        
        add(panelBotones, BorderLayout.CENTER);

        setVisible(true);
    }

    private void cargarImagenes() {
        imagenes = new ArrayList<>();
        for (int i = 1; i <= (rows * cols) / 2; i++) {
            ImageIcon icono = new ImageIcon("src//images//img" + i + ".jpg");
            Image imagen = icono.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(imagen);
            imagenes.add(imageIcon);
            imagenes.add(imageIcon); // Lo hago dos veces para que haya pares de cada carta
        }
        Collections.shuffle(imagenes); // Mezclar aleatoriamente
    }

    private class ManejadorBoton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton boton = (JButton) e.getSource(); // Recojo el boton que ha sido presionado para poder usarlo
            int indice = Integer.parseInt(boton.getActionCommand()); // Recojo el ID del boton para saber que imagen usar

            if (!esperandoSegundoClick) {
                // Primer boton ha sido presionado 
                primerBoton = boton;
                boton.setIcon(imagenes.get(indice));
                esperandoSegundoClick = true;
            } else if (boton != primerBoton) {
                // Segundo boton ha sido presionado
                segundoBoton = boton;
                boton.setIcon(imagenes.get(indice));

                // Lo almaceno en variables que si no la linea de comprobacion es kilometrica
                String nombrePrimeraImagen = imagenes.get(Integer.parseInt(primerBoton.getActionCommand())).toString();
                String nombreSegundaImagen = imagenes.get(Integer.parseInt(segundoBoton.getActionCommand())).toString();

                if (nombrePrimeraImagen.equals(nombreSegundaImagen)) {
                    // Si son iguales deshabilito los dos botones para no poder pulsarlos de nuevo
                    paresEncontrados++;
                    primerBoton.setEnabled(false);
                    segundoBoton.setEnabled(false);
                    primerBoton = null;
                    segundoBoton = null;

                    if (paresEncontrados == 3) {
                        // Y si, los colores lo he elegido a posta, me ha costado mucho tiempo hacer
                        // esto, queria darle el toque personal
                        panelBotones.setVisible(false);
                        JPanel panelJuegoTerminado = new JPanel();
                        panelJuegoTerminado.setLayout(new BorderLayout());
                        panelJuegoTerminado.setBackground(Color.CYAN);

                        JLabel etiqueta = new JLabel("¡Juego Terminado!", SwingConstants.CENTER);
                        etiqueta.setForeground(Color.ORANGE);
                        panelJuegoTerminado.add(etiqueta, BorderLayout.CENTER);

                        setLayout(new BorderLayout());
                        add(panelJuegoTerminado, BorderLayout.CENTER);
                        revalidate();
                        repaint();
                    }
                } else {
                    // Cuando pase un segundo se ejecuta y deja los botones reiniciados
                    Timer timer = new Timer(1000, evt -> {
                        primerBoton.setIcon(imagenInicial);
                        primerBoton.setEnabled(true); 
                        segundoBoton.setIcon(imagenInicial);
                        segundoBoton.setEnabled(true); 
                        primerBoton = null;
                        segundoBoton = null;
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                esperandoSegundoClick = false;
            }
        }
    }

}
