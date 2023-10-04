package com.low.level.test.TaskOne.controller;

import com.low.level.test.TaskOne.controller.TodoController;
import com.low.level.test.TaskOne.model.Item;
import com.low.level.test.TaskOne.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private TodoController todoController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void testCreateItem() throws Exception {
        // Create an Item object to be sent in the request
        Item newItem = new Item();
        newItem.setTitle("New Item");
        newItem.setDescription("Description for the new item");

        // Perform a POST request to create the item
        mockMvc.perform(MockMvcRequestBuilders.post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Item\",\"description\":\"Description for the new item\"}"))
                .andExpect(status().isOk());  // Expect HTTP status 201 (Created)

        // Verify that itemService.saveItem was called once with the correct item
        verify(itemService, times(1)).saveItem(any(Item.class));
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void testGetAllItems() throws Exception {
        // Create a list of items to be returned by the service
        List<Item> itemList = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setTitle("Item 1");
        item1.setDescription("Description for Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setTitle("Item 2");
        item2.setDescription("Description for Item 2");

        itemList.add(item1);
        itemList.add(item2);

        // Mock the behavior of itemService.getAllItems to return the list of items
        when(itemService.getAllItems()).thenReturn(itemList);

        // Perform a GET request to retrieve all items
        mockMvc.perform(MockMvcRequestBuilders.get("/todo/getTodoList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Item 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Description for Item 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Item 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Description for Item 2"));

        // Verify that itemService.getAllItems was called once
        verify(itemService, times(1)).getAllItems();
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void testUpdateItem() throws Exception {
        // Create an updated Item object
        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setTitle("Sleep");
        updatedItem.setDescription("Go to beed before 23:00");

        // Mock the behavior of itemService.getItemById to return an existing item
        when(itemService.getItemById(1L)).thenReturn(updatedItem);

        // Perform a PUT request to update the item
        mockMvc.perform(MockMvcRequestBuilders.put("/todo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Item\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk());  // Expect HTTP status 200 (OK)

        // Verify that itemService.getItemById was called to retrieve the existing item
        verify(itemService, times(1)).getItemById(1L);

        // Verify that itemService.saveItem was called once with the updated item
        verify(itemService, times(1)).saveItem(updatedItem);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    public void testDeleteItem() throws Exception {
        Long itemId = 1L;

        // Mock the behavior of itemService.getItemById to return an existing item
        when(itemService.getItemById(itemId)).thenReturn(new Item());

        // Perform a DELETE request to delete the item
        mockMvc.perform(MockMvcRequestBuilders.delete("/todo/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // Expect HTTP status 200 (OK)


        // Verify that itemService.deleteItem was called once with the item ID
        verify(itemService, times(1)).deleteItem(itemId);
        verifyNoMoreInteractions(itemService);
    }

}
