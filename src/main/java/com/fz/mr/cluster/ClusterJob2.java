/**
 * 
 */
package com.fz.mr.cluster;

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

import com.fz.util.HUtils;

/**
 * 聚类算法
 * 计算与当前点距离最小的点的距离（其他点的临近点个数需要大于当前点）
 * 输出：
 * key,value
 * instance , sumAll , distance
 * 输入序列化文件，    输出序列化/格式化文件
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob2 extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(HUtils.getConf(), new ClusterJob2(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=3) {
	      System.err.println("Usage: com.fz.mr.cluster.ClusterJob2 <in> <out> <in>");
	      System.exit(3);
	    }
//	    Job job = new Job(conf, "word count");
	    conf.set("INPUT", otherArgs[2]);
	    Job job =  Job.getInstance(conf,"find the nearest distance with the bigger near neighours");
	    job.setJarByClass(ClusterJob2.class);
	    job.setMapperClass(ClusterJob2Mapper.class);
//	    job.setCombinerClass(ClusterJob2Reducer.class);
	    job.setReducerClass(ClusterJob2Reducer.class);
	    job.setOutputKeyClass(KeyDoubleArr.class);
	    job.setOutputValueClass(SumDistance.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    for (int i = 0; i < otherArgs.length - 2; ++i) {
	      SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[i]));
	    }
	    FileOutputFormat.setOutputPath(job,
	      new Path(otherArgs[otherArgs.length - 2]));
	    FileSystem.get(conf).delete(new Path(otherArgs[otherArgs.length-2]), true);
	    return job.waitForCompletion(true) ? 0 : 1;

	}
	
	

}
