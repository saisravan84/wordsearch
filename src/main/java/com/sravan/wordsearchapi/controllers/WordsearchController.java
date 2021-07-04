package com.sravan.wordsearchapi.controllers;

import com.sravan.wordsearchapi.services.WordGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wordsearch")
public class WordsearchController {

    @Autowired
    WordGridService wordGridService;

    @GetMapping
    public String createWordGrid(@RequestParam int gridSize, @RequestParam List<String> words) {
        char[][] grid = wordGridService.generateGrid(gridSize, words);
        String gridToString = "";
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridToString += grid[i][j] + " ";
            }
            gridToString += "\r\n";
        }
        return gridToString;
    }

//    @GetMapping
//    public String createWordGrid(@RequestParam int gridSize, @RequestParam List<String> words) {
//        return "grid " + gridSize + " " + words.toArray().length;
//    }
}
