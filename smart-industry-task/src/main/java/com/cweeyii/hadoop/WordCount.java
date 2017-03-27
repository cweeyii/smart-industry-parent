package com.cweeyii.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by wenyi on 16/8/4.
 * Email:caowenyi@meituan.com
 */
public class WordCount {
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable longWritable, Text text, OutputCollector<Text, IntWritable> outputCollector, Reporter reporter) throws IOException {
            String line = text.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreElements()) {
                outputCollector.collect(new Text(tokenizer.nextToken()), new IntWritable(1));
            }
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            output.collect(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException {
        JobConf jobConf = new JobConf(WordCount.class);
        jobConf.setJobName("word_count");
        jobConf.setInputFormat(TextInputFormat.class);
        FileInputFormat.setInputPaths(jobConf, new Path(args[0]));
        jobConf.setOutputFormat(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(jobConf, new Path(args[1]));
        jobConf.setOutputKeyClass(Text.class);
        jobConf.setOutputValueClass(IntWritable.class);
        jobConf.setMapperClass(Map.class);
        jobConf.setReducerClass(Reduce.class);
        JobClient.runJob(jobConf);
    }
}
