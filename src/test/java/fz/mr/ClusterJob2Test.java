/**
 * 
 */
package fz.mr;

import org.apache.hadoop.util.ToolRunner;

import com.fz.mr.cluster.ClusterJob2;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob2Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String[] ar={
				"hdfs://node101:/user/root/iris_out/part-r-00000",
				"hdfs://node101:/user/root/iris_out00",
				"hdfs://node101:/user/root/iris_out/part-r-00000"
		};
		
		ToolRunner.run(HUtils.getConf(), new ClusterJob2(), ar);
	}

}
