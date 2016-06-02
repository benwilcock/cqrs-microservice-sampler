docker rm product-cmd-side
docker rm product-qry-side
docker rm discovery
docker ps -a
docker rmi benwilcock/product-command-side:latest
docker rmi benwilcock/product-query-side:latest
docker rmi benwilcock/discovery-service:latest
docker rmi $(docker images -f "dangling=true" -q)
docker images