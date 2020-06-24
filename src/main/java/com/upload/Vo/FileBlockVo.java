package com.upload.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FileBlockVo {
    private String statu;
    private List<FileVo> blocks;

    FileBlockVo(){
        statu = "";
        blocks = new ArrayList<FileVo>();
    }
}
