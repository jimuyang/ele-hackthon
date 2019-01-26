version=`cat .ver`
tag=reg.docker.alibaba-inc.com/jimuyang/dophin-ghost:$version
docker build -t $tag ./
# docker push $tag
