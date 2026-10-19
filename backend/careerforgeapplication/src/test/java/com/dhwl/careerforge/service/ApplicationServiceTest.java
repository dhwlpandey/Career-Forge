package com.dhwl.careerforge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.dhwl.careerforge.dto.ApplicationCreateRequest;
import com.dhwl.careerforge.dto.ApplicationStatusUpdateRequest;
import com.dhwl.careerforge.dto.ApplicationUpdateRequest;
import com.dhwl.careerforge.entity.Application;
import com.dhwl.careerforge.entity.ApplicationStatus;
import com.dhwl.careerforge.entity.Job;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.ApplicationAlreadyExistsException;
import com.dhwl.careerforge.exception.ApplicationNotFoundException;
import com.dhwl.careerforge.exception.InvalidApplicationStateException;
import com.dhwl.careerforge.exception.JobNotFoundException;
import com.dhwl.careerforge.repository.ApplicationRepository;
import com.dhwl.careerforge.repository.JobRepository;
import com.dhwl.careerforge.security.AuthenticatedUserService;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock 
    private ApplicationRepository applicationRepository;

    @Mock 
    private JobRepository jobRepository;

    @Mock 
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks 
    private ApplicationService applicationService;

    private User user;
    private Job job;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        job = new Job();
        job.setId(10L);
        job.setTitle("Java Developer");
        job.setCompanyName("Example Company");
        job.setActive(true);
    }

    @Test
    void applyToJob_shouldCreateApplication_whenJobIsAvailable()
            throws Exception {

        ApplicationCreateRequest request = new ApplicationCreateRequest();
        request.setCoverLetter("I am interested in this role.");
        request.setResumeUrl("https://example.com/resume.pdf");
        request.setNotes("Test application");

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(applicationRepository.existsByUserIdAndJobId(1L, 10L))
                .thenReturn(false);

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = applicationService.applyToJob(10L, request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(10L, response.getJobId());
        assertEquals(ApplicationStatus.APPLIED, response.getStatus());
        assertEquals("Java Developer", response.getJobTitle());

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void applyToJob_shouldRejectDuplicateApplication() {
        ApplicationCreateRequest request = new ApplicationCreateRequest();

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));
        when(applicationRepository.existsByUserIdAndJobId(1L, 10L))
                .thenReturn(true);

        assertThrows(
                ApplicationAlreadyExistsException.class,
                () -> applicationService.applyToJob(10L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
    }
    
    @Test
        void applyToJob_shouldThrowException_whenJobDoesNotExist() {
        ApplicationCreateRequest request = new ApplicationCreateRequest();

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(jobRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                JobNotFoundException.class,
                () -> applicationService.applyToJob(999L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void applyToJob_shouldRejectInactiveJob() {
        ApplicationCreateRequest request = new ApplicationCreateRequest();

        job.setActive(false);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        assertThrows(
                JobNotFoundException.class,
                () -> applicationService.applyToJob(10L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void getMyApplicationById_shouldThrowException_whenApplicationIsNotOwnedByUser() {
        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> applicationService.getMyApplicationById(50L)
        );
        }

        @Test
        void updateMyApplication_shouldThrowException_whenApplicationIsNotOwnedByUser() {
        ApplicationUpdateRequest request = new ApplicationUpdateRequest();
        request.setCoverLetter("Updated cover letter");
        request.setNotes("Updated notes");

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> applicationService.updateMyApplication(50L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void withdrawMyApplication_shouldWithdrawActiveApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = applicationService.withdrawMyApplication(50L);

        assertEquals(ApplicationStatus.WITHDRAWN, response.getStatus());
        verify(applicationRepository).save(application);
        }

        @Test
        void withdrawMyApplication_shouldRejectAlreadyWithdrawnApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.WITHDRAWN);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.withdrawMyApplication(50L)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void withdrawMyApplication_shouldRejectSelectedApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.SELECTED);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.withdrawMyApplication(50L)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void withdrawMyApplication_shouldRejectRejectedApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.REJECTED);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.withdrawMyApplication(50L)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void updateApplicationStatus_shouldUpdateStatus_whenTransitionIsAllowed() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest();
        request.setStatus(ApplicationStatus.SHORTLISTED);

        when(applicationRepository.findById(50L))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = applicationService.updateApplicationStatus(50L, request);

        assertEquals(ApplicationStatus.SHORTLISTED, response.getStatus());
        verify(applicationRepository).save(application);
        }

        @Test
        void updateApplicationStatus_shouldRejectAdminWithdrawal() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest();
        request.setStatus(ApplicationStatus.WITHDRAWN);

        when(applicationRepository.findById(50L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.updateApplicationStatus(50L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void updateApplicationStatus_shouldRejectChangesToSelectedApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.SELECTED);

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest();
        request.setStatus(ApplicationStatus.INTERVIEW);

        when(applicationRepository.findById(50L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.updateApplicationStatus(50L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        void getMyApplications_shouldReturnApplicationsForCurrentUser() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Application> applicationPage =
                new PageImpl<>(List.of(application), pageable, 1);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByUserId(1L, pageable))
                .thenReturn(applicationPage);

        var response = applicationService.getMyApplications(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(1L, response.getContent().get(0).getUserId());
        assertEquals("Java Developer",
                response.getContent().get(0).getJobTitle());

        verify(applicationRepository).findByUserId(1L, pageable);
        }

        @Test
        void getMyApplications_shouldReturnEmptyPage_whenUserHasNoApplications() {
        Pageable pageable = PageRequest.of(0, 10);

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByUserId(1L, pageable))
                .thenReturn(Page.empty(pageable));

        var response = applicationService.getMyApplications(pageable);

        assertTrue(response.isEmpty());
        assertEquals(0, response.getTotalElements());
        }

        @Test
        void getAllApplications_shouldReturnPaginatedApplications() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Application> applicationPage =
                new PageImpl<>(List.of(application), pageable, 1);

        when(applicationRepository.findAll(pageable))
                .thenReturn(applicationPage);

        var response = applicationService.getAllApplications(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals("Java Developer",
                response.getContent().get(0).getJobTitle());

        verify(applicationRepository).findAll(pageable);
        }

        @Test
        void getMyApplicationById_shouldThrowException_whenApplicationDoesNotExist() {
        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(999L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ApplicationNotFoundException.class,
                () -> applicationService.getMyApplicationById(999L)
        );
        }

        @Test
        void updateMyApplication_shouldRejectWithdrawnApplication() {
        Application application = new Application();
        application.setId(50L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.WITHDRAWN);

        ApplicationUpdateRequest request = new ApplicationUpdateRequest();
        request.setCoverLetter("Updated cover letter");
        request.setNotes("Updated notes");

        when(authenticatedUserService.getCurrentUser()).thenReturn(user);
        when(applicationRepository.findByIdAndUserId(50L, 1L))
                .thenReturn(Optional.of(application));

        assertThrows(
                InvalidApplicationStateException.class,
                () -> applicationService.updateMyApplication(50L, request)
        );

        verify(applicationRepository, never()).save(any(Application.class));
        }
}
