/**
 * 
 */
package com.fz.filter.mr;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.fz.fast_cluster.keytype.DoubleArrStrWritable;
import com.fz.util.Utils;

/**
 * 输出
 * EmailHash \t [reputation,upVotes,downVotes,views]
 * @author fansy
 * @date 2015-6-23
 */
public class GetAttributesMapper extends
		Mapper<LongWritable, Text, DoubleArrStrWritable, NullWritable> {
	private DoubleArrStrWritable attributes = null;
	private String emailHash_="EmailHash";
	private String upVotes_="UpVotes";
	private String downVotes_="DownVotes";
	private String reputation_="Reputation";
	private String views_ = "Views";
	public void map(LongWritable key,Text value,Context cxt) throws InterruptedException,IOException{
		if(!value.toString().trim().startsWith("<row")){
			return ;
		}
		String emailHash = Utils.getAttrValInLine(value.toString(),emailHash_);
		double upVotes = getDouble(Utils.getAttrValInLine(value.toString(), upVotes_));
		double downVotes=getDouble(Utils.getAttrValInLine(value.toString(), downVotes_));
		double views=getDouble(Utils.getAttrValInLine(value.toString(), views_));
		double reputation=getDouble(Utils.getAttrValInLine(value.toString(), reputation_));
		
		
		attributes= new DoubleArrStrWritable(new double[]{reputation,upVotes,downVotes,views},emailHash);
		cxt.write(attributes, NullWritable.get());
	}
	
	private double getDouble(String doubleStr){
		double ret =0;
		try{
			ret= Double.parseDouble(doubleStr);
		}catch(Exception e){
			ret = 0;
		}
		return ret;
	}
}
