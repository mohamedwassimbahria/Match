package com.example.micromatch;

import com.example.micromatch.repository.MatchRepository;
import com.example.micromatch.repository.PlanificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MicroMatchApplicationTests {

	@MockBean
	private MatchRepository matchRepository;

	@MockBean
	private PlanificationRepository planificationRepository;

	@Test
	void contextLoads() {
	}

}
