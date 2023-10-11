package com.low.level.test.TaskOne.controller;

import com.low.level.test.TaskOne.model.Item;
import com.low.level.test.TaskOne.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final ItemService itemService;

    @Autowired
    public TodoController(ItemService itemService) {
        this.itemService = itemService;
    }
    @GetMapping("/greetUser")
    public String greeting(){
        return "Hello, welcome";
    }

    @GetMapping("/getTodoList")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public void addItem(@RequestBody Item item) {
        itemService.saveItem(item);
    }

    @PutMapping("/{id}")
    public void updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item existingItem = itemService.getItemById(id);
        if (existingItem != null) {
            // Update the existing item's properties with the new item's properties
            existingItem.setTitle(item.getTitle());
            existingItem.setDescription(item.getDescription());
            itemService.saveItem(existingItem);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
