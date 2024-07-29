package net.mhd.backendstudentsapp;

import net.mhd.backendstudentsapp.entities.Payment;
import net.mhd.backendstudentsapp.entities.PaymentStatus;
import net.mhd.backendstudentsapp.entities.PaymentType;
import net.mhd.backendstudentsapp.entities.Student;
import net.mhd.backendstudentsapp.repository.PaymentRepository;
import net.mhd.backendstudentsapp.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class BackendStudentsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendStudentsAppApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository, PaymentRepository paymentRepository) {
		return args -> {
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).code("11111").firstName("MHD").lastName("Moh").email("email").photo("photo").build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).code("22222").firstName("BAMBA").lastName("Anna").email("email").photo("photo").build());
			studentRepository.save(Student.builder().id(UUID.randomUUID().toString()).code("3333").firstName("Test").lastName("name").email("email").photo("photo").build());

			PaymentType[] paymentTypes = PaymentType.values();

			Random random = new Random();

			studentRepository.findAll().forEach(student -> {
				for (int i = 0; i < 10 ; i++) {
					int index = random.nextInt(paymentTypes.length);
					Payment payment = Payment.builder()
							.amount((double) (1000+(int)(Math.random()*10000)))
							.date(LocalDate.now())
							.type(paymentTypes[index])
							.status(PaymentStatus.CREATED)
							.paymentfile(UUID.randomUUID().toString())
							.student(student)
							.build();
					paymentRepository.save(payment);
				}
			});

		};
	}
}
