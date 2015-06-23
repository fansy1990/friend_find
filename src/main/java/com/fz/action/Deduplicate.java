/**
 * 
 */
package com.fz.action;

import org.apache.hadoop.util.ToolRunner;

import com.fz.filter.DeduplicateJob;
import com.fz.util.HUtils;

/**
 * @author fansy
 * @date 2015-6-23
 */
public class Deduplicate implements Runnable {

	private String input;
	private String output;
	
	public Deduplicate(String input,String output){
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
			ToolRunner.run(HUtils.getConf(), new DeduplicateJob(),args );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
