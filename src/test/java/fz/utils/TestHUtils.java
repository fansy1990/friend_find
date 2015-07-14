/**
 * 
 */
package fz.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-7-10
 */
public class TestHUtils {

	public TestHUtils(){
		
	}
	public static Configuration getConf(){
		set();
		System.out.println("flag:"+HUtils.flag);
		return HUtils.getConf();
	} 
	
	public static FileSystem getFs(){
		set();
		System.out.println("flag:"+HUtils.flag);
		return HUtils.getFs();
	}
	
	public static void set(){
		HUtils.flag=false;
	}
}
