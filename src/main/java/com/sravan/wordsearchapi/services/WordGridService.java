package com.sravan.wordsearchapi.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {

    private enum Direction {
        HORIZANTAL,
        VERTICAL,
        DIAGONAL,
        HORIZANTAL_INVERSE,
        VERTICAL_INVERSE,
        DIAGONAL_INVERSE
    }

    private class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    public char[][] generateGrid(int gridSize, List<String> words) {
        char[][] contents = new char[gridSize][gridSize];
        List<Coordinate> coordinates = new ArrayList<>();
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                coordinates.add(new Coordinate(i, j));
                contents[i][j] = '_';
            }
        }

        for(String word : words) {
            Collections.shuffle(coordinates);
            for(Coordinate coordinate: coordinates){
                int x = coordinate.x;
                int y = coordinate.y;
                Direction selectedDirection = getDirectionForFit(contents, word, coordinate);
//                System.out.println(word + " => " + x + "," + y);
                if (selectedDirection != null) {
                    switch (selectedDirection) {
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y] = c;
                            }
                            break;
                        case HORIZANTAL:
                            for (char c : word.toCharArray()) {
                                contents[x][y++] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y++] = c;
                            }
                            break;
                        case VERTICAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y] = c;
                            }
                            break;
                        case HORIZANTAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x][y--] = c;
                            }
                            break;
                        case DIAGONAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y--] = c;
                            }
                            break;
                    }
                    break;
                }
            }
        }
        randomFillGrid(contents);
        return contents;
    }

    private Direction getDirectionForFit(char[][] contents, String word, Coordinate coordinate) {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        for(Direction direction: directions){
            if(doesFit(contents, word, coordinate, direction)){
                return direction;
            }
        }
        return null;
    }

    private boolean doesFit(char[][] contents, String word, Coordinate coordinate, Direction direction) {
        int gridSize = contents[0].length;
        int wordLength = word.length();
        switch (direction) {
            case VERTICAL:
                if(coordinate.x + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x + i][coordinate.y];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case HORIZANTAL:
                if(coordinate.y + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x][coordinate.y + i];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL:
                if(coordinate.y + wordLength > gridSize || coordinate.x + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x + i][coordinate.y + i];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case VERTICAL_INVERSE:
                if(coordinate.x - wordLength < 0) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x - i][coordinate.y];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case HORIZANTAL_INVERSE:
                if(coordinate.y - wordLength < 0) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x][coordinate.y - i];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL_INVERSE:
                if(coordinate.y - wordLength < 0 || coordinate.x - wordLength < 0) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x - i][coordinate.y - i];
                    if(letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
        }
        return true;
    }

    public void displayGrid(char[][] contents) {
        int gridSize = contents[0].length;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                System.out.print(contents[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void randomFillGrid(char[][] contents) {
        String alphabets = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
        int gridSize = contents[0].length;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if(contents[i][j] == '_') {
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, alphabets.length());
                    contents[i][j] = alphabets.charAt(randomIndex);
                }
            }
        }
    }
}
