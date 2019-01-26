docker stop pacman ghost engine
# docker rm pacman ghost engine

pacman_image="reg.docker.alibaba-inc.com/pacman/pacman:latest"
engine_image="reg.docker.alibaba-inc.com/jimuyang/pacman-engine:0"
ghost_image="reg.docker.alibaba-inc.com/jimuyang/dophin-ghost:0"

docker run -d --rm -v /tmp:/data/log --name=pacman --entrypoint=/data/start.sh $pacman_image
docker run -d --rm -v /tmp:/data/log --name=ghost --entrypoint=/data/start.sh $ghost_image
docker run -d --rm -v /tmp:/data/log -e Tag=2019_0_pacman_ghost --name=engine --link=pacman:pacman --link=ghost:ghost --entrypoint=/data/start.sh $engine_image