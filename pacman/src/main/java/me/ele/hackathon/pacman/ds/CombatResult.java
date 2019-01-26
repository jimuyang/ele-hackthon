package me.ele.hackathon.pacman.ds;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by lanjiangang on 2018/12/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CombatResult {
    String pacman;
    String ghost;
    String day;
    int round;
    int pacman_score;
    int ghost_score;
    int status;

    public String getPacman() {
        return pacman;
    }

    public void setPacman(String pacman) {
        this.pacman = pacman;
    }

    public String getGhost() {
        return ghost;
    }

    public void setGhost(String ghost) {
        this.ghost = ghost;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPacman_score() {
        return pacman_score;
    }

    public void setPacman_score(int pacman_score) {
        this.pacman_score = pacman_score;
    }

    public int getGhost_score() {
        return ghost_score;
    }

    public void setGhost_score(int ghost_score) {
        this.ghost_score = ghost_score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int uuid() {
        int result = Integer.valueOf(pacman);
        result = 31 * result + Integer.valueOf(ghost);
        result = 31 * result + day.hashCode();
        result = 31 * result + round;
        return result;
    }

    @Override
    public String toString() {
        return "CombatResult{" +
                "pacman='" + pacman + '\'' +
                ", ghost='" + ghost + '\'' +
                ", day='" + day + '\'' +
                ", round=" + round +
                ", pacman_score=" + pacman_score +
                ", ghost_score=" + ghost_score +
                ", status=" + status +
                '}';
    }
}
