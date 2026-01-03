package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.Notification;
import cn.gcc.gearcorelab.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 显示消息提醒页面
     */
    @GetMapping
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            System.out.println("用户未登录，重定向到登录页面");
            return "redirect:/login";
        }

        String username = userDetails.getUsername();
        System.out.println("用户 " + username + " 访问通知页面");
        
        List<Notification> notifications = notificationService.getNotificationsByUsername(username);
        System.out.println("为用户 " + username + " 获取到 " + notifications.size() + " 条通知");
        
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", notificationService.getUnreadCount(username));
        
        return "notifications/list";
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Long id, 
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            notificationService.markAsRead(id, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "通知已标记为已读");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }

        return "redirect:/notifications";
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/mark-all-read")
    public String markAllAsRead(@AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            notificationService.markAllAsRead(userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "所有通知已标记为已读");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }

        return "redirect:/notifications";
    }

    /**
     * 删除通知
     */
    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            notificationService.deleteNotification(id, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "通知已删除");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }

        return "redirect:/notifications";
    }
}