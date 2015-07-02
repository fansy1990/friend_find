/**
 * 
 */
package com.fz.fast_cluster.mr;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

import com.fz.fast_cluster.keytype.DDoubleWritable;
import com.fz.fast_cluster.keytype.DoubleArrStrWritable;


/**
 * @author fansy
 * @date 2015-6-1
 */
public class DeltaDistanceReducer extends
		Reducer<DoubleArrStrWritable, DDoubleWritable, DoubleArrStrWritable, DDoubleWritable> {

	private DDoubleWritable sd = new DDoubleWritable();
	@Override
	public void reduce(DoubleArrStrWritable key,Iterable<DDoubleWritable> values,Context cxt) throws IOException,InterruptedException{
		double minDistance = Double.MAX_VALUE;
		double sum =0;
		for(DDoubleWritable s:values){
			if(s.getDistance()<minDistance){
				minDistance = s.getDistance();
				sum =s.getSum();
			}
		}
		sd.setDistance(minDistance);
		sd.setSum(sum);
		
		cxt.write(key, sd);
	}
}
