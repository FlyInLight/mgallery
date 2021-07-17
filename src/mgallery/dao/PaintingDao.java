package mgallery.dao;


import java.util.ArrayList;
import java.util.List;

import mgallery.entity.Painting;
import mgallery.utils.PageModel;
import mgallery.utils.XmlDataSource;

//油画数据访问对象
public class PaintingDao {
	
	//分页查询油画数据
	public PageModel pagination(int page,int rows) {
		//Painting油画对象集合
		List<Painting> list =XmlDataSource.getRawData();
		PageModel pageModel = new PageModel(list,page,rows);
		return pageModel;
	}
	//按类别分页查询
	public PageModel pagination(int category, int page,int rows) {
		List<Painting> list = XmlDataSource.getRawData();
		List<Painting> categoryList = new ArrayList();
		for(Painting p :list) {
			if(p.getCategory() == category) {
				categoryList.add(p);
			}
		}
		PageModel pageModel = new PageModel(categoryList,page,rows);
		return pageModel;
	}
	
	//数据新增
	public void create(Painting painting) {
		XmlDataSource.append(painting );
	}
	//数据修改
	public void update(Painting painting) {
		XmlDataSource.update(painting);
	}
	//数据删除
	public void delete(Integer id) {
		XmlDataSource.delete(id);
	}
	
	
	
	public Painting findById(Integer id) {
		List<Painting> data = XmlDataSource.getRawData();
		Painting painting = null;
		for(Painting p : data) {
			if(p.getId() == id) {
				painting = p;
				break;
			}
		}
		return painting;
	}

}
