package mgallery.controller;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mgallery.service.PaintingService;
import mgallery.utils.PageModel;

/**
 * Servlet implementation class PaintingController
 */
@WebServlet("/page")
public class PaintingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PaintingService paintingService = new PaintingService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaintingController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. ����Http����
		String page = request.getParameter("p");
		String rows = request.getParameter("r");
		String category = request.getParameter("c");
		
		if(page==null) {
			page = "1";
		}
		if(rows == null) {
			rows = "6";
		}
		//2. ����Service�������õ�������
		PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows),category);
	    request.setAttribute("pageModel", pageModel);
	    //����ת������ӦJSP(view)��������չ��
	    request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request,response);
	
	
	}

}

