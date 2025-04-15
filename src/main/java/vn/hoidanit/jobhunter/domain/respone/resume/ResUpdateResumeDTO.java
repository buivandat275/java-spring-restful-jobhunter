package vn.hoidanit.jobhunter.domain.respone.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ResUpdateResumeDTO {
    private ResumeStateEnum status;

    private Instant updatedAt;
    private String updatedBy;
}
