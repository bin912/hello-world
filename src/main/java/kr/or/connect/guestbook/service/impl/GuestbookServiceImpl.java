package kr.or.connect.guestbook.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.guestbook.dao.GuestbookDao;
import kr.or.connect.guestbook.dao.LogDao;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.dto.Log;
import kr.or.connect.guestbook.service.GuestbookService;

@Service
public class GuestbookServiceImpl implements GuestbookService{
	//GuestbookService 인터페이스를 구현, 메서드들을 오버라이드한다.
	@Autowired//아래 빈들을 자동 등록.
	GuestbookDao guestbookDao;
	
	@Autowired
	LogDao logDao;

	@Override
	@Transactional//read only인 경우 해당 어노테이션 지정.
	public List<Guestbook> getGuestbooks(Integer start) {
		List<Guestbook> list = guestbookDao.selectAll(start, LIMIT);
		//LIMIT은 인터페이스에서 상수로 지정되어있음 : 5
		return list;
	} 

	@Override
	@Transactional(readOnly=false)
	public int deleteGuestbook(Long id, String ip) {
		
		int deleteCount = guestbookDao.deleteById(id);
		//guestbook 테이블에서 id에 맞는 데이터 삭제.
		
		Log log = new Log();
		log.setIp(ip);
		log.setMethod("delete");
		log.setRegdate(new Date());
		logDao.insert(log);
		//log 테이블에 해당 id값에 맞는 데이터를 추가.
		return deleteCount;
	}

	@Override
	@Transactional(readOnly=false)
	public Guestbook addGuestbook(Guestbook guestbook, String ip) {
		//클라이언트에서 컨트롤러를 통해 입력받을 guestbook 객체, ip를 받아온다.
		guestbook.setRegdate(new Date());
		Long id = guestbookDao.insert(guestbook);
		//데이터베이스에 저장.
		
		guestbook.setId(id);
		//id값은 자동생성되기때문에 로컬에 다시 불러와 저장.
		Log log = new Log();
		log.setIp(ip);
		log.setMethod("insert");
		log.setRegdate(new Date());
		logDao.insert(log);
		//log 테이블에 해당 id값에 맞는 데이터를 추가.
		
		return guestbook;
		//id값이 채워진 guestbook을 리턴
	}

	@Override
	public int getCount() {
		return guestbookDao.selectCount();
	}
}