/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

//import com.fz.filter.FindInitDCJob;
import com.fz.filter.GetAttributesJob;
//import com.fz.filter.GetMaxMinJob;
//import com.fz.filter.NormalizationJob;
import com.fz.util.HUtils;
import com.fz.util.Utils;

/**
 * @author fansy
 * @date 2015-6-25
 */
public class PreProcess implements Runnable {

	private String input;
	private String output;
	private float delta;
	
	
	public PreProcess(String input, String output, String delta) {
		this.input=input;
		this.output=output;
		this.delta=Float.parseFloat(delta.substring(0,delta.length()-1))/100;
	}
	/**
	 * 运行4个MR任务
	 * 1. 属性提取
	 * 2. 列最大最小值
	 * 3.  归一化
	 * 4. 寻找dc阈值
	 */
	@Override
	public void run() {
		// 
		
		String[] args= new String[]{
				HUtils.getHDFSPath(input),
				HUtils.getHDFSPath(HUtils.FILTER_GETATTRIBUTES)
		};
		try{
			//属性提取
			int ret=-1;
			ret=ToolRunner.run(HUtils.getConf(), new GetAttributesJob(),args );
			if(ret!=0){
				Utils.simpleLog("属性提取任务失败！");
				return ;
			}
			
//			// 列最大最小值
//			args=new String[]{
//					HUtils.getHDFSPath(HUtils.FILTER_GETATTRIBUTES),
//					HUtils.getHDFSPath(HUtils.FILTER_GETMAXMIN),
//					"4"
//			};
//			ret=ToolRunner.run(HUtils.getConf(), new GetMaxMinJob(),args );
//			if(ret!=0){
//				Utils.simpleLog("列最大最小值任务失败！");
//				return ;
//			}
//			// 归一化
//			args=new String[]{
//					HUtils.getHDFSPath(HUtils.FILTER_GETATTRIBUTES),
//					HUtils.getHDFSPath(HUtils.FILTER_NORMALIZATION),
//					HUtils.getHDFSPath(HUtils.FILTER_GETMAXMIN+"/part-r-00000")
//			};
//			ret=ToolRunner.run(HUtils.getConf(), new NormalizationJob(),args );
//			if(ret!=0){
//				Utils.simpleLog("归一化任务失败！");
//				return ;
//			}
//			// 最佳dc阈值
//			args=new String[]{
//					HUtils.getHDFSPath(HUtils.FILTER_NORMALIZATION),
//					HUtils.getHDFSPath(output)
//			};
//			ret=ToolRunner.run(HUtils.getConf(), new FindInitDCJob(),args );
//			if(ret!=0){
//				Utils.simpleLog("寻找最佳DC阈值任务失败！");
//				return ;
//			}
			
			// 最佳阈值寻找，非MR任务
//			double bestDCDelta= HUtils.findInitDC(delta,output);
//			Utils.simpleLog("最佳阈值："+bestDCDelta);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	/**
	 * @return the delta
	 */
	public float getDelta() {
		return delta;
	}
	/**
	 * @param delta the delta to set
	 */
	public void setDelta(float delta) {
		this.delta = delta;
	}

}
