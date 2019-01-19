
public class ArrayMaths {
	public static double[] Add(double[] r, double[] array) {
		for(int i = 0; i<r.length; i++){
			r[i] = r[i] + array[i];
		}
		return r;
	}

	public static long[] Histograma(double[] samples) {
		// TODO Auto-generated method stub
		long intensidad[];
		int lenght = ArrayMaths.Max(samples)+1;
		if (lenght < 256) lenght = 256;
		intensidad = new long[lenght];
		intensidad = put(0 , intensidad );
		//System.out.println(samples.length);
		for(int k = 0; k < samples.length; k++ ) {
			intensidad[(int) samples[k]]++;
			//System.out.println((int)samples[k]);
		}
		return intensidad;
	}
	
	public static long[] HistogramaAcumulativo(double[] samples) {
		return Acumulativo(Histograma(samples));
	}
	
	static double[] toDouble(long[] intensidad) {
		double[] x = new double[intensidad.length];
		for(int i = 0; i < x.length ; i++) x[i] = (double) intensidad[i];
		return x;
	}

	static int Max(double[] samples) {
		int max=0;
		for(int i =0; i<samples.length; i++) {
			if((int)samples[i] > max) max = (int) samples[i]; 
		}
		return max;
	}

	private static double[] append(double[] raster, double[] put) {
		// TODO Auto-generated method stub
		double[] rv = new double[raster.length+put.length];
		for(int i = 0; i < rv.length; i++) {
			if(i < raster.length) {
				rv[i] = raster[i];
			} else {
				rv[i] = put[i-raster.length];
			}
		}
		return rv;
	}

	private static double[] put(int j, double[] ds) {
		// TODO Auto-generated method stub
		for(int i = 0; i < ds.length; i++) ds[i] = j;
		return ds;
	}

	private static long[] Acumulativo(long[] intensidad) {
		// TODO Auto-generated method stub
		for(int i = 1; i < intensidad.length; i++) {
				intensidad[i] += intensidad[i-1];
		}
		return intensidad;
	}
	private static long[] put(int j , long[] intensidad) {
		for(int i = 0; i < intensidad.length; i++) intensidad[i] = j;
		return intensidad;
	}
	
	static double[] multiply(double[]x, double d) {
		for(int i = 0; i < x.length; i++) x[i] = x[i] * d;
		return x;
	}

	public static int Min(long[] histograma) {
		// TODO Auto-generated method stub
		int min=Integer.MAX_VALUE;
		for(int i =0; i<histograma.length; i++) {
			if((int)histograma[i] < min) min = (int) histograma[i]; 
		}
		return min;
	}

	public static int MaxWithValue(long[] histograma) {
		int max = 0;
		for(int i =0; i<histograma.length; i++) {
			if(histograma[i] != 0) max = i; 
		}
		return max;
	}

	public static int MinWithValue(long[] histograma) {
		// TODO Auto-generated method stub
		int min = 0;
		for(int i = histograma.length - 1; i >= 0; i--) {
			//System.out.println(i + " " +histograma[i]);
			if(histograma[i] != 0 && (i != 0 || histograma[i] != 1)) min = i;
		}
		return min;
	}

	public static int average(long[] histograma) {
		// TODO Auto-generated method stub
		long acum=0;
		for(int i = 0; i < histograma.length; i++) {
			acum += histograma[i]; 
		}
		return (int) (acum/histograma.length);
	}

	public static int lumAverage(long[] histograma) {
		// TODO Auto-generated method stub
		long lumAcum=0;
		long acum = 0;
		for(int i = 0; i < histograma.length; i++) {
			lumAcum += histograma[i] * i;
			acum += histograma[i]; 
		}
		return (int) (lumAcum/acum);
	}

	public static double TypicalDeviation(long[] histograma, int brillo, int j) {
		// TODO Auto-generated method stub
		int average = brillo;
		double contAcum = 0;
		for(int i = 0; i < histograma.length; i++) {
			contAcum += histograma[i] * Math.pow((i - average), 2);
		}
		return Math.sqrt(contAcum/(j - 1));
	}

	public static double Entropy(long[] histograma, int j) {
		// TODO Auto-generated method stub
		double contAcum = 0;
		for(int i = 0; i < histograma.length; i++) {
			double pI = (double) (histograma[i])/j;
			//System.out.println(pI);
			if(pI > 0) contAcum += pI * (Math.log(pI)/Math.log(2));
		}
		return -contAcum;
	}

}
