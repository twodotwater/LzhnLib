package com.lzhn.utils.math;

public class MathUtils {

	public static float format2Decimal(double d) {
		return formatDecimal(d, 2);
	}

	public static float formatDecimal(double d, int len) {
		float tmp = 1f;
		for (int i = 1; i <= len; i++) {
			tmp *= 10;
		}
		return Math.round(d * tmp) / tmp;
	}

	public static float[] logOfArray(float[] arr) {
		float[] tmp = new float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			tmp[i] = (float) Math.log(arr[i]);
		}
		return tmp;
	}

	public static double[] logOfArray(double[] arr) {
		double[] tmp = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			tmp[i] = Math.log(arr[i]);
		}
		return tmp;
	}

	public static int[] absOfArray(int[] arr) {
		int[] tmp = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			tmp[i] = Math.abs(arr[i]);
		}
		return tmp;
	}

	public static int signOfValue(double value) {
		if (value > 0) {
			return 1;
		} else if (value < 0) {
			return -1;
		}
		return 0;

	}

	public static float sumOfArray(float[] arr, int start, int end) {
		float sum = 0;
		for (int i = start; i < end; i++) {
			sum += arr[i];
		}
		return sum;
	}

	public static double sumOfArray(double[] arr, int start, int end) {
		double sum = 0;
		for (int i = start; i < end; i++) {
			sum += arr[i];
		}
		return sum;
	}

	public static float averageOfArray(float[] arr, int start, int end) {
		float aver = sumOfArray(arr, start, end);
		return aver / (end - start);
	}

	public static double averageOfArray(double[] arr, int start, int end) {
		double aver = sumOfArray(arr, start, end);
		return aver / (end - start);
	}

	public static float[] minusArrayByValue(float[] arr, float value) {
		float[] dif = new float[arr.length];
		for (int i = 0; i < dif.length; i++) {
			dif[i] = Math.abs(arr[i] - value);
		}
		return dif;
	}

	public static double[] minusArrayByValue(double[] arr, double value) {
		double[] dif = new double[arr.length];
		for (int i = 0; i < dif.length; i++) {
			dif[i] = Math.abs(arr[i] - value);
		}
		return dif;
	}

	public static float minValueOfArray(float[] arr) {
		float min = arr[0];
		for (int i = 1; i < arr.length; i++) {
			min = min > arr[i] ? arr[i] : min;
		}
		return min;
	}

	public static double minValueOfArray(double[] arr) {
		double min = arr[0];
		for (int i = 1; i < arr.length; i++) {
			min = min > arr[i] ? arr[i] : min;
		}
		return min;
	}

	public static float maxValueOfArray(float[] arr) {
		float max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			max = max < arr[i] ? arr[i] : max;
		}
		return max;
	}

	public static double maxValueOfArray(double[] arr) {
		double max = arr[0];
		for (int i = 1; i < arr.length; i++) {
			max = max < arr[i] ? arr[i] : max;
		}
		return max;
	}

	public static float[] convertIntToFloat(int[] intArray) {
		float[] floatArray = new float[intArray.length];
		for (int i = 0; i < floatArray.length; i++) {
			floatArray[i] = intArray[i];
		}
		return floatArray;
	}

	public static <T, W> void convertArrayType(W[] w, T[] t) {
		Class<T> cls = (Class<T>) t[0].getClass();
		for (int i = 0; i < t.length; i++) {
			t[i] = convertType(w[i], cls);
		}
	}

	public static <T, W> T convertType(W w, Class<T> cls) {
		T t = (T) w;
		return t;
	}
}
