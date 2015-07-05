/**
 * 
 */
package com.fz.fastcluster.mr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.fastcluster.keytype.DoublePairWritable;
import com.fz.filter.keytype.IntPairWritable;
import com.fz.util.HUtils;

/**
 * 
 * @author fansy
 * @date 2015-6-1
 */
public class DeltaDistanceMapper extends
		Mapper<DoubleWritable, IntPairWritable, IntWritable, DoublePairWritable> {
	private Logger log = LoggerFactory.getLogger(DeltaDistanceMapper.class);
	
	private IntWritable vector_i= new IntWritable();
	private DoublePairWritable density_distance = new DoublePairWritable();
	
	private Path densityPath ;
	private Map<Integer,Double> densityMap= new HashMap<Integer,Double>();// vector_id,density
	private int max_density_vector_id=-1;// 最大的局部密度下标
	private double max_density=-Double.MAX_VALUE;// 最大的局部密度 
	@Override
	public void setup(Context cxt) throws IOException{
		densityPath = new Path(cxt.getConfiguration().get("DENSITYPATH"));
		Configuration conf = cxt.getConfiguration();
		SequenceFile.Reader reader = null;
		FileStatus[] fss=densityPath.getFileSystem(conf).listStatus(densityPath);
		for(FileStatus f:fss){
			if(!f.toString().contains("part")){
				continue; // 排除其他文件
			}
			try {
				reader = new SequenceFile.Reader(conf, Reader.file(f.getPath()),
						Reader.bufferSize(4096), Reader.start(0));
				IntWritable dkey = (IntWritable) ReflectionUtils.newInstance(
						reader.getKeyClass(), conf);
				DoubleWritable dvalue = (DoubleWritable) ReflectionUtils.newInstance(
						reader.getValueClass(), conf);
				while (reader.next(dkey, dvalue)) {// 循环读取文件
					densityMap.put(dkey.get(), dvalue.get());
					if(dvalue.get()>max_density){// 寻找最大局部密度的下标
						max_density=dvalue.get();
						max_density_vector_id=dkey.get();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				max_density_vector_id=-1;
			} finally {
				IOUtils.closeStream(reader);
			}
		}
		if(max_density_vector_id==-1){
			log.info("没有找到局部密度最大的向量，程序出错！");
		}
		// 把max_density_vector_id写入文件
		FileSystem fs = FileSystem.get(cxt.getConfiguration());
		Path path = new Path(HUtils.DELTADISTANCEBIN);
		if(fs.exists(path)){
			fs.delete(path, false);
		}
		FSDataOutputStream out = fs.create(path);
		try{
			out.writeInt(max_density_vector_id);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
		log.info("densityMap中的键值对个数：{},局部密度最大向量id：{},局部密度：{}",
				new Object[]{densityMap.size(),max_density_vector_id,max_density});
	}

	// distance_ij,<i,j>
	@Override
	public void map(DoubleWritable key, IntPairWritable value, Context cxt) throws IOException,InterruptedException{
		int vectorI= value.getFirst();
		int vectorJ= value.getSecond();
		if(vectorI==max_density_vector_id||vectorJ==max_density_vector_id){// 最大局部密度，需寻找离该点最大的距离,应该在reducer中判断
			// 这里应该直接输出即可
			vector_i.set(max_density_vector_id);
			density_distance.setFirst(max_density);
			density_distance.setSecond(key.get());
			cxt.write(vector_i, density_distance);
			log.info("vector_i:{},density:{},distance:{}",new Object[]{max_density_vector_id,max_density,key.get()});
			return ;
			
		}
		// 不是局部密度最大点，则找最小的即可
		double densityI=0;
		double densityJ=0;
		if(!densityMap.containsKey(vectorI)||!densityMap.containsKey(vectorJ)){//  两个任一个不存在，则不输出任何
			return ;
		}
		
		densityI=densityMap.get(vectorJ);
		densityJ=densityMap.get(vectorI);
		
		if(densityI<densityJ){// 输出   <I,大于I 的density的distance>
			vector_i.set(vectorI);
			density_distance.setFirst(densityI);
			density_distance.setSecond(key.get());// second 是distance
			cxt.write(vector_i, density_distance);
			log.info("vector_i:{},density:{},distance:{}",new Object[]{vectorI,densityI,key.get()});
		}
		if(densityI>densityJ){// 输出   <J,大于J 的density的distance>
			vector_i.set(vectorJ);
			density_distance.setFirst(densityJ);
			density_distance.setSecond(key.get());
			cxt.write(vector_i, density_distance);
			log.info("vector_i:{},density:{},distance:{}",new Object[]{vectorJ,densityJ,key.get()});
		}
	}
}






