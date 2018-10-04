import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * IA Pract1
 * VentanaEntorno.java
 * Purpose: Clase que define la GUI.
 *
 * @author G.P.A (GUI Preciosa y Asombrosa)
 * @version 1.1.c 10/10/2017
 */

public class VentanaEntorno extends JFrame implements ActionListener, TableModelListener, ChangeListener {

	private static final long serialVersionUID = 1L;
	private Entorno backEnd;
	private JPanel panelContenido;
	private JButton openImage;
	private JLabel imagen, quieroVer;
	private final JFileChooser fc = new JFileChooser();
	private int j = 0;
	ButtonGroup metodos;
	protected AudioFormat audioFormat;
	protected AudioInputStream audioInputStream;
	protected SourceDataLine sourceDataLine;
	protected boolean stopPlayback = false;
	/**
	 * Metodo que observa las acciones realizadas en la interfaz grafica
	 * 
	 * @param e Evento que lanzo este metodo
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == openImage) {
			int returnVal = fc.showOpenDialog(panelContenido);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					backEnd = new Entorno(file.getAbsolutePath());
					quieroVer.setText("abierto bro");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				System.out.println("Open command cancelled by user.");
			}
		}
		imagen.setIcon(backEnd.getImagen());
		pack();
	}

	public VentanaEntorno(Entorno x) {
		ImageIcon img = new ImageIcon("gpa.jpg");
		setIconImage(img.getImage());
		panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		openImage = new JButton("Abrir Imagen");
		quieroVer = new JLabel("Cool");
		imagen = new JLabel();
		openImage.addActionListener(this);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(openImage)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(imagen)
						.addComponent(quieroVer)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(openImage)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(imagen)
						.addComponent(quieroVer)
						)
				);
		this.setContentPane(panelContenido);
		this.setTitle("SIMP");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		pack();
		setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
		this.setVisible(true);
	}
	/**
	 * Metodo que observa cambios realizados en la JTable matriz
	 * @param e Evento que lanzo este metodo
	 */
	public void tableChanged(TableModelEvent e) {
	}
	//=============================================//
	//Inner class to play back the data from the
	// audio file.
	//===================================//
	@Override
	public void stateChanged(ChangeEvent e) {
	    }
		
}
