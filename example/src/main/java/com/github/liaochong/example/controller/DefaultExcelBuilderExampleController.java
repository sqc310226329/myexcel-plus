package com.github.liaochong.example.controller;

import com.github.liaochong.example.pojo.ArtCrowd;
import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.strategy.AutoWidthStrategy;
import com.github.liaochong.myexcel.core.strategy.WidthStrategy;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liaochong
 * @version 1.0
 */
@Controller
public class DefaultExcelBuilderExampleController {
    @GetMapping("/default/excel/example")
    public void defaultBuild(HttpServletResponse response) throws Exception {
        List<ArtCrowd> dataList = this.getDataList();
        Workbook workbook = DefaultExcelBuilder.of(ArtCrowd.class).build(dataList);
        AttachmentExportUtil.export(workbook, "艺术生信息", response);
    }

    /**
     * 根据传的列名进行导出
     * @param response
     * @throws Exception
     */
    @GetMapping("/customize/excel/example")
    public void customizeBuild(HttpServletResponse response) throws Exception {
        Field[] declaredFields = ArtCrowd.class.getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field->{
            //获取val字段上的Foo注解实例
            ExcelColumn foo = field.getAnnotation(ExcelColumn.class);
            //获取 foo 这个代理实例所持有的 InvocationHandler
            InvocationHandler h = Proxy.getInvocationHandler(foo);
            // 获取 AnnotationInvocationHandler 的 memberValues 字段
            Field hField = null;
            try {
                hField = h.getClass().getDeclaredField("memberValues");
                // 因为这个字段事 private final 修饰，所以要打开权限
                hField.setAccessible(true);
                // 获取 memberValues
                Map memberValues = (Map) hField.get(h);
                //根据列名进行导出,相等导出
                if (Objects.equals("需要导出的列名",field.getName())) {
                    // 修改 value 属性值
                    memberValues.put("isShow", true);//
                }
                // 获取 foo 的 value 属性值
                String show = foo.isShow()+"";
                System.out.println(show); // ddd
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        List<ArtCrowd> dataList = this.getDataList();
        Workbook workbook = DefaultExcelBuilder.of(ArtCrowd.class).build(dataList);
        AttachmentExportUtil.export(workbook, "艺术生信息", response);
    }
    @GetMapping("/default/noStyle/example")
    public void defaultBuildWithNoStyle(HttpServletResponse response) throws Exception {
        List<ArtCrowd> dataList = this.getDataList();
        Workbook workbook = DefaultExcelBuilder.of(ArtCrowd.class).noStyle().build(dataList);
        AttachmentExportUtil.export(workbook, "艺术生信息", response);
    }

    @GetMapping("/default/autoWidth/example")
    public void defaultBuildWithAutoWidth(HttpServletResponse response) throws Exception {
        List<ArtCrowd> dataList = this.getDataList();
        Workbook workbook = DefaultExcelBuilder.of(ArtCrowd.class).widthStrategy(WidthStrategy.AUTO_WIDTH).build(dataList);
        AttachmentExportUtil.export(workbook, "艺术生信息", response);
    }

    @GetMapping("/default/continue/example")
    public void defaultBuildWithWorkbook(HttpServletResponse response) throws Exception {
        List<ArtCrowd> dataList = this.getDataList();
        Workbook workbook = DefaultExcelBuilder.of(ArtCrowd.class).widthStrategy(WidthStrategy.AUTO_WIDTH).build(dataList);

        dataList = this.getDataList();
        workbook = DefaultExcelBuilder.of(ArtCrowd.class, workbook).sheetName("sheet2").widthStrategy(WidthStrategy.NO_AUTO).build(dataList);
        AttachmentExportUtil.export(workbook, "艺术生信息", response);
    }

    private List<ArtCrowd> getDataList() {
        List<ArtCrowd> dataList = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            ArtCrowd artCrowd = new ArtCrowd();
            if (i % 2 == 0) {
                artCrowd.setName("Tom");
                artCrowd.setAge(19);
                artCrowd.setGender("Man");
                artCrowd.setPaintingLevel("一级证书");
                artCrowd.setDance(false);
                artCrowd.setAssessmentTime(LocalDateTime.now());
                artCrowd.setHobby(null);
            } else {
                artCrowd.setName("Marry");
                artCrowd.setAge(18);
                artCrowd.setGender("Woman");
                artCrowd.setPaintingLevel("一级证书");
                artCrowd.setDance(true);
                artCrowd.setAssessmentTime(LocalDateTime.now());
                artCrowd.setHobby("钓鱼");
            }
            dataList.add(artCrowd);
        }
        return dataList;
    }


}
