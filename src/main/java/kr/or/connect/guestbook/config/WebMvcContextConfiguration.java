package kr.or.connect.guestbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration // 설정파일임을 알림.
@EnableWebMvc // 기본설정을 자동으로 지정해줌.
@ComponentScan(basePackages = { "kr.or.connect.guestbook.controller" }) // 컨트롤러를 읽어옴.
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter {

// 모든 요청을 관련 위치에서 처리하게끔 관리해준다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
	}

// defaut servlet handler를 사용하게 해줌.
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
//매핑정보가 없는 url매핑을 처리해준다.스프링 defaultServletHttpRequestHandle이 처리.
//WAS의 Default Servlet이 Static한 자원을 읽어서 보여준다.
	}

// 특정 url에 대한 처리를 controller 클래스를 작성하지 않고 매핑할수 있게 해줌.
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		System.out.println("addViewControllers가 호출됩니다. ");
		registry.addViewController("/").setViewName("index");// view name은 view resolver를 이용해서 찾는다.
	}

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
// resolver에 url정보를 수정한다. setViewName과 합쳐져 /WEB-INF/views/index.jsp라는 주소를 갖게된다.
		return resolver;
	}

}