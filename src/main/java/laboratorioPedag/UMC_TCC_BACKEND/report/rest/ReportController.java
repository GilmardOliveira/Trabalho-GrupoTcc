package laboratorioPedag.UMC_TCC_BACKEND.report.rest;

import laboratorioPedag.UMC_TCC_BACKEND.report.service.ReportService;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

@Controller
@RequestMapping("/api/v1/report")
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<?> toExcel(@RequestParam(required = false) Long startDate,
                                     @RequestParam(required = false) Long endDate,
                                     @RequestParam String type) throws Exception {

        Workbook workbook = reportService.modificadorDeReport(type, startDate, endDate);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        ByteArrayInputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        outputStream.close();

        return ResponseEntity
                .ok()
                .contentLength(is.available())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment;filename="+ type +"_report.xlsx")
                .body(new InputStreamResource(is));
    }

}
