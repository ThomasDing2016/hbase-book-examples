package chap3;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by john.shim on 2015. 2. 3..
 */
public class PutWriteBufferExample {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HTable table = new HTable(conf, "testtable");
        System.out.println("Auto flush: " + table.isAutoFlush());

        table.setAutoFlush(false);

        Put put1 = new Put(Bytes.toBytes("row1"));
        put1.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
        table.put(put1);

        Put put2 = new Put(Bytes.toBytes("row2"));
        put2.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val2"));
        table.put(put2);

        Put put3 = new Put(Bytes.toBytes("row3"));
        put3.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val3"));
        table.put(put3);

        Get get = new Get(Bytes.toBytes("row1"));
        Result result1 = table.get(get);
        System.out.println("Result 1: " + result1);

        table.flushCommits();

        Result result2 = table.get(get);
        System.out.println("After flush, Result 2: " + result2);
    }
}
