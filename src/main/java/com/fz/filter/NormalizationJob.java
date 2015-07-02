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
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

import com.fz.fast_cluster.keytype.DoubleArrStrWritable;
import com.fz.filter.mr.NormalizationMapper;
import com.fz.util.HUtils;

/**
 * 最大最小值归一化
 * 输入为 GetAttributesJob的输出
 *   
 * @author fansy
 * @date 2015-6-23
 */
public class NormalizationJob extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = HUtils.getConf();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=3) {
	      System.err.println("Usage: fz.filter.NormalizationJob <in> <out> <max_min>");
	      System.exit(3);
	    }
	    conf.set("MAX_MIN", otherArgs[2]);
	    Job job =  Job.getInstance(conf,"normalization of  input  :"+
	    		otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(NormalizationJob.class);
	    job.setMapperClass(NormalizationMapper.class);
	    job.setNumReduceTasks(0);
	    
//	    job.setMapOutputKeyClass(DoubleArrWritable.class	);
//	    job.setMapOutputValueClass(NullWritable.class);
	    
	    job.setOutputKeyClass(DoubleArrStrWritable.class);
	    job.setOutputValueClass(NullWritable.class);
	    
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    SequenceFileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    int ret =job.waitForCompletion(true) ? 0 : 1;
	    return ret;
	}

}
