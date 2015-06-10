/**
 * 
 */
package fz.mr;

import org.apache.hadoop.util.ToolRunner;

import com.fz.mr.cluster.ClusterJob1;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-1
 */
public class ClusterJob1Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String[] ar={
				"hdfs://node101:/user/root/iris_data.csv",
				"hdfs://node101:/user/root/iris_out",
				"0.5",
				"hdfs://node101:/user/root/iris_data.csv",
				","
		};
		
		ToolRunner.run(HUtils.getConf(), new ClusterJob1(), ar);
	}

}
