package com.github.liaochong.myexcel.core;

import com.github.liaochong.myexcel.core.pojo.CommonPeople;
import com.github.liaochong.myexcel.core.pojo.CsvPeople;
import com.github.liaochong.myexcel.core.pojo.ExceptionPeople;
import com.github.liaochong.myexcel.core.pojo.TitlePeople;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author liaochong
 * @version 1.0
 */
class SaxExcelReaderTest {

    @Test
    void csvReadFile() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CsvPeople> csvPeoples = SaxExcelReader.of(CsvPeople.class).rowFilter(row -> row.getRowNum() > 0).read(path.toFile());
        System.out.println(csvPeoples.size());
    }

    @Test
    void csvReadInputStream() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<Map> csvPeoples = SaxExcelReader.of(Map.class).read(Files.newInputStream(path));
        System.out.println(csvPeoples.size());
    }

    @Test
    void csvReadContinuedException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<ExceptionPeople> csvPeoples = SaxExcelReader.of(ExceptionPeople.class).exceptionally((e, context) -> {
            System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
            return true;
        }).read(path.toFile());
    }

    @Test
    void csvReadException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        try {
            List<ExceptionPeople> csvPeoples = SaxExcelReader.of(ExceptionPeople.class).exceptionally((e, context) -> {
                System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
                return false;
            }).read(path.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readXlsxFile() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class)
                .rowFilter(row -> row.getRowNum() > 0)
                .sheet(0, 1)
                .read(path.toFile());
        System.out.println(commonPeoples.size());
    }

    @Test
    void readXlsFile() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class).rowFilter(row -> row.getRowNum() > 0)
                .sheet(0, 1)
                .read(path.toFile());
        System.out.println(commonPeoples.size());
    }

    @Test
    void readXlsxFileWithName() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class)
                .rowFilter(row -> row.getRowNum() > 0)
                .sheet("人员信息", "工作表1")
                .read(path.toFile());
        System.out.println(commonPeoples.size());
    }

    @Test
    void readXlsFileWithName() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class).rowFilter(row -> row.getRowNum() > 0)
                .sheet("人员信息", "工作表2")
                .read(path.toFile());
        System.out.println(commonPeoples.size());
    }

    @Test
    void readXlsxContinuedException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class).exceptionally((e, context) -> {
            System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
            return true;
        }).read(path.toFile());
    }

    @Test
    void readXlsxException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        try {
            List<CommonPeople> commonPeoples = SaxExcelReader.of(CommonPeople.class).exceptionally((e, context) -> {
                System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
                return false;
            }).read(path.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readXlsContinuedException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<ExceptionPeople> commonPeoples = SaxExcelReader.of(ExceptionPeople.class).exceptionally((e, context) -> {
            System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
            return true;
        }).read(path.toFile());
    }

    @Test
    void readXlsxNoException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        try {
            List<ExceptionPeople> commonPeoples = SaxExcelReader.of(ExceptionPeople.class).read(path.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readXlsException() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        try {
            List<ExceptionPeople> commonPeoples = SaxExcelReader.of(ExceptionPeople.class).exceptionally((e, context) -> {
                System.out.println(context.getField().getName() + "_" + context.getVal() + "_" + context.getRowNum() + "_" + context.getColNum());
                return false;
            }).read(path.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readThen() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(CsvPeople.class).rowFilter(row -> row.getRowNum() > 0).readThen(path.toFile(), d -> {
            System.out.println(d.getMoney());
        });
    }

    @Test
    void readXlsxWithTitle() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(TitlePeople.class).rowFilter(row -> row.getRowNum() > 0).readThen(Files.newInputStream(path), d -> {
            System.out.println(d.getMoney());
        });
    }

    @Test
    void readXlsWithTitle() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(TitlePeople.class).rowFilter(row -> row.getRowNum() > 0).readThen(Files.newInputStream(path), d -> {
            System.out.println(d.getMoney());
        });
    }

    @Test
    void readCsvWithTitle() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(TitlePeople.class).rowFilter(row -> row.getRowNum() > 0).readThen(Files.newInputStream(path), d -> {
            System.out.println(d.getMoney());
        });
    }

    @Test
    void readThenInputStream() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(CommonPeople.class).rowFilter(row -> row.getRowNum() > 0).readThen(Files.newInputStream(path), d -> {
            System.out.println(d.getMoney());
        });
    }

    @Test
    void readXlsMap() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xls");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(Map.class).rowFilter(row -> row.getRowNum() > 0).readThen(Files.newInputStream(path), d -> {
            System.out.println(d);
        });
    }

    @Test
    void readXlsxMap() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common_build.xlsx");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        List<Map> result = SaxExcelReader.of(Map.class).rowFilter(row -> row.getRowNum() > 0).read(Files.newInputStream(path));
        System.out.println("");
    }

    @Test
    void readCsvMap() throws Exception {
        URL htmlToExcelEampleURL = this.getClass().getResource("/common.csv");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        SaxExcelReader.of(Map.class).rowFilter(row -> row.getRowNum() > 0).readThen(path.toFile(), d -> {
            System.out.println(d);
        });
    }
}