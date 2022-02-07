package com.hcl.services.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankServicesApplicationTests {
	
	//@Mock
	//ConfigurableApplicationContext context;

	@Test
	void contextLoads() {
		System.out.println("Test Done");
	}
	
//	@BeforeEach
//	void setUp() throws Exception {
//		context = new ClassPathXmlApplicationContext("pathtoxml.xml");
//	}
//
//	@Test
//	final void testMain() {
//		when(SpringApplication.run(BankServicesApplication.class, new String[] {})).thenReturn(context);
//		BankServicesApplication.main(new String[] {});
//	}
}
