package smu.capstone.heartsignal.controller.viewController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import smu.capstone.heartsignal.session.SessionUser;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final SessionUser sessionUser;

    @GetMapping("/")
    public String index(){return "index";}

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("user", sessionUser);
        return "home";
    }
}
