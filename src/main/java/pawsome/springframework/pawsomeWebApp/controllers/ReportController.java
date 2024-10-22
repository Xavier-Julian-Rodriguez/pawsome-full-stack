package pawsome.springframework.pawsomeWebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawsome.springframework.pawsomeWebApp.services.ReportService;

import java.util.List;

//@CrossOrigin(origins = "http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/user-activity")
    public ResponseEntity<List<Object[]>> getUserActivityReport() {
        List<Object[]> report = reportService.getUserActivityReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/pet-recipe-rank")
    public ResponseEntity<List<Object[]>> getPetWithMostRecipesReport() {
        List<Object[]> report = reportService.getPetsWithMostRecipesReport();
        return ResponseEntity.ok(report);
    }
}
