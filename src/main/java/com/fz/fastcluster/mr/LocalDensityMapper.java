/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.filter.keytype.IntPairWritable;


/**
 * 输入为<距离d_ij,<向量i编号，向量j编号>>
 * 根据距离dc阈值判断距离d_ij是否小于dc，符合要求则
 * 输出
 * 向量i编号，1
 * 向量j编号，1
 * @author fansy
 * @date 2015-7-3
 */
public class LocalDensityMapper extends Mapper<DoubleWritable, IntPairWritable, IntWritable, DoubleWritable> {

	private double dc;
	private String method =null;
	
	private IntWritable vectorId= new IntWritable();
	private DoubleWritable one= new DoubleWritable(1);
	
	@Override 
	public void setup(Context cxt){
		dc=cxt.getConfiguration().getDouble("DC", 0);
		method = cxt.getConfiguration().get("METHOD", "gaussian");
	}
	
	@Override
	public void map(DoubleWritable key,IntPairWritable value,Context cxt)throws InterruptedException,IOException{
		double distance= key.get();
		
		if(method.equals("gaussian")){
            one.set(Math.pow(Math.E, -(distance/dc)*(distance/dc)));
        }
		
		if(distance<dc){
			vectorId.set(value.getFirst());
			cxt.write(vectorId, one);
			vectorId.set(value.getSecond());
			cxt.write(vectorId, one);
		}
	}

	
	
}
