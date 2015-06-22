/**
 * 
 */
package fz.mr;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fz.model.CurrentJobInfo;
import com.fz.thread.RunCluster1;
import com.fz.util.HUtils;

/**
 * 这样获取不到map和reduce进度状态，只能获取runState的，所以需要使用其他方式；
 * @author fansy
 * @date 2015-6-17
 */
public class TestSubmitJob {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		String input ="/user/root/path_based.csv";
		String splitter=",";
		String delta ="1.4";
		String method="gaussian";
		HUtils.setJobStartTime(System.currentTimeMillis());// 使用当前时间即可
		HUtils.JOBNUM=2;
		// 2. 使用Thread的方式启动一组MR任务
		new Thread(new RunCluster1(input, splitter, delta, method)).start();
		Map<String ,Object> jsonMap = new HashMap<String,Object>();
    	List<CurrentJobInfo> currJobList =null;
    	boolean flag =true;
    	while(flag){
    		Thread.sleep(1000);
    		currJobList= HUtils.getJobs();
    		jsonMap.put("rows", currJobList);// 放入数据
    		if(currJobList.size()==0){
    			jsonMap.put("finished", "false");
    			continue ;
    		}
    		if(currJobList.size()==HUtils.JOBNUM){// 如果返回的list有JOBNUM个，那么才可能完成任务
    			if("success".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "true");
    				flag=false;
    			}else if("running".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "false");
    			}else{
    				jsonMap.put("finished", "error");
    				flag=false;
    			}
    		}else{
    			if("fail".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))||
    					"kill".equals(HUtils.hasFinished(currJobList.get(currJobList.size()-1)))){
    				jsonMap.put("finished", "error");
    				flag=false;
    			}else{
    				jsonMap.put("finished", "false");
    			}
        	}
    		System.out.println(new java.util.Date()+": jsonMap:"+jsonMap);
    		
    	}
    	System.out.println("--------------------------");
    	
	}
}
