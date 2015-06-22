/**
 * 
 */
package com.fz.fast_cluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.fast_cluster.mr.LocalDensityMapper;
import com.fz.fast_cluster.mr.LocalDensityReducer;
import com.fz.util.HUtils;


/**
 * Find the local density of every point vector
 * 
 * @author fansy
 * @date 2015-6-2
 */
public class LocalDensityJob extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(HUtils.getConf(), new LocalDensityJob(), args);
	}

	public int run(String[] args) throws Exception {

		Configuration conf = HUtils.getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 5) {
			System.err
					.println("Usage: fz.fast_cluster.LocalDensityJob <in> <out> <dc> <splitter> <method>");
			System.exit(4);
		}
		conf.set("INPUT", otherArgs[0]);
		conf.set("METHOD", otherArgs[4]);
		conf.setDouble("DC", Double.parseDouble(otherArgs[2]));
		conf.set("SPLITTER", otherArgs[3]);
		Job job = Job.getInstance(conf,
				"Count the near neighours with a given dc(distance):"
						+ otherArgs[2]);
		job.setJarByClass(LocalDensityJob.class);
		job.setMapperClass(LocalDensityMapper.class);
		job.setCombinerClass(LocalDensityReducer.class);
		job.setReducerClass(LocalDensityReducer.class);
		job.setOutputKeyClass(DoubleArrWritable.class);
		job.setOutputValueClass(DoubleWritable.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		SequenceFileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
		return job.waitForCompletion(true) ? 0 : 1;

	}

}
