package sk.silvia.projects.IAssesment1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sk.silvia.projects.IAssesment1.dto.ScheduleFormDTO;
import sk.silvia.projects.IAssesment1.dto.UploadedDTO;
import sk.silvia.projects.IAssesment1.model.Manager;
import sk.silvia.projects.IAssesment1.model.entity.Task;
import sk.silvia.projects.IAssesment1.service.ScheduleService;
import sk.silvia.projects.IAssesment1.service.TaskService;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TaskService taskService;

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

    @GetMapping("/")
    public String blank() {
        return "home";
    }

    @GetMapping("/schedule/home")
    public String viewHome() {
        return "home";
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

        model.addAttribute("scheduleFormDTO", scheduleFormDTO);

        int sessionLength = scheduleFormDTO.getSessionLength();
        int breakLength = scheduleFormDTO.getBreakLength();
        int breakFrequency = scheduleFormDTO.getBreakFrequency();

        String categorySelected = scheduleFormDTO.getCategorySelected();
        Assert.isTrue(!StringUtils.isEmpty(categorySelected), "CategorySelected cannot be empty");

        List<Task> display = scheduleService.generateSchedule(sessionLength, breakLength, breakFrequency, categorySelected);
        model.addAttribute("displayList", display);

        List<UploadedDTO> dataForUpload = scheduleService.getDataForUpload(display, breakFrequency, breakLength);
        model.addAttribute("list", dataForUpload);

        int totalDuration = scheduleService.getTotalDuration(dataForUpload);
        model.addAttribute("totalDuration", totalDuration);

        return "generatedSchedule";
    }

    @GetMapping("/schedule/generated")
    public String displaySchedule(@ModelAttribute List<Task> display, Model model) {
        model.addAttribute("displayList", display);
        return "generateSchedule";
    }

}
