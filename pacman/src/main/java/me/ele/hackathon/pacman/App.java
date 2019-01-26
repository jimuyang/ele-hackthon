package me.ele.hackathon.pacman;

import me.ele.hackathon.pacman.ds.*;
import me.ele.hackathon.pacman.engine.*;
import me.ele.hackathon.pacman.interactor.Ghost;
import me.ele.hackathon.pacman.interactor.HttpClient;
import me.ele.hackathon.pacman.interactor.Pacman;
import me.ele.hackathon.pacman.interactor.PlayerInteractor;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by lanjiangang on 2018/12/26.
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private GameInfo info;
    PlayerInteractor pacmanActor;
    PlayerInteractor ghostActor;
    private BlockingQueue<GameState> pacmanStateQueue;
    private BlockingQueue<PlayerInput> pacmanInputQueue;
    private BlockingQueue<GameState> ghostStateQueue;
    private BlockingQueue<PlayerInput> ghostInputQueue;
    private GameEngine engine;

    CountDownLatch initLatch = new CountDownLatch(2);
    private GameConfig config;
    private Ghost ghost;
    private Pacman pacman;

    private ObjectMapper objectMapper = new ObjectMapper();

    private int round = 0;

    public static void main(String[] args) throws Exception {
        new App().start(args);
    }

    private void start(String[] args) throws Exception {
        loadConfig(args[0]);
        initGameEngine(args[1]);
        initPlayerInteractor(args[2], args[3]);
        mainLoop(args[4], args[5]);
    }

    private void loadConfig(String path) throws IOException {
        logger.info("load configure from " + path);
        config = new GameConfig();
        config.loadFromFile(path);
        logger.info("Config loaded: " + config);

    }

    private void initGameEngine(String path) throws IOException {
        GameMap map = GameMap.load(new FileInputStream(new File(path)));
        info = new GameInfo(config, map);
        logger.info("GameInfo: " + info);

        engine = new GameEngine(map, config);
    }

    private void initPlayerInteractor(String pacmanUrl, String ghostUrl) throws Exception {
        logger.info("Init pacmam: {} and ghost: {}", pacmanUrl, ghostUrl);
        pacmanStateQueue = new LinkedBlockingQueue<>();
        pacmanInputQueue = new LinkedBlockingQueue<>();
        pacman = new Pacman(pacmanUrl);
        pacmanActor = new PlayerInteractor(pacmanInputQueue, pacmanStateQueue, info, pacman);
        pacmanActor.setLatch(initLatch);

        ghostStateQueue = new LinkedBlockingQueue<>();
        ghostInputQueue = new LinkedBlockingQueue<>();
        ghost = new Ghost(ghostUrl);
        ghostActor = new PlayerInteractor(ghostInputQueue, ghostStateQueue, info, ghost);
        ghostActor.setLatch(initLatch);

        new Thread(pacmanActor).start();
        new Thread(ghostActor).start();

    }

    private void mainLoop(String callbackUrl, String tag) {
        CombatResult res = new CombatResult();
        String tags[] = tag.split("_");
        res.setDay(tags[0]);
        res.setRound(Integer.valueOf(tags[1]));
        res.setPacman(tags[2]);
        res.setGhost(tags[3]);
        res.setStatus(2);
        res.setGhost_score(engine.getMaxScore());
        res.setPacman_score(engine.getMaxScore());

        //trigger playerinteractors to init game
        pacmanStateQueue.offer(new GameState());
        ghostStateQueue.offer(new GameState());

        try {
            if (!initLatch.await(30, TimeUnit.SECONDS)) {
                if(!pacmanActor.isInitSuccessful()) {
                    logger.info("Pacman init failed");
                    res.setPacman_score(0);
                }
                if(!ghostActor.isInitSuccessful()) {
                    logger.info("Ghost init failed");
                    res.setGhost_score(0);
                }
                reportGameResult(res, callbackUrl);
                return;
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        while (!engine.gameOver()) {
            round++;
            GameState state = engine.genGamaState();
            pacmanInputQueue.clear();
            ghostInputQueue.clear();

            pacmanStateQueue.offer(state);
            ghostStateQueue.offer(state);

            CountDownLatch latch = new CountDownLatch(2);
            GameInput input = new GameInput();
            getPlayerInput(latch, input, pacmanInputQueue, "pacman");
            getPlayerInput(latch, input, ghostInputQueue, "ghost");
            try {
                latch.await();
            } catch (InterruptedException e) {
            }

            try {
                logger.info("round {}, state: {}, input: {}", round, objectMapper.writeValueAsString(state), objectMapper.writeValueAsString(input));
            } catch (IOException e) {
                e.printStackTrace();
            }

            engine.evaluatePlayerInput(input);
        }

        res.setGhost_score(engine.ghostScore());
        res.setPacman_score(engine.pacManScore());

        reportGameResult(res, callbackUrl);

    }

    public void getPlayerInput(CountDownLatch latch, GameInput input, BlockingQueue<PlayerInput> q, String role) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PlayerInput in = q.poll(config.getTimeoutInMs(), TimeUnit.MILLISECONDS);
                    if(in == null) {
                        logger.warn("Timeout to get input from {}", role);
                    }
                    switch (role) {
                    case "pacman":
                        input.setPacman(in);

                        break;
                    case "ghost":
                        input.setGhosts(in);

                        break;
                    }
                } catch (InterruptedException e) {
                    logger.error(role, e);
                }

                latch.countDown();
            }
        }).start();
    }

    //private static class

    private void reportGameResult(CombatResult res, String callbackUrl)  {
        logger.info("Game ends: " + res);

        try {
            HttpClient client = new HttpClient(callbackUrl);
            client.connect();
            ObjectMapper bm = new ObjectMapper();
            client.send(bm.writeValueAsString(res), "/combatresult");
        } catch (Exception e) {
            logger.error("failed to send report", e);
        }

        System.exit(0);
    }

}
