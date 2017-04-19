# Install

## Config all machines

Fetch Mesosphere (GNU Privacy Guard) public keys

```
$ sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
```

Add Mesosphere repository to /etc/apt/source.list.d/

```
$ echo "deb http://repos.mesosphere.io/ubuntu trusty main" | sudo tee /etc/apt/sources.list.d/mesosphere.list
```
Fetch new updates

```
$ sudo apt-get update
```
