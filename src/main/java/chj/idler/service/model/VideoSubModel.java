package chj.idler.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.Date;

@Data
public class VideoSubModel {
    private Integer userId;
    private Integer videoId;
    private Long addTime;
    private Byte status;
}
