/**
 * 
 */
package com.fz.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.fast_cluster.keytype.DoubleArrStrWritable;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 * 合并两两向量，即从N条记录到N*(N+1)条记录
 * 
 * @author fansy
 * @date 2015-6-25
 */
public class PrepareCalDistanceJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: com.fz.filter.PrepareCalDistanceJob <in> <out>");
	      System.exit(2);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"combine vectors  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(PrepareCalDistanceJob.class);
	    job.setMapperClass(PrepareCalDistanceMapper.class);
//	    job.setReducerClass(FindInitReducer.class);
	    job.setNumReduceTasks(0);
	    
	    job.setMapOutputKeyClass(DoubleArrStrWritable.class	);
	    job.setMapOutputValueClass(DoubleArrStrWritable.class);
	    
	    job.setOutputKeyClass(DoubleArrStrWritable.class);
	    job.setOutputValueClass(DoubleArrStrWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret =job.waitForCompletion(true) ? 0 : 1;
	    long records=job.getCounters().findCounter(FilterCounter.MAP_COUNTER)
	    	    .getValue();
	    Utils.simpleLog("总记录数："+records);
	    HUtils.INPUT_RECORDS=records;
	    
	    return ret;
	}
	
	// 命令行测试
	
	public static void main(String[] args) throws Exception {
//		ToolRunner.run(new Configuration(), new PrepareCalDistanceJob(), args);
		Configuration conf = new Configuration();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: com.fz.filter.PrepareCalDistanceJob <in> <out>");
	      System.exit(2);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"combine vectors  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(PrepareCalDistanceJob.class);
	    job.setMapperClass(PrepareCalDistanceMapper.class);
//	    job.setReducerClass(FindInitReducer.class);
	    job.setNumReduceTasks(0);
	    
	    job.setMapOutputKeyClass(DoubleArrStrWritable.class	);
	    job.setMapOutputValueClass(DoubleArrStrWritable.class);
	    
	    job.setOutputKeyClass(DoubleArrStrWritable.class);
	    job.setOutputValueClass(DoubleArrStrWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret=job.waitForCompletion(true) ? 0 : 1;
	    Utils.simpleLog("返回值："+ret);
	    long records=job.getCounters().findCounter(FilterCounter.MAP_COUNTER)
	    	    .getValue();
	    Utils.simpleLog("总记录数："+records);
	    records = job.getCounters().findCounter(FilterCounter.MAP_OUT_COUNTER)
	    	    .getValue();
	    Utils.simpleLog("Map输出总记录数："+records);
	    
	}

}
