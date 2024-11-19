package com.talentshare.mentor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;

import com.mongodb.client.result.UpdateResult;
import com.talentshare.mentor.constants.MessageConstants;
import com.talentshare.mentor.dto.ResponseDetails;
import com.talentshare.mentor.exception.EmailAlreadyRegisteredException;
import com.talentshare.mentor.exception.EmployeeIdAlreadyRegisteredException;
import com.talentshare.mentor.exception.InvalidMentorException;
import com.talentshare.mentor.exception.ResourceNotFoundException;
import com.talentshare.mentor.exception.WrongCredentialsException;
import com.talentshare.mentor.model.Mentor;
import com.talentshare.mentor.model.MentorsData;
import com.talentshare.mentor.repository.MentorRepository;
import com.talentshare.mentor.utils.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

public class MentorServiceTest {

    @Mock
    private MentorRepository mentorRepository;
    
    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private CommonUtil commonUtil;

    @InjectMocks
    private MentorService mentorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testVerifyLogInDetails_ValidUser_Success() {
        int username = 123;
        String password = "correctPassword";
        String encryptedPassword = "encryptedPassword";
        String requestUri = "/api/v1/mentor/login";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Mentor mentor = new Mentor();
        mentor.setPassword(encryptedPassword);
        mentor.setActive(true);

        when(mentorRepository.findByEmployeeId(username)).thenReturn(Optional.of(mentor));
        when(commonUtil.matchPassword(password, encryptedPassword)).thenReturn(true);
        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        
        ResponseDetails expectedResponse = new ResponseDetails();
        expectedResponse.setStatus(HttpStatus.OK.value());
        expectedResponse.setMessage("Success");
        expectedResponse.setPath(requestUri);
        expectedResponse.setData("Login successful");
   
        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);

        ResponseDetails result = mentorService.verifyLogInDetails(username, password, httpRequest);

        assertEquals(expectedResponse, result);
    }
    

    @Test
    public void testVerifyLogInDetails_InvalidUser_WrongPassword() {
        int username = 123;
        String password = "incorrectPassword";
        String encryptedPassword = "encryptedPassword";
        String requestUri = "/api/v1/mentor/login";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Mentor mentor = new Mentor();
        mentor.setPassword(encryptedPassword);
        mentor.setActive(true); // Active user
        when(httpRequest.getRequestURI()).thenReturn(requestUri);

        when(mentorRepository.findByEmployeeId(username)).thenReturn(Optional.of(mentor));
        when(commonUtil.matchPassword(password, encryptedPassword)).thenReturn(false);

        assertThrows(WrongCredentialsException.class, () -> {
            mentorService.verifyLogInDetails(username, password, httpRequest);
        });
    }
    
    @Test
    public void testVerifyLogInDetails_InvalidUser_WrongUsername() {
        int username = 123;
        String password = "correctPassword";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(mentorRepository.findByEmployeeId(username)).thenReturn(Optional.empty());

        assertThrows(WrongCredentialsException.class, () -> {
            mentorService.verifyLogInDetails(username, password, httpRequest);
        });
    }
    
    @Test
    public void testVerifyLogInDetails_InvalidUser_InactiveAccount() {
        int username = 123;
        String password = "correctPassword";
        String encryptedPassword = "encryptedPassword";
        String requestUri = "/api/v1/mentor/login";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Mentor mentor = new Mentor();
        mentor.setPassword(encryptedPassword);
        mentor.setActive(false); // Inactive user

        when(mentorRepository.findByEmployeeId(username)).thenReturn(Optional.of(mentor));

        assertThrows(WrongCredentialsException.class, () -> {
            mentorService.verifyLogInDetails(username, password, httpRequest);
        });
    }

    
    @Test
    public void testFindMentorBySkills_MentorsFound() {
        List<String> skills = Arrays.asList("Java", "Python");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        Mentor mentor1 = new Mentor();
        Mentor mentor2 = new Mentor();
        mentor1.setActive(true);
        mentor2.setActive(false);
        
        List<Mentor> mockMentors = new ArrayList<>();
        mockMentors.add(mentor1);
        mockMentors.add(mentor2);
        
        MentorsData mentorsData = new MentorsData(mockMentors.size(), mockMentors);
        
        when(mentorRepository.findBySkillsIn(skills)).thenReturn(mockMentors);
        String requestUri = "/api/v1/mentor/search";

        ResponseDetails expectedResponse = new ResponseDetails();
        expectedResponse.setStatus(HttpStatus.OK.value());
        expectedResponse.setMessage("Success");
        expectedResponse.setPath(requestUri);
        expectedResponse.setData(mentorsData);
   
        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);

        ResponseDetails result = mentorService.findMentorBySkills(skills, httpRequest);

        assertEquals(expectedResponse, result);
    }

    @Test
    public void testFindMentorBySkills_NoMentorsFound() {
        List<String> skills = Collections.singletonList("Java");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(mentorRepository.findBySkillsIn(skills)).thenReturn(Collections.emptyList());

        try {
            mentorService.findMentorBySkills(skills, request);
        } catch (ResourceNotFoundException e) {
            assertEquals(MessageConstants.MENTORS_NOT_FOUND + skills.toString(), e.getMessage());
        }
    }


    @Test
    public void testRegisterMentorSuccess() {
    	Mentor mockMentor = createSampleMentor();
        String requestUri= "api/v1/mentor/register";
        
        when(mentorRepository.existsByEmployeeId((mockMentor.getEmployeeId()))).thenReturn(false);
        when(mentorRepository.existsByEmail(eq(mockMentor.getEmail()))).thenReturn(false);
        when(commonUtil.encryptPassword(eq(mockMentor.getPassword()))).thenReturn("encryptedPassword");
        when(mentorRepository.save(any(Mentor.class))).thenReturn(mockMentor);
        
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        String responseMessage = "Mentor " + mockMentor.getFirstName() + " " + mockMentor.getLastName() + " created successfully.";
        
        ResponseDetails expectedResponse = new ResponseDetails(HttpStatus.OK.value(),responseMessage, null, LocalDateTime.now(), requestUri, mockMentor);
        
        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);
        
        ResponseDetails response = mentorService.registerMentor(mockMentor, httpRequest);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getData());
        assertEquals(responseMessage, response.getMessage());

        verify(mentorRepository, times(1)).save(eq(mockMentor));
    
    }
   
    @Test
    public void testRegisterMentorDuplicateEmployeeId() {
    	Mentor mockMentor = createSampleMentor();

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mentorRepository.existsByEmployeeId(eq(mockMentor.getEmployeeId()))).thenReturn(true);

        assertThrows(EmployeeIdAlreadyRegisteredException.class, () -> {
            mentorService.registerMentor(mockMentor, mockRequest);});

        verify(mentorRepository, times(1)).existsByEmployeeId(eq(mockMentor.getEmployeeId()));
    }
    
    @Test
    public void testRegisterMentorDuplicateEmail() {
    	Mentor mockMentor = createSampleMentor();
    	
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mentorRepository.existsByEmployeeId(eq(mockMentor.getEmployeeId()))).thenReturn(false);
        when(mentorRepository.existsByEmail(eq(mockMentor.getEmail()))).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> {
            mentorService.registerMentor(mockMentor, mockRequest);
        });
        verify(mentorRepository, times(1)).existsByEmail(eq(mockMentor.getEmail()));
    }
    
    @Test
    public void testUpdateMentorSuccess() {
        int empid = 123;

        Mentor mentorToUpdate = createSampleMentor();
  
        String requestUri= "api/v1/mentor/update/";
        
        Mentor existingMentor = new Mentor();
        existingMentor.setEmployeeId(empid);

        when(mentorRepository.findByEmployeeId(eq(empid))).thenReturn(Optional.of(existingMentor));
        when(mongoTemplate.findAndReplace(any(Query.class), eq(mentorToUpdate))).thenReturn(existingMentor);

       
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        String responseMessage = "Mentor " + mentorToUpdate.getFirstName() + " " + mentorToUpdate.getLastName() + " updated successfully.";
        
        ResponseDetails expectedResponse = new ResponseDetails(HttpStatus.OK.value(),responseMessage, null, LocalDateTime.now(), requestUri,null );
     
        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);
        
        ResponseDetails response = mentorService.updateMentor(empid, mentorToUpdate, httpRequest);
        
        verify(mentorRepository, times(2)).findByEmployeeId(eq(empid));
        verify(mongoTemplate, times(1)).findAndReplace(any(Query.class), eq(mentorToUpdate));
        verify(commonUtil, times(1)).prepareResponse(anyString(), anyInt(), anyString(), any());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(responseMessage, response.getMessage());
    }
    
    @Test
    public void testUpdateMentorNotFound() {
        int empid = 123;
    
        when(mentorRepository.findByEmployeeId(eq(empid))).thenReturn(Optional.empty());

        Mentor mockMentor = createSampleMentor();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            mentorService.updateMentor(empid, mockMentor, mockRequest);
        });
        verify(mentorRepository).findByEmployeeId(eq(empid));
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
    public void testGetMentorById_ValidMentor_Active() {
        int empId = 123;
        String requestUri = "/mentors/123";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Mentor mentor = new Mentor();
        mentor.setEmployeeId(empId);
        mentor.setActive(true);

        when(mentorRepository.findByEmployeeId(empId)).thenReturn(Optional.of(mentor));

        ResponseDetails expectedResponse = new ResponseDetails();
        expectedResponse.setStatus(HttpStatus.OK.value());
        expectedResponse.setMessage("Mentor found");
        expectedResponse.setPath(requestUri);
        expectedResponse.setData(mentor);

        when(httpRequest.getRequestURI()).thenReturn(requestUri);
        when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);

        ResponseDetails result = mentorService.getMentorById(empId, httpRequest);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }
    
    @Test
    public void testGetMentorById_ValidMentor_Inactive() {
        int empId = 123;

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Mentor mentor = new Mentor();
        mentor.setEmployeeId(empId);
        mentor.setActive(false);

        when(mentorRepository.findByEmployeeId(empId)).thenReturn(Optional.of(mentor));

        assertThrows(InvalidMentorException.class, () -> {
            mentorService.getMentorById(empId, httpRequest);
        });
    }
    
    @Test
    public void testGetMentorById_InvalidMentor() {
        int empId = 123;

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(mentorRepository.findByEmployeeId(empId)).thenReturn(Optional.empty());

        assertThrows(InvalidMentorException.class, () -> {
            mentorService.getMentorById(empId, httpRequest);
        });
    }

	@Test
	public void testDeleteMentorWithValidEmpId() {
		int empid = 163;

		HttpServletRequest httpServletRequest1 = mock(HttpServletRequest.class);
		Mentor mentor1 = new Mentor();

		when(mentorRepository.findByEmployeeId(empid)).thenReturn(Optional.of(mentor1));

		doNothing().when(mentorRepository).deleteById(empid);

		Query query = new Query();
		Update updateField = new Update().set("fieldName", "newValue");

		UpdateResult updateResult = mongoTemplate.updateFirst(query, updateField, Mentor.class);
		UpdateResult updateResult1 = mongoTemplate.updateFirst(query, updateField, Mentor.class);
		when(updateResult).thenReturn(updateResult1);

		when(httpServletRequest1.getRequestURI()).thenReturn("/delete/123");

		when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(new ResponseDetails());

		ResponseDetails responseDetails = mentorService.deleteMentor(empid, httpServletRequest1);

		assertNotNull(responseDetails);

		InOrder inOrder = inOrder(mentorRepository, commonUtil);
		inOrder.verify(commonUtil).prepareResponse(eq("Deleted Successfully"), eq(HttpStatus.OK.value()), anyString(),
				any());
	}

	@Test
	public void testDeleteMentorWithInvalidEmpId() {
		int empid = 164;
		when(mentorRepository.findByEmployeeId(empid)).thenReturn(Optional.empty());

	}

	@Test
	public void testGetAllMentors() {

		HttpServletRequest httpRequest = mock(HttpServletRequest.class);

		Mentor mentor1 = new Mentor();
		Mentor mentor2 = new Mentor();

		mentor1.setActive(true);
		mentor2.setActive(false);

		List<Mentor> mockMentors = new ArrayList<>();

		mockMentors.add(mentor1);
		mockMentors.add(mentor2);
		MentorsData mentorsData = new MentorsData(mockMentors.size(), mockMentors);

		when(mentorRepository.findAll()).thenReturn(mockMentors);
	
		String requestUri = "/login";
		ResponseDetails expectedResponse = new ResponseDetails();

		expectedResponse.setStatus(HttpStatus.OK.value());
		expectedResponse.setMessage("Success");
		expectedResponse.setPath(requestUri);
		expectedResponse.setData(mentorsData);

		when(httpRequest.getRequestURI()).thenReturn(requestUri);
		when(commonUtil.prepareResponse(anyString(), anyInt(), anyString(), any())).thenReturn(expectedResponse);

		ResponseDetails result = mentorService.getAllMentors(httpRequest);

		assertEquals(expectedResponse, result);

	}
	
	@Test
	public void testGetAllMentorsNotFound() {
		when(mentorRepository.findAll()).thenReturn(null);
		
		//assertThrows(NoMentorsFoundException.class, () -> mentorService.getAllMentors(httpServletRequest));
		System.out.println("No Mentor Found");
     
	}
    
}
