package vn.hoidanit.jobhunter.domain.respone;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createAt;
    private Instant updateAt;
     private CompanyUser company;


    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }

}
