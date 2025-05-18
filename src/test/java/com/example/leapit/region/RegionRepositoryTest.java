package com.example.leapit.region;

import com.example.leapit.common.region.Region;
import com.example.leapit.common.region.RegionRepository;
import com.example.leapit.common.region.SubRegion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(RegionRepository.class)
@DataJpaTest
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Test
    public void region_repository_test() {
        // when
        List<Region> result = regionRepository.findAllRegion();

        // then (eye)
        for (Region region : result) {
            System.out.println("지역 ID = " + region.getId() + ", 이름 = " + region.getName());
        }
        // 지역 ID = 1, 이름 = 서울특별시
        // 지역 ID = 2, 이름 = 부산광역시
    }

    @Test
    public void find_subregion_by_region_id_test() {
        // given
        Integer regionId = 1; // 예: '서울특별시'

        // when
        List<SubRegion> result = regionRepository.findAllSubRegionByRegionId(regionId);

        // then
        for (SubRegion sr : result) {
            System.out.println("서브지역 ID = " + sr.getId() + ", 이름 = " + sr.getName());
        }
        // 서브지역 ID = 1, 이름 = 강남구
        // 서브지역 ID = 2, 이름 = 서초구
    }
}