package sk.silvia.projects.iassesment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.iassesment.dto.TaskFormDataDTO;
import sk.silvia.projects.iassesment.model.*;
import sk.silvia.projects.iassesment.entity.CompletedTask;
import sk.silvia.projects.iassesment.entity.Task;
import sk.silvia.projects.iassesment.service.TaskService;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/schedule/new")
    public String newTask(Model model) {
        model.addAttribute("taskFormDataDTO", new TaskFormDataDTO());
        boolean valid = true;
        model.addAttribute("valid", valid);
        // return new empty task form
        return "new_task";
    }

    @PostMapping("/schedule/new")
    public String createTask(@ModelAttribute TaskFormDataDTO taskFormDataDTO, Model model) {
        boolean valid = taskService.createTask(taskFormDataDTO.getName(), taskFormDataDTO.getDuration(), taskFormDataDTO.getTaskCategory());
        if (valid)
            return "redirect:/schedule";
        else {
            model.addAttribute("valid", valid);
            return "new_task";
        }

    }

    // napr: http://localhost:8080/schedule/25
    @GetMapping("/schedule/{id}")
    public String editTask(@PathVariable("id") Long id, Model model) {

        Task task = taskService.viewEditTask(id);

        // fill form with task data
        model.addAttribute("task", task);

        return "edit_task";
    }

    @PostMapping("/schedule/{id}")
    public String editTask(@ModelAttribute Task task) {
        taskService.saveEditTask(task);
        return "redirect:/schedule";
    }

    @GetMapping("/schedule/completed")
    public String displayCompleted(Model model) {

        List<CompletedTask> completedTaskList = taskService.listCompleted();
        model.addAttribute("listCompletedTasks", completedTaskList);

        List<String> categs = new Manager().getCategoriesL();
        model.addAttribute("categs", categs);

        return "completed";
    }

    @PostMapping(value = "/schedule/{taskIdParameter}/editing")
    public String edittingTask(@PathVariable("taskIdParameter") Long id, @RequestParam(value = "action") String functionToPerform) {          //@PathVariable String taskIdParameter
        taskService.edittingTask(id, functionToPerform);

        switch(functionToPerform) {
            case "edit":
                return "redirect:/schedule/{taskIdParameter}";
            default:
                return "redirect:/schedule";
        }
    }

    @GetMapping("/schedule/selected")
    public String loadSelected(Model model) {

        List<Task> selectedList = taskService.loadSelected();
        model.addAttribute("taskList", selectedList);

        List<String> categs = new Manager().getCategoriesL();
        model.addAttribute("categs", categs);

        return "selected";
    }

    @PostMapping("/schedule/selected/{taskIdParameter}")
    public String changeSelected(@PathVariable("taskIdParameter") Long id, @RequestParam(value = "action") String functionToPerform) {
        taskService.changeSelected(id, functionToPerform);
        return "redirect:/schedule/selected";
    }


}

