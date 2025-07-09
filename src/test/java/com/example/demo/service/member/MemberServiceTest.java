package com.example.demo.service.member;

import com.example.demo.dto.member.MemberDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.member.Member;
import com.example.demo.model.member.Status;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.request.member.StoreMemberRequest;
import com.example.demo.request.member.UpdateMemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberDTO memberDTO;
    private StoreMemberRequest storeMemberRequest;
    private UpdateMemberRequest updateMemberRequest;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setEmail("john@example.com");
        member.setPhone("1234567890");
        member.setMembershipDate(LocalDateTime.now());
        member.setStatus(Status.ACTIVE);
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());

        memberDTO = new MemberDTO(
            1L,
            "John Doe",
            "john@example.com",
            "1234567890",
            member.getMembershipDate(),
            Status.ACTIVE,
            member.getCreatedAt(),
            member.getUpdatedAt()
        );

        storeMemberRequest = new StoreMemberRequest(
            "John Doe",
            "john@example.com",
            "1234567890",
            Status.ACTIVE
        );

        updateMemberRequest = new UpdateMemberRequest(
            "John Updated",
            "john.updated@example.com",
            "0987654321",
            LocalDateTime.now().plusDays(1),
            Status.INACTIVE
        );
    }

    @Test
    void getMembers_ShouldReturnAllMembers() {
        List<Member> members = Arrays.asList(member);
        when(memberRepository.findAll()).thenReturn(members);

        List<MemberDTO> result = memberService.getMembers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(memberDTO.name(), result.get(0).name());
        assertEquals(memberDTO.email(), result.get(0).email());
        assertEquals(memberDTO.status(), result.get(0).status());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMembers_ShouldReturnEmptyListWhenNoMembers() {
        when(memberRepository.findAll()).thenReturn(new ArrayList<>());

        List<MemberDTO> result = memberService.getMembers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMember_ShouldReturnMemberWhenExists() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberDTO result = memberService.getMember(1L);

        assertNotNull(result);
        assertEquals(memberDTO.name(), result.name());
        assertEquals(memberDTO.email(), result.email());
        assertEquals(memberDTO.phone(), result.phone());
        assertEquals(memberDTO.status(), result.status());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void getMember_ShouldThrowResourceNotFoundExceptionWhenNotExists() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> memberService.getMember(1L)
        );
        assertEquals("Member with id: 1 does not exists", exception.getMessage());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void postMember_ShouldCreateMemberSuccessfully() {
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.postMember(storeMemberRequest);

        assertNotNull(result);
        assertEquals(storeMemberRequest.name(), result.name());
        assertEquals(storeMemberRequest.email(), result.email());
        assertEquals(storeMemberRequest.phone(), result.phone());
        assertEquals(storeMemberRequest.status(), result.status());
        verify(memberRepository, times(1)).existsByEmail(storeMemberRequest.email());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void postMember_ShouldThrowDuplicateEmailExceptionWhenEmailExists() {
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> memberService.postMember(storeMemberRequest)
        );
        assertEquals("Email already in use", exception.getMessage());
        verify(memberRepository, times(1)).existsByEmail(storeMemberRequest.email());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void putMember_ShouldUpdateMemberSuccessfully() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.putMember(1L, updateMemberRequest);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).existsByEmail(updateMemberRequest.email());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void putMember_ShouldThrowResourceNotFoundExceptionWhenMemberNotExists() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> memberService.putMember(1L, updateMemberRequest)
        );
        assertEquals("Member with id: 1 does not exists", exception.getMessage());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void putMember_ShouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> memberService.putMember(1L, updateMemberRequest)
        );
        assertEquals("Email already in use", exception.getMessage());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).existsByEmail(updateMemberRequest.email());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void putMember_ShouldAllowSameEmailForSameMember() {
        UpdateMemberRequest sameEmailRequest = new UpdateMemberRequest(
            "John Updated",
            "john@example.com",
            "0987654321",
            LocalDateTime.now().plusDays(1),
            Status.INACTIVE
        );
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.putMember(1L, sameEmailRequest);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, never()).existsByEmail(anyString());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void putMember_ShouldHandlePartialUpdate() {
        UpdateMemberRequest partialRequest = new UpdateMemberRequest(
            "Updated Name",
            null,
            null,
            null,
            null
        );
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.putMember(1L, partialRequest);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(memberRepository, never()).existsByEmail(anyString());
    }

    @Test
    void deleteMember_ShouldDeleteMemberSuccessfully() {
        when(memberRepository.existsById(1L)).thenReturn(true);

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).existsById(1L);
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMember_ShouldThrowResourceNotFoundExceptionWhenMemberNotExists() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> memberService.deleteMember(1L)
        );
        assertEquals("Member with id: 1 does not exists", exception.getMessage());
        verify(memberRepository, times(1)).existsById(1L);
        verify(memberRepository, never()).deleteById(anyLong());
    }

    @Test
    void postMember_ShouldCreateActiveMember() {
        StoreMemberRequest activeRequest = new StoreMemberRequest(
            "Active Member",
            "active@example.com",
            "1111111111",
            Status.ACTIVE
        );
        Member activeMember = new Member();
        activeMember.setId(1L);
        activeMember.setName("Active Member");
        activeMember.setEmail("active@example.com");
        activeMember.setPhone("1111111111");
        activeMember.setStatus(Status.ACTIVE);
        activeMember.setCreatedAt(LocalDateTime.now());
        activeMember.setUpdatedAt(LocalDateTime.now());
        
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(activeMember);

        MemberDTO result = memberService.postMember(activeRequest);

        assertNotNull(result);
        assertEquals(activeRequest.name(), result.name());
        assertEquals(activeRequest.status(), result.status());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void postMember_ShouldCreateInactiveMember() {
        StoreMemberRequest inactiveRequest = new StoreMemberRequest(
            "Inactive Member",
            "inactive@example.com",
            "2222222222",
            Status.INACTIVE
        );
        Member inactiveMember = new Member();
        inactiveMember.setId(2L);
        inactiveMember.setName("Inactive Member");
        inactiveMember.setEmail("inactive@example.com");
        inactiveMember.setPhone("2222222222");
        inactiveMember.setStatus(Status.INACTIVE);
        inactiveMember.setCreatedAt(LocalDateTime.now());
        inactiveMember.setUpdatedAt(LocalDateTime.now());

        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(inactiveMember);

        MemberDTO result = memberService.postMember(inactiveRequest);

        assertNotNull(result);
        assertEquals(inactiveRequest.name(), result.name());
        assertEquals(inactiveRequest.status(), result.status());
        assertEquals(Status.INACTIVE, result.status());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void putMember_ShouldUpdateMemberStatus() {
        UpdateMemberRequest statusUpdateRequest = new UpdateMemberRequest(
            null,
            null,
            null,
            null,
            Status.INACTIVE
        );
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.putMember(1L, statusUpdateRequest);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void putMember_ShouldUpdateMembershipDate() {
        LocalDateTime newMembershipDate = LocalDateTime.now().plusDays(30);
        UpdateMemberRequest dateUpdateRequest = new UpdateMemberRequest(
            null,
            null,
            null,
            newMembershipDate,
            null
        );
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO result = memberService.putMember(1L, dateUpdateRequest);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
