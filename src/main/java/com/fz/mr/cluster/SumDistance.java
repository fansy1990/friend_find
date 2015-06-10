/**
 * 
 */
package com.fz.mr.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * ClusterJob2的输出value
 * @author fansy
 * @date 2015-6-1
 */
public class SumDistance implements WritableComparable<SumDistance>{

	private double distance;
	private int sum;
	
	public SumDistance(){}
	
	public SumDistance(double distance ,int sum){
		this.distance=distance;
		this.sum=sum;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(sum);  
        out.writeDouble(distance);  
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		sum = in.readInt();  
        distance = in.readDouble();  
	}

	@Override
	public int compareTo(SumDistance o) {
		if(this.sum<o.sum){
			return -1;
		}else if(this.sum>o.sum){
			return 1;
		}
		
		double oDistance =o.distance;  
        if(this.distance<oDistance){  
            return -1;  
        }else if(this.distance>oDistance){  
            return 1;  
        }  
        return 0;
	}
	
	@Override  
    public int hashCode(){  
        int hashCode =0;  
        hashCode +=Double.doubleToLongBits(distance)+Integer.toString(sum).hashCode();
        return hashCode;  
    }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	@Override
	public String toString(){
		
		return this.sum+","+this.distance;
	}
}
