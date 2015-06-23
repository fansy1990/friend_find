/**
 * 
 */
package com.fz.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.filter.mr.GetMaxMinMapper;
import com.fz.filter.mr.GetMaxMinReducer;
import com.fz.util.HUtils;

/**
 * 输入为 GetAttributesJob的输出
 * 获取每列数据的最大最小值
 * mapper:
 *  输出<0, DoubleArrWritable> 包含每列的最小值
 * 	输出<1, DoubleArrWritable> 包含每列的最大值
 * 	
 * reducer：
 * reduce函数整合；
 * cleanup函数直接写入HDFS文件即可，
 * 
 * @author fansy
 * @date 2015-6-23
 */
public class GetMaxMinJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=3) {
	      System.err.println("Usage: fz.filter.GetMaxMinJob <in> <out> <columns>");
	      System.exit(3);
	    }
	    conf.setInt("COLUMNS", Integer.parseInt(otherArgs[2]));
	    Job job =  Job.getInstance(conf,"Get max and min value of all columns  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(GetMaxMinJob.class);
	    job.setMapperClass(GetMaxMinMapper.class);
	    job.setReducerClass(GetMaxMinReducer.class);
	    job.setNumReduceTasks(1);
	    
	    job.setMapOutputKeyClass(IntWritable.class	);
	    job.setMapOutputValueClass(DoubleArrWritable.class);
	    
	    job.setOutputKeyClass(DoubleArrWritable.class);
	    job.setOutputValueClass(NullWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;
	}

}
