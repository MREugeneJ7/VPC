import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

public class Entorno {
	
	private ImageIcon imagen;
	private BufferedImage imagenBf;
	private String direccion,tipoImagen;
	private int bits;
	private HistogramDataset dataset;
	private XYBarRenderer renderer;

	public Entorno(String absolutePath) throws IOException {
		direccion = absolutePath;
		imagen = new ImageIcon(absolutePath);
		tipoImagen = direccion.substring(direccion.lastIndexOf('.'));
		imagenBf = ImageIO.read(new File(direccion));
		bits = imagenBf.getColorModel().getPixelSize();
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

}
