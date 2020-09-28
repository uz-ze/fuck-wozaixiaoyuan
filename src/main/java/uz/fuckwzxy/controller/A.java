package uz.fuckwzxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.fuckwzxy.service.CheckService;
import uz.fuckwzxy.util.SendUtil;

/**
 * @author wzh
 * 2020/9/23 19:00
 */
@RestController
@Slf4j
public class A {
    @RequestMapping("/ss")
    public void test() {
        log.info("sdsaffsdf");
        int i = 1 / 0;
    }

    @Autowired
    CheckService checkService;
    @RequestMapping("/go")
    public void go() {
        checkService.morningCheck();
    }

    @RequestMapping("/gogo")
    public void gogo() {
        checkService.singIn();
    }
}
