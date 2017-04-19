
## Setup (on canarie)

[Canarie QC Console](https://nova-qc.dair-atir.canarie.ca)

```
$ source collège-boréal-openrc-quebec.sh
```

Créer 4 instances:  
* Un mesos master (avec zookeeper)  
* 3 slaves

* Mesos-Zookeeper


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
