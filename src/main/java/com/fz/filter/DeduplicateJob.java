/**
 * 
 */
package com.fz.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.filter.mr.DeduplicateMapper;
import com.fz.filter.mr.DeduplicateReducer;
import com.fz.util.HUtils;

/**
 * users.xml
 * 去除重复记录
 * @author fansy
 * @date 2015-6-23
 */
public class DeduplicateJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: fz.filter.DeduplicateJob <in> <out>");
	      System.exit(2);
	    }
	    Job job =  Job.getInstance(conf,"Deduplicate input  :"+otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(DeduplicateJob.class);
	    job.setMapperClass(DeduplicateMapper.class);
	    job.setReducerClass(DeduplicateReducer.class);
//	    job.setNumReduceTasks(0);
	    job.setNumReduceTasks(1);
	    
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(NullWritable.class);
	    
//	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;
	}

}
