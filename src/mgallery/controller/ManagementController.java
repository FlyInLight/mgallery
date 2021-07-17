package mgallery.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import mgallery.entity.Painting;
import mgallery.service.PaintingService;
import mgallery.utils.PageModel;

/**
 * ��̨������Controller
 */
@WebServlet("/management")
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PaintingService paintingService = new PaintingService();  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagementController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String method = request.getParameter("method");
		if(method.equals("list")) {
			this.list(request,response);
		}else if(method.equals("delete")) {
			this.delete(request, response);
		}else if(method.equals("show_create")) {
			this.showCreatePage(request,response);
		}else if(method.equals("create")) {
			this.create(request,response);
		}else if(method.equals("show_update")) {
			this.showUpdatePage(request,response);
		}else if(method.equals("update")) {
			this.update(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String p =request.getParameter("p");
    	String r =request.getParameter("r");
    	if(p==null) {
    		p="1";
    	}
    	if(r==null) {
    		r="6";
    	}
    	PageModel pageModel = paintingService.pagination(Integer.parseInt(p), Integer.parseInt(r));
    	request.setAttribute("pageModel", pageModel);
    	request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }
    
    //��ʾ����ҳ��
    private  void showCreatePage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(request, response);
    }
    
    
    private void create(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	//�ļ��ϴ�ʱ�����ݴ������׼����ȫ��ͬ
    	
    	//1.��ʼ��FileUpload���
    	/**
    	 * FileItemFactory ����ǰ�˱�������ת��Ϊһ��FileItem����
    	 * ServletFileUpload ����ΪFileUpload����ṩJava web��HTTP�������
    	 */
    	FileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload sf = new ServletFileUpload(factory);
    	//2.��������FileItem
    	try {
			List<FileItem> formData = sf.parseRequest(request);
			Painting painting = new Painting();
			for(FileItem fi:formData) {
				if(fi.isFormField()) {
					System.out.println("��ͨ������" + fi.getFieldName() +":" +fi.getString("UTF-8"));
				switch(fi.getFieldName()) {
					case "pname":
						painting.setPname(fi.getString("UTF-8"));
						break;
					case "category"	:
						painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
					    break;
					case "price"  :
						painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
					    break;
					case "description" :
						painting.setDescription(fi.getString("UTF-8"));
					}
				}else {
					System.out.println("�ļ��ϴ���" +fi.getFieldName());
					//3.�ļ����浽������Ŀ¼
					String path = request.getServletContext().getRealPath("/upload");
				    System.out.println("�ϴ��ļ�Ŀ¼:"+ path);
				    //�������Ψһ�ļ���(Ҳ������ʱ������)
				    String fileName=UUID.randomUUID().toString();
					//��ȡ��׺
				    String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
					fi.write(new File(path,fileName+suffix));
					painting.setPreview("/upload/" +fileName + suffix);
				
				}
			}
			paintingService.create(painting);//��������
			response.sendRedirect("management?method=list");//��Ӧ�ض���,�����б�ҳ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    private void showUpdatePage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("id");
    	Painting painting = paintingService.findById(Integer.parseInt(id));
    	request.setAttribute("painting", painting);
    	request.getRequestDispatcher("/WEB-INF/jsp/update.jsp").forward(request, response);
    }
    
    

    
    /**
	 * ʵ���ͻ�����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void update(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		int isPreviewModified = 0;
		//�ļ��ϴ�ʱ�����ݴ������׼����ȫ��ͬ
		/*
		String pname = request.getParameter("pname");
		System.out.println(pname);
		*/
		//1. ��ʼ��FileUpload���
		FileItemFactory factory = new DiskFileItemFactory();
		/**
		 * FileItemFactory ���ڽ�ǰ�˱�������ת��Ϊһ����FileItem����
		 * ServletFileUpload ����ΪFileUpload����ṩJava web��Http�������
		 */
		ServletFileUpload sf = new ServletFileUpload(factory);
		//2. ��������FileItem
		try {
			List<FileItem> formData = sf.parseRequest(request);
			Painting painting = new Painting();
			for(FileItem fi:formData) {
				if(fi.isFormField()) {
					System.out.println("��ͨ������:" + fi.getFieldName() + ":" + fi.getString("UTF-8"));
					switch (fi.getFieldName()) {
					case "pname":
						painting.setPname(fi.getString("UTF-8"));
						break;
					case "category":
						painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
						break;
					case "price":
						painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
						break;
					case "description":
						painting.setDescription(fi.getString("UTF-8"));
						break;
					case "id":
						painting.setId(Integer.parseInt(fi.getString("UTF-8")));
						break;
					case "isPreviewModified":
						isPreviewModified = Integer.parseInt(fi.getString("UTF-8"));
						break;
					default:
						break;
					}
				}else {
					if(isPreviewModified == 1) { 
						System.out.println("�ļ��ϴ���:" + fi.getFieldName());
						//3.�ļ����浽������Ŀ¼
						String path = request.getServletContext().getRealPath("/upload");
						System.out.println("�ϴ��ļ�Ŀ¼:" + path);
						//String fileName = "test.jpg";
						String fileName = UUID.randomUUID().toString();
						//fi.getName()�õ�ԭʼ�ļ���,��ȡ���һ��.�������ַ���,����:wxml.jpg->.jpg
						String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
						//fi.write()д��Ŀ���ļ�
						fi.write(new File(path,fileName + suffix));
						painting.setPreview("/upload/" + fileName + suffix);
					}
				}
			}
			//�������ݵĺ��ķ���
			paintingService.update(painting, isPreviewModified);
			response.sendRedirect("/management?method=list");//�����б�ҳ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String id = request.getParameter("id");
		PrintWriter out = response.getWriter();
		try {
			
			paintingService.delete(Integer.parseInt(id));
			out.println("{\"result\":\"ok\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.println("{\"result\":\"" + e.getMessage() + "\"}");
		}
	}
	
	
	
	
}
