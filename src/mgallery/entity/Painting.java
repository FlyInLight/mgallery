package mgallery.entity;


//油画实体类
public class Painting {
	 //油画编号
   private Integer id;
   //名称
   private String pname;
   //分类 1-现实主义 2-抽象主义
   private Integer category;
   //价格
   private Integer price;
	//油画 图片地址
   private String preview;
   //描述
   private String description;
   
   
   
   public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getPreview() {
		return preview;
	}
	public void setPreview(String preview) {
		this.preview = preview;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
