/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.filter.FilterCounter;
import com.fz.filter.keytype.IntPairWritable;

/**
 * @author fansy
 * @date 2015-6-26
 */
public class CalDistanceReducer extends
		Reducer<DoubleWritable, IntPairWritable, DoubleWritable, IntPairWritable> {

	public void reduce(DoubleWritable key,Iterable<IntPairWritable> values,Context cxt)throws InterruptedException,IOException{
		for(IntPairWritable v:values){
			cxt.getCounter(FilterCounter.REDUCE_COUNTER).increment(1);
			cxt.write(key, v);
		}
	}
}
