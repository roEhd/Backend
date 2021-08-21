package com.example.hackathon.controller;

import com.example.hackathon.dto.ApprovedLoanDto;
import com.example.hackathon.entity.ApprovedLoanModel;
import com.example.hackathon.entity.InvestModel;
import com.example.hackathon.entity.LoanModel;
import com.example.hackathon.entity.UserInfo;
import com.example.hackathon.service.ApprovedLoanService;
import com.example.hackathon.service.InvestService;
import com.example.hackathon.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageApi {
    private final InvestService investService;
    private final LoanService loanService;
    private final ApprovedLoanService approvedLoanService;

    @GetMapping("/loan")
    public String loanList(@AuthenticationPrincipal UserInfo userInfo, Model model) {
        List<LoanModel> loans = loanService
                .findUserLoans(userInfo.getSeq())
                .stream()
                .filter(loan -> loan.getPermit() == 1)
                .collect(Collectors.toList());
        model.addAttribute("loans", loans);
        return "mypage/loanList";
    }

    @GetMapping("/invest")
    public String investList(@AuthenticationPrincipal UserInfo userInfo, Model model){
        List<InvestModel> invests = investService.findAllInvested(userInfo.getSeq());
        List<ApprovedLoanModel> approvedLoanModels = approvedLoanService.findMyPageApproved(invests);
        List<LoanModel> loanModels = loanService.findMyPageLoan(approvedLoanModels);
        model.addAttribute("invests", invests);
        model.addAttribute("approveds", approvedLoanModels);
        model.addAttribute("loans", loanModels);
        // 다른거 끌고 오기
        return "mypage/investList";
    }


    @PostMapping("/repay/{id}")
    public String repay(@PathVariable("id") Long loan_id, ApprovedLoanDto approvedLoanDto,
                        HttpServletResponse response) throws IOException{

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1:모인금액 범위 안에서 상환 완료, 0:상환할 금액이 없음, -1:범위 초과해서 상환함
        int result = approvedLoanService.updateRepayment(loan_id, approvedLoanDto);
        switch(result){
            case 1:
                out.println("<script>alert('상환이 성공적으로 처리되었습니다.'); location.href='/mypage'</script>");
                break;
            case 0:
                out.println("<script>alert('상환할 금액이 없습니다.'); history.go(-1);</script>");
                break;
            case -1:
                out.println("<script>alert('모인 금액을 초과한 값입니다.'); history.go(-1);</script>");
                break;
        }
        out.flush();
        return "";
        //return "redirect:/mypage/loan";
    }
}
