package com.moviebooking.service;

import com.moviebooking.dto.BookingDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    // Optional injection — app works fine even if mail is not configured
    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, BookingDto booking) {
        if (mailSender == null) {
            log.warn("Mail sender not configured — skipping booking confirmation email");
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("🎬 Booking Confirmed! Ref: " + booking.getBookingReference());
            helper.setText(buildConfirmationHtml(booking), true);
            mailSender.send(message);
            log.info("Booking confirmation sent to {}", toEmail);
        } catch (Exception e) {
            // Never let email failure crash the booking
            log.warn("Could not send booking confirmation email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendCancellationEmail(String toEmail, BookingDto booking) {
        if (mailSender == null) {
            log.warn("Mail sender not configured — skipping cancellation email");
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("❌ Booking Cancelled - Ref: " + booking.getBookingReference());
            helper.setText(buildCancellationHtml(booking), true);
            mailSender.send(message);
            log.info("Cancellation email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Could not send cancellation email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String buildConfirmationHtml(BookingDto booking) {
        String seats = String.join(", ", booking.getSeatNumbers());
        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8"/>
              <style>
                body { margin:0; padding:0; background:#f5f5f5; font-family:'Segoe UI',Arial,sans-serif; }
                .wrapper { max-width:560px; margin:40px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
                .header { background:#E50914; padding:32px 40px; text-align:center; }
                .header h1 { color:#fff; margin:0; font-size:28px; letter-spacing:2px; }
                .header p { color:rgba(255,255,255,0.85); margin:6px 0 0; font-size:14px; }
                .body { padding:32px 40px; }
                .success-icon { text-align:center; font-size:52px; margin-bottom:8px; }
                .title { text-align:center; font-size:22px; font-weight:700; color:#111; margin-bottom:4px; }
                .subtitle { text-align:center; color:#888; font-size:14px; margin-bottom:28px; }
                .ref-box { background:#fff5f5; border:2px dashed #E50914; border-radius:8px; padding:14px 20px; text-align:center; margin-bottom:28px; }
                .ref-label { font-size:11px; color:#888; text-transform:uppercase; letter-spacing:1px; }
                .ref-code { font-size:22px; font-weight:700; color:#E50914; letter-spacing:3px; margin-top:4px; }
                .details-table { width:100%%; border-collapse:collapse; margin-bottom:24px; }
                .details-table tr { border-bottom:1px solid #f0f0f0; }
                .details-table tr:last-child { border-bottom:none; }
                .details-table td { padding:10px 0; font-size:14px; }
                .details-table td:first-child { color:#888; width:40%%; }
                .details-table td:last-child { font-weight:600; color:#111; text-align:right; }
                .total-row td { font-size:16px !important; color:#E50914 !important; font-weight:700 !important; }
                .enjoy { background:#E50914; color:#fff; padding:14px 32px; border-radius:8px; display:inline-block; font-weight:700; font-size:15px; margin:16px 0 24px; }
                .footer { background:#f9f9f9; padding:20px 40px; text-align:center; border-top:1px solid #f0f0f0; }
                .footer p { margin:4px 0; color:#aaa; font-size:12px; }
              </style>
            </head>
            <body>
              <div class="wrapper">
                <div class="header">
                  <h1>🎬 CINEBOOK</h1>
                  <p>Your Premier Movie Booking Platform</p>
                </div>
                <div class="body">
                  <div class="success-icon">🎉</div>
                  <div class="title">Booking Confirmed!</div>
                  <div class="subtitle">Hey %s, your tickets are booked. See you at the movies!</div>
                  <div class="ref-box">
                    <div class="ref-label">Booking Reference</div>
                    <div class="ref-code">%s</div>
                  </div>
                  <table class="details-table">
                    <tr><td>🎬 Movie</td><td>%s</td></tr>
                    <tr><td>🏛️ Theatre</td><td>%s</td></tr>
                    <tr><td>📅 Date</td><td>%s</td></tr>
                    <tr><td>⏰ Time</td><td>%s</td></tr>
                    <tr><td>💺 Seats</td><td>%s</td></tr>
                    <tr><td>🎟️ Tickets</td><td>%d ticket(s)</td></tr>
                    <tr class="total-row"><td>💰 Total</td><td>₹%.2f</td></tr>
                  </table>
                  <div style="text-align:center;"><div class="enjoy">🍿 Enjoy the Show!</div></div>
                </div>
                <div class="footer">
                  <p>Please carry this reference number to the theatre.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                booking.getUserName(), booking.getBookingReference(),
                booking.getMovieTitle(), booking.getTheatreName(),
                booking.getShowDate(), booking.getShowTime(),
                seats, booking.getNumberOfTickets(), booking.getTotalAmount()
        );
    }

    private String buildCancellationHtml(BookingDto booking) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"/>
              <style>
                body { margin:0; padding:0; background:#f5f5f5; font-family:'Segoe UI',Arial,sans-serif; }
                .wrapper { max-width:560px; margin:40px auto; background:#fff; border-radius:12px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
                .header { background:#333; padding:28px 40px; text-align:center; }
                .header h1 { color:#fff; margin:0; font-size:26px; }
                .body { padding:32px 40px; text-align:center; }
                .icon { font-size:52px; margin-bottom:12px; }
                .title { font-size:22px; font-weight:700; color:#111; margin-bottom:8px; }
                .sub { color:#888; font-size:14px; margin-bottom:24px; line-height:1.6; }
                .ref { font-size:18px; font-weight:700; background:#f5f5f5; padding:10px 24px; border-radius:8px; display:inline-block; margin-bottom:20px; }
                .refund { background:#d1fae5; border-radius:8px; padding:14px 20px; color:#065f46; font-size:14px; font-weight:600; }
                .footer { background:#f9f9f9; padding:16px 40px; text-align:center; border-top:1px solid #f0f0f0; }
                .footer p { margin:4px 0; color:#aaa; font-size:12px; }
              </style>
            </head>
            <body>
              <div class="wrapper">
                <div class="header"><h1>🎬 CINEBOOK</h1></div>
                <div class="body">
                  <div class="icon">❌</div>
                  <div class="title">Booking Cancelled</div>
                  <div class="sub">Hi %s, your booking for <strong>%s</strong> has been cancelled.</div>
                  <div class="ref">Ref: %s</div>
                  <div class="refund">✅ Refund will be credited within 5-7 business days.</div>
                </div>
                <div class="footer"><p>Questions? Contact support@cinebook.com</p></div>
              </div>
            </body>
            </html>
            """.formatted(booking.getUserName(), booking.getMovieTitle(), booking.getBookingReference());
    }
}