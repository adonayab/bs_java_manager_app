package managerapp.controllers;


import managerapp.models.Category;
import managerapp.models.Message;
import managerapp.models.data.CategoryDao;
import managerapp.models.data.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/")
public class MessageController {

    private static String[] shifts = {"All Shifts", "Morning", "Afternoon", "Evening" };

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(value = "")
    public String index(Model model, Message newMessage) {

        String mark = "Mark Complete";

        int urgentId = 0;
        int generalId = 0;
        boolean newUrgent = false;
        boolean newGeneral = false;
        Iterable<Message> messages = messageDao.findAll();
        ArrayList<Message> notCompleted = new ArrayList<>();
        for (Message message: messages) {
            if (!message.isMarkDone()) {
                if (! message.getCategory().getName().equals("Daily Tasks")) {
                    notCompleted.add(message);
                    if (message.getCategory().getName().equals("Urgent")) {
                        urgentId = message.getCategory().getId();
                        newUrgent = true;
                    }
                    if (message.getCategory().getName().equals("General")) {
                        generalId = message.getCategory().getId();
                        newGeneral = true;
                    }
                }
            }
        }

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("shifts", shifts);

        model.addAttribute("messages", notCompleted);
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("newUrgent", newUrgent);
        model.addAttribute("newGeneral", newGeneral);
        model.addAttribute("mark", mark);
        return "message/index";
    }

    @RequestMapping(value = "completed")
    public String completed(Model model) {

        String unmark = "Mark Not Complete";

        int urgentId = 0;
        int generalId = 0;
        boolean newUrgent = false;
        boolean newGeneral = false;
        Iterable<Message> messages = messageDao.findAll();
        ArrayList<Message> completed = new ArrayList<>();
        for (Message message: messages) {
            if (message.isMarkDone()) {
                completed.add(message);
            } else {
                if (message.getCategory().getName().equals("Urgent")) {
                    urgentId = message.getCategory().getId();
                    newUrgent = true;
                }
                if (message.getCategory().getName().equals("General")) {
                    generalId = message.getCategory().getId();
                    newGeneral = true;
                }
            }
        }
        model.addAttribute("messages", completed);
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("newUrgent", newUrgent);
        model.addAttribute("newGeneral", newGeneral);
        model.addAttribute("unmark", unmark);

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
        if (cat.getName().equals("Daily Task")) {
            newMessage.setMarkDone(true);
        } else {
            newMessage.setMarkDone(false);
        }
        messageDao.save(newMessage);
        return "redirect:";
    }

    @RequestMapping(value = "remove/{messageId}", method = RequestMethod.POST)
    public String processRemoveMessage(@PathVariable int messageId) {

        Message message = messageDao.findOne(messageId);
        message.setMarkDone(true);
        messageDao.save(message);
       return "redirect:/";
    }

    @RequestMapping(value = "restore/{messageId}", method = RequestMethod.POST)
    public String processRestoreMessage(@PathVariable int messageId) {

        Message message = messageDao.findOne(messageId);
        message.setMarkDone(false);
        messageDao.save(message);
        return "redirect:/completed";
    }

    @RequestMapping(value = "delete/{messageId}", method = RequestMethod.POST)
    public String processDeleteMessage(@PathVariable int messageId) {
        messageDao.delete(messageId);
        return "redirect:/completed";
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
        workMessage.setShift(message.getShift());
        workMessage.setDateUpdated(LocalDateTime.now());
        workMessage.setMarkDone(false);

        messageDao.save(workMessage);

        return "redirect:/";
    }

}
