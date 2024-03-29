package net.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		
		String realFolder = "";
		String saveFolder = "/boardupload";
		
		int fileSize = 5 * 1024 * 1024;
		
		/* realFolder = request.getRealPath(saveFolder); */
		
		realFolder = request.getSession().getServletContext().getRealPath(saveFolder);
							// session주소값. 서버 웹어플리케이션 경로. saveFolder 주소값  (= 절대경로)
							// boardupload 경로 찾아줌
		
		System.out.println(realFolder);
		boolean result = false;
		
		try {
			MultipartRequest multi = null;
			
			multi = new MultipartRequest(request, realFolder, fileSize, "UTF-8", new DefaultFileRenamePolicy());
									//  서블릿 요청, 파일저장할 dir, 최대크기, 인코딩, 중복 금지
			
			// multipart/form-data로 선언하고 제출한 데이터들은 request가 아닌 multipartRequest로 받아와야 한다.
			
			boarddata.setBOARD_ID(multi.getParameter("BOARD_ID")); 
			boarddata.setBOARD_SUBJECT(multi.getParameter("BOARD_SUBJECT"));
			boarddata.setBOARD_CONTENT(multi.getParameter("BOARD_CONTENT"));
			boarddata.setBOARD_FILE(multi.getFilesystemName((String)multi.getFileNames().nextElement()));
			
			result = boarddao.boardInsert(boarddata);
			
			
			if (result == false) {
				System.out.println("게시판 등록 실패");
				return null;
			}
			System.out.println("게시판 등록 완료");
			
			forward.setRedirect(true);
			forward.setPath("./BoardList.bo");
			return forward;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
