package com.example.leapit.common.region;

import lombok.Data;

import java.util.List;

public class RegionResponse {


    @Data
    public static class DTO {
        private Integer id;
        private String name;
        private List<SubRegionDTO> subRegions;

        public DTO(Integer id, String name, List<SubRegion> subRegions) {
            this.id = id;
            this.name = name;
            this.subRegions = subRegions.stream()
                    .map(sr -> new SubRegionDTO(sr.getId(), sr.getName()))
                    .toList();
        }
    }

    @Data
    public static class SubRegionDTO {
        private Integer id;
        private String name;

        public SubRegionDTO(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
