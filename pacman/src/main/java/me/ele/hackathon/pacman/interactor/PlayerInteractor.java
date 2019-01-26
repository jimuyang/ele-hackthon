package me.ele.hackathon.pacman.interactor;

import me.ele.hackathon.pacman.ds.GameInfo;
import me.ele.hackathon.pacman.ds.GameState;
import me.ele.hackathon.pacman.engine.PlayerInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lanjiangang on 2018/12/26.
 */
public  class PlayerInteractor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInteractor.class);

    int maxRetry = 10;
    protected BlockingQueue<PlayerInput> inputQueue;
    protected BlockingQueue<GameState> stateQueue;
    protected GameInfo info;
    protected Player player;
    private boolean connected = false;
    private boolean initSuccessful = false;
    CountDownLatch latch;

    public PlayerInteractor(BlockingQueue<PlayerInput> inputQueue, BlockingQueue<GameState> stateQueue, GameInfo info, Player player) {
        this.inputQueue = inputQueue;
        this.stateQueue = stateQueue;
        this.info = info;
        this.player = player;
    }

    public void init() throws Exception {
        player.initGame(info);
    }

    public void send(GameState state) {
        PlayerInput playerInput = new PlayerInput();
        try {
            playerInput = player.latestState(state);
        } catch (Exception e) {
            logger.warn("Fail to send latest state to player", e);
        }
        inputQueue.offer(playerInput);
    }

    public void play() {

        try {
            stateQueue.take();
        } catch (InterruptedException e) {
        }

        for(int i = 0; i < maxRetry; i++) {
            try {
                player.connect();
                connected = true;
                break;
            } catch (Exception e) {
                logger.warn("Failed to connect {}", player.getRole(), e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        if(!connected) {
            logger.warn("Connect failed!");
            return;
        }

        for(int i = 0; i < maxRetry; i++) {
            try {
                init();
                initSuccessful = true;
                latch.countDown();
                break;
            } catch (Exception e) {
                logger.warn("failed to init game to {}", player.getRole(),  e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }


        if(!initSuccessful) {
            logger.warn("Init failed!");
            return;
        }

        for (; ; ) {
            try {
                GameState state = stateQueue.take();
                if (state == null)
                    break;

                send(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isInitSuccessful() {
        return initSuccessful;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        play();
    }

}
