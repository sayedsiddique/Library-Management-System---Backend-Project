package com.example.demo.service.member;

import com.example.demo.dto.member.MemberDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.member.Member;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.request.member.StoreMemberRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDTO> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MemberDTO postMember(StoreMemberRequest storeMemberRequest) {
        if (memberRepository.existsByEmail(storeMemberRequest.email())) {
            throw new DuplicateEmailException("Email already in use");
        }

        Member member = new Member();
        member.setName(storeMemberRequest.name());
        member.setEmail(storeMemberRequest.email());
        member.setPhone(storeMemberRequest.phone());
        member.setStatus(storeMemberRequest.status());
        return this.convertToDTO(memberRepository.save(member));
    }

    public MemberDTO getMember(Long id) {
        return memberRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Member with id: " + id + " does not exists"));
    }

    private MemberDTO convertToDTO(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getMembershipDate(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
