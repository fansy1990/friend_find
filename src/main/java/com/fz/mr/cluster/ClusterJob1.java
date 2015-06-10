/**
 * 
 */
package com.fz.mr.cluster;

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
import org.apache.hadoop.util.ToolRunner;

import com.fz.util.HUtils;

/**
 * 聚类算法
 * 计算每个点的紧邻点个数
 * 输出：
 * key，value
 * Instance ，sumAll
 *  
 *  输入文本文件，输出序列化文件
 * @author fansy
 * @date 2015-5-28
 */
public class ClusterJob1 extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(HUtils.getConf(), new ClusterJob1(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length != 5) {
	      System.err.println("Usage: com.fz.mr.cluster.ClusterJob1 <in> <out> <dc> <in> <splitter>");
	      System.exit(4);
	    }
//	    Job job = new Job(conf, "word count");
	    conf.set("INPUT", otherArgs[3]);
	    conf.setDouble("DC", Double.parseDouble(otherArgs[2]));
	    conf.set("SPLITTER", otherArgs[4]);
	    Job job =  Job.getInstance(conf,"Count the near neighours with a given dc(distance)");
	    job.setJarByClass(ClusterJob1.class);
	    job.setMapperClass(ClusterJob1Mapper.class);
//	    job.setCombinerClass(ClusterJob1Reducer.class);
	    job.setReducerClass(ClusterJob1Reducer.class);
	    job.setOutputKeyClass(KeyDoubleArr.class);
	    job.setOutputValueClass(IntWritable.class);
//	    equenceFileInputFormat.addInputPath(job,new Path(in));
//	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    for (int i = 0; i < otherArgs.length - 4; ++i) {
//	      SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[i]));
	      FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
	    }
//	    FileOutputFormat.setOutputPath(job,new Path(otherArgs[otherArgs.length - 3]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[otherArgs.length - 4]));
	    FileSystem.get(conf).delete(new Path(otherArgs[otherArgs.length-4]), true);
	    return job.waitForCompletion(true) ? 0 : 1;

	}
	
	

}
