package main.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaFallbackController {

    @RequestMapping(value = {
            "/{path:^(?!users|data|comments|visit-items|visitItemLikes|trips).*$}/**"
    })
    public String redirect() {
        return "forward:/index.html";
    }
}

