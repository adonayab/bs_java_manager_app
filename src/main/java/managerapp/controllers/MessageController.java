package managerapp.controllers;


import managerapp.models.Category;
import managerapp.models.Message;
import managerapp.models.data.CategoryDao;
import managerapp.models.data.MessageDao;
import managerapp.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("messages", messageDao.findAll());
        model.addAttribute("title", "Manager App");
        int urgentId = 0;
        int generalId = 0;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat: cats){
            if (cat.getName().equals("Urgent")) { urgentId = cat.getId(); }
            if (cat.getName().equals("General")) { generalId = cat.getId();}
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        return "message/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model, Message message){
        model.addAttribute("categories", categoryDao.findAll());
        return "message/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(Model model, @ModelAttribute @Valid Message newMessage, Errors errors, @RequestParam int categoryId){

        model.addAttribute("title", "Add Message");

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryDao.findAll());
            return "message/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newMessage.setCategory(cat);
        newMessage.setDateUpdated(LocalDateTime.now());
        messageDao.save(newMessage);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model){
        model.addAttribute("title", "Remove Message");
        model.addAttribute("cheeses", messageDao.findAll());
        return "message/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] messageIds) {

        for (int messageId: messageIds) {
            messageDao.delete(messageId);
        }
        return "redirect:";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(@PathVariable int cheeseId, Message message, Model model) {

        model.addAttribute("title", "Edit Message");
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("editMessage", messageDao.findOne(cheeseId));

        return "message/edit";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.POST)
    public String processEditForm(@PathVariable int cheeseId,
                                  @Valid Message editMessage, Errors errors, Model model,
                                  @RequestParam int categoryId ) {

        if (errors.hasErrors()) {
            model.addAttribute("editMessage", messageDao.findOne(cheeseId));
            model.addAttribute("categories", categoryDao.findAll());
            return "message/edit";
        }

        Message workMessage = messageDao.findOne(cheeseId);
        workMessage.setTitle(editMessage.getTitle());
        workMessage.setBody(editMessage.getBody());
        workMessage.setAuthor(editMessage.getAuthor());
        workMessage.setCategory(categoryDao.findOne(categoryId));
        workMessage.setDateUpdated(LocalDateTime.now());

        messageDao.save(workMessage);

        return "redirect:/message";
    }

    @RequestMapping(value = "/index/{id}")
    public String category(Model model, @PathVariable int id) {

        Category category = categoryDao.findOne(id);
        model.addAttribute("title", "Messages By Category");
        model.addAttribute("messages", category.getMessages());

        int urgentId = 0;
        int generalId = 0;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat: cats){
            if (cat.getName().equals("Urgent")) { urgentId = cat.getId(); }
            if (cat.getName().equals("General")) { generalId = cat.getId();}
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);

        return "message/index";
    }

}
