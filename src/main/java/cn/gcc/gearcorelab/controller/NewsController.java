package cn.gcc.gearcorelab.controller;

import cn.gcc.gearcorelab.model.News;
import cn.gcc.gearcorelab.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    // 科技新闻列表页面
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        List<News> newsList = newsService.listAllWithPagination(page, pageSize);
        long totalNews = newsService.getTotalNewsCount();
        int totalPages = (int) Math.ceil((double) totalNews / pageSize);
        
        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalNews", totalNews);
        
        return "news/list";
    }
    
    // 新闻详情页面
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        News news = newsService.findById(id);
        System.out.println("[DEBUG] 查询新闻ID: " + id + ", 查询结果: " + (news == null ? "null" : news.toString()));
        if (news == null) {
            return "redirect:/news";
        }
        model.addAttribute("news", news);
        return "news/detail";
    }
    
    // 管理员创建新闻页面
    // 创建新闻功能已迁移到AdminController
    
    // 管理员编辑新闻页面
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        News news = newsService.findById(id);
        if (news == null) {
            return "redirect:/news";
        }
        model.addAttribute("news", news);
        return "news/edit";
    }
    
    // 管理员更新新闻
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, 
                        @ModelAttribute News news,
                        @RequestParam("image") MultipartFile image,
                        @RequestParam(required = false) String publishTimeStr,
                        RedirectAttributes redirectAttributes) {
        try {
            news.setId(id);
            
            // 处理图片上传
            if (!image.isEmpty()) {
                String imageUrl = saveImage(image);
                news.setImageUrl(imageUrl);
            }
            
            // 处理发布时间
            if (publishTimeStr != null && !publishTimeStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                news.setPublishTime(LocalDateTime.parse(publishTimeStr, formatter));
            }
            
            newsService.update(news);
            redirectAttributes.addFlashAttribute("message", "新闻更新成功！");
            return "redirect:/news/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新闻更新失败：" + e.getMessage());
            return "redirect:/news/edit/" + id;
        }
    }
    
    // 管理员删除新闻
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsService.delete(id);
            redirectAttributes.addFlashAttribute("message", "新闻删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新闻删除失败：" + e.getMessage());
        }
        return "redirect:/news";
    }
    
    // 保存图片文件
    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        File targetFile = new File(uploadDir + filename);
        file.transferTo(targetFile);
        
        return "/uploads/" + filename;
    }
}