/**
 * 
 */
package com.fz.mr.cluster;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.util.HUtils;

/**
 * 
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob2Mapper extends
		Mapper<KeyDoubleArr, IntWritable, KeyDoubleArr, SumDistance> {
	private Logger log = LoggerFactory.getLogger(ClusterJob2Mapper.class);
	private Path input ;
	private SumDistance sd= new SumDistance();
	
	@Override
	public void setup(Context cxt){
		input = new Path(cxt.getConfiguration().get("INPUT"));
	}
	
	@Override
	public void map(KeyDoubleArr key, IntWritable value, Context cxt) throws IOException,InterruptedException{
		// hdfs
		Configuration conf = cxt.getConfiguration();

        SequenceFile.Reader reader = null;
        double minDistance = Double.MAX_VALUE;
//        log.info("key:{},value:{},value.get():{}",new Object[]{key,value,value.get()});
        try {
            reader = new SequenceFile.Reader(conf,Reader.file(input),
            		Reader.bufferSize(4096),Reader.start(0));
            KeyDoubleArr dkey = (KeyDoubleArr) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            IntWritable dvalue = (IntWritable) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
//            long position = reader.getPosition();
            
            while (reader.next(dkey, dvalue)) {//循环读取文件
//              String syncSeen = reader.syncSeen() ? "*" : "";//SequenceFile中都有sync标记
//                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, dkey,
//                        dvalue);
            	if(value.get()<dvalue.get()){// 临近点个数需要大于当前值
            		double d =HUtils.getDistance(key.getDoubleArr(),dkey.getDoubleArr());
            		if(d<minDistance&&d>0){ // 取较小的距离,距离d需要大于0，排除自身和重复点
            			minDistance=d;
            		}
            	}
//            	position = reader.getPosition(); //下一条record开始的位置
            }
        } catch(Exception e){
        	e.printStackTrace();
        }finally {
            IOUtils.closeStream(reader);
        }
        sd.setDistance(minDistance);
        sd.setSum(value.get());
        log.info("sd.sum:{},sd.distance:{}", new Object[]{value.get(),minDistance});
        cxt.write(key, sd);
	}
}






