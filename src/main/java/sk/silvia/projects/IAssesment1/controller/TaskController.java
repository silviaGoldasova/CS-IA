package sk.silvia.projects.IAssesment1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.IAssesment1.model.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompletedTasksRepository completedTasksRepository;

    @GetMapping("/schedule/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        // return new empty task form
        return "new_task";
    }

    @PostMapping("/schedule/new")
    public String createTask(@ModelAttribute TaskFormDataDTO taskFormDataDTO) {
        Task task = new Task();
        task.setDuration(taskFormDataDTO.getDuration());
        if (taskFormDataDTO.getTaskCategory() == null) {
            task.setTaskCategory("Other");
        } else {
            task.setTaskCategory(taskFormDataDTO.getTaskCategory());
        }
        task.setName(taskFormDataDTO.getName());
        taskRepository.save(task);
        return "/schedule";        // return homepage with task lists        // change to return "schedule"
    }

    @GetMapping("/schedule")
    public String loadTasks(Model model) {
        // load all tasks from database
        List<Task> taskList = taskRepository.findAll();
        // return homepage with tasks listed
        model.addAttribute("taskList", taskList);

        Manager man = new Manager();
        List<String> categs = man.categoriesL;
        model.addAttribute("categs", categs);

        return "/schedule";
    }

    // napr: http://localhost:8080/schedule/25
    @GetMapping("/schedule/{id}")
    public String editTask(@PathVariable("id") Long id, Model model) {

        // load task from database - find task by id
        Task task = taskRepository.getOne(id);

        // fill form with task data
        model.addAttribute("task", task);

        // show form
        return "edit_task";
    }


    // napr: http://localhost:8080/schedule/25
    @GetMapping("/schedule/seed-data")
    public String seedData() {

        for (int i = 0; i<16; i++) {
            String taskcategory;
            Task t = new Task();
            t.setName("Learn");
            t.setDuration(40);
            switch(i%4) {
                case 0: taskcategory = "School";
                    break;
                case 1: taskcategory = "Work";
                    break;
                case 2: taskcategory = "Housework";
                    break;
                default: taskcategory = "Other";
                    break;
            }
            t.setTaskCategory(taskcategory);
            taskRepository.save(t);
        }

        // show form
        return "schedule";
    }

    @PostMapping("/schedule/{id}")
    public String editTask(@ModelAttribute Task task) {

        // fill form with task data
        taskRepository.save(task);

        // show form
        return "schedule";         //change to schedule
    }

    @GetMapping("/schedule/login")
    public String viewHome() {
        return "login";
    }

    @PostMapping("/schedule/login")
    public String login(@ModelAttribute User user) {
        userRepository.save(user);
        return "login";
    }

    @GetMapping("/schedule/home")
    public String viewHome1() {
        return "home";
    }

    @GetMapping("/schedule/home1")
    public String viewHome2() {
        return "home1";
    }

    @GetMapping("/blank")
    public String viewBlank() {
        return "blank";
    }

    //get all data, one line
    @GetMapping("schedule/list")
    @ResponseBody
    public List<Task> getTaskList() {
        return taskRepository.findAll();
    }

    @PostMapping(value = "/schedule/{taskIdParameter}/editing")
    public String edittingTask(@PathVariable("taskIdParameter") Long id, @RequestParam(value = "action") String functionToPerform) {          //@PathVariable String taskIdParameter
        switch(functionToPerform) {
            case "edit":
                return "redirect:/schedule/{taskIdParameter}";
            case "delete":
                Task task = taskRepository.getOne(id);
                taskRepository.delete(task);
                return "redirect:/schedule";
            case "completed":
                Task task1 = taskRepository.getOne(id);
                CompletedTask completedTask = new CompletedTask(task1);
                completedTasksRepository.save(completedTask);
                taskRepository.delete(task1);
                return "redirect:/schedule";
            default:
                return "redirect:/schedule";
        }
    }

}

