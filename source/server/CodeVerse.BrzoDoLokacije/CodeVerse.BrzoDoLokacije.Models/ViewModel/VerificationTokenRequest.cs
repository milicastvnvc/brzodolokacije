using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class VerificationTokenRequest
    {
        [Required, EmailAddress]
        public string Email { get; set; }
        public bool? IsForgotPassword { get; set; }
    }
}
