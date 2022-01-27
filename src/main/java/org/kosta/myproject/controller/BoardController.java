package org.kosta.myproject.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kosta.myproject.model.domain.BoardDTO;
import org.kosta.myproject.model.domain.BoardTypeDTO;
import org.kosta.myproject.model.domain.CategoryDTO;
import org.kosta.myproject.model.domain.MemberDTO;
import org.kosta.myproject.model.domain.PagingBean;
import org.kosta.myproject.model.mapper.BoardMapper;
import org.kosta.myproject.model.mapper.CommentBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Resource
	private BoardMapper boardMapper;
	@Resource
	private CommentBoardMapper commentBoardMapper;
	@Resource
	private PagingBean pagingBean;

	@Autowired
	public BoardController(BoardMapper boardMapper, CommentBoardMapper commentBoardMapper, 
			PagingBean pagingBean) {
		super();
		this.boardMapper = boardMapper;
		this.commentBoardMapper = commentBoardMapper;
		this.pagingBean = pagingBean;
	}

	@RequestMapping("/write/{boardId}/{categoryId}") 
	public String boardWriteForm(Authentication authentication, Model model, @PathVariable("boardId") int boardId,
			@PathVariable("categoryId") int categoryId) {
		model.addAttribute("boardId", boardId);
		model.addAttribute("categoryId", categoryId);
		MemberDTO userDetails = (MemberDTO) authentication.getPrincipal();
		model.addAttribute("boardname", boardMapper.getBoardName(boardId));
		model.addAttribute("categoryname", boardMapper.getCatName(categoryId));

		String nickname = userDetails.getNickname();
		System.out.println("작성화면으로 들어감!");
		model.addAttribute("nick", nickname);
		return "board/board-write.tiles2";
	}

	@RequestMapping("/writepro/{boardId}/{categoryId}")
	public String boardWrite(HttpSession session, Authentication authentication, BoardDTO boardDTO, Model model,  MultipartFile file,
			@PathVariable("boardId") int boardId, @PathVariable("categoryId") int categoryId)
			throws IllegalStateException, IOException {

		MemberDTO memberDTO = new MemberDTO();
		memberDTO = (MemberDTO) authentication.getPrincipal();
		boardDTO.setMemberDTO(memberDTO);
		model.addAttribute(memberDTO);

		BoardTypeDTO boardtypeDTO = new BoardTypeDTO();
		boardtypeDTO.setBoardId(boardId);
		boardDTO.setBoardTypeDTO(boardtypeDTO);
		model.addAttribute(boardtypeDTO);

		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(categoryId);
		boardDTO.setCategoryDTO(categoryDTO);
		model.addAttribute(categoryDTO);
		System.out.println("***********************");
		System.out.println("파일이 있으면 false >> " + file.isEmpty());/
		if(file.isEmpty()==false) {

		String projectPath = session.getServletContext().getRealPath("/")+"newfiles";
		System.out.println(projectPath);//C:\kosta224\study\FINAL_PROJECT\GIT_FINAL\pawPadSmell.net\src\main\webapp\

		UUID uuid = UUID.randomUUID();

		System.out.println(file.getOriginalFilename());//dog1.jpg
		String fileName = uuid + "_" + file.getOriginalFilename();

		File saveFile = new File(projectPath, fileName);

		file.transferTo(saveFile); 

		boardDTO.setFilename(fileName);
		boardDTO.setFilepath("/files/" + fileName);
		}else {
			boardDTO.setFilename("");
			boardDTO.setFilepath("");
		}
		boardMapper.boardWrite(boardDTO);

		return "redirect:/board/list/{boardId}/{categoryId}";
	}



	@GetMapping("/modify/{postId}/{boardId}/{categoryId}") 
	public String boardModify(@PathVariable("postId") int postId, @PathVariable("boardId") int boardId,
			@PathVariable("categoryId") int categoryId, Model model, Authentication authentication) {
		model.addAttribute("boardId", boardId);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("postId", postId);

		MemberDTO userDetails = (MemberDTO) authentication.getPrincipal();
		model.addAttribute("boardname", boardMapper.getBoardName(boardId));
		model.addAttribute("categoryname", boardMapper.getCatName(categoryId));

		String nickname = userDetails.getNickname();
		System.out.println("게시물 수정 페이지로 이동!");
		model.addAttribute("nick", nickname);


		model.addAttribute("boardDTO", boardMapper.getpostDetail(postId)); 
		return "board/board-modify.tiles2";
	}

	@PostMapping("/update/{postId}/{boardId}/{categoryId}")
	public String boardUpdate( HttpSession session, @PathVariable("postId") int postId, @PathVariable("boardId") int boardId, @PathVariable("categoryId") int categoryId, Model model, BoardDTO boardDTO,Authentication authentication, MultipartFile file) throws IllegalStateException, IOException {// 여기 boardDTO에 새로 입력한 내용을 받아옴.
		
		
		if(file.isEmpty()==false) {


			
		String projectPath = session.getServletContext().getRealPath("/")+"newfiles";
		UUID uuid = UUID.randomUUID();

		String fileName = uuid + "_" + file.getOriginalFilename();

		File saveFile = new File(projectPath, fileName);

		file.transferTo(saveFile);

	
		boardDTO.setFilename(fileName);
		boardDTO.setFilepath("/files/" + fileName);

		}else {
			boardDTO.setFilename("");
			boardDTO.setFilepath("");
		}
		

		
		boardMapper.boardUpdate(boardDTO);
		//return "redirect:/board/goDetail/{postId}";
		return "redirect:/board/{postId}";
		

	}


	
	@RequestMapping("/storewrite/{boardId}/{categoryId}") 
	public String storeWriteForm(Authentication authentication, Model model, @PathVariable("boardId") int boardId,
			@PathVariable("categoryId") int categoryId) {
		model.addAttribute("boardId", boardId);
		model.addAttribute("categoryId", categoryId);
		MemberDTO userDetails = (MemberDTO) authentication.getPrincipal();
		model.addAttribute("boardname", boardMapper.getBoardName(boardId));
		model.addAttribute("categoryname", boardMapper.getCatName(categoryId));

		String nickname = userDetails.getNickname();
		System.out.println("중고거래 작성화면으로 들어감!");
		model.addAttribute("nick", nickname);
		return "board/storeboard-write.tiles2";
	}

	@RequestMapping("/storewritepro/{boardId}/{categoryId}")
	public String storeboardWrite(HttpSession session, Authentication authentication, BoardDTO boardDTO, Model model, MultipartFile file,
			@PathVariable("boardId") int boardId, @PathVariable("categoryId") int categoryId)
			throws IllegalStateException, IOException {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO = (MemberDTO) authentication.getPrincipal();
		boardDTO.setMemberDTO(memberDTO);
		model.addAttribute(memberDTO);

		BoardTypeDTO boardtypeDTO = new BoardTypeDTO();
		boardtypeDTO.setBoardId(boardId);
		boardDTO.setBoardTypeDTO(boardtypeDTO);
		model.addAttribute(boardtypeDTO);

		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(categoryId);
		boardDTO.setCategoryDTO(categoryDTO);
		model.addAttribute(categoryDTO);

		String projectPath = session.getServletContext().getRealPath("/")+"newfiles";

		UUID uuid = UUID.randomUUID();

		String fileName = uuid + "_" + file.getOriginalFilename();

		File saveFile = new File(projectPath, fileName);

		file.transferTo(saveFile); 

		boardDTO.setFilename(fileName);
		boardDTO.setFilepath("/files/" + fileName);

		boardMapper.storeboardWrite(boardDTO);

		return "redirect:/board/list/{boardId}/{categoryId}";
	}
	
	
	
	
	@GetMapping("/list/{boardId}/{categoryId}")
	public String getAllLists(Model model, @PathVariable("boardId") int boardId,
			@PathVariable("categoryId") int categoryId, String pageNo) {
			
		int totalPostCount = boardMapper.getCategoryCount(boardId, categoryId);
		PagingBean pagingBean = null;

		if (pageNo == null) {

			pagingBean = new PagingBean(totalPostCount);
		} else {

			pagingBean = new PagingBean(totalPostCount, Integer.parseInt(pageNo));
		}

		model.addAttribute("pagingBean", pagingBean);

		System.out.println(boardMapper.getBoardName(boardId));
		model.addAttribute("boardId", boardId);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("boardname", boardMapper.getBoardName(boardId));
		model.addAttribute("categoryname", boardMapper.getCatName(categoryId));
		model.addAttribute("list", boardMapper.getAllLists(boardId, categoryId, pagingBean.getStartRowNumber(),
				pagingBean.getEndRowNumber()));



		if(boardId==1)
			return "board/board-list.tiles2";
		else
			return "board/storeboard-list.tiles2";

	}

	@RequestMapping(value = "/{postId}")
	public String getPostDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Authentication authentication, Model model, @PathVariable int postId) {
		

//		
		Cookie cookie = new Cookie("postId", Integer.toString(postId));
		response.addCookie(cookie);
		Cookie[] cookies = request.getCookies();
		int visitor = 0;


		for (Cookie cookie1 : cookies) {
			if (cookie1.getName().equals("visit")) {
				visitor = 1;

				if (cookie1.getValue().contains(Integer.toString(postId))) {

				} else {
					cookie1.setValue(cookie1.getValue() + "_" + Integer.toString(postId));
					response.addCookie(cookie1);
					boardMapper.hitsUpdate(postId);


				} 

			}
		}

		if (visitor == 0) {
			Cookie cookie1 = new Cookie("visit", request.getParameter(Integer.toString(postId)));
			response.addCookie(cookie1);
			boardMapper.hitsUpdate(postId);
		} 

		model.addAttribute("list", boardMapper.getpostDetail(postId));
		model.addAttribute("comment", commentBoardMapper.findByComment(postId));
		MemberDTO userDetails = (MemberDTO) authentication.getPrincipal();
//		System.out.println(userDetails);
		String nickname = userDetails.getNickname();
		int memberId = userDetails.getMemberId();
		model.addAttribute("nick", nickname);

		model.addAttribute("userMemberId", memberId);
		System.out.println(nickname);
		System.out.println(boardMapper.getpostDetail(postId).memberDTO.getNickname());

		model.addAttribute("commentsCount", commentBoardMapper.getCommentCount(postId));

		
		return "board/board-detail.tiles2";

	}

	@RequestMapping("/delete/{postId}/{boardId}/{categoryId}")
	public String deletePost(Model model, HttpServletRequest request, @PathVariable("postId") int postId,
			@PathVariable("boardId") int boardId, @PathVariable("categoryId") int categoryId) {
		boardMapper.deletePost(postId);
		model.addAttribute("board", boardMapper.getBoardName(boardId));
		model.addAttribute("category", boardMapper.getCatName(categoryId));
		model.addAttribute("list", boardMapper.getAllLists(boardId, categoryId, pagingBean.getStartRowNumber(),
				pagingBean.getEndRowNumber()));

		return "redirect:/board/list/{boardId}/{categoryId}";
	}
	@GetMapping("/search/{boardId}/{categoryId}")
	public String search(@PathVariable("boardId") int boardId,@PathVariable("categoryId") int categoryId,String search,String pageNo,Model model) {
		
		
		int totalPostCount = boardMapper.getSearchCount(boardId, categoryId,search);
		PagingBean pagingBean = null;

		if (pageNo == null) {
			
			pagingBean = new PagingBean(totalPostCount);
		} else {
			
			pagingBean = new PagingBean(totalPostCount, Integer.parseInt(pageNo));
		}
		model.addAttribute("search",search);
		model.addAttribute("pagingBean", pagingBean);
		model.addAttribute("boardId", boardId);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("boardname", boardMapper.getBoardName(boardId));
		model.addAttribute("categoryname", boardMapper.getCatName(categoryId));
		List<BoardDTO> list =boardMapper.searchPost(categoryId,boardId,search,pagingBean.getStartRowNumber(),pagingBean.getEndRowNumber());
		System.out.println(list);
		String return1=null;
		if(list.isEmpty()) {
			return1="board/search_fail";
		}else if(boardId == 1) {
			model.addAttribute("list", list);
			return1="board/board-list.tiles2";
		}else {
			model.addAttribute("list", list);
			return1="board/storeboard-list.tiles2";
		}
		return return1;
	}
}
