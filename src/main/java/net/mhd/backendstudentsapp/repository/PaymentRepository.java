package net.mhd.backendstudentsapp.repository;

import net.mhd.backendstudentsapp.entities.Payment;
import net.mhd.backendstudentsapp.entities.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStudentCode(String code);

    List<Payment> findByStatus(PaymentStatus status);
}
