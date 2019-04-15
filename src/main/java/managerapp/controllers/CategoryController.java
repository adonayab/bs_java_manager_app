package managerapp.controllers;

import managerapp.models.Category;
import managerapp.models.Task;
import managerapp.models.data.CategoryDao;
import managerapp.models.data.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TaskDao taskDao;


    @RequestMapping (value = "")
    public String index(Model model) {
        model.addAttribute("title", "Categories");
        model.addAttribute("categories", categoryDao.findAll());
        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add Category");
        model.addAttribute("category", new Category());
        return "category/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(Model model, @ModelAttribute @Valid Category category, Errors errors){

        model.addAttribute("title", "Add Category");

        if (errors.hasErrors()) {
            return "category/add";
        }

        Category cat = categoryDao.save(category);
        return "redirect:";
    }

    @RequestMapping(value = "daily-task", method = RequestMethod.GET)
    public String displayDailyTask(Model model) {

        int urgentId = 0;
        int generalId = 0;
        Iterable<Category> cats = categoryDao.findAll();
        for (Category cat: cats){
            if (cat.getName().equals("Urgent")) { urgentId = cat.getId(); }
            if (cat.getName().equals("General")) { generalId = cat.getId();}
        }
        model.addAttribute("urgentId", urgentId);
        model.addAttribute("generalId",generalId);

        Iterable<Task> tasks =  taskDao.findAll();
        ArrayList<Task> mornings = new ArrayList<>();
        ArrayList<Task> afternoons = new ArrayList<>();
        ArrayList<Task> evenings = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getShift().equals("Morning")) mornings.add(task);
            if (task.getShift().equals("Afternoon")) afternoons.add(task);
            if (task.getShift().equals("Evening")) evenings.add(task);
        }
        model.addAttribute("mornings", mornings);
        model.addAttribute("afternoons", afternoons);
        model.addAttribute("evenings", evenings);

        return "category/daily-task";
    }

    @RequestMapping(value = "add-task", method = RequestMethod.GET)
    public String addTask(Model model) {

        String[] shifts = {"Morning", "Afternoon", "Evening" };
        model.addAttribute("task", new Task());
        model.addAttribute("shifts", shifts);
        return "category/add-task";
    }

    @RequestMapping(value = "add-task", method = RequestMethod.POST)
    public String processAddTaskForm(Model model, @ModelAttribute @Valid Task task, Errors errors, @RequestParam String shift){

        if (errors.hasErrors()) {
            String[] shifts = {"Morning", "Afternoon", "Evening" };
            return "category/add-task";
        }

        task.setShift(shift);
        taskDao.save(task);

        return "redirect:/category/daily-task";
    }
}
