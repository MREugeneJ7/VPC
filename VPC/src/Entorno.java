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
import org.jfree.data.xy.IntervalXYDataset;

public class Entorno  {
	
	private ImageIcon imagen;
	private BufferedImage imagenBf;
	private String direccion,tipoImagen;
	private int bits, color;
	private HistogramDataset dataset;
	private Linea seleccion;
	//private JTextField valor;
	//private JButton aceptar;

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

	public IntervalXYDataset getDataSet() {
		// TODO Auto-generated method stub
		return dataset;
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



	
	public void cambiarBrillo(int brillo) {
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

	public BufferedImage getImagenBf() {
		// TODO Auto-generated method stub
		return imagenBf;
	}

	public void setDataset(HistogramDataset auxDataset) {
		// TODO Auto-generated method stub
		dataset = auxDataset;
		
	}

	public void cambiarContraste(float contraste) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf.getWidth();i++) {
			for(int j =0; j < imagenBf.getHeight();j++) {
				Color color = new Color(imagenBf.getRGB(i, j));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				red *= contraste;
				blue *= contraste;
				green *= contraste;
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

	public void gamma(double gamma) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf.getWidth();i++) {
			for(int j =0; j < imagenBf.getHeight();j++) {
				Color color = new Color(imagenBf.getRGB(i, j));
				int red = color.getRed();

				int blue = color.getBlue();
				int green = color.getGreen();
				red = (int) ((Math.pow((((double)red)/255),gamma))*255);
				blue = (int) ((Math.pow((((double)blue)/255),gamma))*255);
				green = (int) ((Math.pow((((double)green)/255),gamma))*255);
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

	public void ecualizar() {
		long[][] histograma = new long[3][256];
		final int w = imagenBf.getWidth();
        final int h = imagenBf.getHeight();
        final int average = (w*h)/255;
        Raster raster = imagenBf.getRaster();
        double[] r = new double[w * h +1];
	    histograma[0] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 0, r));
	    histograma[1] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 1, r));
	    histograma[2] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 2, r));
	    for(int i = 0; i < 3; i++)
	    	for(int j = 0; j < histograma[i].length; j++) {
	    		System.out.println(i + " " + j);
	    		long diff = histograma[i][j] - (long)average;
	    		int x = 0, y = 0;
	    		while((diff != 0) && (y < h)) {
	    			Color rgb = new Color(imagenBf.getRGB(x, y));
	    			int aux = 0;
	    			switch (i) {
	    			case 1:	
	    				aux = rgb.getRed();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(j , rgb.getGreen(), rgb.getBlue() );
		    				diff++;
		    				histograma[i][aux]--;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(random , rgb.getGreen(), rgb.getBlue() );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}
	    			case 2:
	    				aux = rgb.getGreen();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , j, rgb.getBlue() );
		    				diff++;
		    				histograma[i][aux]--;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(rgb.getRed() , random, rgb.getBlue() );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}
	    			case 3:
	    				aux = rgb.getBlue();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), j );
		    				diff++;
		    				histograma[i][aux]--;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), random );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf.setRGB(x, y, rgb.getRGB());
		    			}
	    			}
	    			x++;
	    			if((x == w) && (y < h) ) {
	    				x = 0;
	    				y++;
	    			}
	    		}
	    	}
	    imagen = new ImageIcon(imagenBf);
	}

}
