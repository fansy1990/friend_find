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
public class IntDoublePairWritable implements WritableComparable<IntDoublePairWritable>{

	private int third;
	private double first;
	private double second;
	
	public IntDoublePairWritable(){}
	
	public IntDoublePairWritable(double distance ,double sum,int third){
		this.first=distance;
		this.second=sum;
		this.third=third;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeDouble(second);  
        out.writeDouble(first);
        out.writeInt(third);
	}

	public void readFields(DataInput in) throws IOException {
		second = in.readDouble();  
        first = in.readDouble();
        third=in.readInt();
	}

	public int compareTo(IntDoublePairWritable o) {
		if(o.third!=this.third){
			return this.third<o.third?-1:1;
		}
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
        hashCode +=Double.doubleToLongBits(first)+Double.doubleToLongBits(second)+
        		Integer.highestOneBit(third);
        return hashCode;  
    }


	@Override
	public String toString(){
		
		return this.third+","+this.second+","+this.first;
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

	/**
	 * @return the third
	 */
	public int getThird() {
		return third;
	}

	/**
	 * @param third the third to set
	 */
	public void setThird(int third) {
		this.third = third;
	}
}
