/**
 * 
 */
package com.fz.thread;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fz.fastcluster.ClusterDataJob;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 * @author fansy
 * @date 2015-7-10
 */
public class RunCluster2 implements Runnable {

	private String input;
	private String output;
	private String delta;
	private String k;
	
	private Logger log = LoggerFactory.getLogger(RunCluster2.class);
	@Override
	public void run() {
		input=input==null?HUtils.FILTER_PREPAREVECTORS:input;
		output=output==null?HUtils.CENTERPATHPREFIX:output+"/iter_";
	
		// 加一个操作，把/user/root/preparevectors里面的数据复制到/user/root/_center/iter_0/unclustered里面
		
		HUtils.copy(input,output+"0/unclustered");
		try {
			Thread.sleep(200);// 暂停200ms 
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		
		// 求解dc的阈值，这里的dc不用传入进来即可，即delta的值
		// 阈值问题可以在讨论，这里暂时使用传进来的阈值即可
//		double dc =dcs[0];
		// 读取聚类中心文件
		Map<Object,Object> vectorsMap= HUtils.readSeq(output+"0/clustered/part-m-00000", Integer.parseInt(k));
		double[][] vectors = HUtils.getCenterVector(vectorsMap);
		double[] distances= Utils.getDistances(vectors);
		// 这里不使用传入进来的阈值
		
		int iter_i=0;
		int ret=0;
		double tmpDelta=0;
		int kInt = Integer.parseInt(k);
		try {
			do{
				if(iter_i>=distances.length){
//					delta= String.valueOf(distances[distances.length-1]/2);
					// 这里使用什么方式还没有想好。。。
					
					
					// 使用下面的方式
					tmpDelta=Double.parseDouble(delta);
					while(kInt-->0){// 超过k次后就不再增大
						tmpDelta*=2;// 每次翻倍
					}
					delta=String.valueOf(tmpDelta);
				}else{
					delta=String.valueOf(distances[iter_i]/2);
				}
				log.info("this is the {} iteration,with dc:{}",new Object[]{iter_i,delta});
				String[] ar={
						HUtils.getHDFSPath(output)+iter_i+"/unclustered",
						HUtils.getHDFSPath(output)+(iter_i+1),//output
						//HUtils.getHDFSPath(HUtils.CENTERPATHPREFIX)+iter_i+"/clustered/part-m-00000",//center file
						k,
						delta,
						String.valueOf((iter_i+1))
				};
				try{
					ret = ToolRunner.run(HUtils.getConf(), new ClusterDataJob(), ar);
					if(ret!=0){
						log.info("ClusterDataJob failed, with iteration {}",new Object[]{iter_i});
						break;
					}	
				}catch(Exception e){
					e.printStackTrace();
				}
				iter_i++;
				HUtils.JOBNUM++;// 每次循环后加1

			}while(shouldRunNextIter());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		if(ret==0){
			log.info("All cluster Job finished with iteration {}",new Object[]{iter_i});
		}
		
	}
	
	/**
	 * 是否应该继续下次循环
	 * 直接使用分类记录数和未分类记录数来判断
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	private boolean shouldRunNextIter()  {
		
		if(HUtils.UNCLUSTERED==0||HUtils.CLUSTERED==0){
			HUtils.JOBNUM-=2;// 不用监控 则减去2;
			return false;
		}
		return true;
		
	}
	
	public RunCluster2(){}
	
	public RunCluster2(String input,String output,String delta,String k){
		this.delta=delta;
		this.input=input;
		this.output=output;
		this.k=k;
	}
}
