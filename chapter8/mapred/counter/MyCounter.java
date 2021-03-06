package mapred.counter;
import java.io.IOException;  
  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Counter;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.util.GenericOptionsParser;  
  
public class MyCounter {  
  
    public static class MyCounterMap extends Mapper<LongWritable, Text, Text, Text> {  
          
        public static Counter ct = null;  
  
        protected void map(LongWritable key, Text value,  
                org.apache.hadoop.mapreduce.Mapper<LongWritable, Text, Text, Text>.Context context)  
                throws java.io.IOException, InterruptedException {  
            String arr_value[] = value.toString().split("/t");  
            if (arr_value.length < 4) {  
                ct = context.getCounter("ErrorCounter", "ERROR_LOG_TIME");  
                ct.increment(1);  
            }
        }  
    }  
  
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {  
        Configuration conf = new Configuration();  
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();  
        if (otherArgs.length != 2) {  
            System.err.println("Usage: MyCounter <in> <out>");  
            System.exit(2);  
        }  
  
        Job job = new Job(conf, "MyCounter");  
        job.setJarByClass(MyCounter.class);    
        job.setMapperClass(MyCounterMap.class);  
  
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));  
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));  
        System.exit(job.waitForCompletion(true) ? 0 : 1);  
    }  
}  
