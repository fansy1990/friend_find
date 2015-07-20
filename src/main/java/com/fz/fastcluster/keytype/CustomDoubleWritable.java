/**
 * 
 */
package com.fz.fastcluster.keytype;

/**
 * 自定义DoubleWritable
 * 修改其排序方式，
 * 从大到小排列
 * @author fansy
 * @date 2015-7-3
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
* Writable for Double values.
*/
@InterfaceAudience.Public
@InterfaceStability.Stable
public class CustomDoubleWritable implements WritableComparable<CustomDoubleWritable> {

 private double value = 0.0;
 
 public CustomDoubleWritable() {
   
 }
 
 public CustomDoubleWritable(double value) {
   set(value);
 }
 
 @Override
 public void readFields(DataInput in) throws IOException {
   value = in.readDouble();
 }

 @Override
 public void write(DataOutput out) throws IOException {
   out.writeDouble(value);
 }
 
 public void set(double value) { this.value = value; }
 
 public double get() { return value; }

 /**
  * Returns true iff <code>o</code> is a DoubleWritable with the same value.
  */
 @Override
 public boolean equals(Object o) {
   if (!(o instanceof CustomDoubleWritable)) {
     return false;
   }
   CustomDoubleWritable other = (CustomDoubleWritable)o;
   return this.value == other.value;
 }
 
 @Override
 public int hashCode() {
   return (int)Double.doubleToLongBits(value);
 }
 
 @Override
 public int compareTo(CustomDoubleWritable o) {// 修改这里即可
   return (value < o.value ? 1 : (value == o.value ? 0 : -1));
 }
 
 @Override
 public String toString() {
   return Double.toString(value);
 }

 /** A Comparator optimized for DoubleWritable. */ 
 public static class Comparator extends WritableComparator {
   public Comparator() {
     super(CustomDoubleWritable.class);
   }

   @Override
   public int compare(byte[] b1, int s1, int l1,
                      byte[] b2, int s2, int l2) {
     double thisValue = readDouble(b1, s1);
     double thatValue = readDouble(b2, s2);
     return (thisValue < thatValue ? 1 : (thisValue == thatValue ? 0 : -1));
   }
 }

 static {                                        // register this comparator
   WritableComparator.define(CustomDoubleWritable.class, new Comparator());
 }

}


