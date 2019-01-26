package me.ele.hackathon.pacman.interactor;

import me.ele.hackathon.pacman.ds.GameInfo;
import me.ele.hackathon.pacman.ds.GameState;
import me.ele.hackathon.pacman.engine.PlayerInput;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by lanjiangang on 2018/12/26.
 */
public class Player {
    private HttpClient client;
    private String role;

    public Player(String url, String role) {
        client = new HttpClient(url);
        this.role = role;
    }

    public void connect() throws Exception {
        client.connect();
    }

    public void initGame(GameInfo info) throws Exception {
        send(info, "init");
    }

    protected byte[] send(Object msg, String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return client.send(mapper.writeValueAsString(msg), path);
    }

    public PlayerInput latestState(GameState state) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        byte[] buf = send(state, "state");

        return mapper.readValue(buf, PlayerInput.class);
    }

    public String getRole() {
        return role;
    }
}
