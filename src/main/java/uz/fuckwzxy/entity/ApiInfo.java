package uz.fuckwzxy.entity;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class ApiInfo {
    /**
     *
     */
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 请求方法  0get1post
     */
    private Integer method;

    /**
     * 方法体
     */
    private String body;

    /**
     * 类型  0三检1签到
     */
    @NonNull
    private Integer type;
}

