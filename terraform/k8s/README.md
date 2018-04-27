# Introduction

This piece of Terraform creates the absolute minimum required to provision a Kube cluster in AWS with (kops)[https://github.com/kubernetes/kops/blob/master/docs/aws.md]. Your task is to deploy a new kube cluster into the existing VPC as you see fit. Also, we would like the CQRS application to be deployed into this newly provisioned cluster.

# Requirements

* (kops)[https://github.com/kubernetes/kops/releases/tag/1.9.0]
* ssh_key


```bash
ssh-keygen -t rsa -b 2048 -C "joe_bloggs@iw-c.co.uk" -f joe_bloggs_at_iwc

kops create cluster --name=joe-bloggs.iw-c.co.uk --state=s3://joe-bloggs-k8s-infinityworks-com --zones eu-west-1a --dns-zone=Z36ZVL8ROT71UD

kops create secret --name joe-bloggs.iw-c.co.uk sshpublickey admin -i ./key/joe_bloggs_at_iwc.pub

kops update cluster --name joe-bloggs.iw-c.co.uk --state=s3://joe-bloggs-k8s-infinityworks-com --target terraform

```

 --dns private

--

  --name=kubernetes-cluster.example.com \
  --state=s3://kops-state-1234 \
  --zones=eu-west-1a \
  --node-count=2 \
  --node-size=t2.micro \
  --master-size=t2.micro \
  --dns-zone=example.com

kops edit cluster ${NAME}

export AWS_PROFILE=${1} # ie.: joe_bloggs
kops create cluster --name=joe-bloggs.iw-c.co.uk --state=s3://joe-bloggs-k8s-infinityworks-com \
  --cloud aws \
  --admin-access 2.123.107.8/32 \
  --topology private \
  --networking calico \
  --bastion \
  --ssh-public-key ./key/joe_bloggs_at_iwc.pub \
  --subnets subnet-3f658565 \
  --utility-subnets subnet-d01afa8a \
  --zones eu-west-1a \
  --vpc vpc-27831841 \
  --dns-zone=Z36ZVL8ROT71UD
kops update cluster --name joe-bloggs.iw-c.co.uk --state=s3://joe-bloggs-k8s-infinityworks-com --target terraform --out kops.terraform