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
 * ����Դ�࣬���ڽ�XML�ļ�����ΪJava����
 *
 */
public class XmlDataSource {
       
	
	//ͨ��static��̬�ؼ��ֱ�֤����ȫ��Ψһ
	private static List<Painting> data = new ArrayList();
	private static String dataFile;
	static {
	    //�ڳ������к�õ���·���µ�painting.xml�ļ���������·��
		dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
		reload();
		
	}
	
	//���¼��ػ�ȡpainting.xml�е�����
	private static void reload() {
		/*���ܳ����쳣���,dataFile���������ַ�
		����c:\new style\painting.xml�����ո�
		getPath�Ὣ�����ַ�ת��Ϊc:\new%20style\painting.xml
		����java.io�޷�����*/
		URLDecoder decoder = new URLDecoder();
		try {
			dataFile = decoder.decode(dataFile,"UTF-8");
			System.out.println(dataFile);
			//����Dom4j��XML���н���
			SAXReader reader = new SAXReader();
			//1.��ȡDocument�ĵ�����
			Document document = reader.read(dataFile);
			//2.Xpath�õ�XML�ڵ㼯��
			List<Node> nodes = document.selectNodes("/root/painting");
			data.clear();
			for(Node node : nodes) {
				//��Node���нڵ����ת��ΪElementԪ�ض���
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
	
	//��ȡ�����ͻ�Painting����
	public static List<Painting> getRawData(){
		return data;
	}
	
	public static void append(Painting painting) {
		//1.��ȡXML�ĵ��õ�Document����
		SAXReader reader = new SAXReader();
		Writer writer = null;
		try {
			Document document = reader.read(dataFile);
			//2.�����µ�painting�ڵ�
			Element root = document.getRootElement();
			Element p = root.addElement("painting");
			//3.����painting�ڵ�ĸ����ӽڵ�
			p.addAttribute("id", String.valueOf(data.size()+1));
			p.addElement("pname").setText(painting.getPname());
			p.addElement("category").setText(painting.getCategory().toString());
			
			p.addElement("price").setText(painting.getPrice().toString());
		    p.addElement("preview").setText(painting.getPreview());
		    p.addElement("description").setText(painting.getDescription());
		    //4.д��XML�����׷�Ӳ���
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
			reload();	//�ڴ����ļ�˳��һ��
		}
		
		
	}
	/**
	 * ���¶�Ӧid��XML�ͻ�����
	 * @param painting
	 * @throws Exception 
	 */
	
	public static void update(Painting painting)  {
		SAXReader reader = new SAXReader();
		Writer writer = null;
		Document document;
		try {
			document = reader.read(dataFile);
			//�ڵ�·��[@������=����ֵ]
			//	/root/painting[@id=x]
			List <Node> nodes = document.selectNodes("/root/painting[@id=" +painting.getId()+"]");
			if(nodes.size()==0) {
				throw new RuntimeException("id=" +painting.getId()+"����ͻ�������");
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
	 * ��id��ɾ��XML�ͻ�����
	 * @param id
	 */
	
	public static void delete(Integer id) {
		SAXReader reader = new SAXReader();
		Writer  writer = null;
		try {
			Document document = reader.read(dataFile);
			List<Node> nodes = document.selectNodes("/root/painting[@id=" + id + "]");
			if(nodes.size() == 0) {
				throw new RuntimeException("id=" + id + "����ͻ�������");
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
		p.setPname("�����ͻ�");
		p.setCategory(1);
		p.setPrice(4000);
		p.setPreview("/upload/10.jpg");
		p.setDescription("�����ͻ�����");
		XmlDataSource.append(p);
	}
}
