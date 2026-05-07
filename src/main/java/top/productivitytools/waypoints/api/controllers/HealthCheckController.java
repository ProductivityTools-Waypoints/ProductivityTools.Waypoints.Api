package top.productivitytools.waypoints.api.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HealthCheckController {
     @GetMapping
     @ResponseBody
     public String helloQuery() {
        return "Hello World";
    }
}
