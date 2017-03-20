# Devoir  

  * Finir l'installation d'OpenStack - [Devstack](DEVSTACK.md)
  
# Cluster Management

## Créer une deuxieme machine Standby dans la région d'Alberta

* Dans Git Bash, setter l'environnement
```
$ source ~/Developer/canarie.ca/collège-boréal-openrc-alberta.sh
```

* Creér la deuxiéme VM  
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
  CB-<ton ID>-standby
```
## Installer le cluster 
   (i.e. se référer au livre `Mesos In Action`)
