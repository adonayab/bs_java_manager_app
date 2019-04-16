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
import java.util.ArrayList;

@Controller
@RequestMapping("message")
public class MessageController {

    private static String[] shifts = {"All Shifts", "Morning", "Afternoon", "Evening" };

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        Iterable<Message> messages = messageDao.findAll();
        ArrayList<Message> notCompleted = new ArrayList<>();
        for (Message message: messages) {
            if (!message.isMarkDone()) {
                notCompleted.add(message);
            }
        }
        model.addAttribute("messages", notCompleted);

        int urgentId = 0;
        int generalId = 0;
        int dailyTasksId = 0;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat: cats){
            if (cat.getName().equals("Urgent")) { urgentId = cat.getId(); }
            if (cat.getName().equals("General")) { generalId = cat.getId();}
            if (cat.getName().equals("Daily Tasks")) { dailyTasksId = cat.getId();}
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("dailyTasksId", dailyTasksId);
        return "message/index";
    }

    @RequestMapping(value = "completed")
    public String completed(Model model) {

        Iterable<Message> messages = messageDao.findAll();
        ArrayList<Message> completed = new ArrayList<>();
        for (Message message: messages) {
            if (message.isMarkDone()) {
                completed.add(message);
            }
        }
        model.addAttribute("messages", completed);

        int urgentId = 0;
        int generalId = 0;
        int dailyTasksId = 0;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat: cats){
            if (cat.getName().equals("Urgent")) { urgentId = cat.getId(); }
            if (cat.getName().equals("General")) { generalId = cat.getId();}
            if (cat.getName().equals("Daily Tasks")) { dailyTasksId = cat.getId();}
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("dailyTasksId", dailyTasksId);
        return "message/completed";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMessageForm(Model model, Message message){
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("shifts", shifts);
        return "message/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMessageForm(Model model, @ModelAttribute @Valid Message newMessage,
                                        Errors errors,
                                        @RequestParam int categoryId){

        model.addAttribute("title", "Add Message");

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("shifts", shifts);
            return "message/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newMessage.setCategory(cat);
        newMessage.setDateUpdated(LocalDateTime.now());
        newMessage.setMarkDone(false);
        messageDao.save(newMessage);
        return "redirect:";
    }

    @RequestMapping(value = "remove/{messageId}", method = RequestMethod.POST)
    public String processRemoveMessageForm(@PathVariable int messageId) {

        Message message = messageDao.findOne(messageId);
        message.setMarkDone(true);
        messageDao.save(message);
       return "redirect:/message";
    }

    @RequestMapping(value = "restore/{messageId}", method = RequestMethod.POST)
    public String processRestoreMessageForm(@PathVariable int messageId) {

        Message message = messageDao.findOne(messageId);
        message.setMarkDone(false);
        messageDao.save(message);
        return "redirect:/message/completed";
    }

    @RequestMapping(value = "edit/{messageId}", method = RequestMethod.GET)
    public String displayEditForm(@PathVariable int messageId, Model model) {


        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("message", messageDao.findOne(messageId));
        model.addAttribute("shifts", shifts);


        return "message/edit";
    }

    @RequestMapping(value = "edit/{messageId}", method = RequestMethod.POST)
    public String processEditForm(@PathVariable int messageId,
                                  @Valid Message message, Errors errors, Model model,
                                  @RequestParam int categoryId ) {

        if (errors.hasErrors()) {
            model.addAttribute("message", messageDao.findOne(messageId));
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("shifts", shifts);
            return "message/edit";
        }

        Message workMessage = messageDao.findOne(messageId);
        workMessage.setTitle(message.getTitle());
        workMessage.setBody(message.getBody());
        workMessage.setAuthor(message.getAuthor());
        workMessage.setCategory(categoryDao.findOne(categoryId));
        workMessage.setToShift(message.getToShift());
        workMessage.setDateUpdated(LocalDateTime.now());
        workMessage.setMarkDone(false);

        messageDao.save(workMessage);

        return "redirect:/message";
    }

    @RequestMapping(value = "/index/{id}")
    public String category(Model model, @PathVariable int id) {

        Category category = categoryDao.findOne(id);

        Iterable<Message> messages = category.getMessages();
        ArrayList<Message> notCompleted = new ArrayList<>();
        for (Message message: messages) {
            if (!message.isMarkDone()) {
                notCompleted.add(message);
            }
        }
        model.addAttribute("messages", notCompleted);

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
