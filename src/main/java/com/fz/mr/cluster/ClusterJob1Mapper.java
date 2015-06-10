/**
 * 
 */
package com.fz.mr.cluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.util.HUtils;

/**
 * 获取给定的dc阈值
 * 输出给定dc阈值范围内，每个点的个数和
 * @author fansy
 * @date 2015-5-28
 */
public class ClusterJob1Mapper extends Mapper<LongWritable, Text, KeyDoubleArr, IntWritable> {

	private double dc;
	private Path input;
	private String splitter=",";
	
	private IntWritable sumAll= new IntWritable();
	private KeyDoubleArr keyDoubleArr;
	
	@Override 
	public void setup(Context cxt){
		dc=cxt.getConfiguration().getDouble("DC", 0);
		input=new Path(cxt.getConfiguration().get("INPUT"));// 输入数据
		splitter = cxt.getConfiguration().get("SPLITTER", ",");
	}
	
	@Override
	public void map(LongWritable key,Text value,Context cxt)throws InterruptedException,IOException{
		// 获取 输入数据的第i行；
		double[] inputI= HUtils.getInputI(value,splitter);
		
		// 循环读取input，然后输出满足阈值的点的个数
		int sum=0;
		
		// hdfs
		try{
			FileSystem fs = FileSystem.get(cxt.getConfiguration());
			InputStream in = fs.open(input);
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while((line=buff.readLine())!=null){
				double[] inputLine = HUtils.getInputI(line,splitter);
				double distance = HUtils.getDistance(inputI,inputLine);
			if(distance<dc&&distance>0){ // distance 要大于0，等于0为本节点，则不考虑
					sum+=1;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// 输出 点i，和个数
		sumAll.set(sum);
		keyDoubleArr = new KeyDoubleArr(inputI);
		cxt.write(keyDoubleArr, sumAll);
	}

	
	
}
