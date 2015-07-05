/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

import com.fz.fastcluster.keytype.DoubleArrStrWritable;
import com.fz.filter.FilterCounter;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class FindInitDCMapper extends Mapper<DoubleArrStrWritable,NullWritable,DoubleWritable, NullWritable> {

	private Path input;
	private DoubleWritable distance=new DoubleWritable();
	@Override 
	public void setup(Context cxt){
		input=new Path(cxt.getConfiguration().get("INPUT"));// 
	}
	
	@Override
	public void map(DoubleArrStrWritable key, NullWritable value,Context cxt)throws InterruptedException,IOException{
		cxt.getCounter(FilterCounter.MAP_COUNTER).increment(1L);
		Configuration conf = cxt.getConfiguration();
		SequenceFile.Reader reader = null;
		FileStatus[] fss=input.getFileSystem(conf).listStatus(input);
		for(FileStatus f:fss){
			if(!f.toString().contains("part")){
				continue; // 排除其他文件
			}
			try {
				reader = new SequenceFile.Reader(conf, Reader.file(f.getPath()),
						Reader.bufferSize(4096), Reader.start(0));
				DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils.newInstance(
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
	}
	
	@Override
	public void cleanup(Context cxt){
		cxt.getConfiguration().setLong(HUtils.MAP_COUNTER, 
				cxt.getCounter(FilterCounter.MAP_COUNTER).getValue());
	}
}
