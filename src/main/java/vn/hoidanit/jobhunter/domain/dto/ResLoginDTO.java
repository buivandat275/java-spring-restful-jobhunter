package vn.hoidanit.jobhunter.domain.dto;
// class để đồng bộ kiểu giữ liệu của accessToken bên Authcontroller về stringstring
public class ResLoginDTO {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    
    
}
