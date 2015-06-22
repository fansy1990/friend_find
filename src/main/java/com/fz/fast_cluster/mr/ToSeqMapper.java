/**
 * 
 */
package com.fz.fast_cluster.mr;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.util.HUtils;


/**
 * @author fansy
 * @date 2015-6-3
 */
public class ToSeqMapper extends Mapper<LongWritable, Text, DoubleArrWritable, IntWritable> {

	private String splitter =null;
	
	private DoubleArrWritable doubleArr;
	private IntWritable typeInt = new IntWritable(-1);
	@Override
	public void setup(Context cxt){
		splitter = cxt.getConfiguration().get("SPLITTER", ",");
	}
	
	@Override
	public void map(LongWritable key,Text value,Context cxt)throws IOException,InterruptedException{
		double[] inputI= HUtils.getInputI(value,splitter);
		
		doubleArr = new DoubleArrWritable(inputI);
		cxt.write(doubleArr, typeInt);
	}
}
