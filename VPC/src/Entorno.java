import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

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
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class Entorno implements ImageObserver  {
	
	private ImageIcon imagen;
	private BufferedImage[] imagenBf;
	private String direccion,tipoImagen;
	private int bits, color, maxLum, minLum, brillo, currentImage;
	private double contrast, entropy;
	private HistogramDataset histogramDataset;
	private DefaultCategoryDataset acumDataset;
	private Linea seleccion;
	//private JTextField valor;
	//private JButton aceptar;
	private int prevrot;

	public Entorno(String absolutePath) throws IOException {
		direccion = absolutePath;
		imagen = new ImageIcon(absolutePath);
		tipoImagen = direccion.substring(direccion.lastIndexOf('.') + 1);
		imagenBf = new BufferedImage[10];
		for(int i = 0 ; i < 10; i++) imagenBf[i] = ImageIO.read(new File(direccion));
		currentImage = 9;
		bits = imagenBf[currentImage].getColorModel().getPixelSize();
		
		seleccion = new Linea(imagenBf[currentImage].getHeight()*imagenBf[currentImage].getWidth());
		minLum = calcMin();
		maxLum = calcMax();
		brillo = calcBrillo();
		contrast = calcContrast();
		entropy = calcEntr();
		updateIcon();
	}

	private double calcEntr() {
		// TODO Auto-generated method stub
		long[] histograma = new long[256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] greenSamples = null;
        double[] blueSamples = null;
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] redSamples = r.clone();
        if(bits != 8){
        	r = raster.getSamples(0, 0, w, h, 1, r);
        	greenSamples = r.clone();
        	r = raster.getSamples(0, 0, w, h, 2, r);
        	blueSamples = r.clone();
        }
        histograma = ArrayMaths.Histograma(redSamples);
	    if(bits != 8) {
	    	double[] calculos = r;
	    	calculos = ArrayMaths.multiply(r, 0.33);
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(greenSamples, 0.33));
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(blueSamples, 0.33));
	    	histograma = ArrayMaths.Histograma(calculos);
	    }
		return ArrayMaths.Entropy(histograma, w * h);
	}

	private double calcContrast() {
		// TODO Auto-generated method stub
		long[] histograma = new long[256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] greenSamples = null;
        double[] blueSamples = null;
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] redSamples = r.clone();
        if(bits != 8){
        	r = raster.getSamples(0, 0, w, h, 1, r);
        	greenSamples = r.clone();
        	r = raster.getSamples(0, 0, w, h, 2, r);
        	blueSamples = r.clone();
        }
        histograma = ArrayMaths.Histograma(redSamples);
	    if(bits != 8) {
	    	double[] calculos = r;
	    	calculos = ArrayMaths.multiply(r, 0.33);
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(greenSamples, 0.33));
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(blueSamples, 0.33));
	    	histograma = ArrayMaths.Histograma(calculos);
	    }
		return ArrayMaths.TypicalDeviation(histograma, brillo, w * h);
	}

	private int calcBrillo() {
		// TODO Auto-generated method stub
		long[] histograma = new long[256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] greenSamples = null;
        double[] blueSamples = null;
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] redSamples = r.clone();
        if(bits != 8){
        	r = raster.getSamples(0, 0, w, h, 1, r);
        	greenSamples = r.clone();
        	r = raster.getSamples(0, 0, w, h, 2, r);
        	blueSamples = r.clone();
        }
        histograma = ArrayMaths.Histograma(redSamples);
	    if(bits != 8) {
	    	double[] calculos = r;
	    	calculos = ArrayMaths.multiply(r, 0.33);
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(greenSamples, 0.33));
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(blueSamples, 0.33));
	    	histograma = ArrayMaths.Histograma(calculos);
	    }
		return ArrayMaths.lumAverage(histograma);
	}

	private int calcMax() {
		// TODO Auto-generated method stub
		long[] histograma = new long[256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] greenSamples = null;
        double[] blueSamples = null;
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] redSamples = r.clone();
        if(bits != 8){
        	r = raster.getSamples(0, 0, w, h, 1, r);
        	greenSamples = r.clone();
        	r = raster.getSamples(0, 0, w, h, 2, r);
        	blueSamples = r.clone();
        }
	    histograma = ArrayMaths.Histograma(redSamples);
	    if(bits != 8) {
	    	double[] calculos = r;
	    	calculos = ArrayMaths.multiply(r, 0.33);
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(greenSamples, 0.33));
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(blueSamples, 0.33));
	    	histograma = ArrayMaths.Histograma(calculos);
	    }
		return ArrayMaths.MaxWithValue(histograma);
	}

	private int calcMin() {
		// TODO Auto-generated method stub
		long[] histograma = new long[256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] greenSamples = null;
        double[] blueSamples = null;
        r = raster.getSamples(0, 0, w, h, 0, r);
        double[] redSamples = r.clone();
        if(bits != 8){
        	r = raster.getSamples(0, 0, w, h, 1, r);
        	greenSamples = r.clone();
        	r = raster.getSamples(0, 0, w, h, 2, r);
        	blueSamples = r.clone();
        }
	    histograma = ArrayMaths.Histograma(redSamples);
	    if(bits != 8) {
	    	double[] calculos = r;
	    	calculos = ArrayMaths.multiply(r, 0.33);
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(greenSamples, 0.33));
	    	calculos = ArrayMaths.Add(calculos ,ArrayMaths.multiply(blueSamples, 0.33));
	    	histograma = ArrayMaths.Histograma(calculos);
	    }
		return ArrayMaths.MinWithValue(histograma);
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

	public DefaultCategoryDataset getDataSet() {
		// TODO Auto-generated method stub
		return acumDataset;
	}

	public void guardarSeleccion(MouseEvent e) {
		// TODO Auto-generated method stub
		seleccion.add(e.getX(),e.getY());
		
	}

	public void borrarSeleccion() {
		// TODO Auto-generated method stub
		seleccion = new Linea(imagenBf[currentImage].getHeight()*imagenBf[currentImage].getWidth());
		
	}

	public void mostrarSeleccion() throws IOException {
		// TODO Auto-generated method stub
		imagen = new ImageIcon(imagenBf[currentImage]);
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
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				color = (-1)*imagenBf[currentImage].getRGB(i,j);
				if(color >  256 * 256 + 256 ){
					color = imagenBf[currentImage].getRGB(i, j);
				} else {
					color = (int)((int) (Math.random()*256) * 256 * 256 +
							(int) (Math.random()*256) * 256 + 
							(int) (Math.random()*256))/2 +
							((int)imagenBf[currentImage].getRGB(i, j)/2);
				}
				
				imagenBf[currentImage].setRGB(i, j, color);
			}
		}
		notifyChange();
		updateIcon();
	}



	
	public void cambiarBrillo(int brillo) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
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
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();
	}

	public BufferedImage getImagenBf() {
		// TODO Auto-generated method stub
		return imagenBf[currentImage];
	}

	public void setDataset(DefaultCategoryDataset auxDataset) {
		// TODO Auto-generated method stub
		acumDataset = auxDataset;
		
	}

	public void cambiarContraste(float contraste) {
		// TODO Auto-generated method stub
		int brilloAntiguo = new Integer(brillo);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
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
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		int diferencia = brilloAntiguo - getBrillo();
		cambiarBrillo(diferencia);
		notifyChange();
		updateIcon();
	}

	public void gamma(double gamma) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
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
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();
	}

	public void ecualizar() {
		long[][] histograma = new long[3][256];
		if (bits == 8) histograma = new long[1][256];
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        final int average = (w*h)/255;
        Raster raster = imagenBf[currentImage].getRaster();
        double[] r = new double[w * h +1];
	    histograma[0] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 0, r));
	    if(bits != 8) {
	    	histograma[1] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 1, r));
	    	histograma[2] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 2, r));
	    }
	    for(int i = 0; i < bits/8; i++)
	    	for(int j = 0; j < histograma[i].length; j++) {
	    		System.out.println(i + " " + j);
	    		long diff = histograma[i][j] - (long)average;
	    		int x = 0, y = 0;
	    		while((diff != 0) && (y < h)) {
	    			Color rgb = new Color(imagenBf[currentImage].getRGB(x, y));
	    			int aux = 0;
	    			switch (i) {
	    			case 0:
	    				if(bits == 8) {
	    					aux = rgb.getRed();
		    				if((diff < 0) && (aux > j)) {
			    				rgb = new Color(j , j, j );
			    				diff++;
			    				histograma[i][aux]--;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}else if((diff > 0) && (aux == j)) {
			    				int random = j + 1;
			    				rgb = new Color(random , random, random );
			    				diff--;
			    				histograma[i][random]++;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}
	    				}
	    				else {
		    				aux = rgb.getRed();
		    				if((diff < 0) && (aux > j)) {
			    				rgb = new Color(j , rgb.getGreen(), rgb.getBlue() );
			    				diff++;
			    				histograma[i][aux]--;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}else if((diff > 0) && (aux == j)) {
			    				int random = 1 + j;
			    				rgb = new Color(random , rgb.getGreen(), rgb.getBlue() );
			    				diff--;
			    				histograma[i][random]++;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}
	    				}
	    			case 1:
	    				aux = rgb.getGreen();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , j, rgb.getBlue() );
		    				diff++;
		    				histograma[i][aux]--;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(rgb.getRed() , random, rgb.getBlue() );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}
	    			case 2:
	    				aux = rgb.getBlue();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), j );
		    				diff++;
		    				if(aux < histograma[i].length && aux > 0) histograma[i][aux]--;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = 1 + j;
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), random );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}
	    			}
	    			x++;
	    			if((x == w) && (y < h) ) {
	    				x = 0;
	    				y++;
	    			}
	    		}
	    	}
	    notifyChange();
	    updateIcon();
	}

	public void calcularDiferencia(String path, int umbral) throws IOException {
		// TODO Auto-generated method stub
		int r, g, b;
		BufferedImage imagenCalculo;
		imagenCalculo = ImageIO.read(new File(path));
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
				Color color2 = new Color(0);
				if((i >= imagenCalculo.getWidth()) || (j >= imagenCalculo.getHeight()))	color2 = color;
				else color2 = new Color(imagenCalculo.getRGB(i, j));
				if(color.getRed() - color2.getRed() <= umbral && color.getRed() - color2.getRed() >= -umbral) r = 0;
				else r = color.getRed() - color2.getRed();
				if(color.getGreen() - color2.getGreen() <= umbral && color.getGreen() - color2.getGreen() >= -umbral) g = 0;
				else g = color.getGreen() - color2.getGreen();
				if(color.getBlue() - color2.getBlue() <= umbral && color.getBlue() - color2.getBlue() >= -umbral) b = 0;
				else b = color.getBlue() - color2.getBlue();
				if(r < 0) r *= -1;
				if(g < 0) g *= -1;
				if(b < 0) b *= -1;
				color = new Color(r,g,b);
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();
	}

	public void specifyHisotgram(String path) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage imagenObjetivo;
		imagenObjetivo = ImageIO.read(new File(path));
		if(imagenObjetivo.getColorModel().getPixelSize() != bits) {
			System.out.println("no se puede especificar el historgrama de dos imagenes con distinto tama�o de pixel");
			return;
		}
		long[][] histograma = new long[3][256];
		long[][] histogramaObjetivo = new long[3][256];
		if (bits == 8) {
			histograma = new long[1][256];
			histogramaObjetivo = new long[1][256];
		}
		
		
		final int w = imagenBf[currentImage].getWidth();
        final int h = imagenBf[currentImage].getHeight();
        Raster raster = imagenBf[currentImage].getRaster();
        Raster rasterObjetivo = imagenObjetivo.getRaster();
        double[] r = new double[w * h +1];
	    histograma[0] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 0, r));
	    histogramaObjetivo[0] = ArrayMaths.Histograma(rasterObjetivo.getSamples(0, 0, w, h, 0, r));
	    if(bits!=8) {
		    histograma[1] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 1, r));
		    histograma[2] = ArrayMaths.Histograma(raster.getSamples(0, 0, w, h, 2, r));
		    histogramaObjetivo[1] = ArrayMaths.Histograma(rasterObjetivo.getSamples(0, 0, w, h, 1, r));
		    histogramaObjetivo[2] = ArrayMaths.Histograma(rasterObjetivo.getSamples(0, 0, w, h, 2, r));
	    }
	    for(int i = 0; i < bits/8; i++)
	    	for(int j = 0; j < histograma[i].length; j++) {
	    		System.out.println(i + " " + j);
	    		long diff = 0;
	    		if(j < histogramaObjetivo[i].length) diff = histograma[i][j] - histogramaObjetivo[i][j];
	    		int x = 0, y = 0;
	    		while((diff != 0) && (y < h)) {
	    			Color rgb = new Color(imagenBf[currentImage].getRGB(x, y));
	    			int aux = 0;
	    			switch (i) {
	    			case 0:	
	    				if(bits == 8) {
	    					aux = rgb.getRed();
		    				if((diff < 0) && (aux > j)) {
			    				rgb = new Color(j , j, j );
			    				diff++;
			    				histograma[i][aux]--;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}else if((diff > 0) && (aux == j)) {
			    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
			    				rgb = new Color(random , random, random );
			    				diff--;
			    				histograma[i][random]++;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}
	    				}
	    				else {
		    				aux = rgb.getRed();
		    				if((diff < 0) && (aux > j)) {
			    				rgb = new Color(j , rgb.getGreen(), rgb.getBlue() );
			    				diff++;
			    				histograma[i][aux]--;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}else if((diff > 0) && (aux == j)) {
			    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
			    				rgb = new Color(random , rgb.getGreen(), rgb.getBlue() );
			    				diff--;
			    				histograma[i][random]++;
			    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
			    			}
	    				}
	    			case 1:
	    				aux = rgb.getGreen();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , j, rgb.getBlue() );
		    				diff++;
		    				if(aux < histograma[i].length && aux > 0)  histograma[i][aux]--;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(rgb.getRed() , random, rgb.getBlue() );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}
	    			case 2:
	    				aux = rgb.getBlue();
	    				if((diff < 0) && (aux > j)) {
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), j );
		    				diff++;
		    				if(aux < histograma[i].length && aux > 0) histograma[i][aux]--;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}else if((diff > 0) && (aux == j)) {
		    				int random = (int)(Math.random() * (histograma[i].length - j)) + j;
		    				rgb = new Color(rgb.getRed() , rgb.getGreen(), random );
		    				diff--;
		    				histograma[i][random]++;
		    				imagenBf[currentImage].setRGB(x, y, rgb.getRGB());
		    			}
	    			}
	    			x++;
	    			if((x == w) && (y < h) ) {
	    				x = 0;
	    				y++;
	    			}
	    		}
	    	}
	    notifyChange();
	    updateIcon();
	}

	public void grayScale() {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				int gray = (red+blue+green)/3;
				color = new Color(gray, gray, gray);
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		bits = 8;
		notifyChange();
		updateIcon();
	}
	
	void updateIcon() {
		double factor = 0;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)screenSize.getHeight() - 200;
		int width = (int)screenSize.getWidth() - 200;
		if(imagenBf[currentImage].getHeight() > height) {
			factor = (double)height / imagenBf[currentImage].getHeight();
			width = (int)( imagenBf[currentImage].getWidth() * factor);
		}
		if(imagenBf[currentImage].getWidth() > width) {
			factor = (double)width / imagenBf[currentImage].getWidth();
			height = (int)( imagenBf[currentImage].getHeight() * factor);
		}
		
		imagen = new ImageIcon(imagenBf[currentImage]);
		Image resize = imagen.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		imagen = new ImageIcon(resize);

	}

	public void negative() {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				color = new Color(255- red, 255 - green, 255 - blue);
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();
	}

	public void daltonismo(int selector) {
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int j =0; j < imagenBf[currentImage].getHeight();j++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, j));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				switch(selector) {
					case 1:
						red = 0;
						break;
					case 2:
						green = 0;
						break;
					case 3:
						blue = 0;
						break;
				}
				color = new Color(red, green, blue);
				imagenBf[currentImage].setRGB(i, j, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();		
	}

	public void tranLinPT(int[] brillo, float[] contraste, int[] min, int[] max) {
		// TODO Auto-generated method stub
		for(int i = 0; i < brillo.length; i++){
			cambiarBrilloCondicionado(brillo[i], min[i], max[i]);
			cambiarContrasteCondicionado(contraste[i], min[i], max[i]);
		}
	}

	private void cambiarContrasteCondicionado(float contraste, int j, int k) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				if(red >= j && red <= k) red *= contraste;
				if(blue >= j && blue <= k) blue *= contraste;
				if(green >= j && green <= k) green *= contraste;
				if(red < 0) red = 0;
				if(red > 255) red = 255;
				if(blue < 0) blue = 0;
				if(blue > 255) blue = 255;
				if(green < 0) green = 0;
				if(green > 255) green = 255;
				color = new Color(red, green, blue);
				imagenBf[currentImage].setRGB(i, h, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();		
	}

	private void cambiarBrilloCondicionado(int brillo, int j, int k) {
		// TODO Auto-generated method stub
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				int red = color.getRed();
				int blue = color.getBlue();
				int green = color.getGreen();
				if(red >= j && red <= k) red += brillo;
				if(blue >= j && blue <= k) blue += brillo;
				if(green >= j && green <= k) green += brillo;
				if(red < 0) red = 0;
				if(red > 255) red = 255;
				if(blue < 0) blue = 0;
				if(blue > 255) blue = 255;
				if(green < 0) green = 0;
				if(green > 255) green = 255;
				color = new Color(red, green, blue);
				imagenBf[currentImage].setRGB(i, h, color.getRGB());
			}
		}
		notifyChange();
		updateIcon();
	}

	public void setDataset(HistogramDataset auxHistDataset) {
		// TODO Auto-generated method stub
		histogramDataset = auxHistDataset;
	}

	public IntervalXYDataset getHDataSet() {
		// TODO Auto-generated method stub
		return histogramDataset;
	}
	
	public void espejoHorizontal(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getWidth(), imagenBf[currentImage].getHeight(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(imagenBf[currentImage].getWidth() - (i + 1), h, color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void espejoVertical(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getWidth(), imagenBf[currentImage].getHeight(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(i, imagenBf[currentImage].getHeight() - (h + 1), color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void transpuesta(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getHeight(), imagenBf[currentImage].getWidth(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(h, i, color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void rot90(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getHeight(), imagenBf[currentImage].getWidth(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(h, imagenBf[currentImage].getWidth() - (i + 1), color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void rot180(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getWidth(), imagenBf[currentImage].getHeight(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(imagenBf[currentImage].getWidth() - (i + 1), imagenBf[currentImage].getHeight() - (h + 1), color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void rot270(){
		BufferedImage buffered = new BufferedImage(imagenBf[currentImage].getHeight(), imagenBf[currentImage].getWidth(), Image.SCALE_SMOOTH);
		for(int i = 0; i < imagenBf[currentImage].getWidth();i++) {
			for(int h =0; h < imagenBf[currentImage].getHeight();h++) {
				Color color = new Color(imagenBf[currentImage].getRGB(i, h));
				buffered.setRGB(imagenBf[currentImage].getHeight() - (h + 1), i, color.getRGB());
			}
		}
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void escalado(int w, int h){
		Image image = imagenBf[currentImage].getScaledInstance(w, h, Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(w, h, Image.SCALE_SMOOTH);
		buffered.getGraphics().drawImage(image, 0, 0 , null);
		imagenBf[currentImage] = buffered;
		notifyChange();
		updateIcon();
	}
	
	public void rotate(int grados){
		double radians = (prevrot + grados) * Math.PI / 180;
		prevrot+=grados;
		int nw = Math.abs((int)Math.floor( (Math.cos(radians) * imagenBf[currentImage].getWidth() + Math.sin(radians) * imagenBf[currentImage].getHeight())));
		int nh = Math.abs((int)Math.floor(Math.cos(radians) * imagenBf[currentImage].getHeight() + Math.sin(radians) * imagenBf[currentImage].getWidth()));
		BufferedImage buffered = new BufferedImage(nw, nh, Image.SCALE_SMOOTH);
		Graphics2D g2d = buffered.createGraphics();
		AffineTransform transform = new AffineTransform();
		transform.translate((nw - imagenBf[currentImage].getWidth())/2, (nh - imagenBf[currentImage].getHeight())/2);
	    transform.rotate(radians, imagenBf[currentImage].getWidth()/2, imagenBf[currentImage].getHeight()/2);
	    g2d.setTransform(transform);
	    g2d.drawImage(imagenBf[currentImage], 0, 0, this);
        g2d.dispose();
	    //transform.rotate(radians, imagenBf.getWidth()/2, imagenBf.getHeight()/2);
	    //AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    //imagenBf = op.filter(imagenBf, null);
        imagen = new ImageIcon(buffered);
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		return true;
	}

	public int getMax() {
		// TODO Auto-generated method stub
		maxLum = calcMax();
		return maxLum;
	}

	public int getMin() {
		// TODO Auto-generated method stub
		minLum = calcMin();
		return minLum;
	}

	public int getBrillo() {
		// TODO Auto-generated method stub
		brillo = calcBrillo();
		return brillo;
	}

	public double getContraste() {
		// TODO Auto-generated method stub
		contrast = calcContrast();
		return contrast;
	}

	public void tranLinPT(int[] newMin, int[] newMax, int[] min, int[] max) {
		// TODO Auto-generated method stub
		for(int i = 0; i < newMin.length; i++){
			int dif = newMin[i] - min[i];
			//System.out.println(dif);
			float contraste =  (float)newMax[i] / (float)(max[i] - dif);
			//System.out.println(contraste);
			cambiarContrasteCondicionado(contraste, min[i], max[i] );
			cambiarBrilloCondicionado(dif, min[i], max[i]);
		}
	}
	
	public void undo(){
		if(currentImage > 0) currentImage--;
		updateIcon();
	}
	
	public void redo(){
		if(currentImage < 9) currentImage++;
		updateIcon();
	}
	
	public void notifyChange(){
		BufferedImage aux[] = new BufferedImage[10];
		for(int i = 0; i < 9; i++){
			int ref = currentImage - (8 - i);
				if(ref < 0) ref = 0;
				if(ref > currentImage) ref = currentImage;
				Image image = imagenBf[ref].getScaledInstance(imagenBf[ref].getWidth(), imagenBf[ref].getHeight(), Image.SCALE_SMOOTH);
				aux[i] = new BufferedImage(imagenBf[ref].getWidth(), imagenBf[ref].getHeight(), Image.SCALE_SMOOTH);
				aux[i].getGraphics().drawImage(image, 0, 0 , null);
		}
		for(int i = 0; i < 9; i++){
			imagenBf[i] = aux[i];
		}
		currentImage = 9;
	}

	public double getEntropia() {
		// TODO Auto-generated method stub
		entropy = calcEntr();
		return entropy;
	}

}
