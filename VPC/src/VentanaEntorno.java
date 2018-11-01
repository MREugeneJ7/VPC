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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.Raster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private JPanel panelContenido, panelHistograma;
	private JButton openImage, histograma, color, acumulativo , aceptar;
	private JLabel imagen, datos, posRaton;
	private JComboBox transformacionesLineales;
	private JTextField valor;
	private final JFileChooser fc = new JFileChooser();
	private int j = 0;
	ButtonGroup metodos;
	protected AudioFormat audioFormat;
	protected AudioInputStream audioInputStream;
	protected SourceDataLine sourceDataLine;
	protected boolean stopPlayback = false, isAcumulativo = false;
	private XYBarRenderer renderer;
	private JFrame h, b;
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
					Aplicacion.logger.log(Level.WARNING, "No se pudo abrir la imagen", e1);
				}
			} else {
				System.out.println("Open command cancelled by user.");
			}
		} else if (e.getSource() == histograma) {
			h = new JFrame("Histograma");
			panelHistograma = crearPanelHistograma();
	        h.add(panelHistograma);
	        h.pack();
	        h.setLocationRelativeTo(null);
	        h.setVisible(true);
		} else if(e.getSource() == color) {
			if(j==0) j = 1;
			else j = 0;
		} else if(e.getSource() == transformacionesLineales) {
			if((transformacionesLineales.getSelectedItem()).equals("brillo")){
				b = new JFrame("Brillo");
		        b.add(crearPaneBrillo());
		        b.pack();
		        b.setLocationRelativeTo(null);
		        b.setVisible(true);
			}
		} else if (e.getSource()==aceptar){
			int brillo = Integer.parseInt(valor.getText());
			backEnd.cambiarBrillo(brillo);
		} else if (e.getSource()==acumulativo) {
			if(!isAcumulativo) isAcumulativo = true;
			else isAcumulativo = false;
		}
		imagen.setIcon(backEnd.getImagen());
		pack();
	}

	private Chart crearPanelHistograma() {
		// TODO Auto-generated method stub
		HistogramDataset auxDataset = new HistogramDataset();
        Raster raster = backEnd.getImagenBf().getRaster();
        final int w = backEnd.getImagenBf().getWidth();
        final int h = backEnd.getImagenBf().getHeight();
        double[] r = new double[w * h +1];
        if(!isAcumulativo) {
	        if(j==1) { 
	        r = raster.getSamples(0, 0, w, h, 0, r);
	        auxDataset.addSeries("Red", r, 256);
	        r = raster.getSamples(0, 0, w, h, 1, r);
	        auxDataset.addSeries("Green", r, 256);
	        r = raster.getSamples(0, 0, w, h, 2, r);
	        auxDataset.addSeries("Blue", r, 256);
	        backEnd.setDataset(auxDataset);
	        }
	        //Histograma a color
	        if(j==0){
	        	r = Arrays.stream(raster.getSamples(0, 0, w, h, 0, r)).map(i -> i * 0.33).toArray();
	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 1, r)).map(i -> i * 0.33).toArray());
	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 2, r)).map(i -> i * 0.33).toArray());
	        	auxDataset.addSeries("Luminosidad", r, 256);
	        	backEnd.setDataset(auxDataset);
	        }
        } else {
        	if(j==1) { 
    	        r = ArrayMaths.Acumulativo(raster.getSamples(0, 0, w, h, 0, r));
    	        auxDataset.addSeries("Red", r, 256);
    	        r = ArrayMaths.Acumulativo(raster.getSamples(0, 0, w, h, 1, r));
    	        auxDataset.addSeries("Green", r, 256);
    	        r = ArrayMaths.Acumulativo(raster.getSamples(0, 0, w, h, 2, r));
    	        auxDataset.addSeries("Blue", r, 256);
    	        backEnd.setDataset(auxDataset);
    	        }
    	        //Histograma a color
    	        if(j==0){
    	        	r = Arrays.stream(raster.getSamples(0, 0, w, h, 0, r)).map(i -> i * 0.33).toArray();
    	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 1, r)).map(i -> i * 0.33).toArray());
    	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 2, r)).map(i -> i * 0.33).toArray());
    	        	r = ArrayMaths.Acumulativo(r);
    	        	auxDataset.addSeries("Luminosidad", r, 256);
    	        	backEnd.setDataset(auxDataset);
    	        }

        }
       
            Chart panel = new Chart();
            return panel;
	}
	

	private Component crearPaneBrillo() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		valor = new JTextField(10);
		aceptar = new JButton("Aceptar");
		
		aceptar.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(valor)
						.addComponent(aceptar)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(valor)
						.addComponent(aceptar)
						)
				);
		
		return panelContenido;
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
		acumulativo = new JButton("H. Acum");
		datos = new JLabel("");
		posRaton = new JLabel("");
		imagen = new JLabel();
		String[] listado = {"brillo"};
		transformacionesLineales = new JComboBox(listado);
		
		openImage.addActionListener(this);
		histograma.addActionListener(this);
		color.addActionListener(this);
		acumulativo.addActionListener(this);
		transformacionesLineales.addActionListener(this);
		
		imagen.addMouseListener(this);
		
		imagen.addMouseMotionListener(this);
		
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(openImage)
						.addComponent(histograma)
						.addComponent(color)
						.addComponent(acumulativo)
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
						.addComponent(acumulativo)
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
		imagen.setIcon(backEnd.getImagen());
		pack();
		
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
