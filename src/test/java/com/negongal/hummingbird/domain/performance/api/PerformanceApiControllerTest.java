package com.negongal.hummingbird.domain.performance.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negongal.hummingbird.docs.WithCustomMockUser;
import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.domain.notification.application.NotificationService;
import com.negongal.hummingbird.domain.performance.application.PerformanceHeartService;
import com.negongal.hummingbird.domain.performance.application.PerformanceService;
import com.negongal.hummingbird.domain.performance.application.TicketingService;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformancePageDto;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;

@WebMvcTest(controllers = PerformanceApiController.class)
@AutoConfigureRestDocs
class PerformanceApiControllerTest {
	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected PerformanceService performanceService;
	@MockBean
	protected PerformanceHeartService performanceHeartService;
	@MockBean
	protected TicketingService ticketService;
	@MockBean
	protected ChatRoomService chatRoomService;
	@MockBean
	protected S3Uploader uploader;
	@MockBean
	protected NotificationService notificationService;

	@MockBean
	protected JwtProvider tokenProvider;

	@DisplayName("공연 전체 리스트를 조회한다.")
	@Test
	@WithCustomMockUser
	void getPerformances() throws Exception {
		// given
		List<PerformanceDto> performanceDtos = new ArrayList<>();
		performanceDtos.add(PerformanceDto.builder()
			.id(1L)
			.name("공연 1")
			.artistName("샘 스미스")
			.photo("URL")
			.date(LocalDateTime.now())
			.build());
		PerformancePageDto response = PerformancePageDto.builder()
			.performanceDtos(performanceDtos)
			.totalPages(1)
			.totalElements(1L)
			.isLast(true)
			.currPage(0)
			.build();

		when(performanceService.findAllPerformance(any())).thenReturn(response);

		// when // then
		mockMvc.perform(get("/performance")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "date,asc"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("SUCCESS"))
			.andDo(document("performance-select",
				responseFields(
					fieldWithPath("status").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("data.total_pages").type(JsonFieldType.NUMBER)
						.description("전체 페이지 개수"),
					fieldWithPath("data.total_elements").type(JsonFieldType.NUMBER)
						.description("전체 데이터 개수"),
					fieldWithPath("data.curr_page").type(JsonFieldType.NUMBER)
						.description("현재 페이지 번호"),
					fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
						.description("마지막 페이지인지 여부"),
					fieldWithPath("data.performance_list").type(JsonFieldType.ARRAY)
						.description("공연 리스트"),
					fieldWithPath("data.performance_list[].name").type(JsonFieldType.STRING)
						.description("공연 이름"),
					fieldWithPath("data.performance_list[].artist_name").type(JsonFieldType.STRING)
						.description("가수 이름"),
					fieldWithPath("data.performance_list[].photo").type(JsonFieldType.STRING)
						.description("사진 URL"),
					fieldWithPath("data.performance_list[].date").type(JsonFieldType.STRING)
						.description("공연 날짜"),
					fieldWithPath("data.performance_list[].performance_id").type(JsonFieldType.NUMBER)
						.description("공연 ID")
				)
			));
	}
}
