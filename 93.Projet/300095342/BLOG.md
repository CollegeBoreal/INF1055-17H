# Mesos installation blog

## Create machines to install mesos:

1- Mesos-Master (with ZooKeeper)
3- Mesos-Slaves

* Open ports: (on cloud provider)

```
TCP > 2181
TCP > 5050
TCP > 5051
```

* Mesos-ZooKeeper

```
$ docker-machine \
  create --driver openstack \
  --openstack-flavor-name m1.small \
  --openstack-image-name "Ubuntu 14.04" \
  --openstack-ssh-user ubuntu \
  --openstack-floatingip-pool nova \
  --openstack-sec-groups default  \
  --openstack-nova-network \
  --openstack-net-name nova \
  Mesos-Zookeeper
```

* Mesos-Slave1

```
$ docker-machine \
  create --driver openstack \
  --openstack-flavor-name m1.small \
  --openstack-image-name "Ubuntu 14.04" \
  --openstack-ssh-user ubuntu \
  --openstack-floatingip-pool nova \
  --openstack-sec-groups default  \
  --openstack-nova-network \
  --openstack-net-name nova \
  Mesos-Slave1
```

* Mesos-Slave2

```
$ docker-machine \
  create --driver openstack \
  --openstack-flavor-name m1.small \
  --openstack-image-name "Ubuntu 14.04" \
  --openstack-ssh-user ubuntu \
  --openstack-floatingip-pool nova \
  --openstack-sec-groups default  \
  --openstack-nova-network \
  --openstack-net-name nova \
  Mesos-Slave2
```

* Mesos-Slave3

```
$ docker-machine \
  create --driver openstack \
  --openstack-flavor-name m1.small \
  --openstack-image-name "Ubuntu 14.04" \
  --openstack-ssh-user ubuntu \
  --openstack-floatingip-pool nova \
  --openstack-sec-groups default  \
  --openstack-nova-network \
  --openstack-net-name nova \
  Mesos-Slave3
```

## Config all machines

* Fetch Mesosphere (GNU Privacy Guard) public keys

```
$ sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
```

* Add Mesosphere repository to /etc/apt/source.list.d/

```
$ echo "deb http://repos.mesosphere.io/ubuntu trusty main" | sudo tee /etc/apt/sources.list.d/mesosphere.list
```

* Fetch new updates

```
$ sudo apt-get update
```

* On master (Zookeeper)

```
$ sudo apt-get install mesos=0.22.2-0.2.62.ubuntu1404 zookeeperd
```

* On slaves

```
$ sudo apt-get install mesos=0.22.2-0.2.62.ubuntu1404
```

## ZooKeeper config

* Edit `zoo.cfg` to add zookeeper server(s) (`MESOS-ZOOKEEPER`) public ip with ports

```
$ sudo vi /etc/zookeeper/conf/zoo.cfg
# specify all zookeeper servers
# The fist port is used by followers to connect to the leader
# The second one is used for leader election
#server.1=zookeeper1:2888:3888
#server.2=zookeeper2:2888:3888
#server.3=zookeeper3:2888:3888
server.1=10.1.37.3:2888:3888
```


* Edit myid and replace all the text with your zookeeper server id

```
$ sudo vi /etc/zookeeper/conf/myid
1
```

* add ip and hostname to mesos master files

```
$ echo 10.1.37.3 | sudo tee /etc/mesos-master/ip
$ echo 10.1.37.3 | sudo tee /etc/mesos-master/hostname
```

* Start service

```
$ sudo service zookeeper start
```

* Test to see if zookeeper is running

```
$ echo ruok | nc 127.0.0.1 2181
imok
```

* Mesos-Master Config

Export MESOS variables

```
ubuntu@Mesos-ZooKeeper:~$ export MESOS_zk=zk=://10.1.37.3:2181
ubuntu@Mesos-ZooKeeper:~$ export MESOS_quorum=1
ubuntu@Mesos-ZooKeeper:~$ export MESOS_work_dir=/var/lib/mesos
ubuntu@Mesos-ZooKeeper:~$ export MESOS_log_dir=/var/log/mesos
```

* Add mesos server to /etc/mesos/zk

```
$ vi /etc/mesos/zk
zk://10.1.37.3:2181/mesos
```

* Start Mesos-master

```
ubuntu@Mesos-ZooKeeper:~$ sudo service mesos-master start
mesos-master start/running, process 30188
```

* You can now visit the mesos master page by going to `http://208.75.74.203:5050`

## Mesos-Slave Config

* Export MESOS variables on all slaves

```
ubuntu@Mesos-SlaveX:~$ export MESOS_master=zk://10.1.37.3:2181
ubuntu@Mesos-SlaveX:~$ export MESOS_work_dir=/var/lib/mesos
ubuntu@Mesos-SlaveX:~$ export MESOS_log_dir=/var/log/mesos
```

* add ip and hostname to mesos master files

```
$ echo 10.1.37.X | sudo tee /etc/mesos-slave/ip
$ echo 10.1.37.X | sudo tee /etc/mesos-slave/hostname
```

* Add mesos server to /etc/mesos/zk

```
$ vi /etc/mesos/zk
zk://10.1.37.3:2181/mesos
```

* Start Mesos-slaves

```
ubuntu@Mesos-SlaveX:~$ sudo service mesos-slave start
mesos-slave start/running, process 765
```
