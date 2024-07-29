package net.mhd.backendstudentsapp.services;

import jakarta.transaction.Transactional;
import net.mhd.backendstudentsapp.entities.Payment;
import net.mhd.backendstudentsapp.entities.PaymentStatus;
import net.mhd.backendstudentsapp.entities.PaymentType;
import net.mhd.backendstudentsapp.entities.Student;
import net.mhd.backendstudentsapp.repository.PaymentRepository;
import net.mhd.backendstudentsapp.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    public PaymentService(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    public Payment savePayment (MultipartFile file, LocalDate date, double amount, PaymentType type, String studentCode) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), "BackendStudentsApp", "payments");

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileId = UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"), "BackendStudentsApp", "payments", fileId+".pdf");
        Files.copy(file.getInputStream(), filePath);

        Student student = studentRepository.findByCode(studentCode);
        Payment payment = Payment.builder()
                .date(date)
                .amount(amount)
                .type(type)
                .status(PaymentStatus.CREATED)
                .student(student)
                .paymentfile(filePath.toUri().toString())
                .build();

        return paymentRepository.save(payment);
    }
}
