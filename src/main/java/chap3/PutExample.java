package chap3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by john.shim on 2014. 12. 22..
 */



public class PutExample {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();

        HTable htable = new HTable(conf, "testtable");
        Put put = new Put(Bytes.toBytes("row1"));

        put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"));
        put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("val2"));
        put.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual4"), Bytes.toBytes("val4"));

        htable.put(put);
        System.out.println();
    }
}
