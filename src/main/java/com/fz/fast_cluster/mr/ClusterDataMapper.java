/**
 * 
 */
package com.fz.fast_cluster.mr;

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

import com.fz.fast_cluster.keytype.DoubleArrWritable;
import com.fz.util.HUtils;


/**
 * @author fansy
 * @date 2015-6-2
 */
public class ClusterDataMapper extends Mapper<DoubleArrWritable, IntWritable, DoubleArrWritable, IntWritable> {

	private Logger log = LoggerFactory.getLogger(ClusterDataMapper.class);
//	private String center = null;
	private int k =-1;
	private double dc =0.0;
	private int iter_i =0;
	private int start =0;
	private DoubleArrWritable doubleArr;
	private IntWritable typeInt = new IntWritable();
	
	private MultipleOutputs<DoubleArrWritable,IntWritable> out;  

	@Override
	public void setup(Context cxt){
//		center = cxt.getConfiguration().get("CENTER");
		k = cxt.getConfiguration().getInt("K", 3);
		dc = cxt.getConfiguration().getDouble("DC", Double.MAX_VALUE);
		iter_i=cxt.getConfiguration().getInt("ITER_I", 0);
		start=iter_i!=1?1:0;
		out = new MultipleOutputs<DoubleArrWritable,IntWritable>(cxt);  
	}
	
	@Override
	public void map(DoubleArrWritable key,IntWritable value,Context cxt){
		double[] inputI= key.getDoubleArr();
		
		
		int[] types = new int[k];
		double[] smallDistance = new double[k];// k clustered points near the given inpuI
		// initial smallDistance and types
		for(int i=0;i<k;i++){
			smallDistance[i]=Double.MAX_VALUE;
			types[i]=-1;
		}
		
		// hdfs
		Configuration conf = HUtils.getConf();
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
				
				parentFolder=HUtils.getHDFSPath(HUtils.CENTERPATH+"/iter_"+i+"/clustered");
				RemoteIterator<LocatedFileStatus> files=fs.listFiles(new Path(parentFolder), false);
				
				while(files.hasNext()){
					path = files.next().getPath();
					if(!path.toString().contains("part")){
						continue; // return 
					}
					reader = new SequenceFile.Reader(conf, Reader.file(path),
							Reader.bufferSize(4096), Reader.start(0));
					DoubleArrWritable dkey = (DoubleArrWritable) ReflectionUtils.newInstance(
							reader.getKeyClass(), conf);
					IntWritable dvalue = (IntWritable) ReflectionUtils.newInstance(
							reader.getValueClass(), conf);
					while (reader.next(dkey, dvalue)) {// read file literally
						distance = HUtils.getDistance(inputI, dkey.getDoubleArr());
					
						if(distance>=dc){// not count the farest point
							continue;
						}
						// else if distance is small enough than modify the small distance and the type
						checkAndModify(smallDistance,types,distance	,dvalue.get());
					}
				}
			}
			
			// else 
			log.info("smallDistance:{},types:{}",new Object[]{HUtils.doubleArr2Str(smallDistance),
					HUtils.intArr2Str(types)});
			int typeIndex = getTypeIndex(smallDistance,types);
			log.info("smallDistance:{},types:{}",new Object[]{HUtils.doubleArr2Str(smallDistance),
					HUtils.intArr2Str(types)});
			doubleArr = new DoubleArrWritable(inputI);
			typeInt.set(typeIndex);
			
			if(typeIndex!=-1){
				log.info("clustered-->doubleArr:{},typeInt:{}",new Object[]{doubleArr,typeInt});
				out.write("clustered", doubleArr, typeInt,"clustered/part");	
			}else{
				log.info("unclustered---->doubleArr:{},typeInt:{}",new Object[]{doubleArr,typeInt});
				out.write("unclustered", doubleArr, typeInt,"unclustered/part");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}

	}
	


	/**
	 * first select the smallest smallDistance if the types is not -1 then return 
	 * else select the second smallest ,and so on
	 * at last ,return -1
	 * @param smallDistance
	 * @param types
	 * @return
	 */
	private int getTypeIndex(double[] smallDistance, int[] types) {
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
	 * @param smallDistance
	 * @param types
	 * @param distance
	 * @param type 
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

	public void readCenterAndWrite(String centerPath){
		
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
