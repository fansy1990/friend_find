/**
 * 
 */
package com.fz.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.filter.mr.GetAttributesMapper;
import com.fz.util.HUtils;

/**
 * users.xml
 * 获取reputations,upVotes,downVotes,views 和EmailHash属性值输出
 * 输出为序列化文件 
 * @author fansy
 * @date 2015-6-23
 */
public class GetAttributesJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=2) {
	      System.err.println("Usage: fz.filter.GetAttributesJob <in> <out>");
	      System.exit(2);
	    }
	    Job job =  Job.getInstance(conf,"Get attributes from  input  :"+otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(GetAttributesJob.class);
	    job.setMapperClass(GetAttributesMapper.class);
//	    job.setReducerClass(DeduplicateReducer.class);
	    job.setNumReduceTasks(0);
	    
	    job.setOutputKeyClass(DoubleArrWritable.class);
	    job.setOutputValueClass(NullWritable.class);
	    
//	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    return job.waitForCompletion(true) ? 0 : 1;
	}

}
