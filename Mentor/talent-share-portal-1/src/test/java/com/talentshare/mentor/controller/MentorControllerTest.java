package com.talentshare.mentor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.talentshare.mentor.dto.ResponseDetails;
import com.talentshare.mentor.exception.InvalidMentorException;
import com.talentshare.mentor.exception.NoMentorsFoundException;
import com.talentshare.mentor.model.Mentor;
import com.talentshare.mentor.service.MentorService;
import com.talentshare.mentor.utils.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

public class MentorControllerTest {

    @InjectMocks
    private MentorController mentorController;

    @Mock
    private MentorService mentorService;
    
    @Mock
	private CommonUtil commonUtil;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogInMentor() {
        int username = 123;
        String password = "password";
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        ResponseDetails responseDetails = new ResponseDetails();
        when(mentorService.verifyLogInDetails(username, password, httpServletRequest))
            .thenReturn(responseDetails);

        ResponseEntity<ResponseDetails> expectedResponse =
            new ResponseEntity<>(responseDetails, HttpStatus.OK);

        ResponseEntity<ResponseDetails> actualResponse =
            mentorController.logInMentor(username, password, httpServletRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(mentorService).verifyLogInDetails(username, password, httpServletRequest);
    }

    @Test
    public void testGetMentorsListBySkill() {
        List<String> skills = Arrays.asList("Java", "Spring");
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        ResponseDetails responseDetails = new ResponseDetails();
        when(mentorService.findMentorBySkills(skills, httpServletRequest))
            .thenReturn(responseDetails);

        ResponseEntity<ResponseDetails> expectedResponse =
            new ResponseEntity<>(responseDetails, HttpStatus.OK);

        ResponseEntity<ResponseDetails> actualResponse =
            mentorController.getMentorsListBySkill(skills, httpServletRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(mentorService).findMentorBySkills(skills, httpServletRequest);
    }
    
    @Test
    public void testCreateMentorSuccess() {
       
    	Mentor mockMentor = createSampleMentor();
        
    	HttpServletRequest mockRequest = new MockHttpServletRequest();
        ((MockHttpServletRequest) mockRequest).setRequestURI("api/v1/mentor/register");

        when(mentorService.registerMentor(eq(mockMentor), any(HttpServletRequest.class)))
            .thenReturn(new ResponseDetails(201, "Mentor successfully signed up", null, LocalDateTime.now(), mockRequest.getRequestURI(), "Mentors Data"));

        ResponseEntity<ResponseDetails> responseEntity = mentorController.createMentor(mockMentor, mockRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("api/v1/mentor/register", mockRequest.getRequestURI());
        assertNotNull(responseEntity.getBody());
        assertEquals("Mentor successfully signed up", responseEntity.getBody().getMessage());

        verify(mentorService, times(1)).registerMentor(eq(mockMentor), any(HttpServletRequest.class));
    }
    
    @Test
    public void testUpdateMentorSuccess() {
        int empid = 123;

        Mentor mockMentor = createSampleMentor();
        
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ((MockHttpServletRequest) mockRequest).setRequestURI("api/v1/mentor/update/{empid}");
        when(mentorService.updateMentor(eq(empid), any(Mentor.class), any(HttpServletRequest.class)))
                .thenReturn(new ResponseDetails(HttpStatus.OK.value(), "Mentor updated", null, LocalDateTime.now(), mockRequest.getRequestURI(), "Mentors Data"));
        
        ResponseEntity<ResponseDetails> responseEntity = mentorController.updateMentor(empid, mockMentor, mockRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Mentor updated", responseEntity.getBody().getMessage());

        verify(mentorService).updateMentor(eq(empid), any(Mentor.class), any(HttpServletRequest.class));
    }

    
    private Mentor createSampleMentor() {
        Mentor mentor = new Mentor();
        mentor.setEmployeeId(123);
        mentor.setFirstName("Rachel");
        mentor.setLastName("Green");
        mentor.setEmail("Rachel.G@example.com");
        mentor.setPassword("securePassword");
        mentor.setJobTitle("Sr.Software Engineer");
        mentor.setLocation("India");
        mentor.setCompany("Sys Inc.");
        List<String> skillsList = Arrays.asList("Java", "Spring Boot");
        mentor.setSkills(skillsList);
        mentor.setBio("Experienced software engineer.");
        return mentor;
    }
    
    @Test
	public void testGetMentorById_ValidMentor() {
		
		int empid = 123;
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		Mentor mentor = new Mentor(); 
		mentor.setEmployeeId(empid);
		ResponseDetails expectedResponse = new ResponseDetails(); 

		when(mentorService.getMentorById(empid, httpServletRequest)).thenReturn(expectedResponse);

		ResponseEntity<ResponseDetails> responseEntity = mentorController.getMentorById(empid, httpServletRequest);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); 
		assertEquals(expectedResponse, responseEntity.getBody());

		verify(mentorService, times(1)).getMentorById(empid, httpServletRequest);
	}

	@Test
	public void testGetMentorById_InvalidMentor() {
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		
		int empid = 123;

		when(mentorService.getMentorById(empid, httpServletRequest)).thenThrow(InvalidMentorException.class);
		assertThrows(InvalidMentorException.class, () -> mentorService.getMentorById(empid, httpServletRequest));
		verify(mentorService, times(1)).getMentorById(empid, httpServletRequest);
		System.out.println("INVALID MENTOR");

	}

	@Test
	public void testGetAllMentorsWithMentorsFound() {
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		List<Mentor> mentors = createMockMentors(); 

		when(mentorService.getAllMentors(httpServletRequest)).thenReturn((ResponseDetails) mentors);

		ResponseEntity<ResponseDetails> responseDetails = mentorController.getAllMentors(httpServletRequest);

		assertNotNull(responseDetails);
		assertEquals(HttpStatus.OK, responseDetails.getStatusCode());

		verify(mentorService).getAllMentors(httpServletRequest);
		// verify(commonUtil).prepareResponse(eq("List Found"),
		// eq(HttpStatus.OK.value()), anyString(), eq(mentors));
		verifyNoMoreInteractions(mentorService, commonUtil);
	}

	@Test
    public void testGetAllMentorsWithNoMentorsFound() {
    		       
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    		        when(mentorService.getAllMentors(httpServletRequest)).thenThrow(NoMentorsFoundException.class);

    		        assertThrows(NoMentorsFoundException.class, () -> mentorController.getAllMentors(httpServletRequest));

    		        verify(mentorService).getAllMentors(httpServletRequest);
    		        verifyNoMoreInteractions(mentorService, commonUtil);
    		    }

	private List<Mentor> createMockMentors() {
		
		return null;
	}

	@Test
	public void testDeleteMentor() {
		int empId = 161; 
		HttpServletRequest request = mock(HttpServletRequest.class);

		ResponseDetails responseDetails = new ResponseDetails();
		when(mentorService.deleteMentor(eq(empId), any(HttpServletRequest.class))).thenReturn(responseDetails);

		ResponseEntity<ResponseDetails> responseEntity = mentorController.deleteMentor(empId, request);

		verify(mentorService).deleteMentor(eq(empId), any(HttpServletRequest.class));

		assert (responseEntity.getStatusCode() == HttpStatus.OK);
	}


}

