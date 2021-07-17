package mgallery.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import mgallery.entity.Painting;

/**
 * 数据源类，用于将XML文件解析为Java对象
 *
 */
public class XmlDataSource {
       
	
	//通过static静态关键字保证数据全局唯一
	private static List<Painting> data = new ArrayList();
	private static String dataFile;
	static {
	    //在程序运行后得到类路径下的painting.xml文件完整物理路径
		dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
		reload();
		
	}
	
	//重新加载获取painting.xml中的数据
	private static void reload() {
		/*可能出现异常情况,dataFile包含特殊字符
		比如c:\new style\painting.xml包含空格
		getPath会将特殊字符转化为c:\new%20style\painting.xml
		但是java.io无法解析*/
		URLDecoder decoder = new URLDecoder();
		try {
			dataFile = decoder.decode(dataFile,"UTF-8");
			System.out.println(dataFile);
			//利用Dom4j对XML进行解析
			SAXReader reader = new SAXReader();
			//1.获取Document文档对象
			Document document = reader.read(dataFile);
			//2.Xpath得到XML节点集合
			List<Node> nodes = document.selectNodes("/root/painting");
			data.clear();
			for(Node node : nodes) {
				//将Node所有节点对象转换为Element元素对象
				Element element = (Element)node;
				String id = element.attributeValue("id");
				String pname = element.elementText("pname");
				Painting painting = new Painting();
				painting.setId(Integer.parseInt(id));
				painting.setPname(pname);
				painting.setCategory(Integer.parseInt(element.elementText("category")));
				painting.setPrice(Integer.parseInt(element.elementText("price")));
				painting.setPreview(element.elementText("preview"));
				painting.setDescription(element.elementText("description"));
				data.add(painting);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取所有油画Painting对象
	public static List<Painting> getRawData(){
		return data;
	}
	
	public static void append(Painting painting) {
		//1.读取XML文档得到Document对象
		SAXReader reader = new SAXReader();
		Writer writer = null;
		try {
			Document document = reader.read(dataFile);
			//2.创建新的painting节点
			Element root = document.getRootElement();
			Element p = root.addElement("painting");
			//3.创建painting节点的各个子节点
			p.addAttribute("id", String.valueOf(data.size()+1));
			p.addElement("pname").setText(painting.getPname());
			p.addElement("category").setText(painting.getCategory().toString());
			
			p.addElement("price").setText(painting.getPrice().toString());
		    p.addElement("preview").setText(painting.getPreview());
		    p.addElement("description").setText(painting.getDescription());
		    //4.写入XML，完成追加操作
		    writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
		    document.write(writer);
		    System.out.println(dataFile);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer != null) {
				try {
					writer.close();	
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			reload();	//内存与文件顺序一致
		}
		
		
	}
	/**
	 * 更新对应id的XML油画数据
	 * @param painting
	 * @throws Exception 
	 */
	
	public static void update(Painting painting)  {
		SAXReader reader = new SAXReader();
		Writer writer = null;
		Document document;
		try {
			document = reader.read(dataFile);
			//节点路径[@属性名=属性值]
			//	/root/painting[@id=x]
			List <Node> nodes = document.selectNodes("/root/painting[@id=" +painting.getId()+"]");
			if(nodes.size()==0) {
				throw new RuntimeException("id=" +painting.getId()+"编号油画不存在");
			}
			Element p = (Element)nodes.get(0);
			p.selectSingleNode("pname").setText(painting.getPname());
			p.selectSingleNode("category").setText(painting.getCategory().toString());
			p.selectSingleNode("price").setText(painting.getPrice().toString());
			p.selectSingleNode("preview").setText(painting.getPreview());
			p.selectSingleNode("description").setText(painting.getDescription());
			writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
			document.write(writer);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			reload();
		}
		
	}
	
	/**
	 * 按id号删除XML油画数据
	 * @param id
	 */
	
	public static void delete(Integer id) {
		SAXReader reader = new SAXReader();
		Writer  writer = null;
		try {
			Document document = reader.read(dataFile);
			List<Node> nodes = document.selectNodes("/root/painting[@id=" + id + "]");
			if(nodes.size() == 0) {
				throw new RuntimeException("id=" + id + "编号油画不存在");
			}
			Element p = (Element)nodes.get(0);
			p.getParent().remove(p);
			writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
			document.write(writer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			reload();
		}
		
		
		
		
	}
	public static void main(String[] args) {
//		new XmlDataSource();
		List<Painting> ps = XmlDataSource.getRawData();
		System.out.println(ps);
		
		Painting p = new Painting();
		p.setPname("测试油画");
		p.setCategory(1);
		p.setPrice(4000);
		p.setPreview("/upload/10.jpg");
		p.setDescription("测试油画描述");
		XmlDataSource.append(p);
	}
}
