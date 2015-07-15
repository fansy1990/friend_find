/**
 * 
 */
package com.fz.util;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
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

import com.fz.fastcluster.keytype.CustomDoubleWritable;
import com.fz.fastcluster.keytype.DoubleArrStrWritable;
import com.fz.fastcluster.keytype.DoublePairWritable;
import com.fz.fastcluster.keytype.IntDoublePairWritable;
import com.fz.model.IDistanceDensityMul;


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

	public static void drawPic(String url,String file) throws FileNotFoundException, IOException {
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
			DoublePairWritable dvalue = (DoublePairWritable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
				xyseries.add(dvalue.getFirst(), dvalue.getSecond());
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
			DoublePairWritable dvalue = (DoublePairWritable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);

			while (reader.next(dkey, dvalue)) {// 循环读取文件
//				list.add(dvalue.getSum()*dvalue.getDistance());
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
	
	/**
	 * 每个数据文件提取最多500个数据点
	 * 在提取完成之后，获得所有数据的前100个最大的数据点存储在本地文件中
	 * 供在用户看决策图后选择类别后写入中心点
	 * 每个数据点文件需要先进行排序（从大到小，所以在MR任务中增加了一个）
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static XYSeries getXYseries(String url) throws FileNotFoundException, IOException{
		XYSeries xyseries = new XYSeries("");

		List<IDistanceDensityMul> list = getIDistanceDensityMulList(url);
		
		for(IDistanceDensityMul l:list){
			xyseries.add(l.getDensity(), l.getDistance());
		}
		
		return xyseries;
	}
	
	private static List<IDistanceDensityMul> getIDistanceDensityMulList(String url) throws FileNotFoundException, IOException{
		Configuration conf = HUtils.getConf();
		SequenceFile.Reader reader = null;
		// 多个文件整合，需排序
		List<IDistanceDensityMul> allList= new ArrayList<IDistanceDensityMul>();
		// 单个文件
		List<IDistanceDensityMul> fileList= new ArrayList<IDistanceDensityMul>();
		
		FileStatus[] fss=HUtils.getHDFSPath(url,"true").getFileSystem(conf)
				.listStatus(HUtils.getHDFSPath(url, "true"));
		for(FileStatus f:fss){
			if(!f.toString().contains("part")){
				continue; // 排除其他文件
			}
			try {
				reader = new SequenceFile.Reader(conf, Reader.file(f.getPath()),
						Reader.bufferSize(4096), Reader.start(0));
//				 <density_i*min_distancd_j> <first:density_i,second:min_distance_j,third:i>
//				 	DoubleWritable,  IntDoublePairWritable
				CustomDoubleWritable dkey = (CustomDoubleWritable) ReflectionUtils.newInstance(
						reader.getKeyClass(), conf);
				IntDoublePairWritable dvalue = (IntDoublePairWritable) ReflectionUtils.newInstance(
						reader.getValueClass(), conf);
				int i=Utils.GETDRAWPICRECORDS_EVERYFILE;
				while (reader.next(dkey, dvalue)&&i>0) {// 循环读取文件
					i--;
					fileList.add(new IDistanceDensityMul(dvalue.getSecond(),
							dvalue.getFirst(),dvalue.getThird(),dkey.get()));// 每个文件都是从小到大排序的
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeStream(reader);
			}
			
			// 整合当前文件的前面若干条记录（Utils.GETDRAWPICRECORDS_EVERYFILE 	）
			if(allList.size()<=0){// 第一次可以全部添加
				allList.addAll(fileList);
			}else{
				combineLists(allList,fileList);
			}
		}//for
		//第一个点太大了，选择去掉
		return allList.subList(1, allList.size());
	}

	/**
	 * 按照mul的值进行排序，从大到小排序
	 * @param list1
	 * @param list2
	 */
	private static void combineLists(List<IDistanceDensityMul> list1,
			List<IDistanceDensityMul> list2) {
		List<IDistanceDensityMul> allList = new ArrayList<IDistanceDensityMul>();
		int sizeOne=list1.size();
		int sizeTwo = list2.size();
		
		int i,j;
		for(i=0,j=0;i<sizeOne&&j<sizeTwo;){
			if(list1.get(i).greater(list2.get(j))){
				allList.add(list1.get(i++));
			}else{
				allList.add(list2.get(j++));
			}
		}
		if(i<sizeOne){// list1 has not finished
			allList.addAll(list1.subList(i, sizeOne));
		}
		if(j<sizeTwo){// list2 has not finished
			allList.addAll(list2.subList(j, sizeTwo));
		}
		// 重新赋值
		list1.clear();
		list1.addAll(allList);
		allList.clear();
	}

}
