
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
		intensidad = new long[max(samples)+1];
		intensidad = put(0 , intensidad );
		System.out.println(samples.length);
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

	private static int max(double[] samples) {
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

}
