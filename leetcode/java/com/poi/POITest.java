package com.poi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

/**
 * javaAPI poi导出 Excel应用
 * Created by rensong.pu on 2016/8/29.
 */
public class POITest {
    private static int headerHeight = 1;
    private static int lastRow = 0;
    private static int columnLength = 5;
    private static Logger logger = LoggerFactory.getLogger(POITest.class);

    public static void main(String[] args) {
        /*
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("小铺");
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillForegroundColor((short) 41);//设置背景色
        //设置字体
        XSSFFont xssfFont = wb.createFont();
        xssfFont.setFamily(FontFamily.DECORATIVE);
        xssfFont.setFontHeightInPoints((short) 16);
//        sheet.setColumnWidth(0, 4000);//设置列宽度
        cellStyle.setWrapText(true);//设置自动换行

        File file = new File("E://poi.xlsx");
        sheet.setDefaultColumnWidth(17);//设置默认宽度
        sheet.setDefaultRowHeight((short) (2 * 256));//设置默认行高，2个字符高度
        for (int i = 0; i < 12; i++) {
            writeToExcel(sheet);
            ++lastRow;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("", e);
            logger.error("", e);
            System.out.println();
        }
*/
  /*      //poi导入图片
        XSSFWorkbook workbook = new XSSFWorkbook();
        String imgPath = "E://kola.png";
        insertImg(imgPath, workbook, 1, 1);

*/
//        System.out.println(POITest.class.getResource("/htmlcode")); //文件路径
//        System.out.println(POITest.class.getResource("")); //项目包路径
        XSSFWorkbook wb = new XSSFWorkbook();
        POITest poiTest = new POITest();
//        System.out.println("是否支持unicode编码->" + Charset.isSupported("unicode"));
        //插入htmlcode
        int c = 0;
        XSSFSheet sheet = wb.createSheet("hh");
        sheet.lockPivotTables();
        XSSFSheet sheet2 = wb.createSheet("hh2");
        long cur = System.currentTimeMillis();
        try {
            for (int r = 0; r < 10; r++) {
                insertHtmlCode("<table><img src=\"" + poiTest.imgUrl().get(r) + "\" width=\"320\" height = \"320\"/></table>", sheet, r, c);
                BufferedImage image = ImageIO.read(new BufferedInputStream(new URL(poiTest.imgUrl().get(r)).openStream()));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", bos);
                insertImg(bos, sheet2, r * 28, c);
            }
            try (FileOutputStream fos = new FileOutputStream("E://htmlTest.xlsx")) {
                sheet.getWorkbook().write(fos);
                sheet2.getWorkbook().write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("导出总耗时"+(System.currentTimeMillis()-cur));
    }

    public List<String> imgUrl() {
        List<String> imgurls = new ArrayList<>();
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
            String user = "root";
            String pwd = "root";
            con = getConnection(url, user, pwd);
            try (Statement state = con.createStatement()) {
                try (ResultSet rs = state.executeQuery("SELECT * FROM dim_prod_goods")) {
                    while (rs.next()) {
                        imgurls.add(
                                            rs.getString("pic_url")

                                   );
                    }
                }
            }
            return imgurls;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String htmlCode() {
        try {
            File file = new File(getClass().getResource("/htmlcode").toURI());
            File[] fils = file.listFiles();
            for (File f : fils) {
                String htmlCode = FileUtils.readFileToString(f);
                return htmlCode;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //插入html片段，选择性粘贴到excel中，实现导入图片的功能
    public static void insertHtmlCode(String htmlCode, XSSFSheet sheet, int rowCell, int colCell) throws UnsupportedEncodingException {
        XSSFCell cell = sheet.createRow(rowCell).createCell(colCell);
        XSSFRichTextString textString = new XSSFRichTextString(StringUtils.toString(htmlCode.getBytes(Charset.forName("unicode")), "unicode"));
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(textString);
    }

    public static void insertImg(String imgPath, XSSFWorkbook wb, int rowCell, int colCell) {
        //将图片转为byteArrayOutStream,便于产生byteArray
        FileOutputStream fos = null;
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bufferedImage = ImageIO.read(new File(imgPath));
            ImageIO.write(bufferedImage, "png", baos);
            Sheet sheet = wb.createSheet("img");
            //画图的顶级管理器
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, rowCell, colCell, 5, 8);
//            anchor.setAnchorType(3);
            //插入图片
            int pictureIndex = wb.addPicture(baos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG);
            drawing.createPicture(anchor, pictureIndex).resize(1); //设置图片原始尺寸大小,其中resize是放大或缩小的函数。用了这个函数后，HSSFClientAnchor构造函数中的图片显示的终了cell位置就不起作用了
            fos = new FileOutputStream("E://img.xlsx");
            wb.write(fos);
            System.out.println("Excel生成完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertImg(ByteArrayOutputStream[] os, Sheet sheet, int rowCell, int colCell) {
        FileOutputStream fos = null;
        try {
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            for (int i = 0; i < 2; i++) {
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 0, colCell, rowCell, colCell + 8, rowCell + 22);//15*9的矩形图
                anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
                drawing.createPicture(anchor, sheet.getWorkbook().addPicture(os[i].toByteArray(), Workbook.PICTURE_TYPE_PNG)).resize(1.0 / 1.12);
                rowCell = sheet.getLastRowNum() + 15;
            }
            fos = new FileOutputStream("E://test.xlsx");
            sheet.getWorkbook().write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertImg(ByteArrayOutputStream os, Sheet sheet, int rowCell, int colCell) {
        System.out.println("开始写入的行：" + rowCell + "  ");
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 0, colCell, rowCell, colCell + 8, rowCell + 22);//15*9的矩形图
        anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
        drawing.createPicture(anchor, sheet.getWorkbook().addPicture(os.toByteArray(), Workbook.PICTURE_TYPE_PNG)).resize(1.0 / 1.12);
    }


    public static void insertToHeader(Sheet sheet, Integer lastRow) {
        Cell headerCell = sheet.createRow(lastRow).createCell(0);
        headerCell.setCellValue("XXXXX表");
        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 0, columnLength - 1));
        CellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
        XSSFFont font2 = (XSSFFont) sheet.getWorkbook().createFont();
        font2.setFontHeightInPoints((short) 16);
        font2.setFamily(FontFamily.ROMAN);
        cellStyle2.setFont(font2);
        cellStyle2.setFillForegroundColor((short) 52);
        cellStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        headerCell.setCellStyle(cellStyle2);

        Row rowHeader = sheet.createRow(headerHeight + lastRow);//第i个数据集的表头
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFillForegroundColor((short) 44);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFamily(FontFamily.DECORATIVE);
        cellStyle.setFont(font);

        Cell rowHeaderCell = rowHeader.createCell(0);
        rowHeaderCell.setCellStyle(cellStyle);
        rowHeaderCell.setCellValue("表头列数据行数据");
        //合并表头
        sheet.addMergedRegion(new CellRangeAddress(lastRow + headerHeight, lastRow + headerHeight, 0, columnLength - 1));
    }


    public static void writeToExcel(Sheet sheet) {
        insertToHeader(sheet, lastRow);
        ++lastRow;
        XSSFWorkbook workbook = (XSSFWorkbook) sheet.getWorkbook();
        for (int i = lastRow; i < lastRow + 10; i++) {
            Row row = sheet.createRow(i + headerHeight);
            Cell temp = row.createCell(0);
            temp.setCellValue("当前行:" + i);
            temp.setCellStyle(decorateExcel(workbook));
            for (int j = 1; j < columnLength; j++) {
                Cell temp2 = row.createCell(j);
                temp2.setCellStyle(decorateExcel(workbook));
                temp2.setCellValue("当前列值为" + j);
            }
        }
        int rowTh = sheet.getLastRowNum();
        System.out.println("当前已经写到" + rowTh + "行");
        lastRow = rowTh + 1;
    }


    public static CellStyle decorateExcel(XSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        //设置字体
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFamily(FontFamily.ROMAN);
        cellStyle.setFont(font);
        //设置背景色
        cellStyle.setFillForegroundColor((short) 22);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }
}
//package com.poi;
//
//imp org.apache.poi.ss.usermodel.Cell;
//imp org.apache.poi.ss.usermodel.CellStyle;
//imp org.apache.poi.ss.usermodel.FontFamily;
//imp org.apache.poi.ss.usermodel.Row;
//imp org.apache.poi.ss.usermodel.Sheet;
//imp org.apache.poi.ss.util.CellRangeAddress;
//imp org.apache.poi.xssf.usermodel.XSSFCellStyle;
//imp org.apache.poi.xssf.usermodel.XSSFClientAnchor;
//imp org.apache.poi.xssf.usermodel.XSSFDrawing;
//imp org.apache.poi.xssf.usermodel.XSSFFont;
//imp org.apache.poi.xssf.usermodel.XSSFWorkbook;
//imp org.slf4j.Logger;
//imp org.slf4j.LoggerFactory;
//
//imp javax.imageio.ImageIO;
//imp java.awt.image.BufferedImage;
//imp java.io.ByteArrayOutputStream;
//imp java.io.File;
//imp java.io.FileNotFoundException;
//imp java.io.FileOutputStream;
//imp java.io.IOException;
//
///**
// * javaAPI poi导出 Excel应用
// * Created by rensong.pu on 2016/8/29.
// */
//public class POITest {
//    private static int headerHeight = 1;
//    private static int lastRow = 0;
//    private static int columnLength = 5;
//    private static Logger logger = LoggerFactory.getLogger(POITest.class);
//
//    public static void main(String[] args) {
//        XSSFWorkbook wb = new XSSFWorkbook();
//        Sheet sheet = wb.createSheet("小铺");
//        XSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        cellStyle.setFillForegroundColor((short) 41);//设置背景色
//        //设置字体
//        XSSFFont xssfFont = wb.createFont();
//        xssfFont.setFamily(FontFamily.DECORATIVE);
//        xssfFont.setFontHeightInPoints((short) 16);
////        sheet.setColumnWidth(0, 4000);//设置列宽度
//        cellStyle.setWrapText(true);//设置自动换行
//
//        File file = new File("E://poi.xlsx");
//        sheet.setDefaultColumnWidth(17);//设置默认宽度
//        sheet.setDefaultRowHeight((short) (2 * 256));//设置默认行高，2个字符高度
//        for (int i = 0; i < 12; i++) {
//            writeToExcel(sheet);
//            ++lastRow;
//        }
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            wb.write(fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            logger.error("", e);
//            logger.error("", e);
//            System.out.println();
//        }
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        String imgPath = "E://kola.png";
//        insertImg(imgPath, workbook);
//    }
//
//    public static void insertImg(String imgPath, XSSFWorkbook wb) {
//        //将图片转为byteArrayOutStream,便于产生byteArray
//        FileOutputStream fos = null;
//        BufferedImage bufferedImage = null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            bufferedImage = ImageIO.read(new File(imgPath));
//            ImageIO.write(bufferedImage, "png", baos);
//            Sheet sheet = wb.createSheet("img");
//            //画图的顶级管理器
//            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
//
//
//            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, 1, 1, 5, 8);
//            anchor.setAnchorType(3);
//            //插入图片
//            int pictureIndex = wb.addPicture(baos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG);
//            drawing.createPicture(anchor, pictureIndex);
//            fos = new FileOutputStream("E://img.xlsx");
//            wb.write(fos);
//            System.out.println("Excel生成完成");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void insertToHeader(Sheet sheet, Integer lastRow) {
//        Cell headerCell = sheet.createRow(lastRow).createCell(0);
//        headerCell.setCellValue("XXXXX表");
//        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 0, columnLength - 1));
//
//
//        CellStyle cellStyle2 = sheet.getWorkbook().createCellStyle();
//        XSSFFont font2 = (XSSFFont) sheet.getWorkbook().createFont();
//        font2.setFontHeightInPoints((short) 16);
//        font2.setFamily(FontFamily.ROMAN);
//        cellStyle2.setFont(font2);
//        cellStyle2.setFillForegroundColor((short) 52);
//        cellStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        headerCell.setCellStyle(cellStyle2);
//
//
//        Row rowHeader = sheet.createRow(headerHeight + lastRow);//第i个数据集的表头
//        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
//        cellStyle.setFillForegroundColor((short) 44);
//        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
//        font.setFontHeightInPoints((short) 16);
//        font.setFamily(FontFamily.DECORATIVE);
//        cellStyle.setFont(font);
//
//        Cell rowHeaderCell = rowHeader.createCell(0);
//        rowHeaderCell.setCellStyle(cellStyle);
//        rowHeaderCell.setCellValue("表头列数据行数据");
//        //合并表头
//        sheet.addMergedRegion(new CellRangeAddress(lastRow + headerHeight, lastRow + headerHeight, 0, columnLength - 1));
//    }
//
//
//    public static void writeToExcel(Sheet sheet) {
//        insertToHeader(sheet, lastRow);
//        ++lastRow;
//        XSSFWorkbook workbook = (XSSFWorkbook) sheet.getWorkbook();
//        for (int i = lastRow; i < lastRow + 10; i++) {
//            Row row = sheet.createRow(i + headerHeight);
//            Cell temp = row.createCell(0);
//            temp.setCellValue("当前行:" + i);
//            temp.setCellStyle(decorateExcel(workbook));
//            for (int j = 1; j < columnLength; j++) {
//                Cell temp2 = row.createCell(j);
//                temp2.setCellStyle(decorateExcel(workbook));
//                temp2.setCellValue("当前列值为" + j);
//            }
//        }
//        int rowTh = sheet.getLastRowNum();
//        System.out.println("当前已经写到" + rowTh + "行");
//        lastRow = rowTh + 1;
//    }
//
//
//    public static CellStyle decorateExcel(XSSFWorkbook wb) {
//        CellStyle cellStyle = wb.createCellStyle();
//        //设置字体
//        XSSFFont font = wb.createFont();
//        font.setFontHeightInPoints((short) 16);
//        font.setFamily(FontFamily.ROMAN);
//        cellStyle.setFont(font);
//        //设置背景色
//        cellStyle.setFillForegroundColor((short) 22);
//        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        return cellStyle;
//    }
//}
