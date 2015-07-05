/**
 * 
 */
package com.fz.fastcluster.keytype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * double and double writable
 * @author fansy
 * @date 2015-6-1
 */
public class DoublePairWritable implements WritableComparable<DoublePairWritable>{

	private double first;
	private double second;
	
	public DoublePairWritable(){}
	
	public DoublePairWritable(double distance ,double sum){
		this.first=distance;
		this.second=sum;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeDouble(second);  
        out.writeDouble(first);  
	}

	public void readFields(DataInput in) throws IOException {
		second = in.readDouble();  
        first = in.readDouble();  
	}

	public int compareTo(DoublePairWritable o) {
		if(this.second<o.second){
			return -1;
		}else if(this.second>o.second){
			return 1;
		}
		
		double oDistance =o.first;  
        if(this.first<oDistance){  
            return -1;  
        }else if(this.first>oDistance){  
            return 1;  
        }  
        return 0;
	}
	
    public int hashCode(){  
        int hashCode =0;  
        hashCode +=Double.doubleToLongBits(first)+Double.doubleToLongBits(second);
        return hashCode;  
    }


	@Override
	public String toString(){
		
		return this.second+","+this.first;
	}

	public double getFirst() {
		return first;
	}

	public void setFirst(double first) {
		this.first = first;
	}

	public double getSecond() {
		return second;
	}

	public void setSecond(double second) {
		this.second = second;
	}
}
