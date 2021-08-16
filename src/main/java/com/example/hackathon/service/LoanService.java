package com.example.hackathon.service;

import com.example.hackathon.dto.LoanDto;
import com.example.hackathon.repository.LoanModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanService {
    private final LoanModelRepository loanModelRepository;

    public boolean apply(LoanDto loanDto) {    // 대출 신청
        try {
            if (loanDto.isAgency() == true) {
                loanDto.setBusinessNumber(null);
                loanDto.setBusinessName(null);
            } else {
                loanDto.setCompanyName(null);
                loanDto.setCompanyPayday(null);
            }
            loanModelRepository.save(loanDto.toEntity());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean relatedAgencyCheck(LoanDto loanDto) {
        if (loanDto.isAgency() == true) {
            if (loanDto.getBusinessNumber() == null | loanDto.getBusinessName() == null) {
                return false;
            }
        } else {
            if (loanDto.getCompanyName() == null | loanDto.getCompanyPayday() == null) {
                return false;
            }
        }
        return true;
    }

    public LoanDto lookUp(String userId) {       // 대출 신청 한거 확인  -> but, 나중에 없애야 할 듯
        LoanDto loanDto = new LoanDto(loanModelRepository.findByUserId(userId));
        return loanDto;
    }

    public Long predict(LoanDto loanDto) {  // 예상 대출 가능 금액
        return 5000000L;
    }
}
