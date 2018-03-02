package com.crud.tasks.service;

import com.crud.tasks.domain.*;
import com.crud.tasks.trello.client.TrelloClient;
import com.crud.tasks.trello.config.AdminConfig;
import com.crud.tasks.trello.config.TrelloConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.Silent.class)
public class TrelloServiceTestSuite {

    @InjectMocks
    private TrelloService trelloService;

    @Mock
    private TrelloClient trelloClient;

    @Mock
    private AdminConfig adminConfig;

    @Mock
    private SimpleEmailService emailService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void schouldCreatedTrelloCardDto() {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test card", "Description of test card", "top", "335");

        // When
        when(trelloClient.createNewCard(trelloCardDto)).thenReturn(new CreatedTrelloCardDto("1", "Test", "www.test.com/1123"));
        when(adminConfig.getAdminMail()).thenReturn("kodilla_test@gmail.com");
        CreatedTrelloCardDto result = trelloService.createTrelloCard(trelloCardDto);

        // Then
        assertNotNull(result);
        assertEquals("www.test.com/1123", result.getShortUrl());
        assertEquals("Test", result.getName());
        assertEquals("1", result.getId());
        assertEquals("kodilla_test@gmail.com", adminConfig.getAdminMail());
    }

    @Test
    public void schouldFetchTrelloBoards() throws URISyntaxException {
        // Given
        List<TrelloListDto> lists = new ArrayList<>();
        lists.add(new TrelloListDto("1", "Trello list", false));
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("11", "Test board", lists);
        URI uri = new URI("http://test.com/members/member/boards?key=test&token=test&fields=name,id&lists=all");
        when(restTemplate.getForObject(uri, TrelloBoardDto[].class)).thenReturn(trelloBoards);

        List<TrelloBoardDto> trelloBoardDtoList = new ArrayList<>();
        trelloBoardDtoList.add(new TrelloBoardDto("1", "Board", lists));
        when(trelloClient.getTrelloBoards()).thenReturn(trelloBoardDtoList);

        // When
        List<TrelloBoardDto> result = trelloService.fetchTrelloBoards();

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertFalse(result.get(0).getLists().get(0).isClosed());
        assertEquals("Trello list", result.get(0).getLists().get(0).getName());
        assertEquals("1", result.get(0).getLists().get(0).getId());
    }


}
