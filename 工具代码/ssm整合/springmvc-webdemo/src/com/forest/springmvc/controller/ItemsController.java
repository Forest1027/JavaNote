package com.forest.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.forest.springmvc.pojo.Items;
import com.forest.springmvc.pojo.QueryVo;
import com.forest.springmvc.service.ItemService;

@Controller
public class ItemsController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/itemList")
	public ModelAndView getItemList() {
		List<Items> list = itemService.getItemList();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemList", list);
		modelAndView.setViewName("itemList");
		return modelAndView;
	}
	
	@RequestMapping("/itemEdit")
	public String getItemById(Integer id,Model model) {
		Items item = itemService.getItemById(id);
		model.addAttribute("item", item);
		return "editItem";
	}
	
	@RequestMapping("/updateitem")
	public String updateItem(Items items) {
		itemService.updateItem(items);
		return "success";
	}
	
	@RequestMapping("/item/queryitem.action")
	public String queryItem(QueryVo queryVo,Model model) {
		List<Items> list = itemService.query(queryVo);
		model.addAttribute("itemList", list);
		return "itemList";
	}
}
