package vn.hoidanit.jobhunter.domain.respone;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// class để đồng bộ kiểu giữ liệu của accessToken bên Authcontroller về stringstring
@Getter
@Setter
public class ResLoginDTO {
     
     @JsonProperty("access_token")//đôi tên accessToken ->access_token để phù hợp với forntend
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
   public static class  UserLogin {
        private long id;
        private String email;
        private String name;
    
   }

   @Getter
   @Setter
   @AllArgsConstructor
   @NoArgsConstructor
   public static class  UserGetAccount {
     private UserLogin user;
     
   }

    
    
}
