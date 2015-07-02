/**
 * 
 */
package com.fz.util;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.util.ReflectionUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fz.fast_cluster.keytype.DDoubleWritable;
import com.fz.fast_cluster.keytype.DoubleArrStrWritable;


/**
 * useless
 * not recommend to use this 
 * use matlab instead
 * @author fansy
 * @date 2015-6-2
 */
public class DrawPic {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String path = "hdfs://node101:8020/user/root/iris_deltadistance/part-r-00000";
		drawPic(path,null);
	}

	public static void drawPic(String url,String file) {
//		XYSeries xyseries = getXY(url);
		XYSeries xyseries = getXYseries(url);
		XYSeriesCollection xyseriescollection = new XYSeriesCollection(); // 再用XYSeriesCollection添加入XYSeries
																			// 对象
		xyseriescollection.addSeries(xyseries);

		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		// JFreeChart chart=ChartFactory.createXYAreaChart("xyPoit", "点的标号",
		// "出现次数", xyseriescollection, PlotOrientation.VERTICAL, true, false,
		// false);
		JFreeChart chart = ChartFactory.createScatterPlot("决策图", "点密度",
				"点距离", xyseriescollection, PlotOrientation.VERTICAL, true,
				false, false);
//		String file="d:/decision_chart.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(file), chart,
					470, 470);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new java.util.Date()+": finished drawing the pic in "+file);
	}

	public static XYSeries getXY(String url) {
		XYSeries xyseries = new XYSeries("");

		Path path = new Path(url);
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(path),
					Reader.bufferSize(4096), Reader.start(0));
			DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			DDoubleWritable dvalue = (DDoubleWritable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				xyseries.add(dvalue.getSum(), dvalue.getDistance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
		return xyseries;
	}
	
	/**
	 * return the x*y
	 * @param url
	 * @return
	 */
	public Double[] getR(String url){
		List<Double> list = new ArrayList<Double>();
		Path path = new Path(url);
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(conf, Reader.file(path),
					Reader.bufferSize(4096), Reader.start(0));
			DoubleArrStrWritable dkey = (DoubleArrStrWritable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			DDoubleWritable dvalue = (DDoubleWritable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				list.add(dvalue.getSum()*dvalue.getDistance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(reader);
		}
		Double[] dList = new Double[list.size()];
		dList= list.toArray(dList);
		Arrays.sort(dList);
		return dList;
	}
	
	public static XYSeries getXYseries(String url){
		XYSeries xyseries = new XYSeries("");

		Path path = new Path(url);
		Configuration conf = HUtils.getConf();
	    InputStream in =null;  
        try {  
        	FileSystem fs = FileSystem.get(URI.create(url), conf);  
        	in = fs.open(path);  
        	BufferedReader read = new BufferedReader(new InputStreamReader(in));  
            String line=null;  
             
            while((line=read.readLine())!=null){  
//                System.out.println("result:"+line.trim());  
//                [5.5,4.2,1.4,0.2]	5,0.3464101615137755
                String[] lines = line.split("\t");
                String[] sd= lines[1].split(",");
                xyseries.add(Double.parseDouble(sd[0]), Double.parseDouble(sd[1]));
            }  
     
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }finally{  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
	    }  
		
		return xyseries;
	}

}
