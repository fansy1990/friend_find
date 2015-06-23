/**
 * 
 */
package com.fz.filter.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.filter.FilterCounter;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class FindInitDCMapper extends Mapper<DoubleArrWritable,NullWritable,DoubleWritable, NullWritable> {

	private Path input;
	private DoubleWritable distance;
	@Override 
	public void setup(Context cxt){
		input=new Path(cxt.getConfiguration().get("INPUT"));// 
	}
	
	@Override
	public void map(DoubleArrWritable key, NullWritable value,Context cxt){
		cxt.getCounter(FilterCounter.MAP_COUNTER).increment(1L);
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(input),
					Reader.bufferSize(4096), Reader.start(0));
			DoubleArrWritable dkey = (DoubleArrWritable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			Writable dvalue = (Writable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				distance.set(HUtils.getDistance(key.getDoubleArr(), dkey.getDoubleArr()));
				cxt.write(distance, NullWritable.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
	}
	
	@Override
	public void cleanup(Context cxt){
		cxt.getConfiguration().setLong(HUtils.MAP_COUNTER, 
				cxt.getCounter(FilterCounter.MAP_COUNTER).getValue());
	}
}
