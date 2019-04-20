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
import java.lang.reflect.Array;
import java.util.ArrayList;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MessageDao messageDao;


    @RequestMapping(value = "")
    public String index(Model model) {
        return "redirect:/message";
    }

    @RequestMapping(value = "/index/{id}")
    public String category(Model model, @PathVariable int id) {

        int urgentId = 0;
        int generalId = 0;
        boolean newUrgent = false;
        boolean newGeneral = false;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat : cats) {
            if (cat.getName().equals("Urgent")) {
                urgentId = cat.getId();
                for (Message message: categoryDao.findOne(urgentId).getMessages()) {
                    if (!message.isMarkDone()) newUrgent = true;
                }
            }
            if (cat.getName().equals("General")) {
                generalId = cat.getId();
                for (Message message: categoryDao.findOne(generalId).getMessages()) {
                    if (!message.isMarkDone()) newGeneral = true;
                }
            }
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("newUrgent", newUrgent);
        model.addAttribute("newGeneral", newGeneral);


        Category category = categoryDao.findOne(id);
        Iterable<Message> messages = category.getMessages();
        ArrayList<Message> notCompleted = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isMarkDone()) {
                notCompleted.add(message);
            }
        }
        model.addAttribute("messages", notCompleted);

        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
//        return "redirect:/message";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, @ModelAttribute @Valid Category category, Errors errors) {

        if (errors.hasErrors()) {
            return "category/add";
        }

        categoryDao.save(category);
        return "redirect:/message";
    }

    @RequestMapping(value = "daily-task", method = RequestMethod.GET)
    public String displayDailyTask(Model model) {

        int urgentId = 0;
        int generalId = 0;
        int dailyTasksId = 0;
        boolean newUrgent = false;
        boolean newGeneral = false;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat : cats) {
            if (cat.getName().equals("Urgent")) {
                urgentId = cat.getId();
                for (Message message: categoryDao.findOne(urgentId).getMessages()) {
                    if (!message.isMarkDone()) newUrgent = true;
                }
            }
            if (cat.getName().equals("General")) {
                generalId = cat.getId();
                for (Message message: categoryDao.findOne(generalId).getMessages()) {
                    if (!message.isMarkDone()) newGeneral = true;
                }
            }
            if (cat.getName().equals("Daily Tasks")) {
                dailyTasksId = cat.getId();
            }
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("newUrgent", newUrgent);
        model.addAttribute("newGeneral", newGeneral);

        Iterable<Message> dailyTasks = categoryDao.findOne(dailyTasksId).getMessages();
        model.addAttribute("dailyTasks", dailyTasks);
        return "category/daily-task";
    }

    @RequestMapping(value = "remove-task", method = RequestMethod.GET)
    public String deleteTask(Model model) {

        int urgentId = 0;
        int generalId = 0;
        int dailyTasksId = 0;
        boolean newUrgent = false;
        boolean newGeneral = false;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat : cats) {
            if (cat.getName().equals("Urgent")) {
                urgentId = cat.getId();
                for (Message message: categoryDao.findOne(urgentId).getMessages()) {
                    if (!message.isMarkDone()) newUrgent = true;
                }
            }
            if (cat.getName().equals("General")) {
                generalId = cat.getId();
                for (Message message: categoryDao.findOne(generalId).getMessages()) {
                    if (!message.isMarkDone()) newGeneral = true;
                }
            }
            if (cat.getName().equals("Daily Tasks")) {
                dailyTasksId = cat.getId();
            }
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId", generalId);
        model.addAttribute("newUrgent", newUrgent);
        model.addAttribute("newGeneral", newGeneral);

        Iterable<Message> dailyTasks = categoryDao.findOne(dailyTasksId).getMessages();
        model.addAttribute("dailyTasks", dailyTasks);
        return "category/remove-task";
    }

    @RequestMapping(value = "remove-task/{taskId}", method = RequestMethod.POST)
    public String processDeleteTask(@PathVariable int taskId) {

        messageDao.delete(taskId);

        return "redirect:/category/remove-task";
    }
}
