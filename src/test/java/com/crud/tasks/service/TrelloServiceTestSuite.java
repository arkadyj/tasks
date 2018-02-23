package com.crud.tasks.service;

import com.crud.tasks.domain.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrelloServiceTestSuite {

    @Autowired
    private TrelloService trelloService;



    @Test
    public void shouldFetchedTrelloBoards() {
        TrelloListDto trelloListDto = new TrelloListDto("1","Test name", false);
        List<TrelloListDto> trelloListDtos = new ArrayList<>();
        trelloListDtos.add(trelloListDto);
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("11", "Board name", trelloListDtos);
        List<TrelloBoardDto> trelloBoardDtoList = new ArrayList<>();
        trelloBoardDtoList.add(trelloBoardDto);

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoardDtoList);

        List<TrelloBoardDto> fetchedTrelloBoardListDto = trelloService.fetchTrelloBoards();


        Assert.assertEquals(1, fetchedTrelloBoardListDto.size());
    }

    @Test
    public void shouldCreateTrelloCard() {
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test card", "Test description", "top", "531");

        CreatedTrelloCardDto createdTrelloCardDto = trelloService.createTrelloCard(trelloCardDto);

        System.out.println(createdTrelloCardDto.getId());
    }


}
