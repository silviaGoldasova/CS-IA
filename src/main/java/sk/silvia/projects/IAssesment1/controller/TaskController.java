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

        List<String> categs = new Manager().categoriesL;
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
        Task task = new Task("Maths SL", 20, "School", true);
        Task task2 = new Task("Maths HL", 50, "School", true);
        return selectedTasksList;
    }

    @GetMapping("/schedule/completed")
    public String createSchedule(Model model) {
        List<CompletedTask> completedTaskList = completedTasksRepository.findAll();
        model.addAttribute("listCompletedTasks", completedTaskList);

        List<String> categs = new Manager().categoriesL;
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
        List<Task> allTasks = taskRepository.findAll();
        List<Task> selectedTasksList = getSelected(allTasks);
        model.addAttribute("taskList", selectedTasksList);

        List<String> categs = new Manager().categoriesL;
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
        model.addAttribute("scheduleFormDTO", new ScheduleFormDTO());
        return "generateSchedule";
    }

    @PostMapping("/schedule/generate")
    public String generateSchedule(@ModelAttribute ScheduleFormDTO scheduleFormDTO, Model model) {
        List<Task> allTasks = taskRepository.findAll();
        List<Task> selected = getSelected(allTasks);
        List<Task> display = new LinkedList<>();
        List<Integer> durations = new LinkedList<>(); int durationsTotal = 0;

        Task task; int duration = 0;
        ScheduleFormDTO schedule = scheduleFormDTO;
        List<Task> list = getCategoryTasks(schedule.getCategorySelected(), allTasks);

        for(int i = 0; i < list.size(); i++) {
            task = list.get(i);
            durations.add(task.getDuration());
            durationsTotal += task.getDuration();
        }
        if(durationsTotal < schedule.getSessionLength() ){       // if(durationsTotal < schedule.getSessionLength() + (schedule.getSessionLength()/schedule.getBreakFrequency())*schedule.getBreakLength() ){
            display = list;
        }
        else {
            int i = 0;
            Task[] sorted = sortTasks(getCategoryTasks(schedule.getCategorySelected(), allTasks));
            while(duration + sorted[i].getDuration() <= durationsTotal && duration + sorted[sorted.length-i-1].getDuration() <= durationsTotal) {
                display.add(sorted[i]);
                display.add(sorted[sorted.length-i-1]);
                duration = duration + sorted[i].getDuration() + sorted[sorted.length-i-1].getDuration();
                i++;
            }
            if(sorted[sorted.length-i-1].getDuration() <= durationsTotal) {
                display.add(sorted[sorted.length-i-1]);
                duration = duration + sorted[sorted.length-i-1].getDuration();
            }
            else if(duration + sorted[i].getDuration() <= durationsTotal) {
                display.add(sorted[i]);
                duration = duration + sorted[i].getDuration();
            }
        }
        model.addAttribute("displayList", display);
        return "generateSchedule";
    }


    public static List<Task> getSelected(List<Task> tasks) {
        Task task;
        List<Task> selected = new LinkedList<Task>();
        for( int i = 0; i<tasks.size();i++) {
            if (tasks.get(i).isSelected() == true) {
                task = tasks.get(i);
                selected.add(task);
            }
        }
        return selected;
    }

    public static List<Task> getCategoryTasks(String category, List<Task> tasks){
        List<Task> list = new LinkedList<Task>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskCategory() == category) {
                list.add(tasks.get(i));
            }
        }
        return list;
    }

    public static Task[] sortTasks(List<Task> tasks){
        Task[] sorted = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            sorted[i] = tasks.get(i);
        }
        sort(sorted, 0, sorted.length);
        return sorted;
    }

    public static List<Task> getTasksForUpload(List<Task> tasks) {
        Task task;
        List<Task> forUpload = new LinkedList<Task>();
        for( int i = 0; i<tasks.size();i++) {
            if (tasks.get(i).isForUpload() == true) {
                task = tasks.get(i);
                forUpload.add(task);
            }
        }
        return forUpload;
    }

    public static boolean setTasksForUpload(List<Task> tasksForUpload) {
        Task task;
        int i;
        for(i = 0; i<tasksForUpload.size();i++) {
            tasksForUpload.get(i).setForUpload(true);
        }
        if (i == tasksForUpload.size())
            return true;
        else
            return false;
    }

    public static int partition(Task arr[], int low, int high) {
        int pivot = arr[high].getDuration();
        int i = (low-1);
        for (int j=low; j<high; j++) {
            if (arr[j].getDuration() <= pivot)  {
                i++;
                Task temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Task temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;
        return i+1;
    }
    public static void sort(Task arr[], int low, int high)  {
        if (low < high) {
            int pi = partition(arr, low, high);
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }


    }

