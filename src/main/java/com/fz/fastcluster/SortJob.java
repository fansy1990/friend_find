/**
 * 
 */
package com.fz.fastcluster;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.fz.fastcluster.keytype.CustomDoubleWritable;
import com.fz.fastcluster.keytype.IntDoublePairWritable;
import com.fz.util.HUtils;
import com.fz.util.Utils;


/**
 * 对DeltaDistance结果排序
 * 
 * 输入为 <density_i*min_distancd_j> <first:density_i,second:min_distance_j,third:i>
 * 		DoubleWritable,  IntDoublePairWritable
 *  
 *  输出是同样的内容，只是做了排序
 * @author fansy
 * @date 2015-7-3
 */
public class SortJob extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception
	 * method: gaussian,cutoff
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(HUtils.getConf(), new SortJob(), args);
	}

	public int run(String[] args) throws Exception {

		Configuration conf = HUtils.getConf();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err
					.println("Usage: fz.fastcluster.SortJob <in> <out> <num_reducer>");
			System.exit(4);
		}

		Job job = Job.getInstance(conf,
				"sort the input:"
						+ otherArgs[0]);
		job.setJarByClass(SortJob.class);
		job.setMapperClass(SortMapper.class);
//		job.setCombinerClass(SortReducer.class);
		job.setReducerClass(SortReducer.class);
		
		job.setNumReduceTasks(Integer.parseInt(otherArgs[2]));
		
		job.setOutputKeyClass(CustomDoubleWritable.class);
		job.setOutputValueClass(IntDoublePairWritable.class);
		
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		
		SequenceFileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		SequenceFileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
		return job.waitForCompletion(true) ? 0 : 1;

	}
	
	private static class SortMapper extends Mapper<DoubleWritable,IntDoublePairWritable,
		CustomDoubleWritable,IntDoublePairWritable>{
		private CustomDoubleWritable mul = new CustomDoubleWritable();
		public void map(DoubleWritable key,IntDoublePairWritable value,Context cxt)throws IOException,
			InterruptedException{
			mul.set(key.get());
			cxt.write(mul, value);
		}
	}
	
	private static class SortReducer extends Reducer<CustomDoubleWritable,IntDoublePairWritable,
		CustomDoubleWritable,IntDoublePairWritable>{
		public void reduce(CustomDoubleWritable key,Iterable<IntDoublePairWritable> vs,Context cxt)
			throws IOException,InterruptedException{
			for(IntDoublePairWritable v:vs){
				cxt.write(key, v);
				Utils.simpleLog("mul:"+key.get()+",other:"+v.toString());
			}
		}
	}

}
