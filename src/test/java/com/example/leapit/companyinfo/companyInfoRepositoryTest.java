package com.example.leapit.companyinfo;

import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;

@Import(CompanyInfoRepository.class)
@DataJpaTest
public class companyInfoRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Test
    public void findByUsername_test() {
        // given
        Integer userId = 7;

        // when
        Optional<CompanyInfo> companyInfoOP = companyInfoRepository.findByUserId(userId);

        // eye
        System.out.println("===========유저네임중복체크============");
        System.out.println(companyInfoOP.get().getId());
        System.out.println(companyInfoOP.get().getCompanyName());
        System.out.println(companyInfoOP.get().getAddress());
        System.out.println("===========유저네임중복체크============");
    }

    @Test
    public void findById_test() {
        Integer userId = 7;
        Optional<CompanyInfo> companyInfoOP = companyInfoRepository.findById(userId);

        // eye
        System.out.println("===========유저네임중복체크============");
        System.out.println(companyInfoOP.get().getId());
        System.out.println(companyInfoOP.get().getCompanyName());
        System.out.println(companyInfoOP.get().getAddress());
        System.out.println("===========유저네임중복체크============");
    }

    @Test
    public void save_test() {
        // given
        User user = User.builder()
                .username("testuser")
                .password("1234")
                .email("test@company.com")
                .role(Role.COMPANY)
                .contactNumber("010-1234-5678")
                .build();
        em.persist(user);

        String logoImage = "data:image";
        String companyName = "Leapit";
        LocalDate establishmentDate = LocalDate.of(2018, 1, 1);
        String address = "서울시 강남구 역삼동";
        String mainService = "https://www.naver.com/";
        String introduction = "Ai 서비스";
        String image = "data:image";
        String benefit = "식대지원";

        CompanyInfo companyInfo = CompanyInfo.builder()
                .logoImage(logoImage)
                .companyName(companyName)
                .establishmentDate(establishmentDate)
                .address(address)
                .mainService(mainService)
                .introduction(introduction)
                .image(image)
                .benefit(benefit)
                .build();

        //when
        CompanyInfo companyInfoSave = companyInfoRepository.save(companyInfo);

        // eye
        System.out.println(companyInfoSave.getLogoImage());
        System.out.println(companyInfoSave.getCompanyName());
        System.out.println(companyInfoSave.getEstablishmentDate());
        System.out.println(companyInfoSave.getAddress());
        System.out.println(companyInfoSave.getMainService());
        System.out.println(companyInfoSave.getIntroduction());
        System.out.println(companyInfoSave.getImage());
        System.out.println(companyInfoSave.getBenefit());
    }
}
