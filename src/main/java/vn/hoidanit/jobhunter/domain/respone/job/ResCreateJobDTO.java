package vn.hoidanit.jobhunter.domain.respone.job;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResCreateJobDTO {
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Date startDate;
    private Date endDate;

    private List<String> skill;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    
}
