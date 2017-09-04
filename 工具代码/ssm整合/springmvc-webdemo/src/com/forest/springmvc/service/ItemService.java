package com.forest.springmvc.service;

import java.util.List;

import com.forest.springmvc.pojo.Items;
import com.forest.springmvc.pojo.QueryVo;

public interface ItemService {
	public List<Items> getItemList();
	public Items getItemById(Integer id);
	public void updateItem(Items items);
	public List<Items> query(QueryVo queryVo);
}
