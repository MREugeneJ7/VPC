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

public class VentanaEntorno extends JFrame implements ActionListener, TableModelListener, ChangeListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Entorno backEnd;
	private JPanel panelContenido;
	private JButton openImage, histograma, color;
	private JLabel imagen, datos, posRaton;
	private JComboBox transformacionesLineales;
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
					datos.setText("Tipo:" + backEnd.getType() + " Bits:" + backEnd.getBits()/3 + " " +  
							backEnd.getImagen().getIconWidth() + "x" + backEnd.getImagen().getIconHeight());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				System.out.println("Open command cancelled by user.");
			}
		} else if (e.getSource() == histograma) {
			JFrame f = new JFrame("Histograma");
	        f.add(backEnd.crearPanelHistograma(j));
	        f.pack();
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
		} else if(e.getSource() == color) {
			if(j==0) j = 1;
			else j = 0;
		} else if(e.getSource() == transformacionesLineales) {
			if((transformacionesLineales.getSelectedItem()).equals("brillo")){
				JFrame f = new JFrame("Brillo");
		        f.add(backEnd.crearPaneBrillo());
		        f.pack();
		        f.setLocationRelativeTo(null);
		        f.setVisible(true);
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
		histograma = new JButton("Mostrar Histograma");
		color = new JButton ("color");
		datos = new JLabel("");
		posRaton = new JLabel("");
		imagen = new JLabel();
		String[] listado = {"brillo"};
		transformacionesLineales = new JComboBox(listado);
		
		openImage.addActionListener(this);
		histograma.addActionListener(this);
		color.addActionListener(this);
		transformacionesLineales.addActionListener(this);
		
		imagen.addMouseListener(this);
		
		imagen.addMouseMotionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(openImage)
						.addComponent(histograma)
						.addComponent(color)
						.addComponent(transformacionesLineales)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(imagen)
						)
				.addGroup(layout.createSequentialGroup()
						.addComponent(datos)
						.addComponent(posRaton)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(openImage)
						.addComponent(histograma)
						.addComponent(color)
						.addComponent(transformacionesLineales)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(imagen)
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(datos)
						.addComponent(posRaton)
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		/*backEnd.psicoldelia();
		imagen.setIcon(backEnd.getImagen());
		*/
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		/*try {
			backEnd.mostrarSeleccion();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		imagen.setIcon(backEnd.getImagen());
		backEnd.borrarSeleccion(); */
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseMoved(MouseEvent e){
		posRaton.setText("| X:" + e.getX() + " Y:" + e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//backEnd.guardarSeleccion(e);
		
	}
		
}
