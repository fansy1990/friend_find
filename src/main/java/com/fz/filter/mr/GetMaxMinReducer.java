/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.util.HUtils;

/**
 * 整合多个mapper的输出，如果有多个mapper的话
 * @author fansy
 * @date 2015-6-23
 */
public class GetMaxMinReducer extends Reducer<IntWritable, DoubleArrWritable,DoubleArrWritable, NullWritable> {
	private int column;
	private DoubleArrWritable min_=null;
	private DoubleArrWritable max_ = null;

	@Override
	public void setup(Context cxt){
		column = cxt.getConfiguration().getInt("COLUMNS", 4);
		double[] tmp_= new double[column];
		// 初始化min_,给tmp_赋最大值
		for(int i=0;i<column;i++){
			tmp_[i]=Double.MAX_VALUE;
		}
		min_=new DoubleArrWritable(tmp_,"min");
		
		// 初始化max_,给tmp_赋最小值
		tmp_= new double[column];
		for(int i=0;i<column;i++){
			tmp_[i]=Double.MIN_VALUE;
		}
		max_=new DoubleArrWritable(tmp_,"max");
		
	} 
	@Override
	public void reduce(IntWritable key,Iterable<DoubleArrWritable> values,Context cxt){
		if(key.get()==0){// 最小值，更新最小值即可
			for(DoubleArrWritable value:values){
				HUtils.updateMin(value, min_, column);
			}
		}else{// 最大值，更新最大值
			for(DoubleArrWritable value:values){
				HUtils.updateMax(value, max_, column);
			}
		}
	}
	
	@Override
	public void cleanup(Context cxt)throws InterruptedException,IOException{
		cxt.write(max_, NullWritable.get());
		cxt.write(min_, NullWritable.get());
	}
}
