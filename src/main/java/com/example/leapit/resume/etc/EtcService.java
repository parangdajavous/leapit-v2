package com.example.leapit.resume.etc;

import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EtcService {
    private final EtcRepository etcRepository;

    @Transactional
    public void update(Resume resumePS, @Valid List<ResumeRequest.UpdateDTO.EtcDTO> etcDTOs) {
        List<Etc> etcs = resumePS.getEtcs();

        Map<Integer, ResumeRequest.UpdateDTO.EtcDTO> dtoMap = etcDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ResumeRequest.UpdateDTO.EtcDTO::getId, dto -> dto));

        Iterator<Etc> iterator = etcs.iterator();
        while (iterator.hasNext()) {
            Etc etc = iterator.next();
            ResumeRequest.UpdateDTO.EtcDTO dto = dtoMap.remove(etc.getId());

            if (dto != null) {
                etc.update(
                        dto.getStartDate(),
                        dto.getEndDate(),
                        dto.getHasEndDate(),
                        dto.getTitle(),
                        dto.getEtcType(),
                        dto.getInstitutionName(),
                        dto.getDescription()
                );
            } else {
                iterator.remove();
            }
        }

        for (ResumeRequest.UpdateDTO.EtcDTO dto : etcDTOs) {
            if (dto.getId() == null) {
                Etc newEtc = Etc.builder()
                        .resume(resumePS)
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .hasEndDate(dto.getHasEndDate())
                        .title(dto.getTitle())
                        .etcType(dto.getEtcType())
                        .institutionName(dto.getInstitutionName())
                        .description(dto.getDescription())
                        .build();
                etcs.add(newEtc);
            }
        }
    }
}
