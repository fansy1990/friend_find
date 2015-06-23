/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.fz.util.Utils;

/**
 * 把EmailHash重复的记录的四个属性只取reputation 最大的一个，如果没有，则返回第一条记录即可 
 * @author fansy
 * @date 2015-6-23
 */
public class DeduplicateReducer extends Reducer<Text, Text, Text, NullWritable> {

	public void reduce(Text key,Iterable<Text> values,Context cxt	)throws InterruptedException,IOException{
		List<Text> vectors= new ArrayList<Text>();
		for(Text t:values){
			vectors.add(t);
		}
		if(vectors.size()==1){
			cxt.write(vectors.get(0), NullWritable.get());
			return ;
		}
		
		// 处理重复的记录
		String attrV=null;
		int repM=Integer.MAX_VALUE;
		int index=-1;
		int tmpRep=0;
		for(int i=0; i<vectors.size();i++){
			attrV= Utils.getAttrValInLine(vectors.get(i).toString(), "Reputation");
			try{
				tmpRep=Integer.parseInt(attrV);
			}catch(Exception e){
				tmpRep=repM;
			}
			if(tmpRep<repM){
				index=i;
				repM=tmpRep;
			}
		}
		if(index!=-1){
			cxt.write(vectors.get(index), NullWritable.get());
		}else{
			cxt.write(vectors.get(0), NullWritable.get());
		}
	}
}
