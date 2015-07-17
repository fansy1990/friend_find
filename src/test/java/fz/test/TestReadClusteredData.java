/**
 * 
 */
package fz.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.util.ReflectionUtils;

import com.fz.filter.keytype.DoubleArrIntWritable;
import com.fz.model.UserGroup;
import com.fz.util.HUtils;
import com.fz.util.Utils;

import fz.utils.TestHUtils;

/**
 * 读取记录数有问题？
 * @author fansy
 * @date 2015-7-16
 */
public class TestReadClusteredData {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		TestHUtils.set();
		String input ="/user/root/_center";
		
//		resolve(input);
//		System.out.println(HUtils.getFs().exists(HUtils.getHDFSPath(HUtils.CENTERPATH, "false")));
		RemoteIterator<LocatedFileStatus>t=HUtils.getFs().listFiles(HUtils.getHDFSPath(input,"false"),true);
		FileStatus[] fss =HUtils.getFs().listStatus(HUtils.getHDFSPath(input,"false"));
		for(FileStatus f:fss){
			System.out.println(f.toString());
		}
		
	}
	
	public static List<UserGroup> resolve(String input) throws FileNotFoundException, IOException{
		List<UserGroup> list = new ArrayList<UserGroup>();
		
		Path path = HUtils.getHDFSPath(input, "false");
		FileSystem fs = HUtils.getFs();
		
		RemoteIterator<LocatedFileStatus>files =fs.listFiles(path, true);
		
		while(files.hasNext()){
			LocatedFileStatus lfs = files.next();
			
			input = lfs.getPath().toString();
			if(input.contains("/clustered/part")){
				path= lfs.getPath();
				if(lfs.getLen()>0){
					list.addAll(resolve(path));
				}
			}
		}
		Utils.simpleLog("一共读取了"+list.size()+"条记录！");
		return list;
	}

	/**
	 * 把分类的数据解析到list里面
	 * @param path
	 * @return
	 */
	private static Collection<? extends UserGroup> resolve(Path path) {
		// TODO Auto-generated method stub
		List<UserGroup> list = new ArrayList<UserGroup>();
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(path),
					Reader.bufferSize(4096), Reader.start(0));
			IntWritable dkey =  (IntWritable) ReflectionUtils
					.newInstance(reader.getKeyClass(), conf);
			DoubleArrIntWritable dvalue =  (DoubleArrIntWritable) ReflectionUtils
					.newInstance(reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				// 使用这个进行克隆
				list.add(new UserGroup(dkey.get(),dvalue.getIdentifier()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
		Utils.simpleLog("读取"+list.size()+"条记录，文件："+path.toString());
		return list;
	}
	
	
	

}
