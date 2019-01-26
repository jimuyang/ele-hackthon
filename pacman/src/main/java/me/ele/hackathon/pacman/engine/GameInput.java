package me.ele.hackathon.pacman.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lanjiangang on 2018/11/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInput {
    private PlayerInput pacman = new PlayerInput();
    private PlayerInput ghosts = new PlayerInput();
}
