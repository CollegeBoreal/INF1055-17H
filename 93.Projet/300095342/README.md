# Mesos DC/OS

https://myusernamesite.wordpress.com/mesos/


## Setup (on canarie)

```
$ source collège-boréal-openrc-quebec.sh
```

Créer 4 instances:  
* Un mesos master (avec zookeeper)  
* 3 slaves

* Master  
```
$ docker-machine \
  create --driver openstack \
  --openstack-flavor-name m1.tiny \
  --openstack-image-name "Ubuntu 14.04" \
  --openstack-ssh-user ubuntu \
  --openstack-floatingip-pool nova \
  --openstack-sec-groups default  \
  --openstack-nova-network \
  --openstack-net-name nova \
  CB-<numero étudiant>
```
