package com.example.studentapp.servlet;

import com.example.studentapp.dao.StudentDAO;
import com.example.studentapp.model.Student;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Student> students = new StudentDAO().getAll(1, Integer.MAX_VALUE, null);
            String type = request.getParameter("type");

            if ("csv".equals(type)) {
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=students.csv");
                PrintWriter out = response.getWriter();
                out.println("ID,Name,Email,Course,Created At,Updated At");
                for (Student s : students) {
                    out.printf("%d,\"%s\",\"%s\",\"%s\",%s,%s%n",
                            s.getId(), s.getName(), s.getEmail(), s.getCourse(), s.getCreatedAt(), s.getUpdatedAt());
                }
                out.flush();

            } else if ("excel".equals(type)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=students.xlsx");

                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Students");

                int rowNum = 0;
                Row header = sheet.createRow(rowNum++);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Name");
                header.createCell(2).setCellValue("Email");
                header.createCell(3).setCellValue("Course");

                for (Student s : students) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(s.getId());
                    row.createCell(1).setCellValue(s.getName());
                    row.createCell(2).setCellValue(s.getEmail());
                    row.createCell(3).setCellValue(s.getCourse());
                }

                OutputStream os = response.getOutputStream();
                wb.write(os);
                wb.close();
                os.flush();

            } else if ("pdf".equals(type)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=students.pdf");

                Document document = new Document();
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();
                document.add(new Paragraph("Student List"));
                document.add(new Paragraph(" "));
                for (Student s : students) {
                    document.add(new Paragraph(s.getId() + " | " + s.getName() + " | " + s.getEmail() + " | " + s.getCourse()));
                }
                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}