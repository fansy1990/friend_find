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
public class StringPairWritable implements WritableComparable<StringPairWritable>{

	private String first;
	private String second;
	
	public StringPairWritable(){}
	
	public StringPairWritable(String first,String second){
		this.first=first;
		this.second=second;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeUTF(first);
		out.writeUTF(second);
	}

	public void readFields(DataInput in) throws IOException {
		first = in.readUTF();
		second=in.readUTF();
	}

	public int compareTo(StringPairWritable o) {
		
		if(this.first.equals(o.first)){
			return this.second.compareTo(o.second);
		}
		if(this.second.equals(o.second)){
			return this.first.compareTo(o.first);
		}
        return this.first.compareTo(o.first);
	}
	
    public int hashCode(){  
        int hashCode =0;  
        hashCode +=first.hashCode()+second.hashCode();
        return hashCode;  
    }


	@Override
	public String toString(){
		
		return this.first+","+this.second;
	}
}
