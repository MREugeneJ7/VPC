import javax.imageio.ImageIO;
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
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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

public class VentanaEntorno extends JFrame implements ActionListener, TableModelListener, ChangeListener, MouseListener, MouseMotionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private Entorno backEnd;
	private JPanel panelContenido, panelHistograma;
	private JButton openImage, histograma, color, acumulativo , aceptar, aceptar1, aceptar2, aceptar3, aceptar4, aceptar5, imagenDiferencia, guardar;
	private JLabel imagen, datos, posRaton;
	private JComboBox transformacionesLineales, transformacionesNoLineales, operacionesHistograma;
	private JCheckBox red, green, blue;
	private JTextField valor, valor1;
	private final JFileChooser fc = new JFileChooser();
	private int j = 0;
	ButtonGroup metodos;
	protected AudioFormat audioFormat;
	protected AudioInputStream audioInputStream;
	protected SourceDataLine sourceDataLine;
	protected boolean stopPlayback = false, isAcumulativo = false;
	private XYBarRenderer renderer;
	private JFrame h, b, c, tf, g, d, e, dt;
	private String path;
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
					path = file.getAbsolutePath();
					backEnd = new Entorno(file.getAbsolutePath());
					datos.setText("Tipo:" + backEnd.getType() + " Bits:" + backEnd.getBits() + " " +  
							backEnd.getImagen().getIconWidth() + "x" + backEnd.getImagen().getIconHeight());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Aplicacion.logger.log(Level.WARNING, "No se pudo abrir la imagen", e1);
				}
			} else {
				System.out.println("Open command cancelled by user.");
			}
		} else if(e.getSource() == guardar){
			int returnVal = fc.showSaveDialog(panelContenido);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					ImageIO.write(backEnd.getImagenBf(), backEnd.getType(), file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Aplicacion.logger.log(Level.WARNING, "No se pudo abrir la imagen", e1);
				}
			} else {
				System.out.println("Open command cancelled by user.");
			}
		}
		else if (e.getSource() == histograma) {
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
			} else if((transformacionesLineales.getSelectedItem()).equals("contraste")) {
				c = new JFrame("Contraste");
		        c.add(crearPaneContraste());
		        c.pack();
		        c.setLocationRelativeTo(null);
		        c.setVisible(true);
			}else if((transformacionesLineales.getSelectedItem()).equals("Escala de grises")) backEnd.grayScale();
			else if((transformacionesLineales.getSelectedItem()).equals("Negativo")) backEnd.negative();
			else if((transformacionesLineales.getSelectedItem()).equals("Daltonismo")) {
				dt = new JFrame("Daltonismo");
		        dt.add(crearPaneD());
		        dt.pack();
		        dt.setLocationRelativeTo(null);
		        dt.setVisible(true);
			}
			else {
				tf = new JFrame("Transformacion lineal");
		        tf.add(crearPaneTL());
		        tf.pack();
		        tf.setLocationRelativeTo(null);
		        tf.setVisible(true);
			}
		} else if (e.getSource()==aceptar){
			int brillo = Integer.parseInt(valor.getText());
			backEnd.cambiarBrillo(brillo);
		} else if (e.getSource()==aceptar1) {
			float contraste = Float.parseFloat(valor.getText());
			backEnd.cambiarContraste(contraste);
		} else if (e.getSource() == aceptar2){
			int brillo = Integer.parseInt(valor.getText());
			float contraste = Float.parseFloat(valor1.getText());
			backEnd.cambiarBrillo(brillo);
			backEnd.cambiarContraste(contraste);
		}else if(e.getSource()==aceptar3){
			double gamma = Double.parseDouble(valor.getText());
			backEnd.gamma(gamma);
		}
		else if (e.getSource()==acumulativo) {
			if(!isAcumulativo) isAcumulativo = true;
			else isAcumulativo = false;
		}else if(e.getSource()==transformacionesNoLineales){
			g = new JFrame("Gamma");
	        g.add(crearPanelGamma());
	        g.pack();
	        g.setLocationRelativeTo(null);
	        g.setVisible(true);
		}else if(e.getSource()==operacionesHistograma) {
			if((operacionesHistograma.getSelectedItem()).equals("Diferencia")) {
				d = new JFrame("Diferencia");
		        d.add(crearPanelDiferencia());
		        d.pack();
		        d.setLocationRelativeTo(null);
		        d.setVisible(true);
			}else if((operacionesHistograma.getSelectedItem()).equals("Especificar")) {
				this.e = new JFrame("Especificar histograma");
		        this.e.add(crearPanelEspecificarHistograma());
		        this.e.pack();
		        this.e.setLocationRelativeTo(null);
		        this.e.setVisible(true);
			}else backEnd.ecualizar();
			
		}else if(e.getSource() == imagenDiferencia) {
			int returnVal = fc.showOpenDialog(panelContenido);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
					path = file.getAbsolutePath();
			} else {
				System.out.println("Open command cancelled by user.");
			}
		}else if(e.getSource() == aceptar4) {
			int umbral = Integer.parseInt(valor.getText());
			try {
				backEnd.calcularDiferencia(path , umbral);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Aplicacion.logger.log(Level.WARNING, "No se pudo abrir la imagen", e1);
			}
		}else if(e.getSource() == aceptar5) {
			try {
				backEnd.specifyHisotgram(path);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Aplicacion.logger.log(Level.WARNING, "No se pudo abrir la imagen", e1);
			}
		}
		imagen.setIcon(backEnd.getImagen());
		pack();
	}

	private Component crearPaneD() {
		// TODO Auto-generated method stub
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		red = new JCheckBox("red");
		red.setMnemonic(KeyEvent.VK_R); 
	    red.setSelected(true);
		green = new JCheckBox("green");
		green.setMnemonic(KeyEvent.VK_G); 
	    green.setSelected(true);
		blue = new JCheckBox("blue");
		blue.setMnemonic(KeyEvent.VK_B); 
	    blue.setSelected(true);
		
		red.addItemListener(this);
		green.addItemListener(this);
		blue.addItemListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(red)
						.addComponent(green)
						.addComponent(blue)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(red)
						.addComponent(green)
						.addComponent(blue)
						)
				);
		
		return panelContenido;
	}

	private Component crearPanelEspecificarHistograma() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		imagenDiferencia = new JButton("Imagen");
		aceptar5 = new JButton("Aceptar");
		
		aceptar5.addActionListener(this);
		imagenDiferencia.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(imagenDiferencia)
						.addComponent(aceptar5)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(imagenDiferencia)
						.addComponent(aceptar5)
						)
				);
		
		return panelContenido;
	}

	private Component crearPanelDiferencia() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		valor = new JTextField(10);
		imagenDiferencia = new JButton("Imagen");
		aceptar4 = new JButton("Aceptar");
		
		aceptar4.addActionListener(this);
		imagenDiferencia.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(valor)
						.addComponent(imagenDiferencia)
						.addComponent(aceptar4)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(valor)
						.addComponent(imagenDiferencia)
						.addComponent(aceptar4)
						)
				);
		
		return panelContenido;
	}

	private Component crearPanelGamma() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		valor = new JTextField(10);
		aceptar3 = new JButton("Aceptar");
		
		aceptar3.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(valor)
						.addComponent(aceptar3)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(valor)
						.addComponent(aceptar3)
						)
				);
		
		return panelContenido;
	}

	private Component crearPaneTL() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		valor = new JTextField(10);
		valor1 = new JTextField(10);
		JLabel v1 = new JLabel("V1:");
		JLabel v0 = new JLabel(" + V0* ");
		aceptar2 = new JButton("Aceptar");
		
		aceptar2.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(v1)
								.addComponent(valor)
								.addComponent(v0)
								.addComponent(valor1)
						)
						.addComponent(aceptar2)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(v1)
						.addComponent(valor)
						.addComponent(v0)
						.addComponent(valor1)
					)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(aceptar2)
					)
				);
		
		return panelContenido;
	}

	private Component crearPaneContraste() {
		JPanel panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		valor = new JTextField(10);
		aceptar1 = new JButton("Aceptar");
		
		aceptar1.addActionListener(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(valor)
						.addComponent(aceptar1)
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(valor)
						.addComponent(aceptar1)
						)
				);
		
		return panelContenido;
	}

	private ChartPanel crearPanelHistograma() {
		// TODO Auto-generated method stub
		HistogramDataset auxDataset = new HistogramDataset();
        Raster raster = backEnd.getImagenBf().getRaster();
        final int w = backEnd.getImagenBf().getWidth();
        final int h = backEnd.getImagenBf().getHeight();
        double[] r = new double[w * h +1];
        if(!isAcumulativo) {
        	if(backEnd.getBits() == 8) {
        		r = raster.getSamples(0, 0, w, h, 0, r);
        		auxDataset.addSeries("Luminosidad", r, 256);
        		backEnd.setDataset(auxDataset);
        	}
        	else {  if(j==1) { 
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
		        	r = ArrayMaths.multiply(raster.getSamples(0, 0, w, h, 0, r),0.33);
		        	r = ArrayMaths.Add(r , ArrayMaths.multiply(raster.getSamples(0, 0, w, h, 1, r),0.33));
		        	r = ArrayMaths.Add(r , ArrayMaths.multiply(raster.getSamples(0, 0, w, h, 2, r),0.33));
		        	auxDataset.addSeries("Luminosidad", r, 256);
		        	backEnd.setDataset(auxDataset);
		        }
        	}
        } else {
        	if(j==1) { 
    	        r = ArrayMaths.toDouble(ArrayMaths.HistogramaAcumulativo(raster.getSamples(0, 0, w, h, 0, r)));
    	        auxDataset.addSeries("Red", r, 256);
    	        r = ArrayMaths.toDouble(ArrayMaths.HistogramaAcumulativo(raster.getSamples(0, 0, w, h, 1, r)));
    	        auxDataset.addSeries("Green", r, 256);
    	        r = ArrayMaths.toDouble(ArrayMaths.HistogramaAcumulativo(raster.getSamples(0, 0, w, h, 2, r)));
    	        auxDataset.addSeries("Blue", r, 256);
    	        backEnd.setDataset(auxDataset);
    	        }
    	        //Histograma a color
    	        if(j==0){
    	        	r = Arrays.stream(raster.getSamples(0, 0, w, h, 0, r)).map(i -> i * 0.33).toArray();
    	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 1, r)).map(i -> i * 0.33).toArray());
    	        	r = ArrayMaths.Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 2, r)).map(i -> i * 0.33).toArray());
    	        	r = ArrayMaths.toDouble(ArrayMaths.HistogramaAcumulativo(r));
    	        	auxDataset.addSeries("Luminosidad", r, 256);
    	        	backEnd.setDataset(auxDataset);
    	        }

        }
       
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
                "Count", backEnd.getDataSet(), PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = (XYPlot) chart.getPlot();
            renderer = (XYBarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardXYBarPainter());
            // translucent red, green & blue
            Paint[] paintArray = {
                
            	new Color(0x80ff0000, true),
               	new Color(0x8000ff00, true),
               	new Color(0x800000ff, true)
                //Colores color
            };
            Paint[] black = {new Color(0xff000000, true)};
            if(j==0 || backEnd.getBits() == 8) paintArray = black; 
            
            plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
            ChartPanel panel = new ChartPanel(chart);
            panel.setMouseWheelEnabled(true);
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
		guardar = new JButton("Guardar");
		histograma = new JButton("Mostrar Histograma");
		color = new JButton ("color");
		acumulativo = new JButton("H. Acum");
		datos = new JLabel("");
		posRaton = new JLabel("");
		imagen = new JLabel();
		String[] listado = {"brillo","contraste", "Transformacion Lineal", "Escala de grises", "Negativo", "Daltonismo"};
		transformacionesLineales = new JComboBox(listado);
		String[] listado1 = {"Gamma"};
		transformacionesNoLineales = new JComboBox(listado1);
		String[] listado2 = {"Ecualizar" , "Diferencia", "Especificar"};
		operacionesHistograma = new JComboBox(listado2);
		
		openImage.addActionListener(this);
		guardar.addActionListener(this);
		histograma.addActionListener(this);
		color.addActionListener(this);
		acumulativo.addActionListener(this);
		transformacionesLineales.addActionListener(this);
		transformacionesNoLineales.addActionListener(this);
		operacionesHistograma.addActionListener(this);
		
		imagen.addMouseListener(this);
		
		imagen.addMouseMotionListener(this);
		
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(openImage)
								.addComponent(guardar))
						.addComponent(histograma)
						.addComponent(color)
						.addComponent(acumulativo)
						.addComponent(transformacionesLineales)
						.addComponent(transformacionesNoLineales)
						.addComponent(operacionesHistograma)
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
						.addGroup(layout.createSequentialGroup()
								.addComponent(openImage)
								.addComponent(guardar))
						.addComponent(histograma)
						.addComponent(color)
						.addComponent(acumulativo)
						.addComponent(transformacionesLineales)
						.addComponent(transformacionesNoLineales)
						.addComponent(operacionesHistograma)
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

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getItemSelectable();

	    if (source == red) {
	        backEnd.daltonismo(1);
	    } else if (source == green) {
	    	backEnd.daltonismo(2);
	    } else if (source == blue) {
	    	backEnd.daltonismo(3);
	    }
	    imagen.setIcon(backEnd.getImagen());
		pack();
	}


	
		
}
