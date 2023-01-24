using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class ResetPasswordRequest
    {
        [Required, EmailAddress]
        public string Email { get; set; }
        [Required]
        public string Password { get; set; }
        [Required, Compare("Password")]
        public string PasswordConfirmation { get; set; }
        [Required]
        public string Token { get; set; }
    }
}
