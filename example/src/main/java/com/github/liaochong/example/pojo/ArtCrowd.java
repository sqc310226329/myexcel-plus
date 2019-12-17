package com.github.liaochong.example.pojo;


import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelTable;

import java.time.LocalDateTime;

/**
 * @author liaochong
 * @version 1.0
 */
@ExcelTable(sheetName = "艺术生", useFieldNameAsTitle = true, includeAllField = false)
public class ArtCrowd extends People {

    @ExcelColumn(order = 3, index = 3, width = 20,isShow = true)
    private String paintingLevel;

    @ExcelColumn(order = 4, title = "综合->综合->是否会跳舞", width = 9, groups = {People.class, String.class}, index = 4,isShow = true)
    private boolean dance;

    @ExcelColumn(order = 5, title = "考核时间", width = 10, dateFormatPattern = "yyyy-MM-dd HH:mm:ss", groups = {People.class, String.class}, index = 5,isShow = false)
    private LocalDateTime assessmentTime;

    @ExcelColumn(order = 6, defaultValue = "***",isShow = true)
    private String hobby;

    @ExcelColumn(order = 7,title = "综合->dgfgd",defaultValue = "***",isShow = true)
    private String demo;

    public String getPaintingLevel() {
        return paintingLevel;
    }

    public void setPaintingLevel(String paintingLevel) {
        this.paintingLevel = paintingLevel;
    }

    public boolean isDance() {
        return dance;
    }

    public void setDance(boolean dance) {
        this.dance = dance;
    }

    public LocalDateTime getAssessmentTime() {
        return assessmentTime;
    }

    public void setAssessmentTime(LocalDateTime assessmentTime) {
        this.assessmentTime = assessmentTime;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
