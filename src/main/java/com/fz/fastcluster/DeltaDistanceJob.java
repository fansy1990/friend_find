/**
 * 
 */
package com.fz.fastcluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fz.fastcluster.keytype.DoublePairWritable;
import com.fz.fastcluster.keytype.IntDoublePairWritable;
import com.fz.fastcluster.mr.DeltaDistanceMapper;
import com.fz.fastcluster.mr.DeltaDistanceReducer;
import com.fz.util.HUtils;


/**
 * find delta distance of every point
 * 寻找大于自身密度的最小其他向量的距离
 * mapper输入：
 * 输入为<距离d_ij,<向量i编号，向量j编号>>
 * 把LocalDensityJob的输出
 * 		i,density_i
 * 放入一个map中，用于在mapper中进行判断两个局部密度的大小以决定是否输出
 * mapper输出：
 *      i,<density_i,min_distance_j>
 *      IntWritable,DoublePairWritable
 * reducer 输出：
 * 		<density_i*min_distancd_j> <density_i,min_distance_j,i>
 * 		DoubleWritable,  IntDoublePairWritable
 * @author fansy
 * @date 2015-7-3
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
	    if (otherArgs.length !=4) {
	      System.err.println("Usage: fz.fast_cluster.DeltaDistanceJob <in> <out> <local_density> <num_reducer>");
	      System.exit(4);
	    }
	    conf.set("DENSITYPATH", otherArgs[2]);
	    Job job =  Job.getInstance(conf,"find the nearest distance with the bigger near neighours");
	    job.setJarByClass(DeltaDistanceJob.class);
	    job.setMapperClass(DeltaDistanceMapper.class);
//	    job.setCombinerClass(DeltaDistanceReducer.class);
	    job.setReducerClass(DeltaDistanceReducer.class);
	    
	    job.setNumReduceTasks(Integer.parseInt(otherArgs[3]));
	    
	    job.setMapOutputKeyClass(IntWritable.class);
	    job.setMapOutputValueClass(DoublePairWritable.class);
	    
	    job.setOutputKeyClass(DoubleWritable.class);
	    job.setOutputValueClass(IntDoublePairWritable.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;

	}
}