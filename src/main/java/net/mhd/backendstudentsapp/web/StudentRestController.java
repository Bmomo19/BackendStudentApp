package net.mhd.backendstudentsapp.web;

import net.mhd.backendstudentsapp.entities.Payment;
import net.mhd.backendstudentsapp.entities.PaymentStatus;
import net.mhd.backendstudentsapp.entities.PaymentType;
import net.mhd.backendstudentsapp.entities.Student;
import net.mhd.backendstudentsapp.repository.PaymentRepository;
import net.mhd.backendstudentsapp.repository.StudentRepository;
import net.mhd.backendstudentsapp.services.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
public class StudentRestController {
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final PaymentService paymentService;

    public StudentRestController(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.paymentService = new PaymentService(paymentRepository, studentRepository);
    }

    @GetMapping("/students")
    public List<Student> allStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/payments")
    public List<Payment> allPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/payments/{id}")
    private Payment findById(@PathVariable Long id) {
        return paymentRepository.findById(id).get();
    }

    @GetMapping("/students/{id}")
    public Student findById(@PathVariable String id) {
        return studentRepository.findById(id).get();
    }

    @GetMapping("/payments/student/{code}")
    public List<Payment> findByStudentCode(@PathVariable String code) {
        return paymentRepository.findByStudentCode(code);
    }

    @PostMapping(value = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment save(@RequestParam MultipartFile file, LocalDate date, double amount, PaymentType type, String studentCode) throws IOException {
        return paymentService.savePayment(file, date, amount, type, studentCode);
    }

    @GetMapping(value = "/paymentFile/{paymentId}/file", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException {
        Payment payment = paymentRepository.findById(paymentId).get();

        String filePath = payment.getPaymentfile();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }

    @PutMapping("/payments/updateStatus/{paymentId}")
    public Payment updatePaymentStatus(@RequestParam PaymentStatus status, @PathVariable Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).get();
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
