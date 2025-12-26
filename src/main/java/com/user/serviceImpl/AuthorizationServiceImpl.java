package com.user.serviceImpl;

import com.user.config.JWTService;
import com.user.dom.Payment;
import com.user.dom.ProfileVerification;
import com.user.dto.OTPGeneration;
import com.user.dto.WordFormation;
import com.user.emailSend.EmailSenderService;
import com.user.exception.UserAlreadyExist;
import com.user.model.Registration;
import com.user.repository.RegistrationRepository;
import com.user.request.ForgetPassword;
import com.user.request.Login;
import com.user.service.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final RegistrationRepository registrationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;
    private final EmailSenderService emailSenderService;

    // ================= REGISTRATION =================
    @Override
    public String registration(Registration registration) {

        String email = WordFormation.emailFormation(registration.getEmail());
        String phone = registration.getPhoneNumber();
        String otp = OTPGeneration.generateOTP(4);

        Optional<Registration> existingByEmail =
                registrationRepository.findByEmail(email);

        Optional<Registration> existingByPhone =
                registrationRepository.findByPhoneNumber(phone);

        // Block verified users
        if (existingByEmail.isPresent()
                && existingByEmail.get().getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already exists, please contact Admin!");
        }

        if (existingByPhone.isPresent()
                && existingByPhone.get().getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already exists, please contact Admin!");
        }

        // Remove old pending records
        existingByEmail.ifPresent(registrationRepository::delete);

        if (existingByPhone.isPresent()
                && (existingByEmail.isEmpty()
                || !existingByPhone.get().getId().equals(existingByEmail.get().getId()))) {
            registrationRepository.delete(existingByPhone.get());
        }

        emailSenderService.sendOtpEmail(email, WordFormation.nameFormation(registration.getName()), otp);


        registration.setEmail(email);
        registration.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
        registration.setName(WordFormation.nameFormation(registration.getName()));
        registration.setLocalDateTime(LocalDateTime.now());
        registration.setProfileVerification(ProfileVerification.PENDING);
        registration.setOtp(otp);
        registration.setLocation(WordFormation.nameFormation(registration.getLocation()));
        registration.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));
        registration.setPayment(Payment.PENDING);

        registrationRepository.save(registration);
        return "Verify registration!";
    }

    // ================= OTP VERIFY =================
    @Override
    public String registrationSuccessful(Registration registration) {

        String email = WordFormation.emailFormation(registration.getEmail());

        Registration user = registrationRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (user.getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already verified!");
        }

        if (!Objects.equals(registration.getOtp(), user.getOtp())) {
            throw new BadCredentialsException("Invalid OTP!");
        }

        if (user.getExpireOTPTime().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("OTP expired!");
        }

        user.setProfileVerification(ProfileVerification.VERIFIED);
        user.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
        user.setLocalDateTime(LocalDateTime.now());
        user.setOtp(null);
        user.setExpireOTPTime(null);

        registrationRepository.save(user);
        return "Registration successful!";
    }

    // ================= FORGET PASSWORD =================
    @Override
    public String forgetPassword(ForgetPassword forgetPassword) {

        String email = WordFormation.emailFormation(forgetPassword.getEmail());
        String otp = OTPGeneration.generateOTP(4);

        Registration user = registrationRepository.findByEmail(email)
                .filter(u -> u.getProfileVerification() == ProfileVerification.VERIFIED)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        emailSenderService.sendOtpEmail(email, WordFormation.nameFormation(user.getName()), otp);

        user.setOtp(otp);
        user.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));

        registrationRepository.save(user);
        return "Verify OTP!";
    }

    // ================= VERIFY FORGET PASSWORD =================
    @Override
    public String verifyForgetPassword(ForgetPassword forgetPassword) {

        String email = WordFormation.emailFormation(forgetPassword.getEmail());

        Registration user = registrationRepository.findByEmail(email)
                .filter(u -> u.getProfileVerification() == ProfileVerification.VERIFIED)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (user.getExpireOTPTime().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("OTP expired!");
        }

        if (!Objects.equals(forgetPassword.getOtp(), user.getOtp())) {
            throw new BadCredentialsException("Invalid OTP!");
        }

        user.setPassword(bCryptPasswordEncoder.encode(forgetPassword.getPassword()));
        user.setOtp(null);
        user.setExpireOTPTime(null);

        registrationRepository.save(user);
        return "Forget Password successful!";
    }

    // ================= LOGIN =================
    @Override
    public String login(Login login) {

        String email = WordFormation.emailFormation(login.getEmail());

        Registration user = registrationRepository.findByEmail(email)
                .filter(u -> u.getProfileVerification() == ProfileVerification.VERIFIED)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid email or password!")
                );

        if (!bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password!");
        }

        return jwtService.generateJWTToken(email);
    }
}









//@Service
//@AllArgsConstructor
//public class AuthorizationServiceImpl implements AuthorizationService {
//
//    private final RegistrationRepository registrationRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final JWTService jwtService;
//    private final TwilioOTPService twilioOTPService;
//    private final EmailSenderService emailSenderService;
//
//
//    // ================= REGISTRATION =================
//    @Override
//    public String registration(Registration registration) {
//
//        String email = WordFormation.emailFormation(registration.getEmail());
//        String phone = registration.getPhoneNumber();
//        String otp= OTPGeneration.generateOTP(4);
//
//        Registration existingByEmail = registrationRepository.findByEmail(email);
//        Registration existingByPhone = registrationRepository.findByPhoneNumber(phone);
//
//        // Block verified users
//        if (existingByEmail != null &&
//                existingByEmail.getProfileVerification() != ProfileVerification.PENDING) {
//            throw new UserAlreadyExist("User already exists, please contact Admin!");
//        }
//
//        if (existingByPhone != null &&
//                existingByPhone.getProfileVerification() != ProfileVerification.PENDING) {
//            throw new UserAlreadyExist("User already exists, please contact Admin!");
//        }
//
//        // Remove old pending records
//        if (existingByEmail != null) {
//            registrationRepository.delete(existingByEmail);
//        }
//
//        if (existingByPhone != null &&
//                (existingByEmail == null ||
//                        !existingByPhone.getId().equals(existingByEmail.getId()))) {
//            registrationRepository.delete(existingByPhone);
//        }
//
//        twilioOTPService.sendOTPForRegistration("+91"+registration.getPhoneNumber(),registration.getName(),otp);
//
//        registration.setEmail(email);
//        registration.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
//        registration.setName(WordFormation.nameFormation(registration.getName()));
//        registration.setLocalDateTime(LocalDateTime.now());
//        registration.setProfileVerification(ProfileVerification.PENDING);
//        registration.setOtp(otp);
//        registration.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));
//        registration.setPayment(Payment.PENDING);
//        registrationRepository.save(registration);
//        return "Verify registration!";
//    }
//
//
//
//    // ================= OTP VERIFY =================
//    @Override
//    public String registrationSuccessful(Registration registration) {
//
//        String email = WordFormation.emailFormation(registration.getEmail());
//        Registration already = registrationRepository.findByEmail(email);
////        System.out.println(registration.getOtp());
//
//        if (already == null) {
//            throw new UsernameNotFoundException("User not found!");
//        }
////        System.out.println(already.getOtp());
//        if (already.getProfileVerification() != ProfileVerification.PENDING) {
//            throw new UserAlreadyExist("User already verified!");
//        }
//
//        if (!Objects.equals(registration.getOtp(), already.getOtp())) {
//            throw new BadCredentialsException("Invalid OTP!");
//        }
//
//        if (already.getExpireOTPTime().isBefore(LocalDateTime.now())) {
//            throw new BadCredentialsException("OTP expired!");
//        }
//
////        System.out.println(already.getOtp());
//        already.setProfileVerification(ProfileVerification.VERIFIED);
//        already.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
//        already.setLocalDateTime(LocalDateTime.now());
//        already.setOtp(null);
//        already.setExpireOTPTime(null);
////        System.out.println(registration.getOtp());
//        registrationRepository.save(already);
//        return "Registration successful!";
//    }
//
//    // ================= FORGET PASSWORD =================
//    @Override
//    public String forgetPassword(ForgetPassword forgetPassword) {
//        String otp=OTPGeneration.generateOTP(4);
//        Registration user = registrationRepository.findByEmail(
//                WordFormation.emailFormation(forgetPassword.getEmail())
//        );
//
//        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
//            throw new UsernameNotFoundException("User not found!");
//        }
//
//        emailSenderService.sendOtpEmail(user.getEmail(), user.getName(), otp);
//        user.setOtp(otp);
//        user.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));
//
//        registrationRepository.save(user);
//        return "Verify OTP!";
//    }
//
//    // ================= VERIFY FORGET PASSWORD =================
//    @Override
//    public String verifyForgetPassword(ForgetPassword forgetPassword) {
//
//        Registration user = registrationRepository.findByEmail(
//                WordFormation.emailFormation(forgetPassword.getEmail())
//        );
//        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
//            throw new UsernameNotFoundException("User not found!");
//        }
//        if (user.getExpireOTPTime().isBefore(LocalDateTime.now())) {
//            throw new BadCredentialsException("OTP expired!");
//        }
//        if (!Objects.equals(forgetPassword.getOtp(), user.getOtp())) {
//            throw new BadCredentialsException("Invalid OTP!");
//        }
//        user.setPassword(bCryptPasswordEncoder.encode(forgetPassword.getPassword()));
//        user.setOtp(null);
//        user.setExpireOTPTime(null);
//        registrationRepository.save(user);
//        return "Forget Password successful!";
//    }
//
//    @Override
//    public String login(Login login) {
//
//        String email = WordFormation.emailFormation(login.getEmail());
//        Registration user = registrationRepository.findByEmail(email);
//
//        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
//            throw new BadCredentialsException("Invalid email or password!");
//        }
//
//        if (!bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword())) {
//
//            throw new BadCredentialsException("Invalid email or password!");
//        }
//
//
//        return jwtService.generateJWTToken(email);
//
//    }
//
//}