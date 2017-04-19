# Installation de Mesos DC/OS

## Config sur toutes les machines

Récupérer les clés publiques Mesosphere (GNU Privacy Guard) 

```
$ sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
```

Ajouter le répertoire Mesosphere à /etc/apt/source.list.d/

```
$ echo "deb http://repos.mesosphere.io/ubuntu trusty main" | sudo tee /etc/apt/sources.list.d/mesosphere.list
```

Récupérer les mises-à-jour

```
$ sudo apt-get update
```

## Sur master:

Installer DC/OS et Zookeeper

```
$ sudo apt-get install mesos=0.22.2-0.2.62.ubuntu1404 zookeeperd
```
