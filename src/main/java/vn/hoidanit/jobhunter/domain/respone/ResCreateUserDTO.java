package vn.hoidanit.jobhunter.domain.respone;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;
@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createAt;
    private String createBy;
    private CompanyUser company;


    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }
    
}
