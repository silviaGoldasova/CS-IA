package sk.silvia.projects.iassesment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.iassesment.dto.ScheduleFormDTO;
import sk.silvia.projects.iassesment.dto.UploadedDTO;
import sk.silvia.projects.iassesment.entity.MyUser;
import sk.silvia.projects.iassesment.model.Manager;
import sk.silvia.projects.iassesment.entity.Task;
import sk.silvia.projects.iassesment.service.ScheduleService;
import sk.silvia.projects.iassesment.service.TaskService;
import sk.silvia.projects.iassesment.service.UserService;
import sk.silvia.projects.iassesment.service.Validation;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/schedule")
    public String loadTasks(Model model) {

        // load all tasks from database
        List<Task> taskList = taskService.getAllTasks();

        // return homepage with tasks listed
        model.addAttribute("taskList", taskList);

        List<String> categs = new Manager().getCategoriesL();
        model.addAttribute("categs", categs);

        return "schedule";
    }

    @GetMapping("schedule/data")
    @ResponseBody
    public List<UploadedDTO> uploadData() {
        List<UploadedDTO> list = scheduleService.getUploadDataFromRepository();
        if (list == null) {
            list = new LinkedList<UploadedDTO>();
            return list;
        }
        else
            return list;
    }

    @GetMapping("/schedule/generate")
    public String getSchedule(Model model) {
        model.addAttribute("scheduleFormDTO", new ScheduleFormDTO());
        boolean valid = true;
        model.addAttribute("valid", valid);
        return "generateSchedule";
    }

    @PostMapping("/schedule/generatedButtons")
    public String generateScheduleButtons(@RequestParam(value = "action") String action) {

        scheduleService.buttonsGenerateSchedule(action);
        switch(action) {
            case "decline":
                return "redirect:/schedule/generate";
            case "accept":
                return "redirect:/schedule/home";
            default:
                break;
        }

        return "redirect:/schedule/home";

    }

    @PostMapping("/schedule/generated")
    public String generateSchedule(@ModelAttribute ScheduleFormDTO scheduleFormDTO, Model model) {

        Validation validate = new Validation();
        if (!(validate.validateInputInt(scheduleFormDTO.getSessionLength()) && validate.validateInputInt(scheduleFormDTO.getBreakLength()) && validate.validateInputInt(scheduleFormDTO.getBreakFrequency()))) {
            boolean valid = false;
            model.addAttribute("valid", valid);
            return "generateSchedule";
        }

        int sessionLength = Integer.parseInt(scheduleFormDTO.getSessionLength());
        int breakLength = Integer.parseInt(scheduleFormDTO.getBreakLength());
        int breakFrequency = Integer.parseInt(scheduleFormDTO.getBreakFrequency());

        String categorySelected = scheduleFormDTO.getCategorySelected();
        Assert.isTrue(!StringUtils.isEmpty(categorySelected), "CategorySelected cannot be empty");

        List<Task> display = scheduleService.generateSchedule(sessionLength, categorySelected);
        model.addAttribute("displayList", display);

        List<UploadedDTO> dataForUpload = scheduleService.getDataForUpload(display, breakFrequency, breakLength);
        model.addAttribute("list", dataForUpload);

        int totalDuration = scheduleService.getTotalDuration(dataForUpload);
        model.addAttribute("totalDuration", totalDuration);

        return "generatedSchedule";
    }

    @GetMapping("/schedule/generated")
    public String displaySchedule(Model model) {
        model.addAttribute("scheduleFormDTO", new ScheduleFormDTO());
        return "generateSchedule";
    }

}
