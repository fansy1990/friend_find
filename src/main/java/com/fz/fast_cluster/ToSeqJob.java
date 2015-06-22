/**
 * 
 */
package com.fz.fast_cluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.fast_cluster.mr.ToSeqMapper;
import com.fz.util.HUtils;


/**
 * cluster data 
 * @author fansy
 * @date 2015-6-2
 */
public class ToSeqJob extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=3) {
	      System.err.println("Usage: fz.fast_cluster.ToSeqJob <in> <out> <splitter>");
	      System.exit(3);
	    }
	    conf.set("SPLITTER", otherArgs[2]);
	    Job job =  Job.getInstance(conf,"transform  :"+otherArgs[0]+" to Sequence file");
	    job.setJarByClass(ToSeqJob.class);
	    job.setMapperClass(ToSeqMapper.class);
	    job.setNumReduceTasks(0);
	    
	    
	    job.setOutputKeyClass(DoubleArrWritable.class);
	    job.setOutputValueClass(IntWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;
	}

	
}
