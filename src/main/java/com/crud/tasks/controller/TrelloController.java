package com.crud.tasks.controller;

import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trello")
public class TrelloController {

    @Autowired
    private TrelloClient trelloClient;

    @RequestMapping(method = RequestMethod.GET, value = "getTrelloBoards")
    public List<TrelloBoardDto> getTrelloBoards() {

        List<TrelloBoardDto> lista = new ArrayList<>();
        List<TrelloBoardDto> trelloBoards = trelloClient.getTrelloBoards().orElse(lista);

        return trelloBoards.stream()
                .filter(trelloBoardDto -> trelloBoardDto.getId() != null)
                .filter(trelloBoardDto -> trelloBoardDto.getName().toUpperCase().contains("KODILLA"))
                .collect(Collectors.toList());
    }
}