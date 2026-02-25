package com.example.demo.controller;

import com.example.demo.entity.Page;
import com.example.demo.entity.Site;
import com.example.demo.service.BlockService;
import com.example.demo.service.PageService;
import com.example.demo.service.SiteService;
import com.example.demo.dto.BlockContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private PageService pageService;

    @Autowired
    private BlockService blockService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String home(Model model) {
        Site site = siteService.getCurrentSite();
        if (site == null) {
            return "error/404";
        }
        model.addAttribute("site", site);

        Page homePage = pageService.getHomePage(site.getId());
        if (homePage == null) {
            return "error/404";
        }

        List<BlockContentDTO> blocks = blockService.getPageBlocks(homePage.getId());
        model.addAttribute("blocks", blocks);

        return "index";
    }

    /**
     * 自定义页面
     */
    @GetMapping("/{pageSlug}")
    public String page(@PathVariable String pageSlug, Model model) {
        Site site = siteService.getCurrentSite();
        if (site == null) {
            return "error/404";
        }
        model.addAttribute("site", site);

        Page page = pageService.getPageBySlug(site.getId(), "/" + pageSlug);
        if (page == null) {
            return "error/404";
        }

        List<BlockContentDTO> blocks = blockService.getPageBlocks(page.getId());
        model.addAttribute("blocks", blocks);

        return "page";
    }

    /**
     * 渲染单个区块（用于AJAX加载）
     */
    @GetMapping("/block/{blockId}")
    public String renderBlock(@PathVariable String blockId, Model model) {
        BlockContentDTO blockContent = blockService.getBlockContentDTO(blockId);
        if (blockContent == null) {
            return "error/404";
        }

        model.addAttribute("blockContent", blockContent.getContent());
        return "fragments/sections/" + blockContent.getBlockType() + " :: " + blockContent.getBlockType();
    }
}
