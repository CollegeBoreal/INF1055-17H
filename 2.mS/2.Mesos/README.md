# Devoir  

  ## Installer le cluster 
   (i.e. se référer au livre `Mesos In Action`)  
   Chapître 3 -   
      Setting up Mesos  
      Installing from packages
   
 Tâche: 
   * Créer un cluster mesos  
   * Le documenter à travers un blog
  
  
  > * Finir l'installation d'OpenStack - [Devstack](DEVSTACK.md)
  
# Cluster Management

## Créer une deuxieme machine Standby dans la région d'Alberta

* Dans Git Bash, setter l'environnement
```
$ source ~/Developer/canarie.ca/collège-boréal-openrc-alberta.sh
```

- Vérifier le pointage des variables OpenStack vers la région

```
$ env | grep OS
```


 >HOSTNAME=TO-ORD1255  
 >OS_REGION_NAME=alberta  
 >MSYSTEM_CHOST=x86_64-w64-mingw32  
 >OS=Windows_NT  
 >OS_TENANT_ID=`whoever`  
 >FP_NO_HOST_CHECK=NO  
 >OS_PASSWORD=`whatever`  
 >OS_AUTH_URL=https://somewhere.beyondtheh.ill:5000/v2.0  
 >MINGW_CHOST=x86_64-w64-mingw32  
 >OS_USERNAME=`someuser`  
 >OS_TENANT_NAME=`sometenant`  


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

