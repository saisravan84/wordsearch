package com.sravan.wordsearchapi.controllers;

import com.sravan.wordsearchapi.services.WordGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wordsearch")
public class WordsearchController {

    @Autowired
    WordGridService wordGridService;

    @GetMapping
    @CrossOrigin("http://127.0.0.1:5500")
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
}
