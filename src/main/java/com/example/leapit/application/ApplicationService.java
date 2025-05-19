package com.example.leapit.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    // 개인 마이페이지 지원 현황 관리
    public ApplicationResponse.MyPageDTO getMyApplication(Integer userId) {
        // 지원 현황 통계
        ApplicationResponse.StatusDTO statusDTO = applicationRepository.findStatusByUserId(userId);
        if (statusDTO == null) {
            statusDTO = new ApplicationResponse.StatusDTO(0L, 0L, 0L);
        }

        // 지원 현황 목록 조회
        List<ApplicationResponse.MyPageDTO.ItemDTO> itemDTOs = applicationRepository.findItemsByUserId(userId);
        // respDTO에 담기
        ApplicationResponse.MyPageDTO respDTO = new ApplicationResponse.MyPageDTO(statusDTO, itemDTOs);
        return respDTO;
    }
}