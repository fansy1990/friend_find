/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

import com.fz.fast_cluster.keytype.DoubleArrStrWritable;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class NormalizationMapper extends
		Mapper<DoubleArrStrWritable, NullWritable, DoubleArrStrWritable, NullWritable> {

	private double[] max_;
	private double[] min_;
	
	@Override
	public void setup(Context cxt){
		Configuration conf = cxt.getConfiguration();
		Path input =new Path( conf.get("MAX_MIN"));
		
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(input),
					Reader.bufferSize(4096), Reader.start(0));
			DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			Writable dvalue = (Writable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				if("max".equals(dkey.getIdentifier())){
					max_=dkey.getDoubleArr();
				}
				if("min".endsWith(dkey.getIdentifier())){
					min_=dkey.getDoubleArr();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
	}
	
	@Override
	public void map(DoubleArrStrWritable key, NullWritable value,Context cxt)throws InterruptedException,IOException{
		normalize(key);
		cxt.write(key, value);
	}

	/**
	 * 最大最小值归一化[0,1]
	 * @param key
	 */
	private void normalize(DoubleArrStrWritable key) {
		for(int i=0;i<key.getDoubleArr().length;i++){
			key.getDoubleArr()[i]=(key.getDoubleArr()[i]-min_[i])/(max_[i]-min_[i]);
		}
	}
	
	
}
