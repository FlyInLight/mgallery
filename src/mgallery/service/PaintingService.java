package mgallery.service;


import java.util.List;

import mgallery.dao.PaintingDao;
import mgallery.entity.Painting;
import mgallery.utils.PageModel;

//XxxService类的方法是用于处理完整的业务逻辑,而XxxDao类是只于数据进行交换的,不包含任何逻辑
public class PaintingService {
        private PaintingDao paintingDao = new PaintingDao();
        
        public PageModel pagination(int page,int rows,String...category) {
        	
        	if(rows == 0) {
        		throw new RuntimeException("无效的rows参数");
        	}
        	if(category.length==0 || category[0]==null) {
        		return paintingDao.pagination(page, rows);
        	}else {
        		return paintingDao.pagination(Integer.parseInt(category[0]), page, rows);
        	}
        	
        }
        
       
        
       /*
        * 新增油画 
        */
      public void create(Painting painting) {
    	  paintingDao.create(painting);
      }
      
      /**
       * 按编号查询油画
       * @param id  油画编号
       * @return  油画对象
       */
      public Painting findById(Integer id) {
    	  Painting p = paintingDao.findById(id);
    	  if(p==null) {
    		  throw  new RuntimeException("[id=" +id +"]油画不存在");
    	  }
    	  return p;
      }
      /**
       * 更新油画
       * @param newPainting 新的油画数据
       * @param isPreviewModified 是否修改Preview属性
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
       * 删除油画
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
