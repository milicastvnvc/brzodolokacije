using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.EmailService
{
    public interface IEmailService
    {
        Task SendMail(MailRequest request);
    }
}
