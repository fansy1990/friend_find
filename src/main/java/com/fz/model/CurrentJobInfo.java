/**
 * 
 */
package com.fz.model;

import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;

import com.fz.util.Utils;

/**
 * MR任务监控简要信息类
 * @author fansy
 * @date 2015-6-16
 */
public class CurrentJobInfo implements Serializable,Comparable<CurrentJobInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobId;
	private String jobName;
	private float mapProgress;
	private float redProgress;
	private String runState;
	private long startTime;
	
	public CurrentJobInfo(RunningJob runningJob,long startTime,int runStateInt) throws IOException{
		this.jobId=runningJob.getID().toString();
		this.jobName=runningJob.getJobName();
		this.mapProgress=runningJob.mapProgress();
		this.redProgress=runningJob.reduceProgress();
		this.startTime=startTime;
		this.runState=JobStatus.getJobRunState(runStateInt);
		if("PREP".equals(this.runState)||"RUNNING".equals(this.runState)){
			this.runState=Utils.getDotState(this.runState);
		}
	}
	public CurrentJobInfo(){
		this.jobId="null";
		this.jobName="null";
		this.mapProgress=0;
		this.redProgress=0;
		this.runState="null";
		this.startTime=System.currentTimeMillis();
	}
	@Override
	// 升序
	public int compareTo(CurrentJobInfo o) {
		if(this.startTime==o.startTime){
			return 0;
		}
		return this.startTime>o.startTime?1:-1;
	}


	public String getJobId() {
		return jobId;
	}


	public void setJobId(String jobId) {
		this.jobId = jobId;
	}


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}


	public float getMapProgress() {
		return mapProgress;
	}


	public void setMapProgress(float mapProgress) {
		this.mapProgress = mapProgress;
	}


	public float getRedProgress() {
		return redProgress;
	}


	public void setRedProgress(float redProgress) {
		this.redProgress = redProgress;
	}

/**
 * 
 * @return
 */
	public String getRunState() {
		
		return runState;
	}


	public void setRunState(String runState) {
		this.runState = runState;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	
	@Override
	public String toString(){
		return "jobID:"+this.jobId+",jobName:"+this.jobName+",map:"+this.mapProgress+",reduce:"+
	this.redProgress+",state:"+this.runState;
	}
}
