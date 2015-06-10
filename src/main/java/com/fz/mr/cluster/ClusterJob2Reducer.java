/**
 * 
 */
package com.fz.mr.cluster;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob2Reducer extends
		Reducer<KeyDoubleArr, SumDistance, KeyDoubleArr, SumDistance> {

	private SumDistance sd = new SumDistance();
	@Override
	public void reduce(KeyDoubleArr key,Iterable<SumDistance> values,Context cxt) throws IOException,InterruptedException{
		double minDistance = Double.MAX_VALUE;
		int sum =0;
		for(SumDistance s:values){
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
