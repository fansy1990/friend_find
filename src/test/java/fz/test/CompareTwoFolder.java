/**
 * 
 */
package fz.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.fz.util.HUtils;

import fz.utils.TestHUtils;

/**
 * @author fansy
 * @date 2015-7-10
 */
public class CompareTwoFolder {

	public static void main(String[] args) throws IOException {
		TestHUtils.set();
		String tt="/user/root/_filter/deduplicate";
//		tt="/user/root/_filter/preparevectors"";
		Path one = new Path(HUtils.getHDFSPath(tt));
		
		String t =getFolderInfo(one.toString(),"true");
		System.out.println(t);
	}
	
	public static String getFolderInfo(String folder,String flag) throws FileNotFoundException, IOException{
		StringBuffer buff = new StringBuffer();
		FileSystem fs = HUtils.getFs();
		Path path = HUtils.getHDFSPath(folder, flag);
		
		FileStatus[] files = fs.listStatus(path);
		
		buff.append("contain files:"+files.length+"\t[");
		String filename="";
		for(FileStatus file:files){
			path = file.getPath();
			filename=path.toString();
			buff.append(filename.substring(filename.length()-1))
				.append(":").append(file.getLen()).append(",");
		}
		filename=buff.substring(0, buff.length()-1);
		
		
		return filename+"]";
	}
}
