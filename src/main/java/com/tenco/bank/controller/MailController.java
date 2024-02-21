package com.tenco.bank.controller;

import com.tenco.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MailController {

    @Autowired
    private UserService userService;


    
    
    
    
    @GetMapping("/mail/sendMail")
    public String sendEamil(){

        String to = "jyj0298@naver.com";
        String subject = "Test Email with Attachment";
        String text = "Hello! This is a test email with an attachment. 걍 메일 테스트임";

        try {
            userService.sendEmail(to, subject, text);
            return "redirect:/user/sign-in";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }

    }
}
