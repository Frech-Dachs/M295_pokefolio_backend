package ch.leon.troller.M295_pokefolio_backend.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {
    @RequestMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}