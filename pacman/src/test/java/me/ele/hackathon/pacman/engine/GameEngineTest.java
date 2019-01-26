package me.ele.hackathon.pacman.engine;

import me.ele.hackathon.pacman.ds.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameEngineTest {

    private GameEngine game;
    private GameConfig config = new GameConfig();

    @Before
    public void setup() throws IOException {
        config.loadFromFile(Objects.requireNonNull(this.getClass().getClassLoader().getResource("txt/test.txt")).getPath());
        GameMap map = GameMap.load(new FileInputStream(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("txt/map2.txt")).getPath())));
        game = new GameEngine(map, config);
    }

    @Test
    public void ghostNormalMove() {
        GameState state = game.genGamaState();
        Coordinate pos0 = state.getGhosts().get(0);
        Coordinate pos1 = state.getGhosts().get(1);

        Map<Integer, Direction> ghostDirMap = new HashMap<>();
        ghostDirMap.put(0, Direction.UP);
        ghostDirMap.put(1, Direction.UP);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());
        state = game.genGamaState();

        assertEquals(pos0.move(Direction.UP), state.getGhosts().get(0));
        assertEquals(pos1.move(Direction.UP), state.getGhosts().get(1));
        pos0 = state.getGhosts().get(0);
        pos1 = state.getGhosts().get(1);

        ghostDirMap.put(0, Direction.RIGHT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());
        state = game.genGamaState();

        assertEquals(pos0.move(Direction.RIGHT), state.getGhosts().get(0));
        assertEquals(pos1.move(Direction.RIGHT), state.getGhosts().get(1));
    }

    @Test
    public void pacmanEatenScore() { // pacman move, eat score++

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertEquals("pacman被吃 剩余1", 1, game.pacman().size());

        pacManDirMap.remove(0);
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertEquals("pacman得分", 2, game.pacManScore().intValue());
        assertEquals("ghost得分", 11, game.ghostScore().intValue());
    }

    @Test
    public void pacmanEatPowerPellte() {

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        pacManDirMap.put(0, Direction.UP);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());
        pacManDirMap.put(0, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertTrue("pacman吃大力丸 状态变更", game.pacManFeast().get(0));

        for(int i = 0; i < config.getFeastTime(); i++) {
            assertTrue(game.pacManFeast().get(0));
            game.evaluatePlayerInput(GameInput.builder().build());
        }

        assertTrue("时间结束 pacman恢复普通状态", !game.pacManFeast().get(0));
    }

    @Test
    public void pacManEatGhostScore() { // 变更状态后与ghost-0位置重叠 ghost-0被吃 score多加1

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        pacManDirMap.put(1, Direction.DOWN);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertTrue("pacman吃大力丸 状态变更", game.pacManFeast().get(1));

        pacManDirMap.put(1, Direction.DOWN);
        ghostDirMap.put(1, Direction.UP);
        game.evaluatePlayerInput(
                GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        assertEquals("pacman吃ghost 2个豆子加ghost", 7, game.pacManScore().intValue());
        assertEquals("ghost被吃", 6, game.ghostScore().intValue());
    }

    @Test
    public void updateGameRound() { // round为0 gameOver

        for(int i = 0; i < config.getRound(); i++) {
            game.evaluatePlayerInput(GameInput.builder().build());
        }

        assertEquals("经过10轮 round为0", 0, game.round().intValue());
        assertTrue("round为0 游戏结束", game.gameOver());
    }

    @Test
    public void pacManEatenUpdateGame() { // pacDots & powerPellets empty, gameOver

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        pacManDirMap.put(0, Direction.UP);
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        pacManDirMap.put(0, Direction.DOWN);
        pacManDirMap.put(1, Direction.DOWN);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertTrue("豆子全被吃光 游戏结束", game.gameOver());
    }

    @Test
    public void ghostsEatPacMan() { // two ghosts eat one pacman

        Map<Integer, Direction> ghostDirMap = new HashMap<>();
        ghostDirMap.put(0, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());
        ghostDirMap.put(0, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        assertEquals("2个ghost同一位置", game.ghosts().get(0), game.ghosts().get(1));

        ghostDirMap.put(0, Direction.RIGHT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        assertEquals("pacman的数量减1", 1, game.pacman().size());
    }

    @Test
    public void pacManOverlapGhostEat() {

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        pacManDirMap.put(0, Direction.UP);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        ghostDirMap.put(1, Direction.UP);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        assertEquals("pacman得分7", 7, game.pacManScore().intValue());
        assertEquals("ghost得分6", 6, game.ghostScore().intValue());
        assertEquals("ghost被吃", 1, game.ghosts().size());
    }

    @Test
    public void pacManOverlapGhostWasEat() {

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        pacManDirMap.put(0, Direction.UP);
        ghostDirMap.put(1, Direction.UP);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(0, Direction.LEFT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertEquals("pacman得分1", 1, game.pacManScore().intValue());
        assertEquals("ghost得分12", 12, game.ghostScore().intValue());
        assertEquals("pacman被吃", 1, game.pacman().size());
    }

    @Test
    public void ghostEatPacMan() {

        Map<Integer, Direction> pacManDirMap = new HashMap<>();
        Map<Integer, Direction> ghostDirMap = new HashMap<>();

        pacManDirMap.put(0, Direction.LEFT);
        pacManDirMap.put(1, Direction.LEFT);
        ghostDirMap.put(1, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        assertEquals("pacman被吃", 1, game.pacman().size());
        assertEquals("ghost加分", 12, game.ghostScore().intValue());

        pacManDirMap.put(0, null);
        pacManDirMap.put(1, Direction.DOWN);
        ghostDirMap.put(1, null);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(1, Direction.LEFT);
        ghostDirMap.put(0, Direction.RIGHT);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).ghosts(PlayerInput.builder().dirs(ghostDirMap).build()).build());

        pacManDirMap.put(1, Direction.DOWN);
        game.evaluatePlayerInput(GameInput.builder().pacman(PlayerInput.builder().dirs(pacManDirMap).build()).build());

        assertEquals(9, game.pacManScore().intValue());
    }
}
