/**
 * 
 */
package com.fz.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fz.filter.mr.FindInitDCMapper;
import com.fz.filter.mr.FindInitReducer;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 * 寻找初始化的dc阈值
 * 输入为 GetAttributesJob的输出
 * 计算数据向量两两之间的距离，
 * mapper输出距离，同时记录行数
 * 这样reducer就会按照距离进行排序，
 * 
 * @author fansy
 * @date 2015-6-23
 */
public class FindInitDCJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: fz.filter.GetMaxMinJob <in> <out>");
	      System.exit(2);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"calculate two vectors distance  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(FindInitDCJob.class);
	    job.setMapperClass(FindInitDCMapper.class);
	    job.setReducerClass(FindInitReducer.class);
	    job.setNumReduceTasks(1);
	    
	    job.setMapOutputKeyClass(DoubleWritable.class	);
	    job.setMapOutputValueClass(NullWritable.class);
	    
	    job.setOutputKeyClass(DoubleWritable.class);
	    job.setOutputValueClass(NullWritable.class);
	    
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
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new FindInitDCJob(), args);
	}

}
