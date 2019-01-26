package me.ele.hackathon.example.ghost;

/**
 * Created by lanjiangang on 2018/12/30.
 */
public class AppTest {

    public static  void main(String arsg[]) throws InterruptedException {
        WebServer ws =  new WebServer();
        ws.start(3001);
        ws.sync();
    }

}