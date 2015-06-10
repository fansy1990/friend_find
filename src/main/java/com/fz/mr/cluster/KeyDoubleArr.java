/**
 * 
 */
package com.fz.mr.cluster;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * ClusterJob1的输出key
 * @author fansy
 * @date 2015-5-28
 */
public class KeyDoubleArr implements WritableComparable<KeyDoubleArr>{

	private double[] doubleArr;
	private int len;
	
	public KeyDoubleArr(){}
	
	public KeyDoubleArr(double[] doubleArr){
		this.len=doubleArr.length;  
		this.doubleArr=doubleArr;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(len);  
        for(int i=0;i<doubleArr.length;i++){  
            out.writeDouble(doubleArr[i]);  
        } 
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		len = in.readInt();  
        doubleArr = new double[len];  
        for(int i=0;i<len;i++){  
            doubleArr[i]=in.readDouble();  
        }  
	}

	@Override
	public int compareTo(KeyDoubleArr o) {
		double[] oDistance =o.doubleArr;  
        int cmp=0;  
        for(int i=0;i<oDistance.length;i++){  
              
            if(Math.abs(this.doubleArr[i]-oDistance[i])<0.0000000001){  
                continue; // 比较下一个  
            }  
            if(this.doubleArr[i]<oDistance[i]){  
                return -1;  
            }else{  
                return 1;  
            }  
        }  
          
        return cmp;
	}
	
	@Override  
    public int hashCode(){  
        int hashCode =0;  
        for(int i=0;i<doubleArr.length;i++){  
        	hashCode+=Double.doubleToLongBits(doubleArr[i]);
//            hashCode=+Double.doubleToIntBits(doubleArr[i]);  
        }  
        return hashCode;  
    }

	public double[] getDoubleArr() {
		return doubleArr;
	}

	public void setDoubleArr(double[] doubleArr) {
		this.doubleArr = doubleArr;
	}

	@Override
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		for(int i=0;i<len;i++){
			buff.append(doubleArr[i]).append(",");
		}
		return buff.substring(0, buff.length()-1)+"]";
		
	}
	
}
