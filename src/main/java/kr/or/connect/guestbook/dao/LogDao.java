package kr.or.connect.guestbook.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.guestbook.dto.Log;

@Repository
public class LogDao {
	private NamedParameterJdbcTemplate jdbc;
	//jdbcTemplate은 ?로 파라미터를 바인딩했는데 알아보기 힘들어서 NamedParameter jdbcTemplate을 사용.
	//query():List<Object> , queryForObject():Object , update():int같은 내장객체를 사용하기위해서 선언.
	private SimpleJdbcInsert insertAction;
	//SQL에 insert기능을 수행하는 스프링의 제공객체
	//tableName:String , execute():void 객체를 갖고있다.

	public LogDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource)
				.withTableName("log")//sql log테이블 사용.
				.usingGeneratedKeyColumns("id");//id컬럼의 값 자동생성.
	}

	public Long insert(Log log) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(log);
		//log 객체의 값을 map형태로 변환.
		return insertAction.executeAndReturnKey(params).longValue();
		//SQL문을 내부적으로 실행하고 자동생성된 id값을 리턴한다.
	}
}