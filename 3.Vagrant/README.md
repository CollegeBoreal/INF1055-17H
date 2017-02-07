# Vagrant

## Devoir

Installer dcos vagrant  
https://github.com/dcos/dcos-vagrant

## Blog
https://manski.net/2016/09/vagrant-multi-machine-tutorial/

## Multi Host

```
Vagrant.configure("2") do |config|
   config.vm.define "master" do |subconfig|
      subconfig.vm.box = "bento/ubuntu-16.04"
   end

   config.vm.define "node1" do |subconfig|
	subconfig.vm.box = "bento/ubuntu-16.04"
   end

   config.vm.define "node2" do |subconfig|
	subconfig.vm.box = "bento/ubuntu-16.04"
   end
end

```
