/**
 * 
 */
package com.fz.fast_cluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fz.fast_cluster.keytype.DDoubleWritable;
import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.fast_cluster.mr.DeltaDistanceMapper;
import com.fz.fast_cluster.mr.DeltaDistanceReducer;
import com.fz.util.HUtils;


/**
 * find delta distance of every point
 * @author fansy
 * @date 2015-6-2
 */
public class DeltaDistanceJob extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(HUtils.getConf(), new DeltaDistanceJob(), args);
	}

	public int run(String[] args) throws Exception {

		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: fz.fast_cluster.DeltaDistanceJob <in> <out>");
	      System.exit(3);
	    }
	    conf.set("INPUT", otherArgs[0]);
	    Job job =  Job.getInstance(conf,"find the nearest distance with the bigger near neighours");
	    job.setJarByClass(DeltaDistanceJob.class);
	    job.setMapperClass(DeltaDistanceMapper.class);
	    job.setCombinerClass(DeltaDistanceReducer.class);
	    job.setReducerClass(DeltaDistanceReducer.class);
	    job.setOutputKeyClass(DoubleArrWritable.class);
	    job.setOutputValueClass(DDoubleWritable.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;

	}
}