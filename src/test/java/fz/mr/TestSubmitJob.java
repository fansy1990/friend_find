/**
 * 
 */
package fz.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.fz.fastcluster.keytype.CustomDoubleWritable;

/**
 * 这样获取不到map和reduce进度状态，只能获取runState的，所以需要使用其他方式；
 * @author fansy
 * @date 2015-6-17
 */
public class TestSubmitJob {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

//		Configuration conf = HUtils.getConf();
		Configuration conf = new Configuration();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length !=3) {
	      System.err.println("Usage: fz.filter.DeduplicateJob <in> <out> <userCustomOrNot>");
	      System.exit(2);
	    }
	    Job job =  Job.getInstance(conf,"Test  :"+otherArgs[0]+" to "+otherArgs[1]);
	    job.setJarByClass(TestSubmitJob.class);
	    job.setMapperClass(TestMapper.class);
	    
//	    job.setNumReduceTasks(0);
	    job.setNumReduceTasks(2);
	    job.setMapOutputKeyClass(DoubleWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
	    if(otherArgs[2].equals("true")){
	    	job.setOutputKeyClass(CustomDoubleWritable.class);
	    	job.setReducerClass(TestCustomReducer.class);
	    }else{
	    	job.setOutputKeyClass(DoubleWritable.class);	
	    	job.setReducerClass(TestReducer.class);
	    }
	    
	    job.setOutputValueClass(IntWritable.class);
	    
//	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
	    FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
	    
	   System.exit( job.waitForCompletion(true) ? 0 : 1);
    	
	}
	
	public static class TestMapper extends Mapper<LongWritable,Text,DoubleWritable,IntWritable>{
		private DoubleWritable d= new DoubleWritable();
		private IntWritable i = new IntWritable();
		public void map(LongWritable key,Text value,Context cxt){
			d.set(Double.parseDouble(value.toString()));
			try {
				cxt.write(d, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static class TestReducer extends Reducer<DoubleWritable,IntWritable,DoubleWritable,IntWritable>{
		public void reduce(DoubleWritable key,Iterable<IntWritable> values,Context cxt)throws 
			IOException,InterruptedException{
			for(IntWritable v:values){
				cxt.write(key, v);
			}
		}
	}
	
	public static class TestCustomReducer extends Reducer<DoubleWritable,IntWritable,CustomDoubleWritable,IntWritable>{
//		private CustomDoubleWritable
		public void reduce(DoubleWritable key,Iterable<IntWritable> values,Context cxt)throws 
			IOException,InterruptedException{
			for(IntWritable v:values){
//				cxt.write(key, v);
			}
		}
	}
}
