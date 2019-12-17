/*
 * Copyright 2019 liaochong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.liaochong.myexcel.core.pojo;

import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.core.annotation.ExcelTable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liaochong
 * @version 1.0
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ExcelTable(sheetName = "人员信息")
public class TitlePeople {
    @ExcelColumn(title = "姓名")
    String name;

    @ExcelColumn(title = "年龄")
    Integer age;

    @ExcelColumn(title = "是否会跳舞", groups = CommonPeople.class)
    boolean dance;

    @ExcelColumn(title = "金钱", decimalFormat = "#,000.00")
    BigDecimal money;

    @ExcelColumn(title = "生日", dateFormatPattern = "yyyy-MM-dd HH:mm:ss")
    Date birthday;
}
