package com.example.webserveruploadingvideos.controller;

import com.example.webserveruploadingvideos.service.VideoService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private SimpMessagingTemplate messagingTemplate;

    private VideoService videoService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
//        List<Video> currentUploads = videoService.getCurrentUploads();
//        model.addAttribute("uploads", currentUploads);
        return "admin-dashboard"; // Thymeleaf шаблон для отображения дашборда администратора
    }

    // Метод для отправки данных о текущих загрузках на клиентскую сторону
    @Scheduled(fixedDelay = 1000) // Регулярное выполнение каждую секунду (или другой интервал)
    public void sendUploadProgressToAdmin() {
//        List<Video> currentUploads = videoService.getCurrentUploads();

//        for (Video upload : currentUploads) {
//            String login = upload.getUser().getLogin();
//            String videoName = upload.getVideo().getName();
//            int progress = upload.getProgress();
//
//            messagingTemplate.convertAndSend("/topic/admin-progress/" + login,
//                    new UploadProgress(login, videoName, progress));
//        }
    }
}


