/**
 * 
 */
package com.fz.mr.cluster;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把相同的key的临近点个数相加
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob1Reducer extends
		Reducer<KeyDoubleArr, IntWritable, KeyDoubleArr, IntWritable> {
	private Logger log = LoggerFactory.getLogger(ClusterJob1Reducer.class);
	private IntWritable sumAll = new IntWritable();
	@Override
	public void reduce(KeyDoubleArr key, Iterable<IntWritable> values,Context cxt)
	throws IOException,InterruptedException{
		int sum =0;
		int i=0;
		for(IntWritable v:values){
			sum+=v.get();
			i++;
		}
//		if(log.isDebugEnabled()){
			if(i>1){
				log.info("----------------------key:{},i:{},sum:{}",new Object[]{key.toString(),i,sum});
			}else{	
				
				log.info("key:{},i:{},sum:{}",new Object[]{key.toString(),i,sum});
			}
//		}
		sumAll.set(sum/i);// 改为sum/i ,同一个点只计算一次
		cxt.write(key, sumAll);
	}
}
