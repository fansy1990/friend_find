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
import com.fz.filter.mr.FindInitDCMapper;
import com.fz.filter.mr.FindInitReducer;
import com.fz.util.HUtils;

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
	    Job job =  Job.getInstance(conf,"calculate two vectors distance  from  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(FindInitDCJob.class);
	    job.setMapperClass(FindInitDCMapper.class);
	    job.setReducerClass(FindInitReducer.class);
	    job.setNumReduceTasks(1);
	    
	    job.setMapOutputKeyClass(IntWritable.class	);
	    job.setMapOutputValueClass(DoubleArrWritable.class);
	    
	    job.setOutputKeyClass(DoubleArrWritable.class);
	    job.setOutputValueClass(NullWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret =job.waitForCompletion(true) ? 0 : 1;
	    
	    System.out.println("总记录数,map统计("+HUtils.MAP_COUNTER+"):"+
	    		job.getConfiguration().getLong(HUtils.MAP_COUNTER, -1));
	    System.out.println("总记录数,reduce统计("+HUtils.REDUCE_COUNTER+"):"+
	    		job.getConfiguration().getLong(HUtils.REDUCE_COUNTER, -1));
	    System.out.println("总记录数,reduce record统计("+HUtils.REDUCE_COUNTER2+"):"+
	    		job.getConfiguration().getLong(HUtils.REDUCE_COUNTER2, -1));
	    return ret;
	}

}
