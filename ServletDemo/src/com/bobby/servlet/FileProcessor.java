package com.bobby.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.bobby.common.config.Configuration;

@SuppressWarnings("serial")
public class FileProcessor extends HttpServlet implements Serializable {

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();// 获得磁盘文件条目工厂
        String path = Configuration.getInstance().getValue("FILE_PATH");// 获取文件需要上传到的路径
        /**
         * 先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 然后再将其真正写到 对应目录的硬盘上
         */
        factory.setRepository(new File(path));
        factory.setSizeThreshold(1024 * 1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
            for (FileItem item : list) {
                String name = item.getFieldName();
                if (item.isFormField()) {
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {// File类型的你懂的
                    String value = item.getName();
                    int start = value.lastIndexOf("\\");
                    String filename = value.substring(start + 1);// 新文件名称
                    request.setAttribute(name, filename);

                    // 0.简单的格式验证
                    if (StringUtils.isNotEmpty(filename) && (StringUtils.contains(filename, "xls") || StringUtils.contains(filename, "xlsx"))) {
                        // 1. 写到本地指定文件夹
                        item.write(new File(path, filename));
                        // 2.
                        File file = new File(path, filename);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        FileInputStream fileInputStream = new FileInputStream(file);
                        parseExcel(fileInputStream);
                    } else {
                        System.err.println("格式不正确！请上传excel文件！");
                    }
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    // MIN格式严格的格式验证 excelContentType[] = {"application/vnd.ms-excel", "application/kset", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};

                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseExcel(FileInputStream inputStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet workSheet = workbook.getSheetAt(0);
        // HSSFRow row = workSheet.getRow(7);

        Iterator<Row> iterator = workSheet.rowIterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            for (Iterator iterator2 = row.cellIterator(); iterator2.hasNext();) {
                Cell cell = (Cell) iterator2.next();
                System.out.print(cell.getStringCellValue() + " * ");
            }
            System.out.println();
        }
    }

}