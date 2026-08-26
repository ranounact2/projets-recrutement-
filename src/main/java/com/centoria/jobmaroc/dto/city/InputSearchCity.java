package com.centoria.jobmaroc.dto.city;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputSearchCity {
    private String name;
    private String slug;
    private String image;
    private String domain = null;

    private int page = -1;
    private int numberOfCity = -1;
}
