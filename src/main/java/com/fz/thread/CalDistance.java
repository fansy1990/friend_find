/**
 * 
 */
package com.fz.thread;

import org.apache.hadoop.util.ToolRunner;

import com.fz.filter.CalDistanceJob;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-7-2
 */
public class CalDistance implements Runnable {

	private String input;
	private String output;
	
	public CalDistance(String input,String output){
		this.input=input;
		this.output=output;
	}
	
	@Override
	public void run() {
		String [] args ={
				HUtils.getHDFSPath(input),
				HUtils.getHDFSPath(output)
		};
		try {
			ToolRunner.run(HUtils.getConf(), new CalDistanceJob(),args );
		} catch (Exception e) {
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
	
	

}
