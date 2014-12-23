package com.lzhn.utils.math;

/**
 * 自定义复数类
 * 
 * @author zzha
 * 
 */
public class Complex {
	// 实部
	private double real;
	// 虚部
	private double image;

	public Complex(double real, double image) {
		super();
		this.real = real;
		this.image = image;
	}

	/**
	 * 由实数构造一个复数。虚部为0
	 * 
	 * @param real
	 */
	public Complex(double real) {
		this(real, 0);
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImage() {
		return image;
	}

	public void setImage(double image) {
		this.image = image;
	}

	/**
	 * 复数加法
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Complex add(Complex x, Complex y) {
		double a = x.getReal();
		double b = x.getImage();
		double c = y.getReal();
		double d = y.getImage();
		return new Complex(a + c, b + d);
	}

	/**
	 * 复数减法
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Complex subtract(Complex x, Complex y) {
		double a = x.getReal();
		double b = x.getImage();
		double c = y.getReal();
		double d = y.getImage();
		return new Complex(a - c, b - d);
	}

	/**
	 * 复数乘法
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Complex multiply(Complex x, Complex y) {
		double a = x.getReal();
		double b = x.getImage();
		double c = y.getReal();
		double d = y.getImage();
		return new Complex(a * c - b * d, b * c + a * d);
	}

	/**
	 * 复数除法
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Complex divide(Complex x, Complex y) {
		double a = x.getReal();
		double b = x.getImage();
		double c = y.getReal();
		double d = y.getImage();
		double e = c * c + d * d;
		return new Complex((a * c + b * d) / e, (b * c - a * d) / e);
	}

	/**
	 * 复数的模
	 * 
	 * @param x
	 * @return
	 */
	public static double norm(Complex x) {
		double a = x.getReal();
		double b = x.getImage();
		return Math.sqrt(a * a + b * b);
	}

	/**
	 * 开平方运算。正数返回double，负数返回Complex
	 * 
	 * @param x
	 * @return
	 */
	public static Object sqrt(double x) {
		if (x >= 0) {
			return Math.sqrt(x);
		} else {
			x *= -1;
			return new Complex(0, Math.sqrt(x));
		}
	}

	@Override
	public String toString() {
		return "(" + real + "+" + image + "i)";
	}
}
