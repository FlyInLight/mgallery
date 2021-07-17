package mgallery.dao;


import java.util.ArrayList;
import java.util.List;

import mgallery.entity.Painting;
import mgallery.utils.PageModel;
import mgallery.utils.XmlDataSource;

//�ͻ����ݷ��ʶ���
public class PaintingDao {
	
	//��ҳ��ѯ�ͻ�����
	public PageModel pagination(int page,int rows) {
		//Painting�ͻ����󼯺�
		List<Painting> list =XmlDataSource.getRawData();
		PageModel pageModel = new PageModel(list,page,rows);
		return pageModel;
	}
	//������ҳ��ѯ
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
	
	//��������
	public void create(Painting painting) {
		XmlDataSource.append(painting );
	}
	//�����޸�
	public void update(Painting painting) {
		XmlDataSource.update(painting);
	}
	//����ɾ��
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
