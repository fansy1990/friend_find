/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.util.Utils;


/**
 * filter the same point vector 
 * @author fansy
 * @date 2015-6-1
 */
public class LocalDensityReducer extends
		Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {
	private DoubleWritable sumAll = new DoubleWritable();
	
	@Override
	public void reduce(IntWritable key, Iterable<DoubleWritable> values,Context cxt)
	throws IOException,InterruptedException{
		double sum =0;
		for(DoubleWritable v:values){
			sum+=v.get();
		}
		sumAll.set(sum);// 
		cxt.write(key, sumAll);
		Utils.simpleLog("vectorI:"+key.get()+",density:"+sumAll);
	}
}
