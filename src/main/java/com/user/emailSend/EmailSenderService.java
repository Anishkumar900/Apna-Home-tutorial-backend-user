package com.user.emailSend;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Async("emailExecutor")
    public void sendOtpEmail(String to, String userName, String otp) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Apna Home Tutorial | OTP Verification");

            String htmlContent = buildOtpTemplate(userName, otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    // ================= EMAIL TEMPLATE =================
    private String buildOtpTemplate(String userName, String otp) {

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f6f8;
                        padding: 20px;
                    }
                    .container {
                        max-width: 500px;
                        margin: auto;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                        overflow: hidden;
                    }
                    .header {
                        background: #2f80ed;
                        color: #ffffff;
                        text-align: center;
                        padding: 16px;
                        font-size: 22px;
                        font-weight: bold;
                    }
                    .content {
                        padding: 20px;
                        color: #333333;
                        line-height: 1.6;
                    }
                    .otp-box {
                        background: #f1f7ff;
                        border: 1px dashed #2f80ed;
                        text-align: center;
                        font-size: 28px;
                        letter-spacing: 6px;
                        font-weight: bold;
                        color: #2f80ed;
                        padding: 15px;
                        margin: 20px 0;
                        border-radius: 6px;
                    }
                    .warning {
                        font-size: 13px;
                        color: #777777;
                        margin-top: 20px;
                    }
                    .footer {
                        text-align: center;
                        font-size: 12px;
                        color: #999999;
                        padding: 15px;
                        border-top: 1px solid #eeeeee;
                    }
                </style>
            </head>
            <body>

                <div class="container">
                    <div class="header">
                        Apna Home Tutorial
                    </div>

                    <div class="content">
                        <p>Dear <strong>%s</strong>,</p>

                        <p>
                            Thank you for choosing <strong>Apna Home Tutorial</strong>.
                            Please use the OTP below to complete your verification.
                        </p>

                        <div class="otp-box">
                            %s
                        </div>

                        <p>
                            ⏰ <strong>This OTP is valid for 10 minutes only.</strong>
                        </p>

                        <p class="warning">
                            For your security, please do not share this OTP with anyone.
                            Apna Home will never ask for your OTP.
                        </p>
                    </div>

                    <div class="footer">
                        © 2025 Apna Home Tutorial. All rights reserved.
                    </div>
                </div>

            </body>
            </html>
            """.formatted(userName, otp);
    }
}
