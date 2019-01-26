## 游戏规则

### 对局规则
每天的游戏采取pacman和ghost两两捉对循环厮杀，选手每局比赛的分数累加获得当天的最终得分。<br />
也就是说任一pacman队伍要和所有ghost队伍对战，任一ghost队伍要和所有pacman队伍对战<br />
选择pacman的队伍之间互相排名，选择ghost的队伍之间互相排名。<br />
最终比赛成绩以最后一次对战为准，分别取pacman和ghost的top x进入决赛。
**游戏不禁止使用开源的工程，但必须报备组委会进行批准，并向所有参赛队伍同步，以保持比赛的公平性。**

### 单局游戏规则
单局游戏采取回合制,回合开始前引擎向双方推送游戏最新状态并获取**双方**参赛选手的输入，每回合双方队伍的**每个角色**均可**移动一步**。
游戏引擎执行参赛者的输入并进入下一回合。如果在规定的时间内游行引擎不能从参赛者取得响应，则这一轮视为对应参赛者的角色保持原地不动的状态。<br />
游戏规则基本模仿经典的pacman，具体可参考wiki： https://en.wikipedia.org/wiki/Pac-Man

* **角色**<br/>
角色分为pacman和ghost。
pacman的任务是吃到尽可能多的豆子，并尽可能少被ghost吃掉。<br />
吃到大力丸（PowerPellet）的pacman处于超级状态（Feast Mode），超级状态的pacman可以吃掉ghost。<br />
超级状态维持一定的回合（FeastTime），之后恢复成正常状态。<br />
ghost的任务就是阻止pacman吃豆子，并尽可能的吃掉pacman。ghost可以吃掉正常状态下的pacman。<br />
每一局比赛的pacman和ghost数量都不止一个，出生点位不定，具体详见初始化接口。<br />

* **得分**<br/>
游戏结束后，pacman以为吃掉的豆子和ghost为最终的得分。ghost以最后剩余的豆子和吃掉的pacman为最终的得分。
豆子（Pacdot），大力丸（PowerPellet），pacman，ghost都有自己分值，在游戏初始化是给出。具体参见游戏接口。

* **吃与被吃**<br/>
* pacman与ghost位置相同时:<br/>
1.pacman处于超级状态 pacman吃掉ghost<br/>
2.pacman处于正常状态 pacman被ghost吃掉<br/>
3.pacman处于正常状态 如果ghost在大力丸上未曾移动过 pacman被ghost吃掉<br/>
4.pacman处于正常状态 pacman和ghost同时走向一个大力丸 pacman吃掉大力丸，并且吃掉ghost<br/>
5.多个pacman同时与一个ghost位置相同 有一个pacman处于超级状态 pacman吃掉ghost 其余pacman正常<br/>
6.多个pacman同时与一个ghost位置相同 pacman都处于正常状态 pacman都被ghost吃掉<br/>

* pacman与ghost交叉而过时:<br/>
1.pacman处于超级状态 pacman吃掉ghost<br/>
2.pacman处于正常状态 pacman被ghost吃掉<br/>
3.多个pacman同时与一个ghost交叉而过 有一个panman处于超级状态 pacman吃掉ghost 其余pacman正常<br/>
4.多个pacman同时与一个ghost交叉而过 pacman都处于正常状态 pacman都被ghost吃掉

* 多个pacman吃同一个豆子或大力丸时 以序号小的pacman优先

* **游戏结束条件**<br/>
游戏在满足以下任意一种条件即结束
 * pacman被吃光
 * ghost被吃光
 * 豆子被吃光
 * 规定的回合结束

## 游戏接口

参赛同学需要实现一个HTTP服务，用于与游戏引擎交互。
**HTTP服务需要绑定1080端口。**

### 初始化
游戏开始时引擎会向参赛者推送游戏地图和游戏相关参数<br />
实际使用的配置文件是： src/main/resources/config<br />
实际使用的地图文件是： src/main/resources/map1.txt
* **URL**
`init`
* **Method:**
`POST`
* **Body**

```json
{
    "config":{
        "round":2,
        "feastTime":5,
        "pacDotPoint":1,
        "powerPelletPoint":1,
        "pacmanPoint":5,
        "ghostPoint":5,
        "timeoutInMs":1000
    },
    "map":{
        "height":18,
        "width":26,
        "pixels":[
        [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
        [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,0,2,0,2,2,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1],
        [1,0,2,1,1,1,1,0,1,1,0,0,5,5,0,0,1,2,2,1,0,0,0,0,0,1],
        [1,0,2,2,2,2,1,0,2,1,2,2,2,2,2,2,2,2,2,1,0,0,0,0,0,1],
        [1,0,1,2,3,2,1,0,2,1,2,1,0,1,1,0,1,2,2,1,0,0,0,0,0,1],
        [1,0,1,2,2,2,1,0,3,1,2,1,0,0,1,0,1,2,2,0,0,0,0,0,0,1],
        [1,0,1,1,1,1,1,0,2,1,2,1,0,0,1,0,0,1,1,0,0,0,0,0,0,1],
        [1,0,0,2,2,2,1,0,2,1,2,1,0,0,1,0,0,0,0,1,0,0,0,0,0,1],
        [1,0,0,2,3,2,1,0,2,1,2,1,1,0,1,0,0,0,0,1,0,0,0,0,0,1],
        [1,0,1,2,2,2,1,2,2,1,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,0,1,1,1,0,1,2,1,1,1,2,0,0,0,0,1,0,0,1,0,0,0,0,0,1],
        [1,0,0,2,2,2,2,2,2,2,2,2,0,0,0,0,0,1,1,0,3,0,0,0,0,1],
        [1,0,0,2,2,2,2,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
        [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]]
    }
}
```
 **round**： 本局比赛的回合限制<br/>
 **feastTime**：pacman超级状态可以维持的回合数<br/>
 **pacDotPoint**：一颗豆子的分值<br/>
 **powerPelletPoint**：一颗大力丸的分值<br/>
 **pacmanPoint**：一个pacman的分值<br/>
 **ghostPoint**：一个ghost的分值<br/>
 **timeoutInMs**：每一回合的超时时间,用毫秒表示<br/>
 **height**：地图的高度<br/>
 **width**：地图的宽度<br/>
 **pixels**：地图上每个点的状态，以一个二维数组表示，二维数组的下标表示这个点的坐标，值表示这个坐标上的状态。其中0表示空地，1表示障碍物，2表示豆子，3表示大力丸，4表示pacman，5表示ghost。
   例如 pixels[x][y] == 1即表示在横轴为x，纵轴为y的坐标上上是障碍物。
   * Dir: UP     Y+1  方向: >
   * Dir: RIGHT  X+1  方向: v
   * Dir: DOWN   Y-1  方向: <
   * Dir: LEFT   X-1  方向: ^

### 状态更新
游戏初始化结束后，每回合引擎会向参赛者推送最新的游戏状态，并收集参赛者的输入
* **URL**
`state`
* **Method:**
`POST`
* **Body**

```json
{
    "pacDots":[
        {
            "x":6,
            "y":5
        },
        {
            "x":6,
            "y":3
        },
        {
            "x":6,
            "y":4
        }
    ],
    "powerPellets":[
        {
            "x":11,
            "y":4
        },
        {
            "x":7,
            "y":4
        }
    ],
    "pacman":{
        "0":{
            "x":15,
            "y":12
        }
        "1":{
            "x":15,
            "y":13
        }

    },
    "ghosts":{
        "0":{
            "x":5,
            "y":12
        },
        "1":{
            "x":5,
            "y":13
        }
    },
    "pacmanFeast":{
        "0":false
        "1":false
    }
}
```
 **pacDots**： 剩余的豆子的坐标<br/>
 **powerPellets**：剩余的大力丸的坐标<br/>
 **pacman**：剩余的pacman的坐标<br/>
 **ghosts**：剩余的ghost的坐标<br/>
 **pacmanFeast**：剩余的pacman的状态<br/>

* **Response:**
  * **Code:** 200 <br />
  **Content:**

```json
{
    "dirs":{
        "0":"LEFT",
        "1":"UP"
    }
}
```
**dirc**：每个对象的移动方向，以map方式呈现。key值为对象（pacman或者ghost）的序号，value是移动的方向。如果对象的方向是撞墙，则停在原地不动。**如果想角色原地不动，不给角色的输入即可。**

## 镜像要求
参赛同学需要把自己的程序打包成可执行docker image，并提交至 http://docker.alibaba-inc.com
image有如下要求：
启动命令为 /data/start.sh
**/data/start.sh必须具有可执行权限**
容器规格为4c8g
不用指定entry point, 调度引擎会指定执行/data/start.sh
### 准备工作
首先在http://docker.alibaba-inc.com开通自己的账号，
账号开通后创建一个namespace，并且授权给用户ele-inc。
然后使用开通的账号和密码，在本机的命令行上进行登陆
```
docker login reg.docker.alibaba-inc.com
```

### 如何打包镜像
最简单的打包方式为使用Dockerfile，在pacman-example和ghost-example中已经附带了一个Dockerfile和启动脚本的例子，可以直接使用。
首先进入pacman-example或者ghost-example的目录，执行以下命令：
```
mvn clean install
docker build -t reg.docker.alibaba-inc.com/<namespace>/<imagename:tag> ./
```
imagename可以自定义，tag为每次的提交的版本，约定俗成是个递增的数字。
镜像打包成功后可以提交到集团的镜像仓库：
```
docker push reg.docker.alibaba-inc.com/<namespace>/<imagename:tag>
```
### 如何提交镜像
提交镜像请移步这里： https://hackathon.elenet.me/?team.html
其中Docker地址为：
```
reg.docker.alibaba-inc.com/<namespace>/<imagename:tag>
```
git地址是源码地址，所有入选决赛的队伍要经过组委会的答辩以及code review。
### 如何本地调试镜像
可以通过如下命令运行打包好的镜像：
```
docker stop pacman ghost
docker rm pacman ghost

pacman_image="..."
ghost_image="..."
engine_iamge="..."

docker run -d --rm -v /tmp:/data/log --name=pacman --entrypoint=/data/start.sh $pacman_image
docker run -d --rm -v /tmp:/data/log --name=ghost --entrypoint=/data/start.sh $ghost_image
docker run -d --rm -v /tmp:/data/log -e Tag=2018-12-29_0_123_345 --link=pacman:pacman --link=ghost:ghost --entrypoint=/data/start.sh $engine_iamge
```
## Q&A
