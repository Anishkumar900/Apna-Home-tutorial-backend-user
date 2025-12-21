package com.user.serviceImpl;

import com.user.config.JWTService;
import com.user.dom.ProfileVerification;
import com.user.dto.OTPGeneration;
import com.user.dto.WordFormation;
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

@Service
@AllArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final RegistrationRepository registrationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    // ================= REGISTRATION =================
    @Override
    public String registration(Registration registration) {

        String email = WordFormation.emailFormation(registration.getEmail());
        String phone = registration.getPhoneNumber();

        Registration existingByEmail = registrationRepository.findByEmail(email);
        Registration existingByPhone = registrationRepository.findByPhoneNumber(phone);

        // Block verified users
        if (existingByEmail != null &&
                existingByEmail.getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already exists, please contact Admin!");
        }

        if (existingByPhone != null &&
                existingByPhone.getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already exists, please contact Admin!");
        }

        // Remove old pending records
        if (existingByEmail != null) {
            registrationRepository.delete(existingByEmail);
        }

        if (existingByPhone != null &&
                (existingByEmail == null ||
                        !existingByPhone.getId().equals(existingByEmail.getId()))) {
            registrationRepository.delete(existingByPhone);
        }

        registration.setEmail(email);
        registration.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
        registration.setName(WordFormation.nameFormation(registration.getName()));
        registration.setLocalDateTime(LocalDateTime.now());
        registration.setProfileVerification(ProfileVerification.PENDING);
        registration.setOtp(OTPGeneration.generateOTP(4));
        registration.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));

        registrationRepository.save(registration);
        return "Verify registration!";
    }

    // ================= OTP VERIFY =================
    @Override
    public String registrationSuccessful(Registration registration) {

        String email = WordFormation.emailFormation(registration.getEmail());
        Registration already = registrationRepository.findByEmail(email);

        if (already == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        if (already.getProfileVerification() != ProfileVerification.PENDING) {
            throw new UserAlreadyExist("User already verified!");
        }

        if (!Objects.equals(registration.getOtp(), already.getOtp())) {
            throw new BadCredentialsException("Invalid OTP!");
        }

        if (already.getExpireOTPTime().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("OTP expired!");
        }

        already.setProfileVerification(ProfileVerification.VERIFIED);
        already.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
        already.setLocalDateTime(LocalDateTime.now());
        already.setOtp(null);
        already.setExpireOTPTime(null);

        registrationRepository.save(already);
        return "Registration successful!";
    }

    // ================= FORGET PASSWORD =================
    @Override
    public String forgetPassword(ForgetPassword forgetPassword) {

        Registration user = registrationRepository.findByEmail(
                WordFormation.emailFormation(forgetPassword.getEmail())
        );

        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
            throw new UsernameNotFoundException("User not found!");
        }

        user.setOtp(OTPGeneration.generateOTP(4));
        user.setExpireOTPTime(LocalDateTime.now().plusMinutes(15));

        registrationRepository.save(user);
        return "Verify OTP!";
    }

    // ================= VERIFY FORGET PASSWORD =================
    @Override
    public String verifyForgetPassword(ForgetPassword forgetPassword) {

        Registration user = registrationRepository.findByEmail(
                WordFormation.emailFormation(forgetPassword.getEmail())
        );
        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
            throw new UsernameNotFoundException("User not found!");
        }
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

    @Override
    public String login(Login login) {

        String email = WordFormation.emailFormation(login.getEmail());
        Registration user = registrationRepository.findByEmail(email);

        if (user == null || user.getProfileVerification() != ProfileVerification.VERIFIED) {
            throw new BadCredentialsException("Invalid email or password!");
        }

        // 🔒 Check if account is locked
//        if (user.isAccountLocked()) {
//            if (user.getLockTime().plusMinutes(LOCK_DURATION_MINUTES).isAfter(LocalDateTime.now())) {
//                throw new BadCredentialsException("Account is locked. Please try again later.");
//            } else {
//                // 🔓 Unlock account after duration
//                user.setAccountLocked(false);
//                user.setFailedLoginAttempts(0);
//                user.setLockTime(null);
//            }
//        }

        // ❌ Password mismatch
        if (!bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword())) {

//            int attempts = user.getFailedLoginAttempts() + 1;
//            user.setFailedLoginAttempts(attempts);
//
//            if (attempts >= MAX_FAILED_ATTEMPTS) {
//                user.setAccountLocked(true);
//                user.setLockTime(LocalDateTime.now());
//            }

//            registrationRepository.save(user);
            throw new BadCredentialsException("Invalid email or password!");
        }

        // ✅ Successful login
//        user.setFailedLoginAttempts(0);
//        user.setAccountLocked(false);
//        user.setLockTime(null);
//        registrationRepository.save(user);

        return jwtService.generateJWTToken(email);

    }

}
