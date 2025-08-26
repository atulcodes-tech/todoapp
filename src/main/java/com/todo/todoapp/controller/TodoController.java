package com.todo.todoapp.controller;
//
//
import com.todo.todoapp.entity.TodoEntity;
import com.todo.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class TodoController {

    private final TodoRepository todoRepository;

    @GetMapping({"", "/", "/home"})
    public String showHomePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {  // ✅ user is logged in
            String email = principal.getAttribute("email");
            model.addAttribute("todos", todoRepository.findByOwnerEmail(email));
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", email);
        } else {  // ✅ user is NOT logged in
            model.addAttribute("name", "Guest");
            model.addAttribute("email", "Not logged in");
            model.addAttribute("todos", java.util.Collections.emptyList());
        }

        return "index";
    }



    @PostMapping("/add")
    public String add(@RequestParam String title,
                      @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");

        TodoEntity newTodo = TodoEntity.builder()
                .title(title)
                .completed(false)
                .ownerEmail(email)   // 👈 store logged-in user’s email
                .build();

        todoRepository.save(newTodo);

        return "redirect:/";
    }


    @GetMapping("/update/{id}")
    public String update(@AuthenticationPrincipal OAuth2User principal,
                         @PathVariable Long id) {
        String email = principal.getAttribute("email");

        TodoEntity existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

        // 👇 ensure user can only update their own todos
        if (!existingTodo.getOwnerEmail().equals(email)) {
            throw new RuntimeException("You cannot update this todo!");
        }

        // toggle instead of always true
        existingTodo.setCompleted(!existingTodo.getCompleted());
        todoRepository.save(existingTodo);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal OAuth2User principal,
                         @PathVariable Long id) {
        String email = principal.getAttribute("email");

        TodoEntity existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found: " + id));

        // 👇 ensure user can only delete their own todos
        if (!existingTodo.getOwnerEmail().equals(email)) {
            throw new RuntimeException("You cannot delete this todo!");
        }

        todoRepository.delete(existingTodo);
        return "redirect:/";
    }
}
