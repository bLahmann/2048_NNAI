import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;

public class Board {

    private final int NUM_TILES = 4;
    private final double FOUR_FRACTION = 0.1;

    private int [][] tiles = new int[NUM_TILES][NUM_TILES]; // [y][x] 0,0 is top left

    public Board() {
        spawnTile();
        spawnTile();
    }

    public boolean shiftLeft(){

        boolean validMove = false;

        //  Check for copies first
        for (int i = 0; i < tiles.length; i++){

            int lastIndex = 0;
            for (int j = 1; j < tiles.length; j++) {

                if (tiles[i][j] != 0) {
                    if (tiles[i][j] == tiles[i][lastIndex]) {
                        validMove = true;
                        tiles[i][lastIndex] *= 2;
                        tiles[i][j] = 0;
                        if (lastIndex + 1 < tiles[i].length)    lastIndex++;
                    }
                    else {
                        lastIndex = j;
                    }
                }
            }
        }

        // Then move tiles
        for (int i = 0; i < tiles.length; i++){

            int freeIndex = 0;
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != 0) {
                    if (j != freeIndex) {
                        validMove = true;
                        tiles[i][freeIndex] = tiles[i][j];
                        tiles[i][j] = 0;
                    }
                    freeIndex++;
                }
            }

        }

        if (validMove)  spawnTile();
        return validMove;
    }

    public boolean shiftRight(){

        boolean validMove = false;

        //  Check for copies first
        for (int i = 0; i < tiles.length; i++){

            int lastIndex = tiles.length - 1;
            for (int j = tiles.length - 2; j >= 0; j--) {

                if (tiles[i][j] != 0) {
                    if (tiles[i][j] == tiles[i][lastIndex]) {
                        validMove = true;
                        tiles[i][lastIndex] *= 2;
                        tiles[i][j] = 0;
                        if (lastIndex  > 0)    lastIndex--;
                    }
                    else {
                        lastIndex = j;
                    }
                }
            }
        }

        // Then move tiles
        for (int i = 0; i < tiles.length; i++){

            int freeIndex = tiles.length - 1;
            for (int j = tiles.length - 1; j >= 0; j--) {
                if (tiles[i][j] != 0) {
                    if (j != freeIndex) {
                        validMove = true;
                        tiles[i][freeIndex] = tiles[i][j];
                        tiles[i][j] = 0;
                    }
                    freeIndex--;
                }
            }

        }

        if (validMove)  spawnTile();
        return validMove;
    }

    public boolean shiftUp(){

        boolean validMove = false;

        //  Check for copies first
        for (int j = 0; j < tiles.length; j++){

            int lastIndex = 0;
            for (int i = 1; i < tiles.length; i++) {

                if (tiles[i][j] != 0) {
                    if (tiles[i][j] == tiles[lastIndex][j]) {
                        validMove = true;
                        tiles[lastIndex][j] *= 2;
                        tiles[i][j] = 0;
                        if (lastIndex + 1 < tiles.length)    lastIndex++;
                    }
                    else {
                        lastIndex = i;
                    }
                }
            }
        }

        // Then move tiles
        for (int j = 0; j < tiles.length; j++){

            int freeIndex = 0;
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i][j] != 0) {
                    if (i != freeIndex) {
                        validMove = true;
                        tiles[freeIndex][j] = tiles[i][j];
                        tiles[i][j] = 0;
                    }
                    freeIndex++;
                }
            }

        }

        if (validMove)  spawnTile();
        return validMove;
    }

    public boolean shiftDown(){

        boolean validMove = false;

        //  Check for copies first
        for (int j = 0; j < tiles.length; j++){

            int lastIndex = tiles.length - 1;
            for (int i = tiles.length - 2; i >= 0; i--) {

                if (tiles[i][j] != 0) {
                    if (tiles[i][j] == tiles[lastIndex][j]) {
                        validMove = true;
                        tiles[lastIndex][j] *= 2;
                        tiles[i][j] = 0;
                        if (lastIndex  > 0)    lastIndex--;
                    }
                    else {
                        lastIndex = i;
                    }
                }
            }
        }

        // Then move tiles
        for (int j = 0; j < tiles.length; j++){

            int freeIndex = tiles.length - 1;
            for (int i = tiles.length - 1; i >= 0; i--) {
                if (tiles[i][j] != 0) {
                    if (i != freeIndex) {
                        validMove = true;
                        tiles[freeIndex][j] = tiles[i][j];
                        tiles[i][j] = 0;
                    }
                    freeIndex--;
                }
            }
        }

        if (validMove)  spawnTile();
        return validMove;
    }

    private boolean spawnTile() {

        // Get the indexes of free tiles
        List<Integer> freeTiles = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles[i].length; j++){
                if (tiles[i][j] == 0){
                    freeTiles.add(tiles.length*i + j);
                }
            }
        }

        // If we have no free tiles, we can't spawn one
        if (freeTiles.isEmpty()) return false;


        // Randomly pick a tile
        int freeTile = freeTiles.get(new Random().nextInt(freeTiles.size()));
        int i = (int) Math.floor(freeTile / tiles.length);
        int j = freeTile % tiles.length;

        // Spawn the tile
        if (Math.random() < FOUR_FRACTION)  tiles[i][j] = 4;
        else                                tiles[i][j] = 2;

        return true;

    }

    public int[][] getTiles() {
        return tiles;
    }

    public int getScore(){
        int score = 0;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles.length; j++){
                score += tiles[i][j];
            }
        }
        return score;
    }

    public int getMaxTile(){
        int max = 0;
        for (int i = 0; i < tiles.length; i++){
            for (int j = 0; j < tiles.length; j++){
                max = Math.max(max, tiles[i][j]);
            }
        }
        return max;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tiles.length; i++){
            String prefix = "";
            for (int j = 0; j < tiles.length; j++){
                stringBuilder.append(String.format("%s%d", prefix, tiles[i][j]));
                prefix = " ";
            }
            if (i != tiles.length - 1)  stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
