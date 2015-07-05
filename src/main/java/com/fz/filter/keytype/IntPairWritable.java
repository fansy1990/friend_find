/**
 * 
 */
package com.fz.filter.keytype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * string and string writable
 * @author fansy
 * @date 2015-6-1
 */
public class IntPairWritable implements WritableComparable<IntPairWritable>{

	private int first;
	private int second;
	
	public IntPairWritable(){}
	
	public IntPairWritable(int first,int second){
		this.first=first;
		this.second=second;
	}
	
	public void setValue(int first,int second){
		this.first=first;
		this.second=second;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeInt(first);
		out.writeInt(second);
	}

	public void readFields(DataInput in) throws IOException {
		first = in.readInt();
		second=in.readInt();
	}

	public int compareTo(IntPairWritable o) {
		
		if(this.first==o.first){
			if(this.second==o.second) return 0;
			if(this.second<o.second) return -1;
			return 1;
		}
		if(this.second==o.second){
			if(this.first==o.first) return 0;
			if(this.first<o.first) return -1;
			return 1;
		}
		if(this.first<o.first) return -1;
		return 1;
	}
	
    public int hashCode(){  
        int hashCode =0;  
        hashCode +=String.valueOf(first).hashCode()+String.valueOf(second).hashCode();
        return hashCode;  
    }


	@Override
	public String toString(){
		
		return this.first+","+this.second;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}
}
