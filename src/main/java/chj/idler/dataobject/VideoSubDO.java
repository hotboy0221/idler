package chj.idler.dataobject;

import java.util.Date;

public class VideoSubDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_sub.user_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_sub.video_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    private Integer videoId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_sub.add_time
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    private Long addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_sub.status
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    private Byte status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video_sub.user_id
     *
     * @return the value of video_sub.user_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video_sub.user_id
     *
     * @param userId the value for video_sub.user_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video_sub.video_id
     *
     * @return the value of video_sub.video_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public Integer getVideoId() {
        return videoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video_sub.video_id
     *
     * @param videoId the value for video_sub.video_id
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video_sub.add_time
     *
     * @return the value of video_sub.add_time
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public Long getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video_sub.add_time
     *
     * @param addTime the value for video_sub.add_time
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column video_sub.status
     *
     * @return the value of video_sub.status
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column video_sub.status
     *
     * @param status the value for video_sub.status
     *
     * @mbg.generated Thu May 21 11:52:31 CST 2020
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}