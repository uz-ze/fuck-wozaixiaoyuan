package uz.fuckwzxy.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfo {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String token;

    private String session;
}

