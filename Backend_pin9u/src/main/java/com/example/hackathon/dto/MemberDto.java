package com.example.hackathon.dto;

import com.example.hackathon.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotBlank
    @Pattern(regexp = "^[가-힣]*$")
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{2}([0]\\d|[1][0-2])([0][1-9]|[1-2]\\d|[3][0-1])[-]*[1-4]\\d{6}")
    private String socialNumber;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String mobileNumber;

    @NotBlank
    @Pattern(regexp = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$")
    private String email;

    @NotBlank
    @Size(min = 4, max = 15)
    private String password;

    @NotBlank
    private String account;

    private int balance = 0;

    public Member toEntity() {   // dto -> entity
        return Member.builder()
                .seq(null)
                .name(name)
                .socialNumber(socialNumber)
                .mobileNumber(mobileNumber)
                .userEmail(email)
                .userPw(password)
                .account(account)
                .balance(1000000)
                .build();
    }

    public MemberDto(Member member) {   // entity -> dto
        this.name = member.getName();
        this.socialNumber = member.getSocialNumber();
        this.mobileNumber = member.getMobileNumber();
        this.email = member.getUserEmail();
        this.password = member.getUserPw();
        this.account = member.getAccount();
        this.balance = member.getBalance();
    }

}
