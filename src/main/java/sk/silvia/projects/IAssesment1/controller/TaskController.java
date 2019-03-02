package sk.silvia.projects.IAssesment1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.IAssesment1.model.Task;
import sk.silvia.projects.IAssesment1.model.TaskFormDataDTO;
import sk.silvia.projects.IAssesment1.model.User;
import sk.silvia.projects.IAssesment1.model.Manager;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

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

            Manager man = new Manager(); List<String> categs = man.categoriesL;
            model.addAttribute("categs", categs);

            return "/schedule";
        }

        // napr: http://localhost:8080/schedule/25
        @GetMapping("/schedule/{id}")
        public String editTask(@PathVariable("id") Long id, Model model ) {

            Task t1  = new Task(); t1.setId(1L); t1.setName("CS IA"); t1.setDuration(70); t1.setTaskCategory("School");
            Task t2  = new Task(); t2.setId(35L); t2.setName("IA"); t2.setDuration(60); t2.setTaskCategory("Work");
            Task t3  = new Task(); t3.setId(45L); t3.setName("Internal Assess"); t3.setDuration(3); t2.setTaskCategory("Free time");
            Task t4  = new Task(); t4.setId(22L); t4.setName("maths"); t4.setDuration(45); t4.setTaskCategory("School");
            Task t5  = new Task(); t5.setId(23L); t5.setName("maths2"); t5.setDuration(60); t5.setTaskCategory("Work");
            Task t6  = new Task(); t6.setId(24L); t6.setName("clean"); t6.setDuration(45); t6.setTaskCategory("Household");
            Task t7  = new Task(); t7.setId(25L); t7.setName("maths"); t7.setDuration(90); t7.setTaskCategory("School");
            Task t8  = new Task(); t8.setId(26L); t8.setName("maths2"); t8.setDuration(20); t8.setTaskCategory("Work");
            Task t9  = new Task(); t9.setId(27L); t9.setName("Slovak"); t9.setDuration(30); t9.setTaskCategory("Household");
            taskRepository.save(t1); taskRepository.save(t2); taskRepository.save(t3);
            taskRepository.save(t4); taskRepository.save(t5); taskRepository.save(t6);
            taskRepository.save(t7); taskRepository.save(t8); taskRepository.save(t9);


            // load task from database - find task by id
            Task task = taskRepository.getOne(id);

            // fill form with task data
            model.addAttribute("task", task);

            // show form
            return "edit_task";
        }

        @PostMapping("/schedule/{id}")
        public String editTask(@ModelAttribute Task task ) {

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
        public List<Task> getTaskList(){
            return taskRepository.findAll();
        }

}
