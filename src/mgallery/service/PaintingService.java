package mgallery.service;


import java.util.List;

import mgallery.dao.PaintingDao;
import mgallery.entity.Painting;
import mgallery.utils.PageModel;

//XxxService��ķ��������ڴ���������ҵ���߼�,��XxxDao����ֻ�����ݽ��н�����,�������κ��߼�
public class PaintingService {
        private PaintingDao paintingDao = new PaintingDao();
        
        public PageModel pagination(int page,int rows,String...category) {
        	
        	if(rows == 0) {
        		throw new RuntimeException("��Ч��rows����");
        	}
        	if(category.length==0 || category[0]==null) {
        		return paintingDao.pagination(page, rows);
        	}else {
        		return paintingDao.pagination(Integer.parseInt(category[0]), page, rows);
        	}
        	
        }
        
       
        
       /*
        * �����ͻ� 
        */
      public void create(Painting painting) {
    	  paintingDao.create(painting);
      }
      
      /**
       * ����Ų�ѯ�ͻ�
       * @param id  �ͻ����
       * @return  �ͻ�����
       */
      public Painting findById(Integer id) {
    	  Painting p = paintingDao.findById(id);
    	  if(p==null) {
    		  throw  new RuntimeException("[id=" +id +"]�ͻ�������");
    	  }
    	  return p;
      }
      /**
       * �����ͻ�
       * @param newPainting �µ��ͻ�����
       * @param isPreviewModified �Ƿ��޸�Preview����
       */
      public void update(Painting newPainting,Integer isPreviewModified) {
    	  Painting oldPainting = this.findById(newPainting.getId());
    	  oldPainting.setPname(newPainting.getPname());
    	  oldPainting.setCategory(newPainting.getCategory() );
    	  oldPainting.setPrice(newPainting.getPrice());
    	  oldPainting.setDescription(newPainting.getDescription());
    	  if(isPreviewModified == 1) {
    		  oldPainting.setPreview(newPainting.getPreview());
    	  }
    	  paintingDao.update(oldPainting);
    	
      }
      /**
       * ɾ���ͻ�
       * @param id
       */
      public void delete(Integer id) {
    	  paintingDao.delete(id);
      }
      
      
      public static void main(String[] args) {
		PaintingService paintingService = new PaintingService();
		PageModel PageModel = paintingService.pagination(2, 6);
		List<Painting> paintingList = PageModel.getPageData();
		for(Painting painting :paintingList) {
			System.out.println(painting.getPname());
		}
		System.out.println(PageModel.getPageStartRow()+ ":"+PageModel.getPageEndRow());
	}
}
