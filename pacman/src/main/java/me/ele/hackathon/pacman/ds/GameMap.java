package me.ele.hackathon.pacman.ds;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lanjiangang on 2018/11/26.
 */
@Data
public class GameMap {
    private static final Logger logger = LoggerFactory.getLogger(GameMap.class);
    int height;
    int width;
    int[][] pixels;  //1 means barrier, 0 means empty field.

    public GameMap() {

    }

    public GameMap(int height, int width, int[][] pixels) {
        this.height = height;
        this.width = width;
        this.pixels = pixels;
    }

    public static GameMap load(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int[][] pixels = readMap(reader);

        return new GameMap(pixels[0].length, pixels.length, pixels);
    }

    private static int[][] readMap(BufferedReader reader) throws IOException {
        List<int[]> map = new LinkedList<>();
        for (int[] line = readLine(reader); line != null; line = readLine(reader)) {
            map.add(line);
        }

        return map.toArray(new int[map.size()][map.get(0).length]);

    }

    private static int[] readLine(BufferedReader reader) throws IOException {
        int index = 0;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("//")) {
                continue;
            }

            index++;
            logger.debug("parse line {}: {}", index, line);

            String[] row = line.split(" ");
            int[] nums = new int[row.length];
            for (int i = 0; i < row.length; i++) {
                nums[i] = Integer.parseInt(row[i]);
            }
            return nums;
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameMap{" +
                "height=" + height +
                ", width=" + width +
                ", pixels=" + Arrays.toString(pixels) +
                '}';
    }
}
