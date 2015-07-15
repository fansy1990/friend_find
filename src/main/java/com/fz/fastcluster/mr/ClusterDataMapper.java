/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.fastcluster.ClusterCounter;
import com.fz.filter.keytype.DoubleArrIntWritable;
import com.fz.util.HUtils;


/**
 * 输入：
 *     id  ,用户有效向量
 * 输出：
 *     id_i, <type_i,用户有效向量>
 * @author fansy
 * @date 2015-6-2
 */

// Mapper 的输出有两个，所以这里的输出类型随便写是没有问题的 
public class ClusterDataMapper extends Mapper<IntWritable, DoubleArrIntWritable, IntWritable, DoubleArrIntWritable> {

	private Logger log = LoggerFactory.getLogger(ClusterDataMapper.class);
//	private String center = null;
//	private int k =-1;
	private double dc =0.0;
	private int iter_i =0;
	private int start =0;
	private DoubleArrIntWritable typeDoubleArr = new DoubleArrIntWritable();
	private IntWritable vectorI = new IntWritable();
	
	private MultipleOutputs<IntWritable,DoubleArrIntWritable> out;

	@Override
	public void setup(Context cxt){
//		center = cxt.getConfiguration().get("CENTER");
//		k = cxt.getConfiguration().getInt("K", 3);
		dc = cxt.getConfiguration().getDouble("DC", Double.MAX_VALUE);
		iter_i=cxt.getConfiguration().getInt("ITER_I", 0);
		start=iter_i!=1?1:0;
		out = new MultipleOutputs<IntWritable,DoubleArrIntWritable>(cxt); 
		cxt.getCounter(ClusterCounter.CLUSTERED).increment(0);
		cxt.getCounter(ClusterCounter.UNCLUSTERED).increment(0);
		
		log.info("第{}次循环...",iter_i);
	}
	
	@Override
	public void map(IntWritable key,DoubleArrIntWritable  value,Context cxt){
		double[] inputI= value.getDoubleArr();
		
		// hdfs
		Configuration conf = cxt.getConfiguration();
		FileSystem fs = null;
		Path path = null;
		
		SequenceFile.Reader reader = null;
		try {
			fs = FileSystem.get(conf);
			// read all before center files 
			String parentFolder =null;
			double smallDistance = Double.MAX_VALUE;
			int smallDistanceType=-1;
			double distance;
			
			// if iter_i !=0,then start i with 1,else start with 0
			for(int i=start;i<iter_i;i++){// all files are clustered points
				
				parentFolder=HUtils.CENTERPATH+"/iter_"+i+"/clustered";
				RemoteIterator<LocatedFileStatus> files=fs.listFiles(new Path(parentFolder), false);
				
				while(files.hasNext()){
					path = files.next().getPath();
					if(!path.toString().contains("part")){
						continue; // return 
					}
					reader = new SequenceFile.Reader(conf, Reader.file(path),
							Reader.bufferSize(4096), Reader.start(0));
					IntWritable dkey = (IntWritable) ReflectionUtils.newInstance(
							reader.getKeyClass(), conf);
					DoubleArrIntWritable dvalue = (DoubleArrIntWritable) ReflectionUtils.newInstance(
							reader.getValueClass(), conf);
					while (reader.next(dkey, dvalue)) {// read file literally
						distance = HUtils.getDistance(inputI, dvalue.getDoubleArr());
					
						if(distance>dc){// not count the farest point
							continue;
						}
						// 这里只要找到离的最近的点并且其distance<=dc 即可，把这个点的type赋值给当前值即可
						if(distance<smallDistance){
							smallDistance=distance;
							smallDistanceType=dvalue.getIdentifier();
						}
						
					}// while
				}// while
			}// for
					
			vectorI.set(key.get());// 用户id
			typeDoubleArr.setValue(inputI,smallDistanceType);
			
			if(smallDistanceType!=-1){
				log.info("clustered-->vectorI:{},typeDoubleArr:{}",new Object[]{vectorI,typeDoubleArr.toString()});
				cxt.getCounter(ClusterCounter.CLUSTERED).increment(1);
				out.write("clustered", vectorI, typeDoubleArr,"clustered/part");	
			}else{
				log.info("unclustered---->vectorI:{},typeDoubleArr:{}",new Object[]{vectorI,typeDoubleArr.toString()});
				cxt.getCounter(ClusterCounter.UNCLUSTERED).increment(1);
				out.write("unclustered", vectorI, typeDoubleArr,"unclustered/part");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}

	}
	

	@Override
	public void cleanup(Context cxt){
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
