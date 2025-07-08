package com.example.demo.controller.member;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.member.MemberDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
