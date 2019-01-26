package me.ele.hackathon.example.pacman;

/**
 * Created by lanjiangang on 2018/12/27.
 */
public class App {
    public static  void main(String args[]) throws InterruptedException {
        WebServer ws =  new WebServer();
        ws.start(Integer.valueOf(args[0]));
        ws.sync();
    }
}
