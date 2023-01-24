using MimeKit;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MimeKit.Text;
using System.Net.Mail;
using MailKit.Security;
using MailKit.Net.Smtp;
using SmtpClient = MailKit.Net.Smtp.SmtpClient;

namespace CodeVerse.BrzoDoLokacije.Common.EmailService
{
    public class EmailService : IEmailService
    {
        public async Task SendMail(MailRequest request)
        {
            var email = new MimeMessage();
            email.From.Add(new MailboxAddress("CodeVerse | Brzo Do Lokacije", ConfigProvider.ConfigProvider.MailSettings.Mail));
            email.To.Add(MailboxAddress.Parse(request.ToEmail));
            email.Subject = request.Subject;
            email.Body = new TextPart(TextFormat.Html) { Text = request.Body };

            using var smtp = new SmtpClient();
            smtp.Connect(ConfigProvider.ConfigProvider.MailSettings.Host, 587, SecureSocketOptions.StartTls);
            smtp.Authenticate(ConfigProvider.ConfigProvider.MailSettings.Mail, ConfigProvider.ConfigProvider.MailSettings.Password);
            await smtp.SendAsync(email);
            smtp.Disconnect(true);
        }
    }
}
