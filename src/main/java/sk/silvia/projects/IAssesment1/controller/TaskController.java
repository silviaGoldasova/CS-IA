package sk.silvia.projects.IAssesment1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.IAssesment1.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
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

        return "schedule";
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
        return "redirect:/schedule";
    }

    @PostMapping("/schedule/{id}")
    public String editTask(@ModelAttribute Task task) {

        // fill form with task data
        taskRepository.save(task);

        // show form
        return "redirect:/schedule";         //change to schedule
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
        List<Task> selectedTasksList = new LinkedList<>();
        List<Task> allTasks = taskRepository.findAll();
        for(int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).isSelected() == true)
                selectedTasksList.add(allTasks.get(i));
        }
        return selectedTasksList;
    }

    @GetMapping("/schedule/completed")
    public String createSchedule(Model model) {
        List<CompletedTask> completedTaskList = completedTasksRepository.findAll();
        model.addAttribute("listCompletedTasks", completedTaskList);

        Manager man = new Manager();
        List<String> categs = man.categoriesL;
        model.addAttribute("categs", categs);

        return "completed";
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
                completedTask.setTodayAsDate();
                completedTasksRepository.save(completedTask);
                taskRepository.delete(task1);
                return "redirect:/schedule";
            case "selected":
                Task task2 = taskRepository.getOne(id);
                task2.setSelected(true);
                taskRepository.save(task2);
                return "redirect:/schedule";
            default:
                return "redirect:/schedule";
        }
    }

    @GetMapping("/schedule/selected")
    public String loadSelected(Model model) {
        List<Task> selectedTasksList = new LinkedList<>();
        List<Task> allTasks = taskRepository.findAll();
        for(int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).isSelected() == true)
                selectedTasksList.add(allTasks.get(i));
        }
        model.addAttribute("taskList", selectedTasksList);

        Manager man = new Manager();
        List<String> categs = man.categoriesL;
        model.addAttribute("categs", categs);

        return "selected";
    }

    @PostMapping("/schedule/selected/{taskIdParameter}")
    public String changeSelected(@PathVariable("taskIdParameter") Long id, @RequestParam(value = "action") String functionToPerform) {
        switch(functionToPerform) {
            case "deselect":
                Task task2 = taskRepository.getOne(id);
                task2.setSelected(false);
                taskRepository.save(task2);
                return "redirect:/schedule/selected";
            case "completed":
                Task task1 = taskRepository.getOne(id);
                CompletedTask completedTask = new CompletedTask(task1);
                completedTask.setTodayAsDate();
                completedTasksRepository.save(completedTask);
                taskRepository.delete(task1);
                return "redirect:/schedule/selected";
            default:
                return "redirect:/schedule/selected";
        }
    }

    @GetMapping("/schedule/generate")
    public String getSchedule(Model model) {
        model.addAttribute("scheduleObject", new ScheduleFormDTO());
        return "generateSchedule";
    }

    @PostMapping("/schedule/generate")
    public String generateSchedule(@ModelAttribute ScheduleFormDTO scheduleFormDTO) {
        List<Task> selected = new LinkedList<>();
        List<Task> allTasks = taskRepository.findAll();
        List<Integer> durations = new LinkedList<>();
        int durationsTotal = 0;
        Task task = new Task();
        ScheduleFormDTO schedule = scheduleFormDTO;
        for(int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).isSelected() == true) {
                task = allTasks.get(i);
                selected.add(task);
                durations.add(task.getDuration());
                durationsTotal += task.getDuration();
            }
        }
        if(durationsTotal < schedule.getSessionLength() + (schedule.getSessionLength()/schedule.getBreakFrequency())*schedule.getBreakLength() ){
        }
        else {
            
        }

        return "generateSchedule";
    }

}

