package me.ele.hackathon.pacman.engine;

import me.ele.hackathon.pacman.ds.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static me.ele.hackathon.pacman.ds.Coordinate.coordinateEquals;
import static me.ele.hackathon.pacman.ds.Coordinate.move;
import static me.ele.hackathon.pacman.ds.Coordinate.moveAble;

/**
 * Created by lanjiangang on 2018/11/20.
 */
public class GameEngine {

    private Integer pacManScore = 0;

    private Integer ghostScore;

    private Integer maxScore;
    //current round of game
    private Integer round;

    private GameMap map;
    //coordinates of left pac-dots
    private Map<Coordinate, Boolean> pacDots;
    //coordinates of left Power Pellets
    private Map<Coordinate, Boolean> powerPellets;
    //pacman's coordinate
    private Map<Integer, Coordinate> pacman;
    //ghosts' coordinates
    private Map<Integer, Coordinate> ghosts;

    //pacman whether to eat beans or not
    private Map<Integer, Coordinate> eaten = new HashMap<>();

    private Map<String, Coordinate> eatenMap = new HashMap<>();

    private Boolean gameOver = false;
    //pacman's feast
    private Map<Integer, Boolean> pacManFeast = new HashMap<>();

    private Map<String, Boolean> pacManFeastMap = new HashMap<>();
    //pacman's previous move
    private Map<Integer, Coordinate> pacManPreviousMoves = new HashMap<>();
    //ghosts's previous move
    private Map<Integer, Coordinate> ghostsPreviousMoves = new HashMap<>();

    private Map<Integer, Boolean> pacManPerviousFeast = new HashMap<>();

    private Integer feastTime, pacDotPoint, powerPelletPoint, pacmanPoint, ghostPoint;

    private Integer pacManNum = 0;

    private Integer ghostsNum = 0;

    private Map<Integer, Integer> time = new HashMap<>();

    private List<Replay> replays = new ArrayList<>();

    private Map<String, Coordinate> pacManMap = new HashMap<>();

    private Map<String, Coordinate> ghostMap = new HashMap<>();

    private List<Coordinate> pacDotList = new ArrayList<>();

    private List<Coordinate> powerPelletList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private static GameMap initMap = new GameMap(0, 0, null);

    private static Map<Coordinate, Boolean> initPacDots = new HashMap<>();

    private static Map<Coordinate, Boolean> initPowerPellets = new HashMap<>();

    private static Map<Integer, Coordinate> initPacMan = new HashMap<>();

    private static Map<Integer, Coordinate> initGhosts = new HashMap<>();

    private List<Coordinate> overlaps = new ArrayList<>();

    public GameEngine(GameMap map, Map<Coordinate, Boolean> pacDots, Map<Coordinate, Boolean> powerPellets, Map<Integer, Coordinate> pacman,
                      Map<Integer, Coordinate> ghosts, Integer round, Integer feastTime,
                      Integer pacDotPoint, Integer powerPelletPoint, Integer pacmanPoint, Integer ghostPoint) {
        this.map = map;
        this.pacDots = pacDots;
        this.powerPellets = powerPellets;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.round = round;
        this.feastTime = feastTime;
        this.pacDotPoint = pacDotPoint;
        this.powerPelletPoint = powerPelletPoint;
        this.pacmanPoint = pacmanPoint;
        this.ghostPoint = ghostPoint;
    }

    public GameEngine(GameMap map, GameConfig config) {
        this(initMap, initPacDots, initPowerPellets, initPacMan, initGhosts,
                config.round(), config.feastTime(), config.pacDotPoint(), config.powerPelletPoint(), config.pacmanPoint(), config.ghostPoint());
        int[][] arr = new int[map.getPixels().length][map.getPixels()[0].length];
        for (int i = 0; i < map.getPixels().length; i++) {
            for (int j = 0; j < map.getPixels()[i].length; j++) {
                if (map.getPixels()[i][j] == 0) { // empty field
                    arr[i][j] = 0;
                } else if (map.getPixels()[i][j] == 1) { // barrier
                    arr[i][j] = 1;
                } else if (map.getPixels()[i][j] == 2) { // pacDots
                    initPacDots.put(new Coordinate(i, j), true);
                    arr[i][j] = 0;
                } else if (map.getPixels()[i][j] == 3) { // powerPellets
                    initPowerPellets.put(new Coordinate(i, j), true);
                    arr[i][j] = 0;
                } else if (map.getPixels()[i][j] == 4) { // pacman
                    initPacMan.put(pacManNum, new Coordinate(i, j));
                    pacManFeast.put(pacManNum, false);
                    time.put(pacManNum, feastTime + 1);
                    arr[i][j] = 0;
                    pacManNum++;
                } else { // ghosts
                    initGhosts.put(ghostsNum, new Coordinate(i, j));
                    arr[i][j] = 0;
                    ghostsNum++;
                }
            }
        }
        initMap.setHeight(map.getHeight());
        initMap.setWidth(map.getWidth());
        initMap.setPixels(arr);
        maxScore = ghostScore = initPacDots.size() * config.pacDotPoint() + initPowerPellets.size() * config.powerPelletPoint();
        pacman.forEach((k, v) -> pacManMap.put(String.valueOf(k), v));
        ghosts.forEach((k, v) -> ghostMap.put(String.valueOf(k), v));
        pacManFeast.forEach((k, v) -> pacManFeastMap.put(String.valueOf(k), v));
        pacDots.forEach((k, v) -> pacDotList.add(new Coordinate(k.getX(), k.getY())));
        powerPellets.forEach((k, v) -> powerPelletList.add(new Coordinate(k.getX(), k.getY())));
        replays.add(Replay.builder().pacMan(pacManMap).ghosts(ghostMap).pacDots(pacDotList).powerPellets(powerPelletList).map(map.getPixels())
                .gameOver(gameOver).pacManScore(pacManScore).ghostsScore(ghostScore).round(round).pacManEaten(eatenMap).pacmanFeast(pacManFeastMap).build());
    }

    public void evaluatePlayerInput(GameInput input) {
        if (!gameOver) {
            updatePreviousMove();
            if (null != input.getPacman())
                updatePacman(input.getPacman());
            if (null != input.getGhosts())
                updateGhost(input.getGhosts());
            updateScore();
            updateGame();
            replay(input);
        }
    }

    private void updatePreviousMove() {
        pacman.forEach((k, v) -> pacManPreviousMoves.put(k, v));
        ghosts.forEach((k, v) -> ghostsPreviousMoves.put(k, v));
        pacManFeast.forEach((k, v) -> pacManPerviousFeast.put(k, v));
    }

    private void updatePacman(PlayerInput pacmanInput) {
        pacman.forEach((k, v) -> {
            if (null != pacmanInput.getDirs() && null != pacmanInput.getDirs().get(k) && moveAble(pacmanInput.getDirs().get(k), map, v))
                pacman.put(k, move(pacmanInput.getDirs().get(k), v));
        });
    }

    private void updateGhost(PlayerInput ghostInput) {
        ghosts.forEach((k, v) -> {
            if (null != ghostInput.getDirs() && null != ghostInput.getDirs().get(k) && moveAble(ghostInput.getDirs().get(k), map, v))
                ghosts.put(k, move(ghostInput.getDirs().get(k), v));
        });
    }

    private void updateScore() {
        List<Integer> ids = new ArrayList<>();
        eaten = new HashMap<>();
        overlaps = new ArrayList<>();
        if (pacManFeast.containsValue(true)) {
            pacManFeast.forEach((k, v) -> {
                if (v)
                    ids.add(k);
            });
        }
        if (!ids.isEmpty()) {
            pacman.forEach((k, v) -> ids.forEach(integer -> {
                if (k.equals(integer))
                    overlaps.add(new Coordinate(v.getX(), v.getY()));
            }));
        }
        pacman.forEach((k, v) -> {
            Coordinate coordinate = new Coordinate(v.getX(), v.getY());
            _eatPacDots(k, coordinate);
            _eatPowerPellets(k, coordinate);
        });
    }

    private void updateGame() {
        _feast();
        _feastTime();
        _checkGame();
    }

    private void replay(GameInput input) {
        ghostMap = new HashMap<>();
        pacManMap = new HashMap<>();
        pacManFeastMap = new HashMap<>();
        eatenMap = new HashMap<>();
        pacDotList = new ArrayList<>();
        powerPelletList = new ArrayList<>();
        Map<String, Direction> ghostDirs = new HashMap<>();
        Map<String, Direction> pacManDirs = new HashMap<>();
        if (null != input.getGhosts() && input.getGhosts().getDirs() != null)
            input.getGhosts().getDirs().forEach((k, v) -> ghostDirs.put(String.valueOf(k), v));
        if (null != input.getPacman() && input.getPacman().getDirs() != null)
            input.getPacman().getDirs().forEach((k, v) -> pacManDirs.put(String.valueOf(k), v));
        ghosts.forEach((k, v) -> ghostMap.put(String.valueOf(k), v));
        pacman.forEach((k, v) -> pacManMap.put(String.valueOf(k), v));
        eaten.forEach((k, v) -> eatenMap.put(String.valueOf(k), v));
        pacManFeast.forEach((k, v) -> pacManFeastMap.put(String.valueOf(k), v));
        pacDots.forEach((k, v) -> {
            if (v)
                pacDotList.add(new Coordinate(k.getX(), k.getY()));
        });
        powerPellets.forEach((k, v) -> {
            if (v)
                powerPelletList.add(new Coordinate(k.getX(), k.getY()));
        });
        replays.add(
                Replay.builder().pacMan(pacManMap).ghosts(ghostMap).pacDots(pacDotList).powerPellets(powerPelletList)
                        .pacManDirs(pacManDirs).ghostsDirs(ghostDirs).pacManScore(pacManScore).ghostsScore(ghostScore)
                        .gameOver(gameOver).round(round).pacManEaten(eatenMap).pacmanFeast(pacManFeastMap).build());
        if (gameOver) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                logger.info(new String(objectMapper.writeValueAsString(replays)));
            } catch (IOException e) {
                logger.error("failed to log replaylog", e);
            }
        }
    }

    private void _eatPacDots(Integer pacMan, Coordinate coordinate) {
        if (null != pacDots.get(coordinate) && pacDots.get(coordinate)) {
            pacManScore += pacDotPoint;
            ghostScore -= pacDotPoint;
            pacDots.put(coordinate, false);
            eaten.put(pacMan, coordinate);
        }
    }

    private void _eatPowerPellets(Integer pacMan, Coordinate coordinate) {
        if (null != powerPellets.get(coordinate) && powerPellets.get(coordinate)) {
            if (pacManFeast.get(pacMan))
                time.put(pacMan, feastTime + 1);
            else
                pacManFeast.put(pacMan, true);
            powerPellets.put(coordinate, false);
            pacManScore += powerPelletPoint;
            ghostScore -= powerPelletPoint;
            eaten.put(pacMan, coordinate);
        }
    }

    private void _feast() {
        pacman.forEach((pacManIndex, pacManCoordinate) -> ghosts.forEach((ghostIndex, ghostCoordinate) -> {
            if (samePosition(pacManCoordinate, ghostCoordinate)) { // 位置相同
                if (ordinaryState(pacManIndex)) { // 正常状态
                    if (ghostEatPacManAtPacDots(pacManIndex, pacManCoordinate, ghostIndex, ghostCoordinate)) { // ghost eat pacman
                        pacDots.put(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()), true);
                        _ghostEatenScore(pacDotPoint);
                        _minusBeansPacMan(pacManIndex, pacManCoordinate, overlaps, true);
                    } else { // ghost eat pacman
                        ghostScore += pacmanPoint;
                        _minusDotsPacMan(pacManIndex, pacManCoordinate, overlaps);
                    }
                } else if (superState(pacManIndex)) { // 超级状态
                    if (ghostEatPacManAtPowerPellets(pacManIndex, pacManCoordinate)) {
                        if (ghostPreviousNotMove(ghostIndex, ghostCoordinate)) { // ghost eat pacman
                            powerPellets.put(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()), true);
                            _ghostEatenScore(powerPelletPoint);
                            _minusBeansPacMan(pacManIndex, pacManCoordinate, overlaps, false);
                        } else { // pacman eat ghost
                            pacManScore += ghostPoint;
                            _minusGhost(ghostIndex);
                        }
                    } else { // pacman eat ghost
                        pacManScore += ghostPoint;
                        _minusGhost(ghostIndex);
                    }
                }
            } else if (crossOver(pacManIndex, pacManCoordinate, ghostIndex, ghostCoordinate)) { // 交叉而过
                if (ordinaryState(pacManIndex)) {
                    if (ghostEatPacManCrossPacDots(pacManIndex, pacManCoordinate)) { // ghost eat pacman
                        pacDots.put(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()), true);
                        _ghostEatenScore(pacDotPoint);
                        _minusBeansPacMan(pacManIndex, pacManCoordinate, overlaps, true);
                    } else { // ghost eat pacman
                        ghostScore += pacmanPoint;
                        _minusDotsPacMan(pacManIndex, pacManCoordinate, overlaps);
                    }
                } else if (superState(pacManIndex)) {
                    if (ghostEatPacManCrossPowerPellets(pacManIndex, pacManCoordinate)) { // ghost eat pacman
                        powerPellets.put(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()), true);
                        _ghostEatenScore(powerPelletPoint);
                        _minusBeansPacMan(pacManIndex, pacManCoordinate, overlaps, false);
                    } else { // pacman eat ghost
                        pacManScore += ghostPoint;
                        _minusGhost(ghostIndex);
                    }
                }
            }
        }));
    }

    private void _feastTime() {
        pacManFeast.forEach((k, v) -> {
            if (v)
                time.put(k, time.get(k) - 1);
            if (time.get(k) == 0)
                _feastReset(k);
        });
    }

    private void _feastReset(Integer pacMan) {
        pacManFeast.put(pacMan, false);
        time.put(pacMan, feastTime + 1);
    }

    private void _checkGame() {
        for (int i = 0; i < pacManNum; i++) {
            if (null == pacman.get(i))
                pacman.remove(i);
        }
        for (int i = 0; i < ghostsNum; i++) {
            if (null == ghosts.get(i))
                ghosts.remove(i);
        }
        boolean empty = true;
        if (pacDots.containsValue(true))
            empty = false;
        if (powerPellets.containsValue(true))
            empty = false;
        round--;
        if (empty || round == 0 || pacman.isEmpty() || ghosts.isEmpty()) {
            gameOver = true;
        }
    }

    private void _minusDotsPacMan(Integer k, Coordinate coordinate, List<Coordinate> coordinates) {
        if (coordinates.isEmpty() || !coordinates.contains(coordinate)) {
            _minusPacMan(k);
        } else {
            ghostScore -= pacmanPoint;
        }
    }

    private void _minusBeansPacMan(Integer k, Coordinate coordinate, List<Coordinate> coordinates, Boolean isDots) {
        if (coordinates.isEmpty() || !coordinates.contains(coordinate)) {
            _minusPacMan(k);
        } else {
            if (isDots) {
                pacDots.put(new Coordinate(coordinate.getX(), coordinate.getY()), false);
                pacManScore += pacDotPoint;
                ghostScore -= pacDotPoint;
                ghostScore -= pacmanPoint;
            } else {
                powerPellets.put(new Coordinate(coordinate.getX(), coordinate.getY()), false);
                pacManScore += powerPelletPoint;
                ghostScore -= powerPelletPoint;
                ghostScore -= pacmanPoint;
            }
        }
    }

    private void _minusPacMan(Integer k) {
        pacman.put(k, null);
        pacManFeast.remove(k);
        time.remove(k);
        eaten.remove(k);
        pacManPreviousMoves.remove(k);
    }

    private void _minusGhost(Integer k) {
        ghosts.put(k, null);
        ghostsPreviousMoves.remove(k);
    }

    public GameState genGamaState() {
        //generate latest game state
        List<Coordinate> dots = new LinkedList<>();
        pacDots.keySet().stream().filter(p -> pacDots.get(p)).forEach(p -> dots.add(p));

        List<Coordinate> pellets = new LinkedList<>();
        powerPellets.keySet().stream().filter(p -> powerPellets.get(p)).forEach(p -> pellets.add(p));

        return new GameState(dots, pellets, pacman, ghosts, pacManFeast);
    }

    private Boolean samePosition(Coordinate pacManCoordinate, Coordinate ghostCoordinate) {
        return null != ghostCoordinate && coordinateEquals(pacManCoordinate, ghostCoordinate);
    }

    private Boolean crossOver(Integer pacManIndex, Coordinate pacManCoordinate, Integer ghostIndex, Coordinate ghostCoordinate) {
        return null != ghostCoordinate && null != pacManPreviousMoves && !pacManPreviousMoves.isEmpty() && null != ghostsPreviousMoves && !ghostsPreviousMoves.isEmpty()
                && null != pacManPreviousMoves.get(pacManIndex) && coordinateEquals(pacManPreviousMoves.get(pacManIndex), ghostCoordinate)
                && null != ghostsPreviousMoves.get(ghostIndex) && coordinateEquals(pacManCoordinate, ghostsPreviousMoves.get(ghostIndex));
    }

    private Boolean ghostPreviousNotMove(Integer ghostIndex, Coordinate ghostCoordinate) {
        return null != ghostsPreviousMoves.get(ghostIndex) && coordinateEquals(ghostsPreviousMoves.get(ghostIndex), ghostCoordinate);
    }

    private Boolean ghostEatPacManAtPacDots(Integer pacManIndex, Coordinate pacManCoordinate, Integer ghostIndex, Coordinate ghostCoordinate) {
        return null != pacDots.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && !pacDots.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && null != eaten.get(pacManIndex) && coordinateEquals(eaten.get(pacManIndex), pacManCoordinate)
                && null != ghostsPreviousMoves.get(ghostIndex) && coordinateEquals(ghostsPreviousMoves.get(ghostIndex), ghostCoordinate);
    }

    private Boolean ghostEatPacManAtPowerPellets(Integer pacManIndex, Coordinate pacManCoordinate) {
        return null != powerPellets.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && !powerPellets.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && null != eaten.get(pacManIndex) && coordinateEquals(eaten.get(pacManIndex), pacManCoordinate)
                && !pacManPerviousFeast.get(pacManIndex);
    }

    private Boolean ghostEatPacManCrossPacDots(Integer pacManIndex, Coordinate pacManCoordinate) {
        return null != pacDots.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && !pacDots.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && null != eaten.get(pacManIndex) && coordinateEquals(eaten.get(pacManIndex), pacManCoordinate);
    }

    private Boolean ghostEatPacManCrossPowerPellets(Integer pacManIndex, Coordinate pacManCoordinate) {
        return null != powerPellets.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && !powerPellets.get(new Coordinate(pacManCoordinate.getX(), pacManCoordinate.getY()))
                && null != eaten.get(pacManIndex) && coordinateEquals(eaten.get(pacManIndex), pacManCoordinate)
                && !pacManPerviousFeast.get(pacManIndex);
    }

    private Boolean ordinaryState(Integer pacManIndex) {
        return null != pacManFeast.get(pacManIndex) && !pacManFeast.get(pacManIndex);
    }

    private Boolean superState(Integer pacManIndex) {
        return null != pacManFeast.get(pacManIndex) && pacManFeast.get(pacManIndex);
    }

    private void _ghostEatenScore(Integer beanPoint) {
        pacManScore = pacManScore - beanPoint < 0 ? pacManScore : pacManScore - beanPoint;
        ghostScore += beanPoint;
        ghostScore += pacmanPoint;
    }

    public Integer pacManScore() {
        return pacManScore;
    }

    public Integer ghostScore() {
        return ghostScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    Integer round() {
        return round;
    }

    public Boolean gameOver() {
        return gameOver;
    }

    Map<Integer, Boolean> pacManFeast() {
        return pacManFeast;
    }

    Map<Integer, Coordinate> pacman() {
        return pacman;
    }

    Map<Integer, Coordinate> ghosts() {
        return ghosts;
    }
}
