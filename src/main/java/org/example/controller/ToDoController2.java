package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.beans.SessionUser;
import org.example.repository.ToDoRepository;
import org.example.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RequestMapping("/")
public class ToDoController2 {
    @Autowired
    private ToDoRepository toDoRepository;
    @Autowired
    private SessionUser sessionUser;

    @GetMapping("/begin")
    public String showBeginForm(Model model) {
        model.addAttribute("todo", new ToDo());
        return "add-toDo";
    }

    @PostMapping("/addTodo")
    public String addToDo(@Valid ToDo toDo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-toDo";
        }
        Integer id = sessionUser.userService().getAuthUser().getId();
        toDoRepository.add(toDo, Long.valueOf(id));
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showToDoList(Model model) {
        Long id = Long.valueOf(sessionUser.userService().getAuthUser().getId());
        model.addAttribute("toDos", toDoRepository.getAllToDo(id));
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        ToDo toDo = toDoRepository.getToDo(id);
        model.addAttribute("toDo", toDo);
        return "update-toDo";
    }

    @PostMapping("/update/{id}")
    public String updateToDo(@PathVariable("id") long id, @Valid ToDo toDo,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            toDo.setId(id);
            return "update-toDo";
        }
        toDoRepository.update(toDo);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteToDo(@PathVariable("id") long id, Model model) {
        toDoRepository.delete(id);
        return "redirect:/index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
