package com.example.demo.controller.member;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.member.MemberDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.request.author.StoreAuthorRequest;
import com.example.demo.request.member.StoreMemberRequest;
import com.example.demo.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getMembers() {
        List<MemberDTO> members = memberService.getMembers();
        ApiResponse<List<MemberDTO>> response = new ApiResponse<>("Members retrieved successfully", "success", HttpStatus.OK.value(), members);
        return new ResponseEntity<ApiResponse<List<MemberDTO>>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<MemberDTO>> getMember(@PathVariable("id")Long id) {
        MemberDTO member = memberService.getMember(id);
        ApiResponse<MemberDTO> response = new ApiResponse<>("Member retrieved successfully", "success", HttpStatus.OK.value(), member);
        return new ResponseEntity<ApiResponse<MemberDTO>>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<MemberDTO>> postMember(@Valid @RequestBody StoreMemberRequest storeMemberRequest) {
        MemberDTO member = memberService.postMember(storeMemberRequest);
        ApiResponse<MemberDTO> response = new ApiResponse<>("Member created successfully", "success", HttpStatus.CREATED.value(), member);
        return new ResponseEntity<ApiResponse<MemberDTO>>(response, HttpStatus.CREATED);
    }
}
