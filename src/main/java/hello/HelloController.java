package hello;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "Hello";
    }

    @RequestMapping("/{searchTerm}/{responseCount}")
    public String index(@PathVariable String searchTerm, @PathVariable int responseCount) {
        return Arrays.toString(WordBank.getWords(searchTerm, responseCount));
    }
}