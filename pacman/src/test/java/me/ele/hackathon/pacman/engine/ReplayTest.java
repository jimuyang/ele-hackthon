package me.ele.hackathon.pacman.engine;

import me.ele.hackathon.pacman.ds.Direction;
import me.ele.hackathon.pacman.ds.GameConfig;
import me.ele.hackathon.pacman.ds.GameMap;
import me.ele.hackathon.pacman.ds.GameState;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplayTest {

    private GameEngine game;
    private GameConfig config = new GameConfig();

    private static final Logger logger = LoggerFactory.getLogger(ReplayTest.class);

    @Before
    public void setup() throws IOException {

        config.loadFromFile(this.getClass().getClassLoader().getResource("txt/test.txt").getPath());
        GameMap map = GameMap.load(new FileInputStream(new File(this.getClass().getClassLoader().getResource("txt/map3.txt").getPath())));
        game = new GameEngine(map, config);
    }

    @Test
    public void replayTest() {
        while (!game.gameOver()) {
            GameInput gameInput = new GameInput();
            Map<Integer, Direction> pacManDirMap = new HashMap<>();
            Map<Integer, Direction> ghostDirMap = new HashMap<>();

            pacManDirMap.put(0, Direction.valueOf(random()));
            pacManDirMap.put(1, Direction.valueOf(random()));
            pacManDirMap.put(2, Direction.valueOf(random()));

            ghostDirMap.put(0, Direction.valueOf(random()));
            ghostDirMap.put(1, Direction.valueOf(random()));
            ghostDirMap.put(2, Direction.valueOf(random()));

            gameInput.setPacman(new PlayerInput(pacManDirMap));
            gameInput.setGhosts(new PlayerInput(ghostDirMap));
            game.evaluatePlayerInput(gameInput);
            GameState state = game.genGamaState();

            assertNotNull("每轮返回状态", state);
            logger.info(state.toString());
        }
    }

    private String random() {
        List<String> dir = Arrays.asList("UP", "DOWN", "LEFT", "RIGHT");
        return dir.get((int) (Math.random() * dir.size()));
    }

    @Test
    public void pacManCrossGhost() {
        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        pacManDirMap.put(0, Direction.UP);
        pacManDirMap.put(1, Direction.LEFT);
        pacManDirMap.put(2, Direction.DOWN);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        pacManDirMap.put(2, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, null);
        pacManDirMap.put(2, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        pacManDirMap.put(2, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());
    }

    @Test
    public void pacManOverlapGhost() {
        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(1, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(1, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());


        pacManDirMap.put(1, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());
    }

}
