package com.forest.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.springmvc.mapper.ItemsMapper;
import com.forest.springmvc.pojo.Items;
import com.forest.springmvc.pojo.ItemsExample;
import com.forest.springmvc.pojo.ItemsExample.Criteria;
import com.forest.springmvc.pojo.QueryVo;

@Service
public class ItemServiceImpl implements ItemService{
	@Autowired
	private ItemsMapper itemsMapper;
	
	@Override
	public List<Items> getItemList() {
		
		return itemsMapper.getItemList();
	}

	@Override
	public Items getItemById(Integer id) {
		return itemsMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateItem(Items items) {
		//itemsMapper.updateByPrimaryKey(items);
		itemsMapper.updateByPrimaryKeySelective(items);
	}

	@Override
	public List<Items> query(QueryVo queryVo) {
		// TODO Auto-generated method stub
		System.out.println(queryVo.getItems().getName()+"__"+queryVo.getItems().getId());
		ItemsExample example = new ItemsExample();
		Criteria criteria = example.createCriteria();
		if (queryVo.getItems().getName()!=null) {
			criteria.andNameEqualTo(queryVo.getItems().getName());
		}
		if (queryVo.getItems().getPrice()!=null) {
			criteria.andPriceEqualTo(queryVo.getItems().getPrice());
		}
		
		return itemsMapper.selectByExample(example);
	}
	
}
