/**
 * 文件名：Test.java
 * 版权： Copyright 2002-2017 QLYS. All Rights Reserved.
 * 描述：
 * 修改人：时前程
 * 修改时间：2019年12月03日
 * 修改内容：新增
 **/
package com.github.liaochong.myexcel.core.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p> 
 *      简述一下～
 * <p>
 * @author 时前程 2019年12月03日
 * @see
 * @since 1.0
 */
public class Test {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setName("ddd");
        student.setSex("aaa");
        studentList.add(student);
        studentList.stream().forEach(a->{
            System.out.println(a.getName());
        });

    }

    public static class Student{

        private String name;

        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
