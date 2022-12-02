- Get an api key from here

https://app.localstack.cloud/dashboard

- docker compose up this
``` java
version: "3.8"

services:
localstack:
container_name: "${LOCALSTACK_DOCKER_NAME-localstack_main}"
image: localstack/localstack:latest
ports:
- "127.0.0.1:4566:4566"            # LocalStack Gateway
- "127.0.0.1:4510-4559:4510-4559"  # external services port range
- "127.0.0.1:53:53"                # DNS config (only required for Pro)
- "127.0.0.1:53:53/udp"            # DNS config (only required for Pro)
- "127.0.0.1:443:443"              # LocalStack HTTPS Gateway (only required for Pro)
environment:
- DEBUG=1
- PERSISTENCE=1
- LOCALSTACK_API_KEY=REPLACE_ME
- LAMBDA_EXECUTOR=${LAMBDA_EXECUTOR-}
- DOCKER_HOST=unix:///var/run/docker.sock
volumes:
- "${LOCALSTACK_VOLUME_DIR:-./volume}:/var/lib/localstack"
- "/var/run/docker.sock:/var/run/docker.sock"
- "~/Development/aws_kube_present:/app_folder"

networks:
localstack:
driver: bridge 
```

get a terminal inside the container from docker compose.
this is a mock AWS env :)

run this to install kubectl and docker inside the container

```bash
#!/bin/bash

curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.7.0/bin/linux/amd64/kubectl && chmod +x ./kubectl && mv ./kubectl /usr/local/bin/kubectl && curl -sSL https://get.docker.com/ | sh && apt-get install vim -y

```

- spin up a cluster
```bash
awslocal eks create-cluster --name cluster1 --role-arn r1 --resources-vpc-config '{}'
```

- check when it's ready
```bash
awslocal eks describe-cluster --name cluster1
```

- create an aws ecr to save ur app image
```bash
awslocal ecr create-repository --repository-name "cloud-app-repo"
```

- verify
```bash
awslocal ecr describe-repositories
```

- tag and push image to ecr
```bash
maybe there is another port than 4511

# on host
docker build -t cloudapp .

# inside container
docker tag cloudapp localhost:4511/cloud-app-repo

docker push localhost:4511/cloud-app-repo
```

- verify
```bash 
awslocal ecr list-images --repository-name cloud-app-repo
```
- create service and deployment
```shell
# put these two files from the project inside the container

kubectl create -f service.yml --validate=false
kubectl create -f deployment.yml
```

- verify
```shell
kubectl describe service rest-example # (172.23.0.2:30348)
```

- cluster ip
```shell
kubectl get -n kube-system service/traefik -o jsonpath="{.status.loadBalancer.ingress[0].ip}"
```
