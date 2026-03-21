package com.cenfotec.backendcodesprint.logic.Admin.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendApprovalEmail(String toEmail, String providerName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("¡Tu solicitud como proveedor fue aprobada!");
            helper.setText(buildApprovalHtml(providerName), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar correo de aprobación: " + e.getMessage());
        }
    }

    public void sendRejectionEmail(String toEmail, String providerName, String reason) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Actualización sobre tu solicitud como proveedor");
            helper.setText(buildRejectionHtml(providerName, reason), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar correo de rechazo: " + e.getMessage());
        }
    }

    private String buildApprovalHtml(String providerName) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #F2FDFA; padding: 0; margin: 0;">
                <table width="100%%" style="max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                  <tr>
                    <td style="background-color: #009689; padding: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px; text-align: center; color: #fff;">
                      <h2 style="margin: 0; font-size: 24px;">PuraVida Care</h2>
                      <p style="margin: 0; font-size: 14px;">Cuidado con calidez y confianza</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding: 30px; color: #333;">
                      <h3 style="color: #009689;">¡Felicitaciones, %s! 🎉</h3>
                      <p style="font-size: 16px; line-height: 1.6;">
                        Nos complace informarte que tu solicitud como proveedor en <strong>PuraVida Care</strong> ha sido <strong style="color: #009689;">aprobada</strong>.
                      </p>
                      <p style="font-size: 16px; line-height: 1.6;">
                        Ya puedes acceder a la plataforma y comenzar a ofrecer tus servicios a nuestra comunidad.
                      </p>
                      <div style="text-align: center; margin-top: 20px;">
                        <a href="http://localhost:4200" style="background-color: #009689; color: #fff; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold;">
                          Ingresar a la plataforma
                        </a>
                      </div>
                      <p style="margin-top: 40px; font-size: 13px; color: #999; text-align: center;">
                        © 2025 PuraVida Care. Todos los derechos reservados.
                      </p>
                    </td>
                  </tr>
                </table>
              </body>
            </html>
        """.formatted(providerName);
    }

    private String buildRejectionHtml(String providerName, String reason) {
        String reasonBlock = (reason != null && !reason.isEmpty())
                ? "<div style='background-color: #F2FDFA; padding: 15px; border-radius: 8px; margin: 25px 0;'><p style='margin: 0; font-size: 15px;'><strong>Motivo:</strong> " + reason + "</p></div>"
                : "";

        return """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #F2FDFA; padding: 0; margin: 0;">
                <table width="100%%" style="max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                  <tr>
                    <td style="background-color: #009689; padding: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px; text-align: center; color: #fff;">
                      <h2 style="margin: 0; font-size: 24px;">PuraVida Care</h2>
                      <p style="margin: 0; font-size: 14px;">Cuidado con calidez y confianza</p>
                    </td>
                  </tr>
                  <tr>
                    <td style="padding: 30px; color: #333;">
                      <h3 style="color: #333;">Hola, %s</h3>
                      <p style="font-size: 16px; line-height: 1.6;">
                        Lamentamos informarte que tu solicitud como proveedor en <strong>PuraVida Care</strong> no fue aprobada en esta ocasión.
                      </p>
                      %s
                      <p style="font-size: 16px; line-height: 1.6;">
                        Si tienes preguntas o deseas más información, no dudes en contactarnos.
                      </p>
                      <p style="margin-top: 40px; font-size: 13px; color: #999; text-align: center;">
                        © 2025 PuraVida Care. Todos los derechos reservados.
                      </p>
                    </td>
                  </tr>
                </table>
              </body>
            </html>
        """.formatted(providerName, reasonBlock);
    }
}