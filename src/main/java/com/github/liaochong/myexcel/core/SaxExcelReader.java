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
package com.github.liaochong.myexcel.core;

import com.github.liaochong.myexcel.core.constant.Constants;
import com.github.liaochong.myexcel.exception.ExcelReadException;
import com.github.liaochong.myexcel.exception.SaxReadException;
import com.github.liaochong.myexcel.exception.StopReadException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * sax模式读取excel，支持xls、xlsx、csv格式读取
 *
 * @author liaochong
 * @version 1.0
 */
@Slf4j
public class SaxExcelReader<T> {

    private static final int DEFAULT_SHEET_INDEX = 0;

    private List<T> result = new LinkedList<>();

    private ReadConfig<T> readConfig = new ReadConfig<>(DEFAULT_SHEET_INDEX);

    private SaxExcelReader(Class<T> dataType) {
        this.readConfig.dataType = dataType;
    }

    public static <T> SaxExcelReader<T> of(@NonNull Class<T> clazz) {
        return new SaxExcelReader<>(clazz);
    }

    public SaxExcelReader<T> sheet(Integer... indexs) {
        this.readConfig.sheetIndexs.clear();
        this.readConfig.sheetIndexs.addAll(Arrays.asList(indexs));
        return this;
    }

    public SaxExcelReader<T> sheet(String... sheetNames) {
        this.readConfig.sheetNames.clear();
        this.readConfig.sheetNames.addAll(Arrays.asList(sheetNames));
        return this;
    }

    public SaxExcelReader<T> rowFilter(Predicate<Row> rowFilter) {
        this.readConfig.rowFilter = rowFilter;
        return this;
    }

    public SaxExcelReader<T> beanFilter(Predicate<T> beanFilter) {
        this.readConfig.beanFilter = beanFilter;
        return this;
    }

    public SaxExcelReader<T> charset(String charset) {
        this.readConfig.charset = charset;
        return this;
    }

    public SaxExcelReader<T> exceptionally(BiFunction<Throwable, ReadContext, Boolean> exceptionFunction) {
        this.readConfig.exceptionFunction = exceptionFunction;
        return this;
    }

    public List<T> read(@NonNull InputStream fileInputStream) {
        doReadInputStream(fileInputStream);
        return result;
    }

    public List<T> read(@NonNull File file) {
        doReadFile(file);
        return result;
    }

    public void readThen(@NonNull InputStream fileInputStream, Consumer<T> consumer) {
        this.readConfig.consumer = consumer;
        doReadInputStream(fileInputStream);
    }

    public void readThen(@NonNull File file, Consumer<T> consumer) {
        this.readConfig.consumer = consumer;
        doReadFile(file);
    }

    public void readThen(@NonNull InputStream fileInputStream, Function<T, Boolean> function) {
        this.readConfig.function = function;
        doReadInputStream(fileInputStream);
    }

    public void readThen(@NonNull File file, Function<T, Boolean> function) {
        this.readConfig.function = function;
        doReadFile(file);
    }

    private void doReadFile(@NonNull File file) {
        String suffix = file.getName().substring(file.getName().lastIndexOf(Constants.SPOT));
        switch (suffix) {
            case Constants.XLSX:
                doReadXlsx(file);
                break;
            case Constants.XLS:
                doReadXls(file);
                break;
            case Constants.CSV:
                doReadCsv(file);
                break;
            default:
                throw new IllegalArgumentException("The file type does not match, and the file suffix must be one of .xlsx,.xls,.csv");
        }
    }

    private void doReadXls(File file) {
        try {
            new HSSFSaxReadHandler<>(file, result, readConfig).process();
        } catch (StopReadException e) {
            // do nothing
        } catch (IOException e) {
            throw new SaxReadException("Fail to read file", e);
        }
    }

    private void doReadXlsx(File file) {
        try (OPCPackage p = OPCPackage.open(file, PackageAccess.READ)) {
            process(p);
        } catch (StopReadException e) {
            // do nothing
        } catch (Exception e) {
            throw new SaxReadException("Fail to read file", e);
        }
    }

    private void doReadCsv(File file) {
        try {
            new CsvReadHandler<>(Files.newInputStream(file.toPath()), readConfig, result).read();
        } catch (StopReadException e) {
            // do nothing
        } catch (Throwable throwable) {
            throw new ExcelReadException("Fail to read file", throwable);
        }
    }

    private void doReadXls(@NonNull InputStream fileInputStream) {
        try {
            new HSSFSaxReadHandler<>(fileInputStream, result, readConfig).process();
        } catch (StopReadException e) {
            // do nothing
        } catch (IOException e) {
            throw new SaxReadException("Fail to read file inputStream", e);
        }
    }

    private void doReadCsv(@NonNull InputStream fileInputStream) {
        try {
            new CsvReadHandler<>(fileInputStream, readConfig, result).read();
        } catch (StopReadException e3) {
            // do nothing
        }
    }

    @NonNull
    private InputStream modifyInputStreamTypeIfNotMarkSupported(@NonNull InputStream fileInputStream) {
        if (fileInputStream.markSupported()) {
            return fileInputStream;
        }
        return new BufferedInputStream(fileInputStream);
    }

    private void doReadInputStream(@NonNull InputStream fileInputStream) {
        try (InputStream is = FileMagic.prepareToCheckMagic(fileInputStream);) {
            FileMagic fm = FileMagic.valueOf(is);
            switch (fm) {
                case OLE2:
                    doReadXls(is);
                    break;
                case OOXML:
                    try (OPCPackage p = OPCPackage.open(is)) {
                        process(p);
                    }
                    break;
                default:
                    doReadCsv(is);
            }
        } catch (Throwable throwable) {
            throw new SaxReadException("Fail to read file inputStream", throwable);
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException  If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    private void process(OPCPackage xlsxPackage) throws IOException, OpenXML4JException, SAXException {
        long startTime = System.currentTimeMillis();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        if (!readConfig.sheetNames.isEmpty()) {
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    if (readConfig.sheetNames.contains(iter.getSheetName())) {
                        processSheet(strings, new XSSFSaxReadHandler<>(result, readConfig), stream);
                    }
                }
            }
        } else {
            int index = 0;
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    if (readConfig.sheetIndexs.contains(index)) {
                        processSheet(strings, new XSSFSaxReadHandler<>(result, readConfig), stream);
                    }
                    ++index;
                }
            }
        }
        log.info("Sax import takes {} ms", System.currentTimeMillis() - startTime);
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param strings          The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.
     * @throws java.io.IOException An IO exception from the parser,
     *                             possibly from a byte stream or character stream
     *                             supplied by the application.
     * @throws SAXException        if parsing the XML data fails.
     */
    private void processSheet(
            SharedStrings strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    null, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ReadConfig<T> {

        Class<T> dataType;

        Set<String> sheetNames = new HashSet<>();

        Set<Integer> sheetIndexs = new HashSet<>();

        Consumer<T> consumer;

        Function<T, Boolean> function;

        Predicate<Row> rowFilter = row -> true;

        Predicate<T> beanFilter = bean -> true;

        BiFunction<Throwable, ReadContext, Boolean> exceptionFunction = (t, c) -> false;

        String charset = "UTF-8";

        public ReadConfig(int sheetIndex) {
            sheetIndexs.add(sheetIndex);
        }
    }
}
