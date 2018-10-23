import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

public class Entorno implements ActionListener {
	
	private ImageIcon imagen;
	private BufferedImage imagenBf;
	private String direccion,tipoImagen;
	private int bits, color;
	private HistogramDataset dataset;
	private XYBarRenderer renderer;
	private Linea seleccion;
	private JTextField valor;
	private JButton aceptar;

	public Entorno(String absolutePath) throws IOException {
		direccion = absolutePath;
		imagen = new ImageIcon(absolutePath);
		tipoImagen = direccion.substring(direccion.lastIndexOf('.'));
		imagenBf = ImageIO.read(new File(direccion));
		bits = imagenBf.getColorModel().getPixelSize();
		seleccion = new Linea(imagenBf.getHeight()*imagenBf.getWidth());
	}

	public Entorno() {
		// TODO Auto-generated constructor stub
	}

	public Icon getImagen() {
		// TODO Auto-generated method stub
		return imagen;
	}
	
	public String getType() {
		return tipoImagen;
	}
	
	public int getBits() {
		return bits;
	}

	public ChartPanel crearPanelHistograma(int j) {
		dataset = new HistogramDataset();
        Raster raster = imagenBf.getRaster();
        final int w = imagenBf.getWidth();
        final int h = imagenBf.getHeight();
        double[] r = new double[w * h];
        if(j==1) { 
        r = raster.getSamples(0, 0, w, h, 0, r);
        dataset.addSeries("Red", r, 256);
        r = raster.getSamples(0, 0, w, h, 1, r);
        dataset.addSeries("Green", r, 256);
        r = raster.getSamples(0, 0, w, h, 2, r);
        dataset.addSeries("Blue", r, 256);
        }
        //Histograma a color
        if(j==0){
        	r = Arrays.stream(raster.getSamples(0, 0, w, h, 0, r)).map(i -> i * 0.33).toArray();
        	r = Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 1, r)).map(i -> i * 0.33).toArray());
        	r = Add(r , Arrays.stream(raster.getSamples(0, 0, w, h, 2, r)).map(i -> i * 0.33).toArray());
        	dataset.addSeries("Luminosidad", r, 256);
        }
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
                "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
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
            if(j==0) paintArray = black; 
            
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

	private double[] Add(double[] r, double[] array) {
		for(int i = 0; i<r.length; i++){
			r[i] = r[i] + array[i];
		}
		return r;
	}

	public Object getDataSet() {
		// TODO Auto-generated method stub
		return dataset;
	}

	public Object getRenderer() {
		// TODO Auto-generated method stub
		return renderer;
	}

	public void guardarSeleccion(MouseEvent e) {
		// TODO Auto-generated method stub
		seleccion.add(e.getX(),e.getY());
		
	}

	public void borrarSeleccion() {
		// TODO Auto-generated method stub
		seleccion = new Linea(imagenBf.getHeight()*imagenBf.getWidth());
		
	}

	public void mostrarSeleccion() throws IOException {
		// TODO Auto-generated method stub
		imagen = new ImageIcon(imagenBf);
		BufferedImage imgaux = ImageIO.read(new File(direccion));
		for(int i=0; i< seleccion.getLastIndex();i++) {
			Coordenada aux = seleccion.getCoordenada(i);
			imgaux.setRGB(aux.getX(), aux.getY(), 0);
		}
		imagen = new ImageIcon(imgaux);
	}

	public boolean seleccionVacia() {
		// TODO Auto-generated method stub
		return (seleccion.getLastIndex() == 0);
	}

	public void psicoldelia() {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf.getWidth();i++) {
			for(int j =0; j < imagenBf.getHeight();j++) {
				color = (-1)*imagenBf.getRGB(i,j);
				if(color >  256 * 256 + 256 ){
					color = imagenBf.getRGB(i, j);
				} else {
					color = (int)((int) (Math.random()*256) * 256 * 256 +
							(int) (Math.random()*256) * 256 + 
							(int) (Math.random()*256))/2 +
							((int)imagenBf.getRGB(i, j)/2);
				}
				
				imagenBf.setRGB(i, j, color);
			}
		}
		imagen = new ImageIcon(imagenBf);
	}

	public Component crearPaneBrillo() {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int brillo = Integer.parseInt(valor.getText());
		cambiarBrillo(brillo);
	}

	private void cambiarBrillo(int brillo) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf.getWidth();i++) {
			for(int j =0; j < imagenBf.getHeight();j++) {
				Color color = new Color(imagenBf.getRGB(i, j));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				red +=brillo;
				blue += brillo;
				green += brillo;
				if(red < 0) red = 0;
				if(red > 255) red = 255;
				if(blue < 0) blue = 0;
				if(blue > 255) blue = 255;
				if(green < 0) green = 0;
				if(green > 255) green = 255;
				color = new Color(red, green, blue);
				imagenBf.setRGB(i, j, color.getRGB());
			}
		}
		imagen = new ImageIcon(imagenBf);
	}

}
