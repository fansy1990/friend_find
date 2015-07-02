/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.fast_cluster.keytype.DoubleArrStrWritable;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class GetMaxMinMapper extends Mapper<DoubleArrStrWritable,NullWritable,
	IntWritable,DoubleArrStrWritable> {

	private int column;
	private DoubleArrStrWritable min_=null;
	private DoubleArrStrWritable max_ = null;
	
	private IntWritable zero= new IntWritable(0);
	private IntWritable one = new IntWritable(1);
	@Override
	public void setup(Context cxt){
		column = cxt.getConfiguration().getInt("COLUMNS", 4);
		double[] tmp_= new double[column];
		// 初始化min_,给tmp_赋最大值
		for(int i=0;i<column;i++){
			tmp_[i]=Double.MAX_VALUE;
		}
		min_=new DoubleArrStrWritable(tmp_,"min");
		
		// 初始化max_,给tmp_赋最小值
		tmp_= new double[column];
		for(int i=0;i<column;i++){
			tmp_[i]=Double.MIN_VALUE;
		}
		max_=new DoubleArrStrWritable(tmp_,"max");
		
	} 
	
	@Override
	public void map(DoubleArrStrWritable key, NullWritable value,Context cxt) {
		HUtils.updateMax(key,max_,column);
		HUtils.updateMin(key,min_,column);
	}
	
	

	@Override
	public void cleanup(Context cxt)throws InterruptedException,IOException{
		cxt.write(zero	, min_);
		cxt.write(one, max_);
	}
	
	
}
