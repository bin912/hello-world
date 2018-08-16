package kr.or.connect.guestbook.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.guestbook.dto.Guestbook;

import static kr.or.connect.guestbook.dao.GuestbookDaoSqls.*;

@Repository
public class GuestbookDao {
	private NamedParameterJdbcTemplate jdbc;
	// jdbcTemplate은 ?로 파라미터를 바인딩했는데 알아보기 힘들어서 NamedParameter jdbcTemplate을 사용.
	// query():List<Object> , queryForObject():Object , update():int같은 내장객체를 사용하기위해서
	// 선언.
	private SimpleJdbcInsert insertAction;
	// SQL에 insert기능을 수행하는 스프링의 제공객체
	// tableName:String , execute():void 객체를 갖고있다.
	private RowMapper<Guestbook> rowMapper = BeanPropertyRowMapper.newInstance(Guestbook.class);
	// BeanPropertyRowMapper객체를 이용해서 자동으로 dto클래스인 Guestbook에 담아준다.

	public GuestbookDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource)
				.withTableName("guestbook").usingGeneratedKeyColumns("id");
	//dataSource를 파라미터로 받는 생성자 객체 생성.	
	}
	
	public List<Guestbook> selectAll(Integer start, Integer limit) {
		Map<String, Integer> params = new HashMap<>();
		params.put("start", start);
		params.put("limit", limit);
		return jdbc.query(SELECT_PAGING, params, rowMapper);
	//SELECT_PAGING:SQL문을 Sqls에서 불러오기 위해 import static kr.or.co.henry.board.dao.GuestbookDaoSqls.*;
	}

	public Long insert(Guestbook guestbook) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(guestbook);
		//guestbook을 가져와 맵 형태로 바꾼다.
		return insertAction.executeAndReturnKey(params).longValue();
		//SQL문을 내부적으로 실행하고 자동생성된 id값을 리턴한다.
	}

	public int deleteById(Long id) {
		Map<String, ?> params = Collections.singletonMap("id", id);
		//BeanPropertySqlParameterSource를 사용해도 되지만 
		//값을 한개만 넣을 때, singletonMap을 이용해서 맵객체를 생성해도된다.
		return jdbc.update(DELETE_BY_ID, params);
		//NamedParameterJdbcTemplate의 update메서드 실행.
	}

	public int selectCount() {
		return jdbc.queryForObject(SELECT_COUNT, Collections.emptyMap(), Integer.class);
	}
}