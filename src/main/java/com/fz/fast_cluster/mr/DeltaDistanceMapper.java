/**
 * 
 */
package com.fz.fast_cluster.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.fast_cluster.keytype.DDoubleWritable;
import com.fz.fast_cluster.keytype.DoubleArrStrWritable;
import com.fz.util.HUtils;

/**
 * 
 * @author fansy
 * @date 2015-6-1
 */
public class DeltaDistanceMapper extends
		Mapper<DoubleArrStrWritable, DoubleWritable, DoubleArrStrWritable, DDoubleWritable> {
	private Logger log = LoggerFactory.getLogger(DeltaDistanceMapper.class);
	private Path input ;
	private DDoubleWritable sd= new DDoubleWritable();
	
	@Override
	public void setup(Context cxt){
		input = new Path(cxt.getConfiguration().get("INPUT"));
	}
	
	// only read for one file 
	// maybe add more files support for the another time 
	@Override
	public void map(DoubleArrStrWritable key, DoubleWritable value, Context cxt) throws IOException,InterruptedException{
		// hdfs
		Configuration conf = cxt.getConfiguration();

        SequenceFile.Reader reader = null;
        double minDistance = Double.MAX_VALUE;
        double maxDistance = -Double.MAX_VALUE;
        try {
            reader = new SequenceFile.Reader(conf,Reader.file(input),
            		Reader.bufferSize(4096),Reader.start(0));
            DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            DoubleWritable dvalue = (DoubleWritable) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
            while (reader.next(dkey, dvalue)) {//
            	double d =HUtils.getDistance(key.getDoubleArr(),dkey.getDoubleArr());
            	if(d>maxDistance&& d>0){
        			maxDistance=d;
        		}
            	if(value.get()<dvalue.get()){// 
            		if(d<minDistance&&d>0){ // d should be greater than 0
            			minDistance=d;
            		}
            	}
            	
            }
        } catch(Exception e){
        	e.printStackTrace();
        }finally {
            IOUtils.closeStream(reader);
        }
        // set the biggest point density 
        log.info("key:{},value.get:{},minDistance:{},maxDistance:{}",new Object[]{key,value.get(),minDistance,maxDistance});
        sd.setDistance(minDistance==Double.MAX_VALUE?maxDistance:minDistance);
        sd.setSum(value.get());
//        log.info("sd.sum:{},sd.distance:{}", new Object[]{value.get(),minDistance});
        cxt.write(key, sd);
	}
}






