import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseClient {
    public static void main(String[] args) {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.master", "192.168.200.113");
//        config.set("hbase.zookeeper.quorum", "192.168.200.113");   // 이것도 되는지 확인

        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(config);

            HTable hTable = new HTable(config, "test");

            Put p = new Put(Bytes.toBytes("row1"));
            p.add(Bytes.toBytes("cf"), Bytes.toBytes("a"), Bytes.toBytes("value1"));
            hTable.put(p);

            p = new Put(Bytes.toBytes("row2"));
            p.add(Bytes.toBytes("cf"), Bytes.toBytes("b"), Bytes.toBytes("value2"));
            hTable.put(p);

            p = new Put(Bytes.toBytes("row3"));
            p.add(Bytes.toBytes("cf"), Bytes.toBytes("c"), Bytes.toBytes("value3"));
            hTable.put(p);

            Scan s = new Scan();
            ResultScanner scanner = hTable.getScanner(s);

            try {
                for (Result rowResult = scanner.next(); rowResult != null; rowResult = scanner.next()) {
                    System.out.println("row: " + rowResult);
                }
            } finally {
                scanner.close();
            }

            hTable.close();
            hBaseAdmin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}