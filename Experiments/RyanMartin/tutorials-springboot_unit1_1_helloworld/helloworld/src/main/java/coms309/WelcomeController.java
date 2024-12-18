package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome from Ryan Martin! This is my hello world test program. What is your name? " +
                "You can tell me by adding a /name{name} in the browser window.";
    }

    @GetMapping("/name/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello " + name +"! Do you also go to Iowa State? (You can answer by adding a" +
                " /answer/(yes or no) in the browser window)";
    }

    @GetMapping("/answer/{answer}")
    public String answer(@PathVariable String answer) {
        if(answer.equalsIgnoreCase("yes")) {
            return "Nice! What year are you? (You can tell me your year by adding /year/(year) in the browser window)";
        }
        else {
            return "No? what school do you attend? (You can tell me your school by adding /school/(school) in the browser window";
        }
    }

    @GetMapping("/year/{year}")
    public String year(@PathVariable String year) {
        return "You are a "+year+", nice! I am a junior.";
    }

    @GetMapping("/school/{school}")
    public String school(@PathVariable String school) {
        return "Oh nice! I've never been to "+school+", but I'll have to visit sometime!";
    }
}
