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

import com.fz.filter.keytype.DoubleArrIntWritable;
import com.fz.thread.ClusterCounter;
import com.fz.util.HUtils;


/**
 * 输入：
 *     id  ,用户有效向量
 * 输出：
 *     id_i, <type_i,用户有效向量>
 * @author fansy
 * @date 2015-6-2
 */

// Mapper 的输出有两个，所以这里随便写是没有问题的 
public class ClusterDataMapper extends Mapper<IntWritable, DoubleArrIntWritable, IntWritable, DoubleArrIntWritable> {

	private Logger log = LoggerFactory.getLogger(ClusterDataMapper.class);
//	private String center = null;
	private int k =-1;
	private double dc =0.0;
	private int iter_i =0;
	private int start =0;
	private DoubleArrIntWritable typeDoubleArr = new DoubleArrIntWritable();
	private IntWritable vectorI = new IntWritable();
	
	private MultipleOutputs<IntWritable,DoubleArrIntWritable> out;

	@Override
	public void setup(Context cxt){
//		center = cxt.getConfiguration().get("CENTER");
		k = cxt.getConfiguration().getInt("K", 3);
		dc = cxt.getConfiguration().getDouble("DC", Double.MAX_VALUE);
		iter_i=cxt.getConfiguration().getInt("ITER_I", 0);
		start=iter_i!=1?1:0;
		out = new MultipleOutputs<IntWritable,DoubleArrIntWritable>(cxt); 
		log.info("第{}次循环...",iter_i);
	}
	
	@Override
	public void map(IntWritable key,DoubleArrIntWritable  value,Context cxt){
		double[] inputI= value.getDoubleArr();
		
		int[] types = new int[k];
		double[] smallDistance = new double[k];// k clustered points near the given inpuI
		// initial smallDistance and types
		for(int i=0;i<k;i++){
			smallDistance[i]=Double.MAX_VALUE;
			types[i]=-1;
		}
		
		// hdfs
		Configuration conf = cxt.getConfiguration();
		FileSystem fs = null;
		Path path = null;
		
		SequenceFile.Reader reader = null;
		try {
			fs = FileSystem.get(conf);
			// read all before center files 
			String parentFolder =null;
			double distance = Double.MAX_VALUE;
			
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
					
						if(distance>=dc){// not count the farest point
							continue;
						}
						// else if distance is small enough then modify the small distance and the type
						checkAndModify(smallDistance,types,distance	,dvalue.getIdentifier());
					}
				}
			}
			
			// else 
//			log.info("smallDistance:{},types:{}",new Object[]{HUtils.doubleArr2Str(smallDistance),
//					HUtils.intArr2Str(types)});
			int typeIndex = getTypeIndex(smallDistance,types);
//			log.info("smallDistance:{},types:{}",new Object[]{HUtils.doubleArr2Str(smallDistance),
//					HUtils.intArr2Str(types)});
			
			vectorI.set(key.get());// 用户id
			typeDoubleArr.setValue(inputI,typeIndex);
			
			if(typeIndex!=-1){
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
	


	/**
	 * 针对每个点寻找其k个临近点，这k个临近点与当前点的距离从小到大排序
	 * 从第一个距离开始遍历，如果type不是-1，那么type就是当前点的类别
	 * first select the smallest smallDistance if the types is not -1 then return the type
	 * else select the second smallest ,and so on
	 * at last ,return -1
	 * @param smallDistance
	 * @param types
	 * @return
	 */
	private int getTypeIndex(double[] smallDistance, int[] types) {
		// sort the smallDistance 
		// sort the types array use the same index as smallDistance 
		for (int i = 1; i < smallDistance.length; i++) {
			for (int j = i; j > 0; j--) {
				if (smallDistance[j] < smallDistance[j - 1]) {
					swap(smallDistance,j,j-1);
					swap(types,j,j-1);
				} else
					break;
			}
		}
		for(int i=0;i<types.length;i++){
			if(types[i]!=-1){
				return types[i];
			}
		}
		return -1;
	}

	/**
	 * @param smallDistance
	 * @param j
	 * @param i
	 */
	private void swap(double[] smallDistance, int j, int i) {
		double o = smallDistance[j];
		smallDistance[j]=smallDistance[i];
		smallDistance[i]=o;
	}
	private void swap(int[] smallDistance, int j, int i) {
		int o = smallDistance[j];
		smallDistance[j]=smallDistance[i];
		smallDistance[i]=o;
	}

	/**
	 * 每次寻找smallDistance中最大的值max，以及下标maxIndex，
	 * 如果传入的distance比max小，那么就用distance和type来替换smallDistance[maxIndex]
	 * 以及 types[maxIndex]的值
	 * @param smallDistance 保持的最小的距离数组 
	 * @param types 保持的距离数组对应的类别
	 * @param distance 新的距离
	 * @param type  新的距离对应的类别
	 */
	private void checkAndModify(double[] smallDistance, int[] types,
			double distance, int type) {
		double max= smallDistance[0];
		int maxIndex =0;
		for(int i=1;i<smallDistance.length;i++){
			if(max<smallDistance[i]){
				maxIndex=i;
				max=smallDistance[i];
			}
		}
		if(max>distance){
			smallDistance[maxIndex]=distance;
			types[maxIndex]= type;
		}
	}

//	public void readCenterAndWrite(String centerPath){
//		
//	}
	
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
