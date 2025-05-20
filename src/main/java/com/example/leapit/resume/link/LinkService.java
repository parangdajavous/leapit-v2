package com.example.leapit.resume.link;

import com.example.leapit.resume.Resume;
import com.example.leapit.resume.ResumeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Transactional
    public void update(Resume resumePS, @Valid List<ResumeRequest.UpdateDTO.LinkDTO> linkDTOs) {
        // DB로부터 찾아온 이력서 resumePS에 해당하는 링크 리스트
        List<Link> links = resumePS.getLinks();

        // dtoMap = update하기 위해 받아온 데이터
        Map<Integer, ResumeRequest.UpdateDTO.LinkDTO> dtoMap = linkDTOs.stream()
                .filter(dto -> dto.getId() != null) // id가 null이면 수정X, 추가
                .collect(Collectors.toMap(ResumeRequest.UpdateDTO.LinkDTO::getId, dto -> dto));

        Iterator<Link> iterator = links.iterator();
        while (iterator.hasNext()) {
            Link link = iterator.next();
            ResumeRequest.UpdateDTO.LinkDTO dto = dtoMap.remove(link.getId());
            if (dto != null) {
                // id 존재, dto notNull -> 수정
                link.update(dto.getTitle(), dto.getUrl());
            } else {
                // id 존재, dto null -> 삭제
                iterator.remove();
            }
        }

        // 추가된 항목 저장
        for (ResumeRequest.UpdateDTO.LinkDTO dto : linkDTOs) {
            if (dto.getId() == null) {
                Link newLink = Link.builder()
                        .resume(resumePS)
                        .title(dto.getTitle())
                        .url(dto.getUrl())
                        .build();
                links.add(newLink);
            }
        }
    }

}
