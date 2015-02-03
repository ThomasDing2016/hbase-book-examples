# HBase book examples

* This project contains some examples from 'HBase: The Definitive Guide' by Lars George. So, this project doesn't contain whole examples from hbase-book repository.
* The original examples from hbase-book is based on HBase version 0.92.0.
* But, I need to run examples on Mac OSX Yosemite for HBase 0.98.9. So I made this project and modified examples to run under HBase 0.98.9. It seems there aren't many differences from original examples.

# Setting up HBase 0.98.9 on Mac OSX Yosemite in pseudo distributed mode
* Requirements
    * Zookeeper 3.4.6
    * Hadoop 2.6.0
    * HBase 0.98.9
    * JDK from 1.6 to 1.8 (I used 1.8)

* The versions for Hadoop and HBase should be matched. Make sure check compatibility Metrics before HBase installation

## Zookeeper installation

1. install zookeeper using homebrew

		$ brew install zookeeper

2. edit configuration file

	zoo.cfg

    	# The number of milliseconds of each tick
		tickTime=2000
		# The number of ticks that the initial
		# synchronization phase can take
		initLimit=10
		# The number of ticks that can pass between
		# sending a request and getting an acknowledgement
		syncLimit=5
		# the directory where the snapshot is stored.
        # do not use /tmp for storage, /tmp here is just
        # example sakes.
        #dataDir=/usr/local/var/run/zookeeper/data
        dataDir=/Users/<YOUR_NAME>/zookeeper
        # the port at which the clients will connect
        clientPort=2181
        # the maximum number of client connections.
        # increase this if you need to handle more clients
        #maxClientCnxns=60
        #
        # Be sure to read the maintenance section of the
        # administrator guide before turning on autopurge.
        #
        # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
        #
        # The number of snapshots to retain in dataDir
        #autopurge.snapRetainCount=3
        # Purge task interval in hours
        # Set to "0" to disable auto purge feature
        #autopurge.purgeInterval=1

3. check Zookeeper installation

	start zookeeper server to check Zookeeper installation is success or not.

		$ zkServer start
		$ zkCli
		$ ls /
		[zk: localhost:2181(CONNECTED) 0] ls /
		[zookeeper]


## Hadoop installation

* hadoop configuration follows hadoop installation on Apache hadoop home pages
* http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html


1. install hadoop using homebrew

		$ brew install hadoop

2. passphrase free ssh configuration for ssh local connection

	to connect hadoop to localhost using ssh add publish key to ~/.ssh/authorized_keys.

        $ ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
        $ cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys

3. edit configuration files

	files to configure
		core-site.xml
		hadoop-env.sh
		hdfs-site.xml
		mapred-site.xml
		yarn-site.xml

	core-stie.xml

        <configuration>
             <property>
                  <name>fs.defaultFS</name>
                  <value>hdfs://localhost:9000</value>
             </property>
             <property>
                  <name>hadoop.tmp.dir</name>
                  <value>/Users/<YOUR_NAME>/hadoop/tmp</value>
                  <description>A base for other temporary directories.</description>
             </property>
        </configuration>

	hadoop-env.sh

        export JAVA_HOME="$(/usr/libexec/java_home)"
        export HADOOP_OPTS="-Djava.security.krb5.realm=OX.AC.UK -Djava.security.krb5.kdc=kdc0.ox.ac.uk:kdc1.ox.ac.uk"
        export HADOOP_PREFIX=/usr/local/Cellar/hadoop/2.6.0/libexec

	hdfs-site.xml

        <configuration>
            <property>
                <name>dfs.replication</name>
                <value>1</value>
            </property>
        </configuration>

	mapred-site.xml

        <configuration>
            <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
            </property>
        </configuration>

	yarn-site.xml

        <configuration>
            <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
            </property>
        </configuration>

4. format HDFS file system

		$ hdfs namenode -format

5. check Hadoop installation

	run dfs and yarn to check hadoop installation

        $ start-dfs.sh
        $ start-yarn.sh

    access haddop web interface using below link

		http://localhost:50070/

	access Hadoop job interface

		http://localhost:8088

	create directories to check map reduce of Hadoop

        $ hdfs dfs -mkdir /user
        $ hdfs dfs -mkdir /user/<username>

	copy some files to HDFS's input directory

		$ hdfs dfs -put etc/hadoop input

	run hadoop example executable

        $ hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0.jar grep input output 'dfs[a-z.]+'

	we can see the jobs' progress on http://localhost:8088

	copy files on HDFS to local file system to see results of map reduce

		$ hdfs dfs -get output output

	we can verify results of grep job by typing like below.

        $ cat output/*
        4     dfs.class
        4     dfs.audit.logger
        3     dfs.server.namenode.
        2     dfs.period
        2     dfs.audit.log.maxfilesize
        2     dfs.audit.log.maxbackupindex
        1     dfsmetrics.log
        1     dfsadmin
        1     dfs.servers
        1     dfs.replication
        1     dfs.file


## HBase installation

1. install hbase 0.98.9 using homebrew

	$ brew install hbase


2. configure IP address for HBase

	To run HBase in pseudo distributed mode on Mac OSX, we have to edit /etc/hosts file and use real ip address

		$ sudo vi /etc/hosts
		# 127.0.0.1 localhost <= 이부분을 주석처리하고
		192.168.XXX.XXX localhost <= 이부분 처럼 실제 바인딩 된 IP 주소를 적어준다.

3. edit hbase configuration files

        hbase-env.sh
        hbase-site.xml

	add configurations to hbase-env.sh file

    any java version for your preference

    We manage zookeeper for our own not HBase. Set false HBASE_MANAGES_ZK

    hbase-env.sh
        export JAVA_HOME="$(/usr/libexec/java_home)"
        export HBASE_MANAGES_ZK=false

	hase-site.xml

        <configuration>
            <property>
                    <name>hbase.rootdir</name>
                    <value>hdfs://192.168.XXX.XXX:9000/hbase</value>
            </property>
            <property>
                    <name>hbase.zookeeper.quorum</name>
                    <value>192.168.XXX.XXX</value>
            </property>
            <property>
                    <name>hbase.zookeeper.property.dataDir</name>
                    <value>/Users/<YOUR_NAME>/zookeeper</value>
            </property>
            <property>
                    <name>hbase.cluster.distributed</name>
                    <value>true</value>
            </property>
            <property>
                    <name>hbase.master.info.port</name>
                    <value>60010</value>
            </property>
            <property>
                    <name>hbase.master.info.bindAddress</name>
                    <value>192.168.XXX.XXX</value>
            </property>
            <property>
                    <name>dfs.support.append</name>
                    <value>true</value>
            </property>
            <property>
                    <name>dfs.datanode.max.xcievers</name>
                    <value>4096</value>
            </property>
            <property>
                    <name>hbase.zookeeper.property.clientPort</name>
                    <value>2181</value>
            </property>
            <property>
                    <name>hbase.regionserver.info.bindAddress</name>
                    <value>192.168.XXX.XXX</value>
            </property>
        </configuration>

4. run HBase

	start hbase master and region server.

		$ start-hbase.sh

	check necessary processes are running by invoke jps

        $ jps
        1411 ResourceManager
        5639 ZooKeeperMain
        5231 HRegionServer
        1522 NodeManager
        5107 HMaster
        1012 NameNode
        1591 QuorumPeerMain
        1116 DataNode
        1245 SecondaryNameNode

	check web acess

	    http://localhost:60010/

	run hbase shell

        $ hbase shell
        $ hbase(main):001:0> list
        []

	check hbase directory is created at Zookeeper node

        $ zkCli
        [zk: localhost:2181(CONNECTED) 3] ls /
        [zookeeper, hbase]

	check hbase hbase directory is created at HDFS

        $ hdfs dfs -ls /
        /hbase
        /tmp/
        /user




