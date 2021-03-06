package ww.greendao.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DL_REFEREE.
 */
public class dl_referee {

    private Long referee_ID;
    private String referee_CHN_name;
    private String referee_ENG_name;
    private String referee_CHN_country;
    private String referee_ENG_country;
    private String referee_num;
    private Integer referee_level;
    private Integer referee_type;
    private String referee_position;
    private String referee_password;
    private String referee_gmid;
    private String referee_state;

    public dl_referee() {
    }

    public dl_referee(Long referee_ID) {
        this.referee_ID = referee_ID;
    }

    public dl_referee(Long referee_ID, String referee_CHN_name, String referee_ENG_name, String referee_CHN_country, String referee_ENG_country, String referee_num, Integer referee_level, Integer referee_type, String referee_position, String referee_password, String referee_gmid, String referee_state) {
        this.referee_ID = referee_ID;
        this.referee_CHN_name = referee_CHN_name;
        this.referee_ENG_name = referee_ENG_name;
        this.referee_CHN_country = referee_CHN_country;
        this.referee_ENG_country = referee_ENG_country;
        this.referee_num = referee_num;
        this.referee_level = referee_level;
        this.referee_type = referee_type;
        this.referee_position = referee_position;
        this.referee_password = referee_password;
        this.referee_gmid = referee_gmid;
        this.referee_state = referee_state;
    }

    public Long getReferee_ID() {
        return referee_ID;
    }

    public void setReferee_ID(Long referee_ID) {
        this.referee_ID = referee_ID;
    }

    public String getReferee_CHN_name() {
        return referee_CHN_name;
    }

    public void setReferee_CHN_name(String referee_CHN_name) {
        this.referee_CHN_name = referee_CHN_name;
    }

    public String getReferee_ENG_name() {
        return referee_ENG_name;
    }

    public void setReferee_ENG_name(String referee_ENG_name) {
        this.referee_ENG_name = referee_ENG_name;
    }

    public String getReferee_CHN_country() {
        return referee_CHN_country;
    }

    public void setReferee_CHN_country(String referee_CHN_country) {
        this.referee_CHN_country = referee_CHN_country;
    }

    public String getReferee_ENG_country() {
        return referee_ENG_country;
    }

    public void setReferee_ENG_country(String referee_ENG_country) {
        this.referee_ENG_country = referee_ENG_country;
    }

    public String getReferee_num() {
        return referee_num;
    }

    public void setReferee_num(String referee_num) {
        this.referee_num = referee_num;
    }

    public Integer getReferee_level() {
        return referee_level;
    }

    public void setReferee_level(Integer referee_level) {
        this.referee_level = referee_level;
    }

    public Integer getReferee_type() {
        return referee_type;
    }

    public void setReferee_type(Integer referee_type) {
        this.referee_type = referee_type;
    }

    public String getReferee_position() {
        return referee_position;
    }

    public void setReferee_position(String referee_position) {
        this.referee_position = referee_position;
    }

    public String getReferee_password() {
        return referee_password;
    }

    public void setReferee_password(String referee_password) {
        this.referee_password = referee_password;
    }

    public String getReferee_gmid() {
        return referee_gmid;
    }

    public void setReferee_gmid(String referee_gmid) {
        this.referee_gmid = referee_gmid;
    }

    public String getReferee_state() {
        return referee_state;
    }

    public void setReferee_state(String referee_state) {
        this.referee_state = referee_state;
    }

}
