/**
 * 
 */
package fz.mr;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.util.ReflectionUtils;

import com.fz.mr.cluster.KeyDoubleArr;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-1
 */
public class SequenceFileReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "hdfs://node101:8020/user/root/iris_out/part-r-00000";
		readSeq(url);
	}
	
	
	public static Map<Object,Object> readSeq(String url){
		Path path = new Path(url);
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader =null;
		Map<KeyDoubleArr, IntWritable> map = new HashMap<KeyDoubleArr,IntWritable>();
		try{
			reader = new SequenceFile.Reader(conf, 
					Reader.file(path),Reader.bufferSize(4096),Reader.start(0));
			 KeyDoubleArr dkey = (KeyDoubleArr) ReflectionUtils.newInstance(
	                    reader.getKeyClass(), conf);
	            IntWritable dvalue = (IntWritable) ReflectionUtils.newInstance(
	                    reader.getValueClass(), conf);
	            long position = reader.getPosition();
	            
	            while (reader.next(dkey, dvalue)) {//循环读取文件
	              String syncSeen = reader.syncSeen() ? "*" : "";//SequenceFile中都有sync标记
	                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, dkey,
	                        dvalue);
	    
	            	position = reader.getPosition(); //下一条record开始的位置
	            	map.put(dkey, dvalue);
	            }
		}catch(Exception e){
			
		}finally {
            IOUtils.closeStream(reader);
        }
		return null;
	} 

}
