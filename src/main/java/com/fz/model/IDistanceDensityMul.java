/**
 * 
 */
package com.fz.model;

/**
 * 画决策图时使用到的信息存储类
 * @author fansy
 * @date 2015-7-3
 */
public class IDistanceDensityMul {

	private double distance ;
	private double density;
	private int vectorI;
	private double mul;
	
	public IDistanceDensityMul(){}
	
	public IDistanceDensityMul(double distance,double density,int vectorI,double mul){
		this.mul=mul;
		this.distance=distance;
		this.density=density;
		this.vectorI=vectorI;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
	public int getVectorI() {
		return vectorI;
	}
	public void setVectorI(int vectorI) {
		this.vectorI = vectorI;
	}
	public double getMul() {
		return mul;
	}
	public void setMul(double mul) {
		this.mul = mul;
	}
	
	/**
	 * 比较是否大于给定的
	 * @param other
	 * @return
	 */
	public boolean greater(IDistanceDensityMul other){
		
		return this.getMul()>=other.getMul();
	}
	
	public String toString(){
		return "["+this.vectorI+","+this.density+","+this.distance+","+this.mul+"]";
	}
}
