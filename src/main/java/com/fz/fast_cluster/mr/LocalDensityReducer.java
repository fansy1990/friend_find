/**
 * 
 */
package com.fz.fast_cluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


/**
 * filter the same point vector 
 * @author fansy
 * @date 2015-6-1
 */
public class LocalDensityReducer extends
		Reducer<DoubleArrWritable, DoubleWritable, DoubleArrWritable, DoubleWritable> {
//	private Logger log = LoggerFactory.getLogger(LocalDensityReducer.class);
	private DoubleWritable sumAll = new DoubleWritable();
	@Override
	public void reduce(DoubleArrWritable key, Iterable<DoubleWritable> values,Context cxt)
	throws IOException,InterruptedException{
		double sum =0;
		int i=0;
		for(DoubleWritable v:values){
			sum+=v.get();
			i++;
		}
		sumAll.set(sum/i);// 
		cxt.write(key, sumAll);
	}
}
