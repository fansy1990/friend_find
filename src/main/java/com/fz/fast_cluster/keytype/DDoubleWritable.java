/**
 * 
 */
package com.fz.fast_cluster.keytype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * int and double writable
 * @author fansy
 * @date 2015-6-1
 */
public class DDoubleWritable implements WritableComparable<DDoubleWritable>{

	private double distance;
	private double sum;
	
	public DDoubleWritable(){}
	
	public DDoubleWritable(double distance ,double sum){
		this.distance=distance;
		this.sum=sum;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeDouble(sum);  
        out.writeDouble(distance);  
	}

	public void readFields(DataInput in) throws IOException {
		sum = in.readDouble();  
        distance = in.readDouble();  
	}

	public int compareTo(DDoubleWritable o) {
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
	
    public int hashCode(){  
        int hashCode =0;  
        hashCode +=Double.doubleToLongBits(distance)+Double.doubleToLongBits(sum);
        return hashCode;  
    }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	@Override
	public String toString(){
		
		return this.sum+","+this.distance;
	}
}
