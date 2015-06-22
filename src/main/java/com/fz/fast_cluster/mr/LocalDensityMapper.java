/**
 * 
 */
package com.fz.fast_cluster.mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.util.HUtils;


/**
 * @author fansy
 * @date 2015-5-28
 */
public class LocalDensityMapper extends Mapper<LongWritable, Text, DoubleArrWritable, DoubleWritable> {

	private double dc;
	private Path input;
	private String splitter=",";
	private String method =null;
	
	private DoubleWritable sumAll= new DoubleWritable();
	private DoubleArrWritable keyDoubleArr;
	
	@Override 
	public void setup(Context cxt){
		dc=cxt.getConfiguration().getDouble("DC", 0);
		input=new Path(cxt.getConfiguration().get("INPUT"));// 
		splitter = cxt.getConfiguration().get("SPLITTER", ",");
		method = cxt.getConfiguration().get("METHOD", "gaussian");
	}
	
	@Override
	public void map(LongWritable key,Text value,Context cxt)throws InterruptedException,IOException{
		// get the ith line of all data；
		double[] inputI= HUtils.getInputI(value,splitter);
		
		double sum=0;
		// hdfs
		try{
			FileSystem fs = FileSystem.get(cxt.getConfiguration());
			InputStream in = fs.open(input);
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			String line = null;
			
			while((line=buff.readLine())!=null){
				double[] inputLine = HUtils.getInputI(line,splitter);
				double distance = HUtils.getDistance(inputI,inputLine);
			if(distance<dc&&distance>0){ // distance should be grater than 0，
					if(method.equals("gaussian")){
						sum+=Math.pow(Math.E, -(distance/dc)*(distance/dc));
					}else{
						sum+=1;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// output if the point has at least one neighbor
		if(sum>0){
			sumAll.set(sum);
			keyDoubleArr = new DoubleArrWritable(inputI);
			cxt.write(keyDoubleArr, sumAll);
		}
	}

	
	
}
